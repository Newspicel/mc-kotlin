package net.spaceblock.utils.di.registry

import net.spaceblock.utils.di.DIJavaPlugin
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KFunction

object StartAndStopRegistry {

    private val onEnable = mutableListOf<KFunction<*>>()

    private val onDisable = mutableListOf<KFunction<*>>()

    private val onLoad = mutableListOf<KFunction<*>>()

    fun registerOnEnable(function: KFunction<*>) {
        onEnable.add(function)
    }

    fun registerOnDisable(function: KFunction<*>) {
        onDisable.add(function)
    }

    fun registerOnLoad(function: KFunction<*>) {
        onLoad.add(function)
    }

    fun triggerOnEnable(plugin: DIJavaPlugin) {
        onEnable.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            function.callBy(params)
        }
    }

    fun triggerOnDisable(plugin: DIJavaPlugin) {
        onDisable.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            function.callBy(params)
        }
    }

    fun triggerOnLoad(plugin: DIJavaPlugin) {
        onLoad.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            function.callBy(params)
        }
    }
}
