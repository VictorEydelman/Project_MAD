package ru.itmo.config

import io.ktor.server.application.*
import ru.itmo.keydb.KeyDBAPI

fun Application.configureKeyDB() {
    val keyDBConfig = environment.config.config("keydb")
    val host = "keydb"
    val port = keyDBConfig.property("port").getString().toInt()

    KeyDBAPI.init(host, port)

    monitor.subscribe(ApplicationStopped) {
        KeyDBAPI.close()
    }
}
