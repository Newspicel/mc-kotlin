package net.spaceblock.utils.utils

import org.bukkit.plugin.java.JavaPlugin
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
fun <P, T> contextLazy(initializer: P.() -> T): ReadOnlyProperty<P, T> = UnsafeContextLazy(initializer)

fun <T> pluginLazy(initializer: JavaPlugin.() -> T): ReadOnlyProperty<JavaPlugin, T> = UnsafeContextLazy(initializer)

private class UnsafeContextLazy<P, out T>(private val initializer: P.() -> T) : ReadOnlyProperty<P, T> {
    private var value: T? = null

    override fun getValue(thisRef: P, property: KProperty<*>): T {
        if (null == value) {
            value = thisRef.initializer()
        }
        return value ?: throw IllegalStateException("Property ${property.name} failed to initialize with initializer.")
    }
}
