package net.spaceblock.utils.di

import org.reflections.Reflections
import kotlin.reflect.KClass

open class TestPlugin : GuiceJavaPlugin() {

    override val projectPackagePath: String
        get() = "net.spaceblock.utils.di"

    override fun scanForMinecraftStereotypes(annotation: Array<KClass<out Annotation>>, packagePath: String): List<KClass<*>> {
        val reflections = Reflections(packagePath)
        return annotation
            .map { reflections.getTypesAnnotatedWith(it.java) }
            .flatten()
            .map { it.kotlin }
    }
}
