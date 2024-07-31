package dev.newspicel.di.events

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Event(
    val event: KClass<out Event>,
    val priority: EventPriority = EventPriority.NORMAL,
    val ignoreCancelled: Boolean = false,
)
