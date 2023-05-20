package net.spaceblock.utils.di

import org.springframework.context.annotation.AnnotationConfigApplicationContext

open class TestPlugin : SpringJavaPlugin() {
    override fun test(applicationContext: AnnotationConfigApplicationContext) {
        println("test")
    }

    override val projectPackagePath: String
        get() = "net.spaceblock.utils.di"
}
