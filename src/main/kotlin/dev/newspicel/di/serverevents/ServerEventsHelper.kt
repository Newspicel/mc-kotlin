package dev.newspicel.di.serverevents

import kotlinx.coroutines.runBlocking
import dev.newspicel.di.DIJavaPlugin
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspendBy

object ServerEventsHelper {

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
            try {
                runBlocking {
                    function.callSuspendBy(params)
                }
            } catch (e: Exception) {
                plugin.logger.log(java.util.logging.Level.SEVERE, "Failed to execute onEnable", e)
            }
        }
    }

    fun triggerOnDisable(plugin: DIJavaPlugin) {
        onDisable.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            try {
                runBlocking {
                    function.callSuspendBy(params)
                }
            } catch (e: Exception) {
                plugin.logger.log(java.util.logging.Level.SEVERE, "Failed to execute onDisable", e)
            }
        }
    }

    fun triggerOnLoad(plugin: DIJavaPlugin) {
        onLoad.forEach { function ->
            val params = plugin.getParameterMap(function.parameters)
            try {
                runBlocking {
                    function.callSuspendBy(params)
                }
            } catch (e: Exception) {
                plugin.logger.log(java.util.logging.Level.SEVERE, "Failed to execute onLoad", e)
            }
        }
    }
}
