package ru.itmo.config

import io.github.smiley4.ktoropenapi.get
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itmo.dto.SimpleResponse
import ru.itmo.routes.authRoutes

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            authRoutes()

            authenticate {
                get("/test") {
                    val username = call.principal<String>()
                    call.respond(SimpleResponse.success("user: $username"))
                }
            }
        }
    }
}
