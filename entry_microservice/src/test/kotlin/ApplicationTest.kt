package ru.itmo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.assertThrows
import ru.itmo.dto.api.AuthRequest
import ru.itmo.dto.api.CheckAuthResponse
import ru.itmo.exception.StatusException
import ru.itmo.service.AuthService
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testIncorrectInput(): Unit = testApplication {
        environment { config = ApplicationConfig("application.yaml") }
        for ((username, password) in arrayOf(
            "" to "",
            "123" to "1234",
            "1234" to "123",
            "hello world" to "12345",
            "никита" to "12345",
        )) assertThrows<StatusException> {
            AuthService.register(AuthRequest(username, password))
        }
    }

    @Test
    fun testRoot() = testApplication {
        environment { config = ApplicationConfig("application.yaml") }
        val objectMapper = jacksonObjectMapper()

        client.get("/api/v1/auth/check").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }

        val token = AuthService.createToken("test-user")
        client.get("/api/v1/auth/check", {
            header("Authorization", "Bearer $token")
        }).apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                objectMapper.readValue(bodyAsText(), CheckAuthResponse::class.java),
                CheckAuthResponse("test-user", true)
            )
        }

    }

}
