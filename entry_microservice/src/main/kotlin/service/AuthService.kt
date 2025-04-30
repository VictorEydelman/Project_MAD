package ru.itmo.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import org.mindrot.jbcrypt.BCrypt
import ru.itmo.config.getJwtConfig
import ru.itmo.dto.api.AuthRequest
import ru.itmo.exception.StatusException
import ru.itmo.keydb.KeyDBAPI
import ru.itmo.model.User
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

object AuthService {

    /**
     * Создать JWT токен, содержащий имя пользователя как Subject
     * @param username Имя пользователя токена
     * @return JWT токен
     */
    @OptIn(ExperimentalTime::class)
    fun createToken(username: String): String {
        val config = getJwtConfig()
        val now = Clock.System.now()
        val expiresAt = now.plus(config.property("expiration").getString().toInt().hours)
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(now.toJavaInstant())
            .withExpiresAt(expiresAt.toJavaInstant())
            .sign(Algorithm.HMAC256(config.property("secret").getString()))
    }

    /**
     * Зарегистировать нового пользователя
     * @param request Объект с именем пользователя и паролем
     * @return JWT токен
     */
    suspend fun register(request: AuthRequest): String {
        if (!request.username.matches(Regex("^[a-zA-Z0-9_]{4,32}$")))
            throw StatusException("Username must be form 4 to 32 characters and contain only alphanumeric and underscore characters")
        if (request.password.length < 4)
            throw StatusException("Password must be not less than 4 characters")

        val userEx = KeyDBAPI.getUser(request.username)
        if (userEx != null) throw StatusException("User ${request.username} already exists")
        val password = BCrypt.hashpw(request.password, BCrypt.gensalt())
        val user = User(request.username, password)

        KeyDBAPI.saveUser(user)
        return createToken(user.username)
    }

    /**
     * Авторизировать существующего пользователя
     * @param request Объект с именем пользователя и паролем
     * @return JWT токен
     */
    suspend fun login(request: AuthRequest): String {
        val user = KeyDBAPI.getUser(request.username) ?: throw StatusException("User ${request.username} not found")
        if (BCrypt.checkpw(request.password, user.password))
            return createToken(user.username)
        throw StatusException("Wrong password", HttpStatusCode.Forbidden)
    }

}