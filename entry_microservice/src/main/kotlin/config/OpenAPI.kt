package ru.itmo.config

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.AuthScheme
import io.github.smiley4.ktoropenapi.config.AuthType
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureOpenAPI() {
    install(OpenApi) {
        security {
            securityScheme("MySecurityScheme") {
                type = AuthType.HTTP
                scheme = AuthScheme.BEARER
            }
            defaultSecuritySchemeNames("MySecurityScheme")
        }
    }
    routing {
        route("api.json") {
            openApi()
        }
        route("swagger") {
            swaggerUI("/api.json") {
                persistAuthorization = true
            }
        }
    }
}