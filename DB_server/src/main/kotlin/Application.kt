package mad.project

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>) {
    io.ktor.server.tomcat.jakarta.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    println("r")
    //docker run --name postgres-container --network my_network -e POSTGRES_USER=s291485 -e POSTGRES_PASSWORD=qwe -e POSTGRES_DB=MAD -p 5432:5432 -d postgres
    configureSerialization()
    configureDatabases()
    configureRouting()
}
