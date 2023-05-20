package net.spaceblock.utils.di

import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.beans
import java.util.logging.Logger
import kotlin.reflect.KClass

abstract class SpringJavaPlugin : DIJavaPlugin() {

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

    override fun getMinecraftControllers(): List<KClass<*>> = context.beanDefinitionNames
        .mapNotNull { context.getType(it) }
        //.filter { it.packageName.startsWith(path) }
        .map { it.kotlin }
        .filter { it.java.isAnnotationPresent(MinecraftController::class.java) }

    final override fun getQualifier(annotation: List<Annotation>): String? {
        return annotation
            .filter { it.annotationClass == Qualifier::class }
            .map { it as Qualifier }
            .firstOrNull()
            ?.value
    }

    override fun initDI() {
        logger.info("Initializing DI")
        context = AnnotationConfigApplicationContext()

        val beans = beans {
            bean<JavaPlugin>(isPrimary = true) { this@SpringJavaPlugin }
            bean<JavaPlugin>(name = this@SpringJavaPlugin.name) { this@SpringJavaPlugin }
            bean<Logger>(isPrimary = true, name = this@SpringJavaPlugin.name) { this@SpringJavaPlugin.logger }
            bean<Server> { this@SpringJavaPlugin.server }
        }

        beans.initialize(context)
    }

    final override fun startDI() {
        logger.info("Starting DI")
        context.refresh()
        context.start()
    }

    final override fun stopDI() {
        logger.info("Stopping DI")
        context.stop()
    }
}
