package ru.itmo.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itmo.dto.AuthRequest
import ru.itmo.dto.AuthRespose
import ru.itmo.model.User
import ru.itmo.service.AuthService

fun Route.authRoutes() {
    route("/auth") {
        post<AuthRequest>("/register") { request ->
            val token = AuthService.register(request)
            val response = AuthRespose(request.username, token)
            call.respond(HttpStatusCode.Created, response)
        }

        post<AuthRequest>("/login") { request ->
            val token = AuthService.login(request)
            val response = AuthRespose(request.username, token)
            call.respond(HttpStatusCode.OK, response)
        }

    }
}