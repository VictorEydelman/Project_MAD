package ru.itmo

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import ru.itmo.config.configureKeyDB
import ru.itmo.config.configureRouting
import ru.itmo.config.configureSecurity
import ru.itmo.config.configureSerialization
import ru.itmo.exception.exceptionHandler

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(StatusPages) {
        exceptionHandler()
    }
    configureKeyDB()
    configureSecurity()
    configureSerialization()
    configureRouting()
    log.info("Starting application version {}", environment.config.property("ktor.version").getString())
}
