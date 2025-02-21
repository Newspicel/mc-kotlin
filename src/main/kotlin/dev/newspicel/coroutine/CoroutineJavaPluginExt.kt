package dev.newspicel.coroutine

import dev.newspicel.utils.pluginLazy
import kotlinx.coroutines.*
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

fun Plugin.launch(
    context: CoroutineContext = syncDispatcher,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
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
