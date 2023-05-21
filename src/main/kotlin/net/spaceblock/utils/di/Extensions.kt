package net.spaceblock.utils.di

import java.util.logging.Logger

fun logger(plugin: DIJavaPlugin) = plugin.getDI(Logger::class, "pluginLogger")!!
