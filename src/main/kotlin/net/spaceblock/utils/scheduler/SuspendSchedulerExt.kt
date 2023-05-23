package net.spaceblock.utils.scheduler

import net.spaceblock.utils.coroutine.asyncDispatcher
import net.spaceblock.utils.coroutine.launch
import net.spaceblock.utils.coroutine.syncDispatcher
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun suspendRunTask(plugin: JavaPlugin, block: suspend () -> Unit) = suspendCoroutine {
    plugin.launch(plugin.syncDispatcher) {
        block()
        it.resume(Unit)
    }
}

suspend fun suspendRunTaskAsynchronously(plugin: JavaPlugin, block: suspend () -> Unit) = suspendCoroutine {
    plugin.launch(plugin.asyncDispatcher) {
        block()
        it.resume(Unit)
    }
}
