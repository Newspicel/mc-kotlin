package net.spaceblock.utils.di.commands

import kotlinx.coroutines.delay
import net.spaceblock.utils.chat.text
import net.spaceblock.utils.di.MinecraftController
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@MinecraftController
class CommandTestController(
    private val plugin: JavaPlugin,
) {

    var i = 0

    @Command("test", playerOnly = true, aliases = ["test-alias"])
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
        delay(1)
        i++
    }

    @Command("test-op", playerOnly = true)
    @IsOp
    suspend fun op(player: Player) {
        player.sendMessage(text("test-op"))
        println("test-op")
        delay(1)
    }

    @Command("test-permission", playerOnly = true)
    @HasPermission("test")
    suspend fun permission() {
        println("test-permission")
        delay(1)
    }

    @Command("error")
    suspend fun error() {
        delay(1)
        throw Exception("test")
    }

    @Command("list")
    fun list(args: List<String>) {
        println(args)
    }

    @Command("array")
    fun array(array: Array<String>) {
        println(array)
    }
}
