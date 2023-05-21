package net.spaceblock.utils.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.spaceblock.utils.utils.pluginLazy
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext

fun JavaPlugin.launch(
    context: CoroutineContext = syncDispatcher,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    if (!coroutineScope.isActive) {
        return Job()
    }

    return coroutineScope.launch(context, start, block)
}

val JavaPlugin.asyncDispatcher: CoroutineDispatcher by pluginLazy { MinecraftAsyncCoroutineDispatcher(this) }

val JavaPlugin.syncDispatcher: CoroutineDispatcher by pluginLazy { MinecraftCoroutineDispatcher(this) }

val JavaPlugin.coroutineScope: CoroutineScope by pluginLazy { CoroutineScopeJavaPluginHelper.createScope(this, this.syncDispatcher) }
