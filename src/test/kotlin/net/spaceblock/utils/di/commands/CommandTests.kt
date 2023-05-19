package net.spaceblock.utils.di.commands

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.kotest.assertions.print.print
import io.kotest.matchers.shouldBe
import net.spaceblock.utils.di.TestPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
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
        val player = server.addPlayer()
        server.executePlayer("test").assertSucceeded()
        server.executePlayer("test2").assertSucceeded()
        try {
            server.executePlayer("test21i03023")
        } catch (e: Exception) {
            e::class shouldBe NullPointerException::class
        }
    }

    @Test
    fun `check command is run`() {
        val ctc = plugin.getDI(CommandTestController::class)
        ctc?.i = 0
        server.addPlayer()
        repeat(10) {
            server.executePlayer("test").assertSucceeded()
            server.executePlayer("test2").assertSucceeded()
        }
        ctc?.i shouldBe 20
    }

    @Test
    fun `should return value work`() {
        server.addPlayer()
        server.executePlayer("test").assertSucceeded()
        server.executePlayer("test2").assertSucceeded()
        server.executePlayer("test3").assertFailed()
        server.executePlayer("test4").assertFailed()
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
}
