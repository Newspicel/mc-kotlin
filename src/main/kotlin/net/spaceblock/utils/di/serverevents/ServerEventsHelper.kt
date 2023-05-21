package net.spaceblock.utils.di.serverevents

import kotlinx.coroutines.runBlocking
import net.spaceblock.utils.di.DIJavaPlugin
import kotlin.reflect.KCallable
import kotlin.reflect.full.callSuspendBy

object ServerEventsHelper {

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
            runBlocking {
                function.callSuspendBy(params)
            }
        }
    }

    fun triggerOnDisable(plugin: DIJavaPlugin) {
        onDisable.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            runBlocking {
                function.callSuspendBy(params)
            }
        }
    }

    fun triggerOnLoad(plugin: DIJavaPlugin) {
        onLoad.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            runBlocking {
                function.callSuspendBy(params)
            }
        }
    }
}
