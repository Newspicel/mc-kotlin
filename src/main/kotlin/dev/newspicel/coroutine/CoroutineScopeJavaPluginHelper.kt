package dev.newspicel.coroutine

import kotlinx.coroutines.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.util.logging.Level

internal object CoroutineScopeJavaPluginHelper {

    fun createScope(plugin: Plugin, syncDispatcher: CoroutineDispatcher): CoroutineScope {
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
