package net.spaceblock.utils.paper

import io.papermc.paper.plugin.bootstrap.PluginProviderContext
import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.library.ClassPathLibrary

class PluginClasspathBuilderMock : PluginClasspathBuilder {

    private val libraries = mutableListOf<ClassPathLibrary>()

    override fun addLibrary(classPathLibrary: ClassPathLibrary): PluginClasspathBuilder {
        libraries.add(classPathLibrary)
        return this
    }

    override fun getContext(): PluginProviderContext {
        throw NotImplementedError()
    }

    fun getLibraries(): List<ClassPathLibrary> {
        return libraries
    }
}
