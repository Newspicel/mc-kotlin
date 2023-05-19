package net.spaceblock.utils.di

import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.beans
import java.util.logging.Logger
import kotlin.reflect.KClass

abstract class SpringBootJavaPlugin : DIJavaPlugin() {

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

    final override fun scanForMinecraftControllers(path: String): List<KClass<*>> {
        context.scan(path)

        return context.beanDefinitionNames
            .mapNotNull { context.getType(it) }
            .filter { it.packageName.startsWith(path) }
            .map { it.kotlin }
            .filter { it.java.isAnnotationPresent(MinecraftController::class.java) }
    }

    final override fun getQualifier(annotation: List<Annotation>): String? {
        return annotation
            .filter { it.annotationClass == Qualifier::class }
            .map { it as Qualifier }
            .firstOrNull()
            ?.value
    }

    final override fun startDI() {
        context = AnnotationConfigApplicationContext()

        val beans = beans {
            bean<JavaPlugin>(isPrimary = true) { this@SpringBootJavaPlugin }
            bean<JavaPlugin>(name = this@SpringBootJavaPlugin.name) { this@SpringBootJavaPlugin }
            bean<Logger>(isPrimary = true, name = this@SpringBootJavaPlugin.name) { this@SpringBootJavaPlugin.logger }
            bean<Server> { this@SpringBootJavaPlugin.server }
        }

        beans.initialize(context)

        context.refresh()
        context.start()
    }

    final override fun stopDI() {
        context.stop()
    }
}
