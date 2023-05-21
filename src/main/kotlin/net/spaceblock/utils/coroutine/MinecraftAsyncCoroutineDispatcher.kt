package net.spaceblock.utils.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext

class MinecraftAsyncCoroutineDispatcher(
    private val plugin: JavaPlugin,
) : CoroutineDispatcher() {

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        Dispatchers.Default
        return true
    }
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            runBlocking { block.run() }
            plugin.logger.warning("Blocking coroutine on main thread")
        } else {
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
        }
    }
}
