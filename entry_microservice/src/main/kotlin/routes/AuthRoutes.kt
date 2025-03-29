package ru.itmo.routes

import io.ktor.server.routing.*
import io.ktor.server.response.*
import ru.itmo.dto.SimpleResponse

fun Route.authRoutes() {
    get("/login") {
        call.respond(SimpleResponse(true, "login"))
    }
}