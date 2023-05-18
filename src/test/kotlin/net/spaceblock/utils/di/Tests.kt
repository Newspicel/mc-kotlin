package net.spaceblock.utils.di

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test


open class Tests {

    private lateinit var server: ServerMock
    private lateinit var plugin: TestPlugin

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(TestPlugin::class.java)
    }

    @Test
    fun `is command registered`() {
        /*val testController = plugin.getDI(TestController::class)
        testController shouldNotBe null*/
        server.addPlayer()
        //server.executePlayer("test")
        //server.executePlayer("test2")
    }
}
