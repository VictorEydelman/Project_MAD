package org.example.controller

import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.example.dto.ApiResponse
import org.example.service.HealthDataService
import java.time.LocalDateTime

class HealthDataController(private val service: HealthDataService) {
    fun setupRoutes(routing: Routing) {
        routing.apply {
            get("/health") {
                call.respond(
                    ApiResponse<String>(
                        success = true,
                        message = "Service is running"
                    )
                )
            }

            get("/data") {
                val lastSync =
                    call.request.queryParameters["lastSync"]?.let { LocalDateTime.parse(it) }
                val data = service.generateData(lastSync)
                val response = ApiResponse(success = true, data = data)
                call.respond(response)
            }
        }
    }
}