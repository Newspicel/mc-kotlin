package net.spaceblock.utils.di

import kotlinx.coroutines.CoroutineDispatcher
import net.spaceblock.utils.coroutine.asyncDispatcher
import net.spaceblock.utils.coroutine.launch
import net.spaceblock.utils.coroutine.syncDispatcher
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy

// Lazy plugin logger
fun logger(plugin: DIJavaPlugin) = lazy { plugin.getDI(Logger::class, "pluginLogger")!! }

fun KCallable<*>.callOrSuspendCallBy(plugin: JavaPlugin, params: Map<KParameter, Any?>, dispatcher: CoroutineDispatcher = plugin.syncDispatcher) {
    if (isSuspend) {
        plugin.launch(dispatcher) {
            callSuspendBy(params)
        }
    } else {
        callBy(params)
    }
}
