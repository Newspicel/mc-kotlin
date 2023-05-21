package net.spaceblock.utils.di.serverevents

import net.spaceblock.utils.di.MinecraftController
import org.bukkit.plugin.java.JavaPlugin

@MinecraftController
class ServerEventsTestController {

    var onEnable = false
    var onDisable = false
    var onLoad = false

    @OnEnable
    fun onEnable(plugin: JavaPlugin) {
        onEnable = true
        plugin.server.scheduler.runTaskAsynchronously(
            plugin,
            Runnable {
                println("Hello from onEnable!")
            },
        )
    }

    @OnDisable
    fun onDisable() {
        onDisable = true
    }

    @OnLoad
    fun onLoad() {
        onLoad = true
    }
}
