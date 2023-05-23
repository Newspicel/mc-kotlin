package net.spaceblock.utils.di.commands

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.format.TextColor
import net.spaceblock.utils.adventure.text
import net.spaceblock.utils.di.DIJavaPlugin
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

object CommandsHelper {

    private val commandScope = CoroutineScope(Dispatchers.Default)

    fun registerCommand(plugin: DIJavaPlugin, command: Command, func: KFunction<*>) {
        val pluginCommand = getBukkitCommand(command.label, plugin)
        pluginCommand.aliases = command.aliases.toList()
        pluginCommand.description = command.description.ifEmpty { "No description provided" }
        pluginCommand.usage = command.usage.ifEmpty { "Usage: /${command.label}" }

        pluginCommand.setExecutor(createCommandExecutor(plugin, func, command))

        if (!pluginCommand.isRegistered) {
            if (!plugin.server.commandMap.register(command.label, plugin.name, pluginCommand)) error("Failed to register command $pluginCommand")
        }
    }

    fun registerTabComplete(plugin: DIJavaPlugin, tabComplete: TabComplete, func: KFunction<*>) {
        val pluginCommand = getBukkitCommand(tabComplete.label, plugin)
        pluginCommand.tabCompleter = createTabCompleter(plugin, func, tabComplete)
    }

    private fun getBukkitCommand(label: String, plugin: JavaPlugin): PluginCommand {
        return plugin.getCommand(label) ?: run {
            val constructor = PluginCommand::class.constructors.first()
            constructor.isAccessible = true

            return@run constructor.call(label, plugin)
        }
    }

    private fun createCommandExecutor(plugin: DIJavaPlugin, func: KFunction<*>, command: Command): CommandExecutor = CommandExecutor { sender, _, label, args ->
        if (command.label != label) error("This should never happen")

        if (!checkPermissions(sender, func)) {
            sender.sendMessage(text("You don't have permission to execute this command").color(TextColor.color(0xFF0000)))
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

        try {
            commandScope.launch {
                val paramsMap = plugin.getParameterMap(func.parameters, player, sender, label, args, args.toList())

                try {
                    func.callSuspendBy(paramsMap)
                } catch (e: Exception) {
                    sender.sendMessage(text("An error occurred while executing this command").color(TextColor.color(0xFF0000)))
                    plugin.logger.log(Level.WARNING, "An error occurred while executing command $label", e)
                    e.printStackTrace()
                }
            }
            return@CommandExecutor true
        } catch (e: Exception) {
            sender.sendMessage(text("An error occurred while executing this command").color(TextColor.color(0xFF0000)))
            plugin.logger.log(Level.WARNING, "An error occurred while executing command $label", e)
            return@CommandExecutor false
        }
    }

    private fun createTabCompleter(plugin: DIJavaPlugin, func: KFunction<*>, tabComplete: TabComplete): TabCompleter = TabCompleter { sender, _, label, args ->
        if (tabComplete.label != label) error("This should never happen")

        if (!checkPermissions(sender, func)) {
            return@TabCompleter emptyList()
        }

        val params = plugin.getParameterMap(func.parameters, sender, label, args, args.toList())

        val result = try {
            runBlocking(Dispatchers.Default) {
                func.callSuspendBy(params)
            }
        } catch (e: Exception) {
            sender.sendMessage(text("An error occurred while executing this command"))
            e.printStackTrace()
            return@TabCompleter emptyList()
        }

        when (result) {
            is List<*> -> {
                if (result.isEmpty()) return@TabCompleter emptyList()
                return@TabCompleter result.map { it.toString() }
            }

            is Array<*> -> {
                if (result.isEmpty()) return@TabCompleter emptyList()
                return@TabCompleter result.map { it.toString() }
            }

            else -> {
                throw IllegalStateException("TabCompleter function must return a List<String> or Array<String>")
            }
        }
    }

    @Suppress("RedundantIf")
    private fun checkPermissions(sender: CommandSender, func: KFunction<*>): Boolean {
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
