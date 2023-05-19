package net.spaceblock.utils.di.commands

import kotlinx.coroutines.delay
import net.spaceblock.utils.di.MinecraftController
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@MinecraftController
class CommandTestController(
) {

    var i = 0

    @Command("test", playerOnly = true)
    fun test(player: CommandSender) {
        println("test")
        i++
    }

    @TabComplete("test")
    fun testTab(player: CommandSender): List<String> {
       return listOf("test")
    }

    @Command("test2", playerOnly = true)
    suspend fun test2(player: Player) {
        println("test2")
        i++
    }

    @Command("test-op", playerOnly = true)
    @IsOp
    suspend fun op(player: Player) {
        println("test-op")
    }

    @Command("test-permission", playerOnly = true)
    @HasPermission("test")
    suspend fun permission(player: Player) {
        println("test-permission")
    }

    @Command("test3", playerOnly = true)
    fun test3(player: CommandSender): Boolean {
        return false
    }

    @Command("test4", playerOnly = true)
    suspend fun test4(player: CommandSender): Boolean {
        delay(1)
        return false
    }

}
