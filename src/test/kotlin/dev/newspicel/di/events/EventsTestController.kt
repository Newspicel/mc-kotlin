package dev.newspicel.di.events

import io.papermc.paper.event.player.AsyncChatEvent
import dev.newspicel.di.MinecraftController
import dev.newspicel.di.events.Event
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
