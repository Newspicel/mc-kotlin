package net.spaceblock.utils.di

import kotlinx.coroutines.runBlocking
import java.util.logging.Logger
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.instanceParameter

fun logger(plugin: DIJavaPlugin) = plugin.getDI(Logger::class, plugin.name)!!

fun KCallable<*>.callOrSuspendCallBy(args: Map<KParameter, Any?>): Any? {
    if (isSuspend) {
        return runBlocking {
            return@runBlocking callSuspendBy(args)
        }
    } else {
        return callBy(args)
    }
}
