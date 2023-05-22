package net.spaceblock.utils.di

import org.reflections.Reflections
import kotlin.reflect.KClass

open class TestPlugin : GuiceJavaPlugin() {

    override val projectPackagePath: String
        get() = "net.spaceblock.utils.di"

}
