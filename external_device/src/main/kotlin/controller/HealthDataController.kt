package org.example.controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.dto.ApiResponse
import org.example.dto.HealthDataDto
import org.example.service.HealthDataService
import java.time.Instant

class HealthDataController(private val service: HealthDataService) {
    fun setupRoutes(routing: Routing) {
        routing {
            get("/health") {
                call.respond(
                        ApiResponse<String>(
                                success = true,
                                message = "Service is running"
                        )
                )
            }

            get("/data") {
                val lastSync = call.request.queryParameters["lastSync"]?.let { Instant.parse(it) }
                val data = service.generateData(lastSync)
                call.respond(
                        ApiResponse<List<HealthDataDto>>(
                                success = true,
                                data = data
                        )
                )
            }
        }
    }
}