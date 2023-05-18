package net.spaceblock.utils.di

import java.util.logging.Logger

fun logger(plugin: DIJavaPlugin) = lazy { plugin.getExistingBinding(Logger::class, "pluginLogger")!! }
