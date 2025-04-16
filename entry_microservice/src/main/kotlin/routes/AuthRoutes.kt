package ru.itmo.routes

import io.github.smiley4.ktoropenapi.get
import io.ktor.http.*
import io.github.smiley4.ktoropenapi.post
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import ru.itmo.dto.AuthRequest
import ru.itmo.dto.AuthResponse
import ru.itmo.dto.CheckAuthResponse
import ru.itmo.service.AuthService

fun Route.authRoutes() {
    route("/auth") {
        post<AuthRequest>("/register", {
            request { body<AuthRequest>() }
            response { code(HttpStatusCode.OK) { body<AuthResponse>() } }
        }) { request ->
            val token = AuthService.register(request)
            val response = AuthResponse(request.username, token)
            call.respond(HttpStatusCode.Created, response)
        }

        post<AuthRequest>("/login", {
            request { body<AuthRequest>() }
            response { code(HttpStatusCode.OK) { body<AuthResponse>() } }
        }) { request ->
            val token = AuthService.login(request)
            val response = AuthResponse(request.username, token)
            call.respond(HttpStatusCode.OK, response)
        }

        get("/check", {
            response { code(HttpStatusCode.OK) { body<CheckAuthResponse>() } }
        }) {
            val username = call.principal<String>()
            val response = CheckAuthResponse(username, username != null)
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
