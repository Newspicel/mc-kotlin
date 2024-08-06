package dev.newspicel.di.events

import dev.newspicel.di.DIJavaPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventException
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import java.util.logging.Level
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspendBy

object EventHelper {

    private val eventScope = CoroutineScope(Dispatchers.Default)

    private val emptyListener = object : Listener {}

    fun registerEvent(plugin: DIJavaPlugin, eventAnnotation: Event, func: KFunction<*>) {
        val eventClass = eventAnnotation.event
        plugin.server.pluginManager.registerEvent(
            eventClass.java,
            emptyListener,
            eventAnnotation.priority,
            createEventExecutor(plugin, func, eventAnnotation),
            plugin,
            eventAnnotation.ignoreCancelled,
        )
    }

    private fun createEventExecutor(plugin: DIJavaPlugin, func: KFunction<*>, eventAnnotation: Event): EventExecutor = EventExecutor { _, event ->
        if (!eventAnnotation.event.isInstance(event)) error("Event is not instance of ${eventAnnotation.event} it is instance of ${event::class}, this should never happen!")

        val params = plugin.getParameterMap(func.parameters, event)

        try {
            val isEventCancellable = event is org.bukkit.event.Cancellable

            if (isEventCancellable) {
                runBlocking(eventScope.coroutineContext) {
                    func.callSuspendBy(params)
                }
            } else {
                eventScope.launch {
                    try {
                        func.callSuspendBy(params)
                    } catch (e: Exception) {
                        plugin.logger.log(Level.SEVERE, "Failed to execute event ${eventAnnotation.event.simpleName}", e)
                    }
                }
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Failed to execute event ${eventAnnotation.event.simpleName}", e)
            throw EventException(e)
        }
    }
}
