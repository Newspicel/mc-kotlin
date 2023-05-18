package net.spaceblock.utils.di

open class TestPlugin: SpringBootJavaPlugin() {
     override val projectPackagePath: String
        get() = "net.spaceblock.utils.di"
}
