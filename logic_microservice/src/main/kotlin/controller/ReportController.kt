package ru.itmo.controller

import KeyDBClient
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import ru.itmo.service.RecommendationService
import ru.itmo.service.ReportService

class ReportController(
    private val keydb: KeyDBClient,
    private val reportService: ReportService,
    private val recommendationService: RecommendationService
) {

    fun createKeyDBChannels() = runBlocking {
        launch {
            keydb.subscribeWithResponse("make-daily-report", String::class.java, { username ->
                // Можно ли как то сделать неблокирующе? Спросите Марата
                runBlocking { reportService.makeDailyReport(username) }
            })
        }
        launch {
            keydb.subscribeWithResponse("make-weekly-report", String::class.java, { username ->
                // Можно ли как то сделать неблокирующе? Спросите Марата
                runBlocking { reportService.makeWeeklyReport(username) }
            })
        }
        launch {
            keydb.subscribeWithResponse("make-all-time-report", String::class.java, { username ->
                // Можно ли как то сделать неблокирующе? Спросите Марата
                runBlocking { reportService.makeAllTimeReport(username) }
            })
        }
        launch {
            keydb.subscribeWithResponse("calculate-recommended-times", String::class.java, { username ->
                // Можно ли как то сделать неблокирующе? Спросите Марата
                runBlocking { recommendationService.calculateRecommendedTimes(username) }
            })
        }
    }

    fun configureRouting(routing: Routing) {
        routing {
            post("/daily-report") {
                val username = call.receive<String>()
                val report = withContext(Dispatchers.IO) {
                    reportService.makeDailyReport(username)
                }
                call.respond(report)
            }

            post("/weekly-report") {
                val username = call.receive<String>()
                val report = withContext(Dispatchers.IO) {
                    reportService.makeWeeklyReport(username)
                }
                call.respond(report)
            }

            post("/all-time-report") {
                val username = call.receive<String>()
                val report = withContext(Dispatchers.IO) {
                    reportService.makeAllTimeReport(username)
                }
                call.respond(report)
            }

            post("/calculate-recommended-times") {
                val username = call.receive<String>()
                val times = withContext(Dispatchers.IO) {
                    recommendationService.calculateRecommendedTimes(username)
                }
                call.respond(times)
            }
        }
    }
}