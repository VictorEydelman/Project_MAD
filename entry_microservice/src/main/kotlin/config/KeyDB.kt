package ru.itmo.config

import io.ktor.server.application.*
import ru.itmo.keydb.KeyDBClient

fun Application.configureKeyDB() {
    val keyDBConfig = environment.config.config("keydb")
    val host = keyDBConfig.property("host").getString()
    val port = keyDBConfig.property("port").getString().toInt()

    KeyDBClient.init(host, port)

    monitor.subscribe(ApplicationStopped) {
        KeyDBClient.close()
    }
}
