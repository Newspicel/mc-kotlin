@file:Suppress("unused")

package dev.newspicel.scheduler

import dev.newspicel.coroutine.asyncDispatcher
import dev.newspicel.coroutine.launch
import org.bukkit.plugin.Plugin

private fun suspendToRunnable(plugin: Plugin, block: suspend () -> Unit) = Runnable { plugin.launch { block() } }
private fun suspendToAsynchronouslyRunnable(plugin: Plugin, block: suspend () -> Unit) = Runnable { plugin.launch(plugin.asyncDispatcher) { block() } }

fun runTask(plugin: Plugin, block: () -> Unit) = plugin.server.scheduler.runTask(plugin, block)

fun runSuspendTask(plugin: Plugin, block: suspend () -> Unit) = plugin.launch { block() }

fun runTaskAsynchronously(plugin: Plugin, block: () -> Unit) = plugin.server.scheduler.runTaskAsynchronously(plugin, block)

fun runSuspendTaskAsynchronously(plugin: Plugin, block: suspend () -> Unit) = plugin.launch(context = plugin.asyncDispatcher) { block() }

fun runTaskLater(plugin: Plugin, delay: Long, block: () -> Unit) = plugin.server.scheduler.runTaskLater(plugin, block, delay)

fun runSuspendTaskLater(plugin: Plugin, delay: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskLater(plugin, suspendToRunnable(plugin, block), delay - 1L)

fun runTaskLaterAsynchronously(plugin: Plugin, delay: Long, block: () -> Unit) = plugin.server.scheduler.runTaskLaterAsynchronously(plugin, block, delay)

fun runSuspendTaskLaterAsynchronously(plugin: Plugin, delay: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskLaterAsynchronously(plugin, suspendToAsynchronouslyRunnable(plugin, block), delay - 1L)

fun runTaskTimer(plugin: Plugin, delay: Long, period: Long, block: () -> Unit) = plugin.server.scheduler.runTaskTimer(plugin, block, delay, period)

fun runSuspendTaskTimer(plugin: Plugin, delay: Long, period: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskTimer(plugin, suspendToRunnable(plugin, block), delay, period - 1L)

fun runTaskTimerAsynchronously(plugin: Plugin, delay: Long, period: Long, block: () -> Unit) = plugin.server.scheduler.runTaskTimerAsynchronously(plugin, block, delay, period)

fun runSuspendTaskTimerAsynchronously(plugin: Plugin, delay: Long, period: Long, block: suspend () -> Unit) = plugin.server.scheduler.runTaskTimerAsynchronously(plugin, suspendToAsynchronouslyRunnable(plugin, block), delay - 1L, period)
