package net.spaceblock.utils.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.plus
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

internal object CoroutineScopeJavaPluginHelper {

    fun createScope(plugin: JavaPlugin, syncDispatcher: CoroutineDispatcher): CoroutineScope {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            plugin.logger.log(Level.SEVERE, "Caught exception in coroutine context $coroutineContext", throwable)
        }
        val rootCoroutineScope = CoroutineScope(exceptionHandler)

        val scope = rootCoroutineScope + SupervisorJob() + syncDispatcher

        plugin.server.pluginManager.registerEvents(
            object : Listener {
                @EventHandler
                fun onPluginDisable(event: PluginDisableEvent) {
                    if (event.plugin == plugin) {
                        scope.coroutineContext.cancelChildren()
                        scope.cancel()
                    }
                }
            },
            plugin,
        )

        return scope
    }
}
