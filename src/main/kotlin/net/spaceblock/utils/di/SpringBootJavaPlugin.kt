package net.spaceblock.utils.di

import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.beans
import java.util.logging.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class SpringBootJavaPlugin : DIJavaPlugin() {

    private lateinit var context: AnnotationConfigApplicationContext

    final override fun <T : Any> getDI(type: KClass<T>, qualifier: String?): T? {
        return if (qualifier == null) {
            context.getBean(type.java)
        } else {
            context.getBean(qualifier, type.java)
        }
    }

    final override fun scanForMinecraftControllers(packagePath: String): List<KClass<*>> {
        context.scan(packagePath)

        return context.beanDefinitionNames
            .mapNotNull { context.getType(it) }
            .map { it.kotlin }
            .filter { it.isSubclassOf(Command::class) }
    }

    final override fun getQualifier(annotation: List<Annotation>): String? {
        return annotation
            .filter { it.annotationClass == Qualifier::class }
            .map { it as Qualifier }
            .firstOrNull()
            ?.value
    }

    final override fun startDI(mcSingletons: Map<KClass<*>, Any>) {
        context = AnnotationConfigApplicationContext()

        val beans = beans {
            bean<JavaPlugin>(isPrimary = true) { this@SpringBootJavaPlugin }
            bean<JavaPlugin>(name = this@SpringBootJavaPlugin.name) { this@SpringBootJavaPlugin }
            bean<Logger>(isPrimary = true) { this@SpringBootJavaPlugin.logger }
            bean<ComponentLogger>(isPrimary = true) { this@SpringBootJavaPlugin.componentLogger }
            bean<Server> { this@SpringBootJavaPlugin.server }
        }

        beans.initialize(context)

        context.start()
    }

    final override fun stopDI() {
        context.stop()
    }
}
