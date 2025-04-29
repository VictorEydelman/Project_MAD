package ru.itmo.exception

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import ru.itmo.dto.api.SimpleResponse
import ru.itmo.keydb.Logger

fun StatusPagesConfig.exceptionHandler() {
    exception<StatusException> { call, cause ->
        call.respond(cause.statusCode, SimpleResponse.failure(cause.message))
        Logger.warn("User ${cause.statusCode.description}: ${cause.message}")
    }
    exception<BadRequestException> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, SimpleResponse.failure(cause.message))
        Logger.warn("User BadRequest: ${cause.message}")
    }
    exception<Throwable> { call, cause ->
        call.respond(HttpStatusCode.InternalServerError, SimpleResponse.failure(cause.message))
        Logger.error("Unexpected exception", cause)
    }

    status(
        HttpStatusCode.NotFound,
        HttpStatusCode.Unauthorized,
        HttpStatusCode.UnsupportedMediaType,
    ) { call, status ->
        call.respond(status, SimpleResponse.failure(status.description))
        Logger.warn("User ${status.description}")
    }
}