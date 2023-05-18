package net.spaceblock.utils.di.events

import net.spaceblock.utils.di.DIJavaPlugin
import net.spaceblock.utils.di.callOrSuspendCallBy
import org.bukkit.event.EventException
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import kotlin.reflect.KCallable

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
            func.callOrSuspendCallBy(params)
        } catch (e: Exception) {
            e.printStackTrace()
            throw EventException(e)
        }
    }
}
