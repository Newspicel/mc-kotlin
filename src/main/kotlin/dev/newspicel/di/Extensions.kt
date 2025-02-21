package dev.newspicel.di

import java.util.logging.Logger

const val PLUGIN_LOGGER_KEY = "pluginLogger"

fun logger(plugin: DIJavaPlugin) = lazy { plugin.getExistingBinding(Logger::class, PLUGIN_LOGGER_KEY)!! }
