package dev.newspicel.di

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import java.util.logging.Logger
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
open class UtilsTests {

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
    fun `should get logger`() {
        val log = logger(plugin).value
        log shouldNotBe null
        log shouldBe plugin.logger
        log shouldBe plugin.getExistingBinding(Logger::class, "pluginLogger")
    }
}
