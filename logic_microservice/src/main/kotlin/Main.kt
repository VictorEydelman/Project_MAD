package ru.itmo

import KeyDBClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level
import ru.itmo.controller.ReportController
import ru.itmo.service.RecommendationService
import ru.itmo.service.ReportService

fun main() {
    embeddedServer(Netty, port = 8900, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/api") }
    }

    val keydb = KeyDBClient()
    val reportService = ReportService(keydb)
    val recommendationService = RecommendationService(keydb)
    val reportController = ReportController(keydb, reportService, recommendationService)
    launch {
        keydb.subscribeWithResponse("make-daily-report", String::class.java, { username ->
            // Можно ли как то сделать неблокирующе? Спросите Марата
            runBlocking { reportService.makeDailyReport(username) }
        })
    }
    launch {
        keydb.subscribeWithResponse("make-weekly-report", String::class.java, { username ->
            runBlocking { reportService.makeWeeklyReport(username) }
        })
    }
    launch {
        keydb.subscribeWithResponse("make-all-time-report", String::class.java, { username ->
            runBlocking { reportService.makeAllTimeReport(username) }
        })
    }
    launch {
        keydb.subscribeWithResponse("calculate-recommended-times", String::class.java, { username ->
            runBlocking { recommendationService.calculateRecommendedTimes(username) }
        })
    }

}