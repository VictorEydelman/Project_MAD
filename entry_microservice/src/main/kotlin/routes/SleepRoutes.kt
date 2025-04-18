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

        post<Period>("/make-report", {
            request { body<Period>() }
            response { code(HttpStatusCode.OK) { body<DataResponse<Report>>() } }
        }) { request ->
            val username = call.principal<String>()!!
            val report = KeyDBAPI.makeSleepReport(username, request)
            call.respond(DataResponse.of(report))
        }

        get("/last-sleep", {
            response { code(HttpStatusCode.OK) { body<DataResponse<SleepSession>>() } }
        }) {
            val username = call.principal<String>()!!
            val sleepSession = KeyDBAPI.getLastSleepSession(username)
            call.respond(DataResponse.of(sleepSession))
        }

        post<TimePreference>("/calculate-recommended-asleep-time", {
            request { body<TimePreference>() }
            response { code(HttpStatusCode.OK) { body<DataResponse<TimePreference>>() } }
        }) { request ->
            val username = call.principal<String>()!!
            val timePreference = KeyDBAPI.calculateRecommendedAsleepTime(username, request)
            call.respond(DataResponse.of(timePreference))
        }
    }
}
