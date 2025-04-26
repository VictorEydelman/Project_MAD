package ru.itmo.routes

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import ru.itmo.dto.api.DataResponse
import ru.itmo.dto.api.SimpleResponse
import ru.itmo.keydb.KeyDBAPI
import ru.itmo.model.*

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

        get("/{period}-report", {
            request { pathParameter<String>("period") {
                description = "Allowed values: daily, weekly, all-time"
                example("daily") {value="daily"}
            } }
            response { code(HttpStatusCode.OK) { body<DataResponse<Report>>() } }
        }) {
            val username = call.principal<String>()!!
            val period = call.pathParameters["period"]!!
            val report = KeyDBAPI.makeSleepReport(username, period)
            call.respond(DataResponse.of(report))
        }

        get("/recommended-times", {
            response { code(HttpStatusCode.OK) { body<DataResponse<TimePreference>>() } }
        }) {
            val username = call.principal<String>()!!
            val timePreference = KeyDBAPI.calculateRecommendedTimes(username)
            call.respond(DataResponse.of(timePreference))
        }
    }
}
