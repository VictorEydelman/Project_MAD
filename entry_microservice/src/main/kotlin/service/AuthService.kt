package ru.itmo.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.util.*
import ru.itmo.config.getJwtConfig
import ru.itmo.dto.AuthRequest
import ru.itmo.exception.StatusException
import ru.itmo.keydb.Database
import ru.itmo.model.User
import java.util.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

object AuthService {
    private val digestFunction = getDigestFunction("SHA-256") { "salt${it.length}" }

    private fun digest(str: String): String {
        return Base64.getEncoder().encodeToString(digestFunction(str))
    }

    @OptIn(ExperimentalTime::class)
    private fun createToken(username: String): String {
        val config = getJwtConfig()
        val now = Clock.System.now()
        val expiresAt = now.plus(config.property("expiration").getString().toInt().hours)
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(now.toJavaInstant())
            .withExpiresAt(expiresAt.toJavaInstant())
            .sign(Algorithm.HMAC256(config.property("secret").getString()))
    }

    suspend fun register(request: AuthRequest): String {
        val userEx = Database.getUser(request.username)
        if (userEx != null) throw StatusException("User ${request.username} already exists")
        val password = digest(request.password)
        val user = User(request.username, password)

        Database.saveUser(user)
        return createToken(user.username)
    }

    suspend fun login(request: AuthRequest): String {
        val user = Database.getUser(request.username) ?: throw StatusException("User ${request.username} not found")
        if (digest(request.password) != user.password) throw StatusException("Wrong password", HttpStatusCode.Forbidden)
        return createToken(user.username)
    }

}