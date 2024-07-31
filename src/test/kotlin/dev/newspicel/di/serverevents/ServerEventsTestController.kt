package dev.newspicel.di.serverevents

import dev.newspicel.di.MinecraftController
import dev.newspicel.di.serverevents.OnDisable
import dev.newspicel.di.serverevents.OnEnable
import dev.newspicel.di.serverevents.OnLoad
import org.bukkit.plugin.java.JavaPlugin

@MinecraftController
class ServerEventsTestController(
    private val plugin: JavaPlugin,
) {

    var onEnable = false
    var onDisable = false
    var onLoad = false

    @OnEnable
    fun onEnable() {
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
