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
        plugin.getExistingBinding(CommandTestController::class) shouldNotBe null
        plugin.getExistingBinding(CommandTestController::class) shouldBe plugin.getExistingBinding(CommandTestController::class)
        plugin.getExistingBinding(Server::class) shouldNotBe null
        plugin.getExistingBinding(Server::class) shouldBe plugin.getExistingBinding(Server::class)
        plugin.getExistingBinding(Server::class) shouldBe plugin.server
        plugin.getExistingBinding(JavaPlugin::class) shouldNotBe null
        plugin.getExistingBinding(JavaPlugin::class) shouldBe plugin.getExistingBinding(JavaPlugin::class)
        plugin.getInstance(TypeA::class) shouldNotBe null
        plugin.getInstance(TypeA::class) shouldBe plugin.getInstance(TypeA::class)
        plugin.getInstance(TypeB::class) shouldNotBe null
        plugin.getInstance(TypeB::class) shouldBe plugin.getInstance(TypeB::class)
        plugin.getInstance(TypeC::class) shouldNotBe plugin.getInstance(TypeC::class)
    }


}

@Service
class TypeA

@Repository
class TypeB

class TypeC
