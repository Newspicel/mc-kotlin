package dev.newspicel.di.commands

import dev.newspicel.di.TestPlugin
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
open class CommandTests {

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
    fun `is command registered`() {
        server.addPlayer()
        server.executePlayer("test").hasSucceeded() shouldBe true
        server.executePlayer("test2").hasSucceeded() shouldBe true
        try {
            server.executePlayer("test21i03023")
        } catch (e: Exception) {
            e::class shouldBe NullPointerException::class
        }
    }

    @Test
    fun `check if alias is registered`() {
        server.addPlayer()
        server.executePlayer("test-alias").hasSucceeded() shouldBe true
    }

    @Test
    fun `check command is run`(): Unit = runBlocking {
        val ctc = plugin.getExistingBinding(CommandTestController::class)
        ctc?.i = 0
        server.addPlayer()
        repeat(10) {
            server.executePlayer("test").hasSucceeded() shouldBe true
            server.executePlayer("test2").hasSucceeded() shouldBe true
        }
        delay(4000)
        ctc?.i?.shouldBeInRange(18..20)
    }

    @Test
    fun `should return value work`() {
        server.addPlayer()
        server.executePlayer("test").hasSucceeded() shouldBe true
        server.executePlayer("test2").hasSucceeded() shouldBe true
    }

    @Test
    fun `check if is player`() {
        server.executeConsole("test").hasSucceeded() shouldBe false
        server.addPlayer()
        server.executePlayer("test").hasSucceeded() shouldBe true
    }

    @Test
    fun `check if permissions checks work`() {
        val player = server.addPlayer()
        server.execute("test-op", player).hasSucceeded() shouldBe false
        server.execute("test-permission", player).hasSucceeded() shouldBe false
        server.execute("test", player).hasSucceeded() shouldBe true
        player.isOp = true
        server.execute("test-op", player).hasSucceeded() shouldBe true
        server.execute("test-permission", player).hasSucceeded() shouldBe true
        player.isOp = false
        player.addAttachment(plugin, "test", true)
        server.execute("test-op", player).hasSucceeded() shouldBe false
        server.execute("test-permission", player).hasSucceeded() shouldBe true
        player.addAttachment(plugin, "test", false)
        server.execute("test-op", player).hasSucceeded() shouldBe false
        server.execute("test-permission", player).hasSucceeded() shouldBe false
    }

    @Test
    fun `should args as list or array work`() {
        server.addPlayer()
        server.executePlayer("array", "test").hasSucceeded() shouldBe true
        server.executePlayer("list", "test").hasSucceeded() shouldBe true
    }
}
