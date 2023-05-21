package net.spaceblock.utils.paper

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.Authentication
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.util.repository.AuthenticationBuilder

inline fun PluginClasspathBuilder.addMavenLibrary(builder: Maven.() -> Unit) {
    val maven = Maven()
    builder(maven)
    this.addLibrary(maven.build())
}

class Maven {
    private val library = MavenLibraryResolver()

    fun repository(
        name: String,
        url: String,
        type: String = "default",
        authenticator: MavenRepoAuthenticator? = null,
    ) {
        val repository = RemoteRepository.Builder(name, type, url)
            .setAuthentication(authenticator?.build())
            .build()

        library.addRepository(repository)
    }

    fun mavenCentral() {
        repository("central", "https://repo.maven.apache.org/maven2/")
    }

    fun paperMcRepo() {
        repository("papermc-repo", "https://repo.papermc.io/repository/maven-public/")
    }

    fun githubRepo(url: String, authenticator: MavenRepoAuthenticator? = null) {
        val name = url.split("/").last()
        repository(name, url, authenticator = authenticator)
    }

    fun mcKotlinRepo(authenticator: MavenRepoAuthenticator? = null) {
        githubRepo("https://maven.pkg.github.com/spaceblocknet/mc-kotlin", authenticator)
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

data class MavenRepoAuthenticator(
    var username: String? = null,
    var password: String? = null,
) {
    companion object {
        fun fromEnv(name: String): MavenRepoAuthenticator {
            return MavenRepoAuthenticator(
                System.getenv("maven_repo_${name}_username"),
                System.getenv("maven_repo_${name}_password"),
            )
        }

        fun fromEnv(usernameEnv: String, passwordEnv: String): MavenRepoAuthenticator {
            return MavenRepoAuthenticator(
                System.getenv(usernameEnv),
                System.getenv(passwordEnv),
            )
        }
    }

    fun build(): Authentication {
        return AuthenticationBuilder().addUsername(username).addPassword(password).build()
    }
}
