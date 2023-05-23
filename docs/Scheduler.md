# Scheduler

## Simpler Methods for Scheduling than Bukkit.getScheduler()
```kotlin
runTask(plugin) {
    // Do something
}

runTaskAsynchronously(plugin) {
    // Do something
}

runTaskLater(plugin, 20) {
    // Do something
}

runTaskLaterAsynchronously(plugin, 20) {
    // Do something
}

runTaskTimer(plugin, 20, 20) {
    // Do something
}

runTaskTimerAsynchronously(plugin, 20, 20) {
    // Do something
}
```
You don't really need to use these, because there are not suspend.

## Suspend Methods for Scheduling
```kotlin
runSuspendTask(plugin) {
    // Do something suspend
}

runSuspendTaskAsynchronously(plugin) {
    // Do something suspend
}

runSuspendTaskLater(plugin, 20) {
    // Do something suspend
}

runSuspendTaskLaterAsynchronously(plugin, 20) {
    // Do something suspend
}

runSuspendTaskTimer(plugin, 20, 20) {
    // Do something suspend
}

runSuspendTaskTimerAsynchronously(plugin, 20, 20) {
    // Do something suspend
}
```
You don't really need to use these, because for runSuspendTask & runSuspendTaskAsynchronously you can use plugin.launch().
And for the other you can use:
```kotlin
plugin.launch {
  delay(20) // milliseconds
// Do something suspend
}
```

They all dont block the thread on running. They are all suspend functions and so implemented that they don't use runBlocking {}.
