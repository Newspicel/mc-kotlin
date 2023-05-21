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
        plugin.getDI(CommandTestController::class) shouldBe plugin.getDI(CommandTestController::class)
        plugin.getDI(Server::class) shouldNotBe null
        plugin.getDI(Server::class) shouldBe plugin.getDI(Server::class)
        plugin.getDI(Server::class) shouldBe plugin.server
        plugin.getDI(JavaPlugin::class) shouldNotBe null
        plugin.getDI(JavaPlugin::class) shouldBe plugin.getDI(JavaPlugin::class)
        plugin.getDI(TypeA::class) shouldNotBe null
        plugin.getDI(TypeA::class) shouldBe plugin.getDI(TypeA::class)
        plugin.getDI(TypeB::class) shouldNotBe null
        plugin.getDI(TypeB::class) shouldBe plugin.getDI(TypeB::class)
        plugin.getDI(TypeC::class) shouldNotBe plugin.getDI(TypeC::class)
    }

    @Test
    fun scanForMinecraftControllers() {
        plugin.scanForMinecraftStereotypes(arrayOf(MinecraftController::class), "net.spaceblock.utils.di.commands").size shouldBe 1
        plugin.scanForMinecraftStereotypes(arrayOf(MinecraftController::class), "net.spaceblock.utils.di.not.exist") shouldBe emptyList()
    }
}

@Service
class TypeA

@Repository
class TypeB

class TypeC
