package ru.itmo.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itmo.service.RecommendationService
import ru.itmo.service.ReportService

class ReportController(
    private val reportService: ReportService,
    private val recommendationService: RecommendationService
) {
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