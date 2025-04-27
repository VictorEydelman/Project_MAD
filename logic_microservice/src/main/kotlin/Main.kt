package ru.itmo

import KeyDBClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
    reportController.createKeyDBChannels()
}