package mad.project

import KeyDBClient
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    io.ktor.server.tomcat.jakarta.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    println("r")
    //docker run --name postgres-container --network my_network -e POSTGRES_USER=s291485 -e POSTGRES_PASSWORD=qwe -e POSTGRES_DB=MAD -p 5432:5432 -d postgres
    configureDatabases()

}
