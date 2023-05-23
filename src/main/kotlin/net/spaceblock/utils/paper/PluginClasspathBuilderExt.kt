package net.spaceblock.utils.paper

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.RemoteRepository

inline fun PluginClasspathBuilder.addMavenLibrary(builder: Maven.() -> Unit) {
    val maven = Maven()
    builder(maven)
    this.addLibrary(maven.build())
}

fun addMcKotlinLibs(pluginClasspathBuilder: PluginClasspathBuilder) {
    pluginClasspathBuilder.addMavenLibrary {
        mavenCentral()

        dependency("com.google.inject:guice:6.0.0")
    }
}

class Maven {
    private val library = MavenLibraryResolver()

    fun repository(
        name: String,
        url: String,
        type: String = "default",
    ) {
        val repository = RemoteRepository.Builder(name, type, url)
            .build()

        library.addRepository(repository)
    }

    fun mavenCentral() {
        repository("central", "https://repo.maven.apache.org/maven2/")
    }

    fun dependency(group: String, artifact: String, version: String) {
        library.addDependency(Dependency(DefaultArtifact("$group:$artifact:$version"), null))
    }

    fun dependency(group: String, artifact: String, version: String, classifier: String) {
        library.addDependency(Dependency(DefaultArtifact("$group:$artifact:$classifier:$version"), null))
    }

    fun dependency(string: String) {
        library.addDependency(Dependency(DefaultArtifact(string), null))
    }

    fun build(): MavenLibraryResolver {
        return library
    }
}
