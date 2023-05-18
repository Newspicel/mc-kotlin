package net.spaceblock.utils.di

import net.spaceblock.utils.adventure.text
import net.spaceblock.utils.di.annotations.Command
import net.spaceblock.utils.di.annotations.Event
import net.spaceblock.utils.di.annotations.OnDisable
import net.spaceblock.utils.di.annotations.OnEnable
import net.spaceblock.utils.di.annotations.OnLoad
import net.spaceblock.utils.di.annotations.TabComplete
import net.spaceblock.utils.di.registry.CommandExecuteRegistry
import net.spaceblock.utils.di.registry.EventRegistry
import net.spaceblock.utils.di.registry.StartAndStopRegistry
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class DIJavaPlugin : JavaPlugin() {

    abstract fun startDI(mcSingletons: Map<KClass<*>, Any>)
    abstract fun stopDI()

    abstract fun scanForMinecraftControllers(packagePath: String = projectPackagePath): List<KClass<*>>
    abstract fun <T : Any> getDI(type: KClass<T>, qualifier: String? = null): T?
    abstract fun getQualifier(annotation: List<Annotation>): String?

    abstract val projectPackagePath: String

    override fun onLoad() {
        val singletons = mapOf<KClass<*>, Any>(
            JavaPlugin::class to this,

        )

        startDI(singletons)
        val controllers = scanForMinecraftControllers()
        scanForMinecraftAnnotationsInClassesOnLoad(controllers)

        StartAndStopRegistry.triggerOnLoad(this)
    }

    final override fun onEnable() {
        StartAndStopRegistry.triggerOnEnable(this)
        scanForMinecraftAnnotationsInClassesOnEnable(scanForMinecraftControllers())
    }

    final override fun onDisable() {
        StartAndStopRegistry.triggerOnDisable(this)

        stopDI()
    }

    fun getParameterMap(parameters: List<KParameter>, vararg additional: Any?): Map<KParameter, Any?> = parameters.associateWith { parameter ->
        val qualifier = getQualifier(parameter.annotations)

        val type = parameter.type.classifier as KClass<*>

        val value = additional.firstOrNull { it?.let { it::class == type } ?: false } ?: getDI(type, qualifier)

        if (value == null && !parameter.isOptional) {
            error("Could not find a value for parameter ${parameter.name} of type ${parameter.type} with qualifier $qualifier")
        }

        value
    }

    private fun scanForMinecraftAnnotationsInClassesOnEnable(classes: List<KClass<*>>) {
        classes.forEach { clazz ->
            clazz.members.forEach { func ->
                func.annotations.forEach { annotation ->
                    when (annotation) {
                        is Command -> {
                            CommandExecuteRegistry.registerCommand(this, annotation, func)
                        }

                        is TabComplete -> {
                            CommandExecuteRegistry.registerTabComplete(this, annotation, func)
                        }

                        is Event -> {
                            EventRegistry.registerEvent(this, annotation, func)
                        }
                    }
                }
            }
        }
    }

    private fun scanForMinecraftAnnotationsInClassesOnLoad(classes: List<KClass<*>>) {
        classes.forEach { clazz ->
            clazz.members.forEach { func ->
                func.annotations.forEach { annotation ->
                    when (annotation) {
                        is OnEnable -> {
                            StartAndStopRegistry.registerOnEnable(func)
                        }

                        is OnDisable -> {
                            StartAndStopRegistry.registerOnDisable(func)
                        }

                        is OnLoad -> {
                            StartAndStopRegistry.registerOnLoad(func)
                        }
                    }
                }
            }
        }
    }

    // Lock this stuff down
    final override fun onTabComplete(sender: CommandSender, command: org.bukkit.command.Command, alias: String, args: Array<out String>?): MutableList<String> {
        sender.sendMessage(text("An error occurred while executing this command. Please contact the server administrators."))
        logger.warning("OnTabComplete was called for ${command.name} with alias $alias and args ${args?.joinToString(", ")} but no tab complete was registered.")
        return emptyList<String>().toMutableList()
    }

    final override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>?): Boolean {
        sender.sendMessage(text("An error occurred while executing this command. Please contact the server administrators."))
        logger.warning("OnCommand was called for ${command.name} with alias $label and args ${args?.joinToString(", ")} but no command was registered.")
        return false
    }
}
