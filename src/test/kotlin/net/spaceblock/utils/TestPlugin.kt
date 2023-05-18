package net.spaceblock.utils

import net.spaceblock.utils.di.SpringBootJavaPlugin

class TestPlugin: SpringBootJavaPlugin() {
    override val projectPackagePath: String
        get() = "net.spaceblock.utils"
}
