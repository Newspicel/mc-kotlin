# Dependency Injection

In Spring you use Layered Architecture with Dependency Injection. In Minecraft you now can use this too.
This means in Spring you have @RestController, @Service, @Repository and @Component. In Minecraft we now have @MinecraftController, @Service and @Repository.

In the Controller you put the Input Logic, in the Service the Business Logic and in the Repository the Data Access Logic.

In the Background you have the Plugin Class that extends GuiceJavaPlugin. This is the Main Class for the Dependency Injection.
And we use Guice for the Dependency Injection.

## Usage

Main Plugin Class:
```kotlin
open class GamePlugin : GuiceJavaPlugin() {
    override val projectPackagePath: String
        get() = "dev.newspicel.game"
}
```

Controller (Commands):
```kotlin
@MinecraftController
class TestController(
  private val testService: TestService,
) {
  @Command("test")
  fun test() {
    testService.test()
  }

  @Command("test2")
  suspend fun test2() {
    testService.test2()
  }

  @TabComplete("test2")
  fun test2TabComplete() {
    return listOf("test", "test2")
  }
}
```

Commands are always async.


Controller (Events):
```kotlin
@MinecraftController
class TestController(
  private val testService: TestService,
) {
  @Event(PlayerJoinEvent::class)
  fun onPlayerJoin(event: PlayerJoinEvent) {
    testService.test(event.player)
  }

  @Event(PlayerJoinEvent::class)
  suspend fun onPlayerJoin(event: PlayerJoinEvent) {
    testService.test2(event.player)
  }
}
```

Events are always async, expect they are cancelable.


Controller (ServerEvents):
```kotlin
@MinecraftController
class TestController(
  private val testService: TestService,
) {
  @OnEnable
  fun onEnable() {

  }

  @OnDisable
  fun onDisable() {
  }

  @OnLoad
  fun onLoad() {
  }
}
```

ServerEvents are always sync, to ensure they are run before the start of the rest.


