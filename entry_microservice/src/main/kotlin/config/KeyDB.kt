package ru.itmo.config

import KeyDBClient
import io.ktor.server.application.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.itmo.keydb.KeyDBAPI
import ru.itmo.model.User

fun Application.configureKeyDB() {
    val keyDBConfig = environment.config.config("keydb")
    val host = "keydb"
    val port = keyDBConfig.property("port").getString().toInt()

    KeyDBAPI.init(host, port)

//    KeyDBClient.receiveRequest("get-user", true) { req: String ->
//        User("test", "\$2a\$10\$YHkJELh6c.l27j6x/yZZDuIBgyIcjaVzSjtNmJopGl6X/tT1nDvX.")
//    }
//    KeyDBClient.receiveRequest("save-user", true) { req: User ->
//        true
//    }

    monitor.subscribe(ApplicationStopped) {
        KeyDBAPI.close()
    }
}
