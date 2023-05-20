package net.spaceblock.utils.di

open class TestPlugin : SpringJavaPlugin() {
    override val projectPackagePath: String
        get() = "net.spaceblock.utils.di"
}
