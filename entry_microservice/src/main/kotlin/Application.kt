package ru.itmo

import io.ktor.server.application.*
import ru.itmo.config.configureKeyDB
import ru.itmo.config.configureRouting
import ru.itmo.config.configureSecurity
import ru.itmo.config.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureKeyDB()
}
