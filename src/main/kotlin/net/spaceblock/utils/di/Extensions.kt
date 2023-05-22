package net.spaceblock.utils.di

import java.util.logging.Logger

fun logger(plugin: DIJavaPlugin) = lazy { plugin.getDI(Logger::class, "pluginLogger")!! }
