package ru.itmo.exception

import io.ktor.http.*

class StatusException(
    message: String,
    val statusCode: HttpStatusCode = HttpStatusCode.BadRequest,
) : RuntimeException(message)
