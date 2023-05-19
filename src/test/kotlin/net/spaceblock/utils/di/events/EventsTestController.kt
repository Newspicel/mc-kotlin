package net.spaceblock.utils.di.events

import io.papermc.paper.event.player.AsyncChatEvent
import net.spaceblock.utils.di.MinecraftController
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent

@MinecraftController
class EventsTestController {

    var test = 0

    @Event(PlayerJoinEvent::class)
    fun onJoin() {
        test++
    }

    @Event(PlayerKickEvent::class)
    fun onQuit() {
        test--
    }

    @Event(AsyncChatEvent::class)
    fun onMessage(event: AsyncChatEvent) {
        event.isCancelled = true
        throw Exception("test")
    }
}
