package net.spaceblock.utils.di

import kotlinx.coroutines.delay
import net.spaceblock.utils.di.commands.Command
import org.bukkit.entity.Player

@MinecraftController
class TestController() {

    @Command("test", playerOnly = true)
    fun test(player: Player) {
        println("test")
    }

    @Command("test2", playerOnly = true)
    suspend fun test2(player: Player) {
        delay(1000)
        println("test2")
    }

}
