package dev.newspicel.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import dev.newspicel.utils.pluginLazy
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun Plugin.launch(
    context: CoroutineContext = syncDispatcher,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    if (!coroutineScope.isActive) {
        return Job()
    }

    return coroutineScope.launch(context, start, block)
}

suspend fun Plugin.launchAndAwait(
    context: CoroutineContext = syncDispatcher,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    launch(context, start, block).join()
}

val Plugin.asyncDispatcher: CoroutineDispatcher by pluginLazy { MinecraftAsyncCoroutineDispatcher(this) }

val Plugin.syncDispatcher: CoroutineDispatcher by pluginLazy { MinecraftCoroutineDispatcher(this) }

val Plugin.coroutineScope: CoroutineScope by pluginLazy { CoroutineScopeJavaPluginHelper.createScope(this, this.syncDispatcher) }
