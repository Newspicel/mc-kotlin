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
    fun test() {
        println("test")
        i++
    }

    @TabComplete("test")
    fun testTab(): List<String> {
       return listOf("test")
    }

    @Command("test2", playerOnly = true)
    suspend fun test2() {
        println("test2")
        i++
    }

    @Command("test-op", playerOnly = true)
    @IsOp
    suspend fun op() {
        println("test-op")
    }

    @Command("test-permission", playerOnly = true)
    @HasPermission("test")
    suspend fun permission() {
        println("test-permission")
    }

    @Command("test3", playerOnly = true)
    fun test3(): Boolean {
        return false
    }

    @Command("test4", playerOnly = true)
    suspend fun test4(): Boolean {
        delay(1)
        return false
    }

}
