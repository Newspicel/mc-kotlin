package dev.newspicel.di.commands

import kotlinx.coroutines.delay
import dev.newspicel.di.MinecraftController
import dev.newspicel.di.commands.Command
import dev.newspicel.di.commands.HasPermission
import dev.newspicel.di.commands.IsOp
import dev.newspicel.di.commands.TabComplete
import dev.newspicel.itemstack.flag
import dev.newspicel.itemstack.itemStack
import dev.newspicel.itemstack.meta
import dev.newspicel.itemstack.name
import dev.newspicel.itemstack.setLore
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
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
