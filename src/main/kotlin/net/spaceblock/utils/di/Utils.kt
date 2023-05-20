package net.spaceblock.utils.di

import kotlinx.coroutines.runBlocking
import java.util.logging.Logger
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy

fun logger(plugin: DIJavaPlugin) = plugin.getDI(Logger::class, "pluginLogger")!!

fun KCallable<*>.callOrSuspendCallBy(args: Map<KParameter, Any?>): Any? {
    if (isSuspend) {
        return runBlocking {
            return@runBlocking callSuspendBy(args)
        }
    } else {
        return callBy(args)
    }
}
