package net.spaceblock.utils.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext

class MinecraftCoroutineDispatcher(
    private val plugin: JavaPlugin,
) : CoroutineDispatcher() {

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !plugin.server.isPrimaryThread && plugin.isEnabled
    }
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            runBlocking { block.run() }
            plugin.logger.warning("Blocking coroutine on main thread")
        } else {
            plugin.server.scheduler.runTask(plugin, block)
        }
    }
}
