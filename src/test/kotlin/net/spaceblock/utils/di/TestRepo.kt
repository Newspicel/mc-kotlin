package net.spaceblock.utils.di

import kotlinx.coroutines.delay
import org.springframework.stereotype.Repository

@Repository
class TestRepo {

    suspend fun loadDatabase(): String {
        println("Loading database...")
        delay(1000)
        return "Database loaded!"
    }
}
