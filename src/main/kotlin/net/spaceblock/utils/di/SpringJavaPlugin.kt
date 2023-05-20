package net.spaceblock.utils.di

import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.beans
import java.util.logging.Logger
import kotlin.reflect.KClass

abstract class SpringJavaPlugin : DIJavaPlugin() {

    lateinit var context: AnnotationConfigApplicationContext

    final override fun <T : Any> getDI(type: KClass<T>, qualifier: String?): T? {
        return if (qualifier != null && context.containsBean(qualifier)) {
            context.getBean(qualifier, type.java)
        } else if (context.getBeanNamesForType(type.java).isNotEmpty()) {
            context.getBean(type.java)
        } else {
            null
        }
    }

    final override fun scanForMinecraftControllers(path: String): List<KClass<*>> {
        logger.info("Scanning for Minecraft controllers in $path")
        context.scan(path)

        context.beanDefinitionNames.forEach { logger.info(it) }

        val classes = context.beanDefinitionNames
            .mapNotNull { context.getType(it) }
            //.filter { it.packageName.startsWith(path) }
            .map { it.kotlin }
            .filter { it.java.isAnnotationPresent(MinecraftController::class.java) }

        logger.info("Found ${classes.size} Minecraft controllers in $path")

        return classes
    }

    final override fun getQualifier(annotation: List<Annotation>): String? {
        return annotation
            .filter { it.annotationClass == Qualifier::class }
            .map { it as Qualifier }
            .firstOrNull()
            ?.value
    }

    final override fun startDI() {
        logger.info("Starting DI")
        context = AnnotationConfigApplicationContext()

        val beans = beans {
            bean<JavaPlugin>(isPrimary = true) { this@SpringJavaPlugin }
            bean<JavaPlugin>(name = this@SpringJavaPlugin.name) { this@SpringJavaPlugin }
            bean<Logger>(isPrimary = true, name = this@SpringJavaPlugin.name) { this@SpringJavaPlugin.logger }
            bean<Server> { this@SpringJavaPlugin.server }
        }

        beans.initialize(context)

        context.refresh()
        context.start()
    }

    final override fun stopDI() {
        logger.info("Stopping DI")
        context.stop()
    }
}
