package net.spaceblock.utils.di.events

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.spaceblock.utils.adventure.text
import net.spaceblock.utils.di.TestPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
open class EventsTests {

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
    fun `is event registered`(): Unit = runBlocking {
        val eventsTestController = plugin.getDI(EventsTestController::class)
        val player = server.addPlayer()
        delay(1000)
        eventsTestController?.test shouldBe 1
        player.kick()
        server.addPlayer()
        server.addPlayer()
        server.addPlayer()
        delay(1000)
        eventsTestController?.test shouldBe 3
    }

    @Test
    fun `check errors`() {
        val player = server.addPlayer()
        player.sendMessage(text("test"))
    }
}
