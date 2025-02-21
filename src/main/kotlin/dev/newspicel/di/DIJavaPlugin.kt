package dev.newspicel.di

import dev.newspicel.di.commands.Command
import dev.newspicel.di.commands.CommandsHelper
import dev.newspicel.di.commands.TabComplete
import dev.newspicel.di.events.Event
import dev.newspicel.di.events.EventHelper
import dev.newspicel.di.serverevents.OnDisable
import dev.newspicel.di.serverevents.OnEnable
import dev.newspicel.di.serverevents.OnLoad
import dev.newspicel.di.serverevents.ServerEventsHelper
import net.kyori.adventure.text.Component.text
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import java.util.logging.Level
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.functions

abstract class DIJavaPlugin : JavaPlugin() {

    private val stereotypes = listOf(MinecraftController::class, Service::class, Repository::class)

    protected val stereotypesClasses: List<KClass<*>>
    private val controllerClasses: List<KClass<*>>

    private val projectPackagePath: String
        get() = this.javaClass.`package`.name

    init {
        stereotypesClasses = scanForMinecraftStereotypes()
        controllerClasses = stereotypesClasses.filter { it.findAnnotations(MinecraftController::class).isNotEmpty() }
        logger.log(Level.FINE, "Found ${stereotypesClasses.size} Minecraft Stereotypes in $projectPackagePath")
        startDependencyInjection()
    }

    abstract fun startDependencyInjection()
    abstract fun <T : Any> getExistingBinding(type: KClass<T>, qualifier: String? = null): T?
    abstract fun <T : Any> getInstance(type: KClass<T>, qualifier: String? = null): T?

    abstract fun getQualifier(annotation: List<Annotation>): String?

    override fun onLoad() {
        scanForMinecraftAnnotationsInClassesOnLoad()
        ServerEventsHelper.triggerOnLoad(this)
    }

    final override fun onEnable() {
        scanForMinecraftAnnotationsInClassesOnEnable()
        ServerEventsHelper.triggerOnEnable(this)
    }

    final override fun onDisable() {
        ServerEventsHelper.triggerOnDisable(this)
    }

    private fun scanForMinecraftStereotypes(): List<KClass<*>> {
        val cfg = ConfigurationBuilder()
            .addClassLoaders(this.classLoader)
            .forPackage(projectPackagePath, this.classLoader)

        val reflections = Reflections(cfg)
        return stereotypes
            .map { reflections.getTypesAnnotatedWith(it.javaObjectType) }
            .flatten()
            .map { it.kotlin }
    }

    fun getParameterMap(parameters: List<KParameter>, vararg additional: Any?): Map<KParameter, Any?> = parameters.associateWith { parameter ->
        val type = parameter.type.classifier as KClass<*>

        if (parameter.kind == KParameter.Kind.INSTANCE) {
            return@associateWith getExistingBinding(type)
        }

        additional.filterNotNull().firstOrNull { type.isInstance(it) }
            ?: error("Could not find a value for parameter ${parameter.name} of type ${parameter.type}")
    }

    private fun scanForMinecraftAnnotationsInClassesOnLoad() {
        controllerClasses.forEach { clazz ->
            clazz.functions.forEach { func ->
                func.annotations.forEach { annotation ->
                    when (annotation) {
                        is OnEnable -> {
                            ServerEventsHelper.registerOnEnable(func)
                        }

                        is OnDisable -> {
                            ServerEventsHelper.registerOnDisable(func)
                        }

                        is OnLoad -> {
                            ServerEventsHelper.registerOnLoad(func)
                        }
                    }
                }
            }
        }
    }

    private fun scanForMinecraftAnnotationsInClassesOnEnable() {
        controllerClasses.forEach { clazz ->
            clazz.functions.forEach { func ->
                func.annotations.forEach { annotation ->
                    when (annotation) {
                        is Command -> {
                            CommandsHelper.registerCommand(this, annotation, func)
                        }

                        is TabComplete -> {
                            CommandsHelper.registerTabComplete(this, annotation, func)
                        }

                        is Event -> {
                            EventHelper.registerEvent(this, annotation, func)
                        }
                    }
                }
            }
        }
    }

    // Lock this stuff down
    final override fun onTabComplete(sender: CommandSender, command: org.bukkit.command.Command, alias: String, args: Array<out String>): List<String> {
        sender.sendMessage(text("An error occurred while executing this command. Please contact the server administrators."))
        logger.warning("OnTabComplete was called for ${command.name} with alias $alias and args ${args.joinToString(", ")} but no tab complete was registered.")
        return emptyList<String>().toMutableList()
    }

    final override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage(text("An error occurred while executing this command. Please contact the server administrators."))
        logger.warning("OnCommand was called for ${command.name} with alias $label and args ${args.joinToString(", ")} but no command was registered.")
        return false
    }
}
