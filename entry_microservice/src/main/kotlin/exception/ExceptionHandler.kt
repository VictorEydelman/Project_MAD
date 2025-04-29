package ru.itmo.exception

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import ru.itmo.dto.api.SimpleResponse
import ru.itmo.keydb.Logger

suspend fun extractInfo(call: ApplicationCall): String =
    "by ${call.principal<String>()} on ${call.request.httpMethod} ${call.request.path()} with body '${String(call.receive<ByteArray>())}'"

fun StatusPagesConfig.exceptionHandler() {
    exception<StatusException> { call, cause ->
        call.respond(cause.statusCode, SimpleResponse.failure(cause.message))
        Logger.warn("${cause.statusCode.description} ${extractInfo(call)}: ${cause.message}")
    }
    exception<BadRequestException> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, SimpleResponse.failure(cause.message))
        Logger.warn("BadRequest ${extractInfo(call)}: ${cause.message}")
    }
    exception<Throwable> { call, cause ->
        call.respond(HttpStatusCode.InternalServerError, SimpleResponse.failure(cause.message))
        Logger.error("Unexpected exception ${extractInfo(call)}", cause)
    }

    status(
        HttpStatusCode.NotFound,
        HttpStatusCode.Unauthorized,
        HttpStatusCode.UnsupportedMediaType,
    ) { call, status ->
        call.respond(status, SimpleResponse.failure(status.description))
        Logger.warn("${status.description} ${extractInfo(call)}")
    }
}