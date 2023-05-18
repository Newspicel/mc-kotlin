package net.spaceblock.utils.di

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy

fun logger(plugin: DIJavaPlugin) = plugin.logger

fun KCallable<*>.callOrSuspendCallBy(args: Map<KParameter, Any?>): Any? {
    if (isSuspend) {
        return runBlocking {
            return@runBlocking callSuspendBy(args)
        }
    } else {
        return callBy(args)
    }
}
