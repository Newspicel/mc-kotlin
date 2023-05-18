package net.spaceblock.utils.di

import net.spaceblock.utils.adventure.text
import net.spaceblock.utils.di.annotations.Command
import net.spaceblock.utils.di.annotations.Event
import net.spaceblock.utils.di.annotations.TabComplete
import net.spaceblock.utils.di.registry.StartAndStopRegistry
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Qualifier
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class DIJavaPlugin : JavaPlugin() {


    abstract fun startDI(mcSingletons: Map<KClass<*>, Any>)
    abstract fun stopDI()

    abstract fun scanForMinecraftControllers(packagePath: String = "net.spaceblock"): List<KClass<*>>

    abstract fun getMinecraftControllers(): List<KClass<*>>
    abstract fun <T : Any> getDI(type: KClass<T>, qualifier: String? = null): T?

    abstract fun getQualifier(annotation: List<Annotation>): String?


    override fun onLoad() {
        //Start DI
        val singletons = mapOf<KClass<*>, Any>(
            JavaPlugin::class to this

        )

        startDI(singletons)
        scanForMinecraftControllers()

        StartAndStopRegistry.triggerOnLoad(this)
    }

    final override fun onEnable() {
        StartAndStopRegistry.triggerOnEnable(this)
    }

    final override fun onDisable() {
        StartAndStopRegistry.triggerOnDisable(this)

        // Stop DI
        stopDI()
    }

    fun getParameterMap(parameters: List<KParameter>, vararg additional: Any?): Map<KParameter, Any?> = parameters.associateWith { parameter ->
        val qualifier = getQualifier(parameter.annotations)

        val type = parameter.type.classifier as KClass<*>

        val value = additional.firstOrNull { it?.let { it::class == type } ?: false } ?: getDI(type, qualifier)

        if (value == null && !parameter.isOptional) error("Could not find a value for parameter ${parameter.name} of type ${parameter.type} with qualifier $qualifier")

        value
    }

    fun scanForMinecraftAnnotationsInClasses(classes: List<KClass<*>>) {
        classes.forEach { clazz ->
            // Get Functions
            clazz.members.forEach { method ->
                method.annotations.forEach { annotation ->
                    when (annotation) {
                        is Command -> {
                            registerCommand(annotation, method)
                        }

                        is TabComplete -> {
                            registerTabComplete(annotation, method)
                        }

                        is Event -> {
                            registerEvent(annotation, method)
                        }
                    }
                }
            }
        }
    }

    fun registerCommand(command: Command, func: KCallable<*>) {
        // Register Command
    }

    fun registerTabComplete(tabComplete: TabComplete, func: KCallable<*>) {
        // Register TabComplete
    }

    fun registerEvent(event: Event, func: KCallable<*>) {
        // Register Event
    }


    // Lock this stuff down
    final override fun onTabComplete(sender: CommandSender, command: org.bukkit.command.Command, alias: String, args: Array<out String>?): MutableList<String>? {
        sender.sendMessage(text("An error occurred while executing this command. Please contact the server administrators."))
        return null
    }

    final override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>?): Boolean {
        sender.sendMessage(text("An error occurred while executing this command. Please contact the server administrators."))
        return false
    }


}
