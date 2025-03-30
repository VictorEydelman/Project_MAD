package ru.itmo.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import ru.itmo.exception.StatusException

private lateinit var jwtConfig: ApplicationConfig

fun getJwtConfig() = jwtConfig

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    jwtConfig = environment.config.config("jwt")
    val jwtSecret = jwtConfig.property("secret").getString()
    authentication {
        jwt {
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret)).build()
            )
            validate { credential ->
                credential.payload.subject
            }
        }
    }
}
