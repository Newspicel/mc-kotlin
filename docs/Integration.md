# Integration

Integrate this Lib into your plugin.

## Gradle

```kotlin

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/spaceblocknet/mc-kotlin")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation("net.spaceblock.utils:mc-kotlin:$version")
}

```

## PaperMC
You also need to setup a PluginLoader in your paper-plugin.yml

```yaml
name: TestPlugin
version: '1.0'
main: MainClass
api-version: 1.19
loader: MainPluginLoader // Path to Class that Implement PluginLoader
```

```kotlin
class MainPluginLoader : PluginLoader {
    override fun classloader(classpathBuilder: PluginClasspathBuilder) {
        addMcKotlinLibs(classpathBuilder)
    }
}
```
