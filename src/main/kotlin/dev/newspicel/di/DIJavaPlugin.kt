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
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.functions

abstract class DIJavaPlugin : JavaPlugin() {

    private val stereotypes = arrayOf(MinecraftController::class, Service::class, Repository::class)

    protected lateinit var stereotypesClasses: List<KClass<*>>
    private lateinit var controllerClasses: List<KClass<*>>

    open val projectPackagePath: String
        get() = this.javaClass.`package`.name

    abstract fun startDI()
    abstract fun <T : Any> getExistingBinding(type: KClass<T>, qualifier: String? = null): T?
    abstract fun <T : Any> getInstance(type: KClass<T>, qualifier: String? = null): T?

    abstract fun getQualifier(annotation: List<Annotation>): String?

    override fun onLoad() {
        logger.info("Scanning for Minecraft controllers in $projectPackagePath")
        stereotypesClasses = scanForMinecraftStereotypes()
        controllerClasses = stereotypesClasses.filter { it.findAnnotations(MinecraftController::class).isNotEmpty() }
        logger.info("Found ${stereotypesClasses.size} Minecraft Stereotypes in $projectPackagePath")
        startDI()
        scanForMinecraftAnnotationsInClassesOnLoad()

        ServerEventsHelper.triggerOnLoad(this)
    }

    final override fun onEnable() {
        ServerEventsHelper.triggerOnEnable(this)
        scanForMinecraftAnnotationsInClassesOnEnable()
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
