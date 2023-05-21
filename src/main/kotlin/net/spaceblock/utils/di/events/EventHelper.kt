package net.spaceblock.utils.di.events

import kotlinx.coroutines.runBlocking
import net.spaceblock.utils.coroutine.asyncDispatcher
import net.spaceblock.utils.coroutine.launch
import net.spaceblock.utils.di.DIJavaPlugin
import org.bukkit.event.EventException
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import java.util.logging.Level
import kotlin.reflect.KCallable
import kotlin.reflect.full.callSuspendBy

object EventHelper {

    private val emptyListener = object : Listener {}

    fun registerEvent(plugin: DIJavaPlugin, eventAnnotation: Event, func: KCallable<*>) {
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

    private fun createEventExecutor(plugin: DIJavaPlugin, func: KCallable<*>, eventAnnotation: Event): EventExecutor = EventExecutor { _, event ->
        if (!eventAnnotation.event.isInstance(event)) error("Event is not instance of ${eventAnnotation.event}, this should never happen!")

        val params = plugin.getParameterMap(func.parameters, event)

        try {
            val isEventCancellable = event is org.bukkit.event.Cancellable

            if (isEventCancellable) {
                runBlocking {
                    func.callSuspendBy(params)
                }
            } else {
                plugin.launch(plugin.asyncDispatcher) {
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
