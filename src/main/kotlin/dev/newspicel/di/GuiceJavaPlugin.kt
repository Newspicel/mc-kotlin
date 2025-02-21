package dev.newspicel.di

import com.google.inject.*
import com.google.inject.name.Named
import com.google.inject.name.Names
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.javaConstructor

abstract class GuiceJavaPlugin : DIJavaPlugin() {

    private lateinit var injector: Injector

    override fun startDependencyInjection() {
        val module = MinecraftGuiceModule(getDefaultBindings(), getDefaultAnnotatedBindings(), stereotypesClasses)

        try {
            injector = Guice.createInjector(module) ?: error("Could not create Guice injector")

            stereotypesClasses
                .filter { it.findAnnotations(MinecraftController::class).isNotEmpty() }
                .forEach {
                    injector.getInstance(it.java)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
            return
        }
    }

    override fun <T : Any> getExistingBinding(type: KClass<T>, qualifier: String?): T? {
        val key = if (qualifier == null) {
            Key.get(type.java)
        } else {
            Key.get(type.java, Names.named(qualifier))
        }

        return injector.getExistingBinding(key)?.source?.let {
            injector.getInstance(key)
        }
    }

    override fun <T : Any> getInstance(type: KClass<T>, qualifier: String?): T? {
        val key = if (qualifier == null) {
            Key.get(type.java)
        } else {
            Key.get(type.java, Names.named(qualifier))
        }

        return injector.getInstance(key)
    }

    override fun getQualifier(annotation: List<Annotation>): String? {
        return annotation
            .filter { it.annotationClass == Named::class }
            .map { it as Named }
            .firstOrNull()
            ?.value
    }
}

class MinecraftGuiceModule(
    private val defaultBindings: Map<KClass<*>, Any>,
    private val defaultAnnotatedBindings: Map<KClass<*>, Pair<String, Any>>,
    private val allStereotypes: List<KClass<*>>,
) : AbstractModule() {
    override fun configure() {
        defaultBindings.forEach { (type, instance) ->
            bind(type.java as Class<Any>).toInstance(instance)
        }

        defaultAnnotatedBindings.forEach { (type, instance) ->
            bind(type.java as Class<Any>).annotatedWith(Names.named(instance.first)).toInstance(instance.second)
        }

        allStereotypes.forEach { stereotype ->
            val type = stereotype.java as Class<Any>
            val constructor = stereotype.constructors.first().javaConstructor
                ?: error("Could not find constructor for $stereotype")

            bind(type).toConstructor(constructor).`in`(Singleton::class.java)
        }
    }
}
