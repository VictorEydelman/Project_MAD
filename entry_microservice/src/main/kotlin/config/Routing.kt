package ru.itmo.config

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import ru.itmo.routes.authRoutes
import ru.itmo.routes.profileRoutes
import ru.itmo.routes.sleepRoutes

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            authRoutes()
            authenticate {
                profileRoutes()
                sleepRoutes()
            }
        }
    }
}
