package net.spaceblock.utils.di

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.spaceblock.utils.di.commands.CommandTestController
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SpringJavaPluginTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: TestPlugin

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(TestPlugin::class.java)
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun getDI() {
        plugin.getDI(CommandTestController::class) shouldNotBe null
        plugin.getDI(Server::class) shouldNotBe null
        plugin.getDI(JavaPlugin::class) shouldNotBe null
        plugin.getDI(TypeA::class) shouldNotBe null
        plugin.getDI(TypeB::class) shouldNotBe null
        plugin.getDI(TypeC::class) shouldBe null
    }

    @Test
    fun scanForMinecraftControllers() {
        plugin.scanForMinecraftControllers("net.spaceblock.utils.di.commands").size shouldBe 3
        plugin.scanForMinecraftControllers("net.spaceblock.utils.di.not.exist").size shouldBe 3
    }

    @Test
    fun getQualifier() {
        val annotations = TypeD::class.annotations
        plugin.getQualifier(annotations) shouldBe "test"
    }
}

@Service
class TypeA

@Repository
class TypeB

class TypeC

@Qualifier("test")
class TypeD
