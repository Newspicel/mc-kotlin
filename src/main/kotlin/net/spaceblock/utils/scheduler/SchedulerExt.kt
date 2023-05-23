@file:Suppress("unused")

package net.spaceblock.utils.scheduler

import net.spaceblock.utils.coroutine.asyncDispatcher
import net.spaceblock.utils.coroutine.launch
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

private fun suspendToRunnable(plugin: JavaPlugin, block: suspend () -> Unit) = Runnable { plugin.launch { block() } }
private fun suspendToAsynchronouslyRunnable(plugin: JavaPlugin, block: suspend () -> Unit) = Runnable { plugin.launch(plugin.asyncDispatcher) { block() } }

fun runTask(plugin: JavaPlugin, block: () -> Unit) = plugin.server.scheduler.runTask(plugin, block)

fun runSuspendTask(plugin: JavaPlugin, block: suspend () -> Unit) = plugin.launch { block() }

fun runTaskAsynchronously(plugin: JavaPlugin, block: () -> Unit) = plugin.server.scheduler.runTaskAsynchronously(plugin, block)

fun runSuspendTaskAsynchronously(plugin: JavaPlugin, block: suspend () -> Unit) = plugin.launch(context = plugin.asyncDispatcher) { block() }

fun runTaskLater(plugin: JavaPlugin, delay: Long, block: () -> Unit) = plugin.server.scheduler.runTaskLater(plugin, block, delay)

fun runSuspendTaskLater(plugin: JavaPlugin, delay: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskLater(plugin, suspendToRunnable(plugin, block), delay - 1L)

fun runTaskLaterAsynchronously(plugin: JavaPlugin, delay: Long, block: () -> Unit) = plugin.server.scheduler.runTaskLaterAsynchronously(plugin, block, delay)

fun runSuspendTaskLaterAsynchronously(plugin: JavaPlugin, delay: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskLaterAsynchronously(plugin, suspendToAsynchronouslyRunnable(plugin, block), delay - 1L)

fun runTaskTimer(plugin: JavaPlugin, delay: Long, period: Long, block: () -> Unit) = plugin.server.scheduler.runTaskTimer(plugin, block, delay, period)

fun runSuspendTaskTimer(plugin: JavaPlugin, delay: Long, period: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskTimer(plugin, suspendToRunnable(plugin, block), delay, period - 1L)

fun runTaskTimerAsynchronously(plugin: JavaPlugin, delay: Long, period: Long, block: () -> Unit) = plugin.server.scheduler.runTaskTimerAsynchronously(plugin, block, delay, period)

fun runSuspendTaskTimerAsynchronously(plugin: JavaPlugin, delay: Long, period: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskTimerAsynchronously(plugin, suspendToAsynchronouslyRunnable(plugin, block), delay - 1L, period)
