package dev.newspicel.di.commands

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import dev.newspicel.di.TestPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.NullPointerException

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
        server.executePlayer("test").assertSucceeded()
        server.executePlayer("test2").assertSucceeded()
        try {
            server.executePlayer("test21i03023")
        } catch (e: Exception) {
            e::class shouldBe NullPointerException::class
        }
    }

    @Test
    fun `check if alias is registered`() {
        server.addPlayer()
        server.executePlayer("test-alias").assertSucceeded()
    }

    @Test
    fun `check command is run`(): Unit = runBlocking {
        val ctc = plugin.getExistingBinding(CommandTestController::class)
        ctc?.i = 0
        server.addPlayer()
        repeat(10) {
            server.executePlayer("test").assertSucceeded()
            server.executePlayer("test2").assertSucceeded()
        }
        delay(4000)
        ctc?.i shouldBe 19
    }

    @Test
    fun `should return value work`() {
        server.addPlayer()
        server.executePlayer("test").assertSucceeded()
        server.executePlayer("test2").assertSucceeded()
    }

    @Test
    fun `check if is player`() {
        server.executeConsole("test").assertFailed()
        server.addPlayer()
        server.executePlayer("test").assertSucceeded()
    }

    @Test
    fun `check if permissions checks work`() {
        val player = server.addPlayer()
        server.execute("test-op", player).assertFailed()
        server.execute("test-permission", player).assertFailed()
        server.execute("test", player).assertSucceeded()
        player.isOp = true
        server.execute("test-op", player).assertSucceeded()
        server.execute("test-permission", player).assertSucceeded()
        player.isOp = false
        player.addAttachment(plugin, "test", true)
        server.execute("test-op", player).assertFailed()
        server.execute("test-permission", player).assertSucceeded()
        player.addAttachment(plugin, "test", false)
        server.execute("test-op", player).assertFailed()
        server.execute("test-permission", player).assertFailed()
    }

    @Test
    fun `should args as list or array work`() {
        server.addPlayer()
        server.executePlayer("array", "test").assertSucceeded()
        server.executePlayer("list", "test").assertSucceeded()
    }
}
