package ru.itmo.routes

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import ru.itmo.dto.api.ProfileResponse
import ru.itmo.dto.api.SimpleResponse
import ru.itmo.dto.keydb.ProfileUpdateRequest
import ru.itmo.dto.keydb.SleepUploadRequest
import ru.itmo.keydb.KeyDBAPI
import ru.itmo.model.Profile
import ru.itmo.model.SleepData

fun Route.sleepRoutes() {
    route("/sleep") {
        post<SleepData>("/upload", {
            request { body<SleepData>() }
            response { code(HttpStatusCode.OK) { body<SimpleResponse>() } }
        }) { request ->
            val username = call.principal<String>()!!
            KeyDBAPI.uploadSleepData(username, request)
            call.respond(SimpleResponse.success())
        }

    }
}
