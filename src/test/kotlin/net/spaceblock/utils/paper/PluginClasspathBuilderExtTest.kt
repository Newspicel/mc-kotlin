package net.spaceblock.utils.paper

import io.kotest.matchers.shouldBe
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.junit.jupiter.api.Test

class PluginClasspathBuilderExtTest {

    @Test
    fun `addMavenLibrary should add a library to the classpath`() {
        val builder = PluginClasspathBuilderMock()
        builder.addMavenLibrary {
            mavenCentral()
            dependency("org.jetbrains.kotlin", "kotlin-stdlib", "1.5.21")
        }

        val libraries = builder.getLibraries()
        assert(libraries.size == 1)
        assert(libraries[0] is MavenLibraryResolver)

        val maven = libraries[0] as MavenLibraryResolver
        maven::class.java.getDeclaredField("repositories").apply {
            isAccessible = true
            val repositories = get(maven) as List<*>
            repositories.size shouldBe 1
            repositories[0].toString() shouldBe "central (https://repo.maven.apache.org/maven2/, default, releases+snapshots)"
        }

        maven::class.java.getDeclaredField("dependencies").apply {
            isAccessible = true
            val dependencies = get(maven) as List<*>
            dependencies.size shouldBe 1
            dependencies[0].toString() shouldBe "org.jetbrains.kotlin:kotlin-stdlib:jar:1.5.21 ()"
        }
    }
}
