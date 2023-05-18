package net.spaceblock.utils.di.registry

import net.spaceblock.utils.di.DIJavaPlugin
import net.spaceblock.utils.di.callOrSuspendCallBy
import kotlin.reflect.KCallable

object StartAndStopRegistry {

    private val onEnable = mutableListOf<KCallable<*>>()

    private val onDisable = mutableListOf<KCallable<*>>()

    private val onLoad = mutableListOf<KCallable<*>>()

    fun registerOnEnable(function: KCallable<*>) {
        onEnable.add(function)
    }

    fun registerOnDisable(function: KCallable<*>) {
        onDisable.add(function)
    }

    fun registerOnLoad(function: KCallable<*>) {
        onLoad.add(function)
    }

    fun triggerOnEnable(plugin: DIJavaPlugin) {
        onEnable.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            function.callOrSuspendCallBy(params)
        }
    }

    fun triggerOnDisable(plugin: DIJavaPlugin) {
        onDisable.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            function.callOrSuspendCallBy(params)
        }
    }

    fun triggerOnLoad(plugin: DIJavaPlugin) {
        onLoad.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            function.callOrSuspendCallBy(params)
        }
    }
}
