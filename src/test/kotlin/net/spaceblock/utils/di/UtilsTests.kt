package net.spaceblock.utils.di

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.kotest.assertions.print.print
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.delay
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.util.logging.Logger
import kotlin.reflect.full.functions
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
        val log = logger(plugin)
        log shouldNotBe null
        log shouldBe plugin.logger
        log shouldBe plugin.getDI(Logger::class)
    }

    @Test
    fun `should call suspend function or non suspend functions`() {
        val method1 = UtilsTests::class.functions.find { it.name == "test" }!!
        val method2 = UtilsTests::class.functions.find { it.name == "test2" }!!
        val method3 = UtilsTests::class.functions.find { it.name == "test3" }!!
        val method4 = UtilsTests::class.functions.find { it.name == "test4" }!!

        method1.isSuspend shouldBe false
        method2.isSuspend shouldBe true
        method3.isSuspend shouldBe false
        method4.isSuspend shouldBe true

        method1.parameters.joinToString(separator = ",").print()

        method1.callOrSuspendCallBy(mapOf(method1.parameters.first() to this)) shouldBe Unit
        method2.callOrSuspendCallBy(mapOf(method2.parameters.first() to this)) shouldBe Unit
        method3.callOrSuspendCallBy(mapOf(method3.parameters.first() to this)) shouldBe "test3"
        method4.callOrSuspendCallBy(mapOf(method4.parameters.first() to this)) shouldBe "test4"
    }

    fun test() {
        println("non suspend")
    }

    suspend fun test2() {
        delay(1000)
        println("suspend")
    }

    fun test3(): String {
        println("test3")
        return "test3"
    }

    suspend fun test4(): String {
        delay(1000)
        println("test4")
        return "test4"
    }
}
