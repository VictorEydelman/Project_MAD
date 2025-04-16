package ru.itmo.service

import KeyDBClient
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import org.mindrot.jbcrypt.BCrypt
import ru.itmo.config.getJwtConfig
import ru.itmo.dto.AuthRequest
import ru.itmo.exception.StatusException
import ru.itmo.keydb.KeyDBAPI
import ru.itmo.model.User
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

object AuthService {

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
        val userEx = KeyDBAPI.getUser(request.username)
        if (userEx != null) throw StatusException("User ${request.username} already exists")
        val password = BCrypt.hashpw(request.password, BCrypt.gensalt())
        val user = User(request.username, password)

        KeyDBAPI.saveUser(user)
        return createToken(user.username)
    }

    suspend fun login(request: AuthRequest): String {
        val user = KeyDBAPI.getUser(request.username) ?: throw StatusException("User ${request.username} not found")
        if (BCrypt.checkpw(request.password, user.password))
            return createToken(user.username)
        throw StatusException("Wrong password", HttpStatusCode.Forbidden)
    }

}