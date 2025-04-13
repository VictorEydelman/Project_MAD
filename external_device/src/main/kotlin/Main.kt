package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.example.controller.HealthDataController
import org.example.service.HealthDataService

fun main() {
    embeddedServer(Netty, port = 8909, module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    val service = HealthDataService()
    val controller = HealthDataController(service)
    routing {
        controller.setupRoutes(this)
    }
}
