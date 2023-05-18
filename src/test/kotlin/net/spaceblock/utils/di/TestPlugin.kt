package net.spaceblock.utils.di

import org.bukkit.plugin.java.JavaPlugin

open class TestPlugin: JavaPlugin() {
     val projectPackagePath: String
        get() = "net.spaceblock.utils.di"
}


