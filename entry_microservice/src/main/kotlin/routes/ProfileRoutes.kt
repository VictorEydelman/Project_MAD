package ru.itmo.routes

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itmo.dto.api.ProfileResponse
import ru.itmo.dto.api.SimpleResponse
import ru.itmo.dto.keydb.ProfileUpdateRequest
import ru.itmo.keydb.KeyDBAPI
import ru.itmo.model.Profile

fun Route.profileRoutes() {
    route("/profile") {
        post<Profile>("/update", {
            request { body<Profile>() }
            response { code(HttpStatusCode.OK) { body<SimpleResponse>() } }
        }) { request ->
            val username = call.principal<String>()!!
            KeyDBAPI.updateProfile(ProfileUpdateRequest(username, request))
            call.respond(SimpleResponse.success())
        }
        get("/get", {
            response { code(HttpStatusCode.OK) { body<ProfileResponse>() } }
        }) {
            val username = call.principal<String>()!!
            val profile = KeyDBAPI.getProfile(username)
            call.respond(ProfileResponse(profile))
        }
    }
}
