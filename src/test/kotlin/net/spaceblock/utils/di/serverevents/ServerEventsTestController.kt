package net.spaceblock.utils.di.serverevents

import net.spaceblock.utils.di.MinecraftController

@MinecraftController
class ServerEventsTestController {

    var onEnable = false
    var onDisable = false
    var onLoad = false

    @OnEnable
    fun onEnable() {
        onEnable = true
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
