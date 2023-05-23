# Coroutine in Minecraft

## Introduction
Normal coroutine implementations in Minecraft are often built in the form of:
```kotlin
scheduler.runTask(runBlocking {
    players.teleport()
    delay(1000)
    players.teleport()
})
```
Which would mean that the main thread here waits a second because we actually only have one Runnable that runs runBlocking.

Here it is implemented in such a way that in the example from above the Main Thread is released again with the delay().

## Usage
Mainly there are two method and 3 variables in this package.
```kotlin
plugin.launch {
   //Do something on the main thread: runTask
}

plugin.launch(plugin.asyncDispatcher) {
   //Do something on the async thread in Minecraft: runTaskAsynchronously
}

plugin.launchAndAwait(plugin.syncDispatcher) {
   //Do something on the main thread and wait for it: runTask
}

val JavaPlugin.asyncDispatcher: CoroutineDispatcher
val JavaPlugin.syncDispatcher: CoroutineDispatcher
val JavaPlugin.coroutineScope: CoroutineScope

```

plugin.launch() use default the syncDispatcher. But there are a few option on run this Method.

