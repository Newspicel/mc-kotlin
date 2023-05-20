package net.spaceblock.utils.di

import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.beans
import java.util.logging.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

abstract class SpringJavaPlugin : DIJavaPlugin() {

    abstract fun test(applicationContext: AnnotationConfigApplicationContext)

    private lateinit var context: AnnotationConfigApplicationContext

    final override fun <T : Any> getDI(type: KClass<T>, qualifier: String?): T? {
        return if (qualifier != null && context.containsBean(qualifier)) {
            context.getBean(qualifier, type.java)
        } else if (context.getBeanNamesForType(type.java).isNotEmpty()) {
            context.getBean(type.java)
        } else {
            null
        }
    }

    final override fun scanForMinecraftControllers(path: String) {
        logger.info("Scanning for Minecraft controllers in $path")
        context.scan(path)
    }

    final override fun getMinecraftControllers(): List<KClass<*>> = context.beanDefinitionNames
        .mapNotNull { context.getType(it) }
        .map { it.kotlin }
        .filter { it.findAnnotations(MinecraftController::class).isNotEmpty() }

    final override fun getQualifier(annotation: List<Annotation>): String? {
        return annotation
            .filter { it.annotationClass == Qualifier::class }
            .map { it as Qualifier }
            .firstOrNull()
            ?.value
    }

    final override fun initDI() {
        logger.info("Initializing Dependency Injection")
        context = AnnotationConfigApplicationContext()
    }

    final override fun startDI() {
        logger.info("Starting Dependency Injection")
        val beans = beans {
            bean<JavaPlugin>(isPrimary = true) { this@SpringJavaPlugin }
            bean<JavaPlugin>(name = this@SpringJavaPlugin.name) { this@SpringJavaPlugin }
            bean<Logger>(isPrimary = true, name = this@SpringJavaPlugin.name) { this@SpringJavaPlugin.logger }
            bean<Server> { this@SpringJavaPlugin.server }
        }

        beans.initialize(context)

        test(context)

        context.refresh()
        context.beanDefinitionNames.forEach { logger.info("Found bean $it") }
        context.start()
    }

    final override fun stopDI() {
        logger.info("Stopping Dependency Injection")
        context.stop()
    }
}
