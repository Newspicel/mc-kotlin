# Utils

## ContextLazy

  ```kotlin
  val JavaPlugin.something by pluginLazy { SomethingThatNeedsPlugin(this) }
  ```

Mainly use in the coroutine package.
