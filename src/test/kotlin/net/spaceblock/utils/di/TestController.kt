package net.spaceblock.utils.di

import net.spaceblock.utils.di.commands.Command
import net.spaceblock.utils.di.commands.IsOp
import net.spaceblock.utils.di.events.Event
import net.spaceblock.utils.di.serverevents.OnDisable
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent

@MinecraftController
class TestController(
) {

    @Command("test", playerOnly = true)
    @IsOp
    fun test(player: CommandSender) {
        println("test")
    }

    @Command("test2", playerOnly = true)
    suspend fun test2(player: Player, testRepo: TestRepo): Boolean {
        testRepo.loadDatabase()
        println("test2")

        return false
    }


    @Event(PlayerJoinEvent::class)
    fun onMove(event: PlayerJoinEvent) {
        println("onJoin")
    }

    @OnDisable
    fun onDisable() {
        println("onDisable")
    }

}
