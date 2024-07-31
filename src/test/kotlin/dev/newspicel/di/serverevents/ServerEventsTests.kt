package dev.newspicel.di.serverevents

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.kotest.matchers.shouldBe
import dev.newspicel.di.TestPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
open class ServerEventsTests {

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
    fun `should work`() {
        val serverEventsTestController = plugin.getExistingBinding(ServerEventsTestController::class)
        serverEventsTestController?.onLoad shouldBe true
        serverEventsTestController?.onEnable shouldBe true
        serverEventsTestController?.onDisable shouldBe false
        MockBukkit.unmock()
        serverEventsTestController?.onDisable shouldBe true
    }
}
