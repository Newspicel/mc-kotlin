package net.spaceblock.utils.di.commands

import net.spaceblock.utils.adventure.text
import net.spaceblock.utils.di.DIJavaPlugin
import net.spaceblock.utils.di.callOrSuspendCallBy
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation

object CommandsHelper {

    private val commandMap = Bukkit.getCommandMap()

    fun registerCommand(plugin: DIJavaPlugin, command: Command, func: KCallable<*>) {
        val pluginCommand = getBukkitCommand(command.label, plugin)
        pluginCommand.aliases = command.aliases.toList()
        pluginCommand.description = command.description.ifEmpty { "No description provided" }
        pluginCommand.usage = command.usage.ifEmpty { "Usage: /${command.label}" }

        pluginCommand.setExecutor(createCommandExecutor(plugin, func, command))
    }

    fun registerTabComplete(plugin: DIJavaPlugin, tabComplete: TabComplete, func: KCallable<*>) {
        val pluginCommand = getBukkitCommand(tabComplete.label, plugin)
        pluginCommand.tabCompleter = createTabCompleter(plugin, func, tabComplete)
    }

    private fun getBukkitCommand(label: String, plugin: JavaPlugin): PluginCommand {
        return plugin.getCommand(label) ?: run {
            val constructor = PluginCommand::class.constructors.first()
            val instance = constructor.call(label, plugin)

            if (!commandMap.register(label, plugin.name, instance)) error("Failed to register command $label")

            return@run instance
        }
    }

    private fun createCommandExecutor(plugin: DIJavaPlugin, func: KCallable<*>, command: Command): CommandExecutor = CommandExecutor { sender, _, label, args ->
        if (command.label != label) error("This should never happen")

        if (!checkPermissions(sender, func)) {
            sender.sendMessage(text("You don't have permission to execute this command"))
            return@CommandExecutor false
        }

        val player: Player? = if (command.playerOnly && sender !is Player) {
            sender.sendMessage(text("This command can only be executed by a player"))
            return@CommandExecutor false
        } else if (sender is Player) {
            sender
        } else {
            null
        }

        val params = plugin.getParameterMap(func.parameters, player, sender, label, args)

        val result = try {
            func.callOrSuspendCallBy(params)
        } catch (e: Exception) {
            sender.sendMessage(text("An error occurred while executing this command"))
            e.printStackTrace()
            return@CommandExecutor false
        }

        if (result is Boolean) {
            return@CommandExecutor result
        }

        return@CommandExecutor true
    }

    private fun createTabCompleter(plugin: DIJavaPlugin, func: KCallable<*>, tabComplete: TabComplete): TabCompleter = TabCompleter { sender, _, label, args ->
        if (tabComplete.label != label) error("This should never happen")

        if (!checkPermissions(sender, func)) {
            return@TabCompleter emptyList()
        }

        val params = plugin.getParameterMap(func.parameters, sender, label, args)

        val result = try {
            func.callOrSuspendCallBy(params)
        } catch (e: Exception) {
            sender.sendMessage(text("An error occurred while executing this command"))
            e.printStackTrace()
            return@TabCompleter emptyList()
        }

        if (result is List<*>) {
            if (result.isEmpty()) return@TabCompleter emptyList()

            return@TabCompleter result.map { it.toString() }
        }

        return@TabCompleter emptyList()
    }

    @Suppress("RedundantIf")
    private fun checkPermissions(sender: CommandSender, func: KCallable<*>): Boolean {
        val permission = func.findAnnotation<HasPermission>()
        if (permission != null && !sender.hasPermission(permission.permission)) {
            return false
        }


        val op = func.findAnnotation<IsOp>()
        if (op != null && !sender.isOp) {
            return false
        }

        return true
    }
}
