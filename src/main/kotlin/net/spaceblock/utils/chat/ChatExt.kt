package net.spaceblock.utils.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration

fun text(string: String) = Component.text(string)
fun join(joinCfg: JoinConfiguration = JoinConfiguration.newlines(), vararg components: Component) = Component.join(joinCfg, *components)

fun translatable(key: String, fallback: String) = Component.translatable(key, fallback)
fun translatable(key: String, fallback: Component) = Component.translatable(key, fallback)
