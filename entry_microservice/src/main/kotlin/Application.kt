package ru.itmo

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.statuspages.*
import ru.itmo.config.*
import ru.itmo.exception.exceptionHandler
import ru.itmo.keydb.Logger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(DoubleReceive)
    install(StatusPages) {
        exceptionHandler()
    }
    configureKeyDB()
    configureSecurity()
    configureSerialization()
    configureOpenAPI()
    configureRouting()
    Logger.info("Starting application version ${environment.config.property("ktor.version").getString()}")
}
