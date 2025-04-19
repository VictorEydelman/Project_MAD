package ru.itmo

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import ru.itmo.config.*
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
    configureOpenAPI()
    configureRouting()
    log.info("Starting application version {}", environment.config.property("ktor.version").getString())
}
