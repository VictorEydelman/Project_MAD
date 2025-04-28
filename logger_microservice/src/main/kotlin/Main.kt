package ru.itmo

import KeyDBClient
import LoggerRequestDTO
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val keydb = KeyDBClient()

    Logging.info("Start listening for logs...")

    keydb.subscribe("log", LoggerRequestDTO::class.java, true) {
        Logging.handle(it)
    }

    Logging.info("Stopped listening for logs")
}
