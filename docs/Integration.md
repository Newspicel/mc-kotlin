# Integration

Integrate this Lib into your plugin.

## Gradle

```kotlin

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/newspicel/mc-kotlin")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation("dev.newspicel.utils:mc-kotlin:$version")
}

```

## PaperMC

```yaml
name: TestPlugin
version: '1.0'
main: MainClass
api-version: 1.21
```

```kotlin
class MainPluginLoader : PluginLoader {
    override fun classloader(classpathBuilder: PluginClasspathBuilder) {
        addMcKotlinLibs(classpathBuilder)
    }
}
```
