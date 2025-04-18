package mad.project

import KeyDBClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import mad.project.service.clickhouse.SleepStatisticService
import mad.project.service.postgres.Frequency
import mad.project.service.postgres.FrequencyService
import mad.project.service.postgres.Gender
import mad.project.service.postgres.GenderService
import mad.project.service.postgres.Settings
import mad.project.service.postgres.SettingsService
import mad.project.service.postgres.Users
import mad.project.service.postgres.UsersService
import java.sql.Date
import java.sql.Timestamp

fun Application.configureDatabases() {
    println("f")
    val dbConnection: Connection = connectToPostgres(embedded = false)
    val clickHouseConnection: Connection = connectToClickHouse()
    val cityService = CityService(dbConnection)
    val usersService = UsersService(dbConnection)
    val genderService = GenderService(dbConnection)
    val settingsService = SettingsService(dbConnection)
    val frequency = FrequencyService(dbConnection)
    val sleepStatisticService = SleepStatisticService(clickHouseConnection)
    val keyDBClient = KeyDBClient()

    launch {
        keyDBClient.subscribeWithResponse("get-user", String::class.java, { username: String ->
            usersService.getUserByUsername(username=username)
        })
    }
    launch {
        keyDBClient.subscribeWithResponse("save-user", Users::class.java, { user->
            usersService.insert(user=user)
        })
    }

    routing {
        /*runBlocking {
            while (true) {
                keyDBClient.receiveRequest("getuser", { Username -> usersService.getUserByUsername(username = Username)},
                    Users::class.java)
            }
        }
        runBlocking {
            while (true) {
                keyDBClient.receiveRequest("requestChannel", { _ -> "OK" }, String::class.java)
            }
        }*/
        // Create city
        val u: String = "k"
        post("/user") {
            val user = Users(u,u)
            println(usersService.getUserByUsername(u))
            println(usersService.insert(user))
            println(usersService.getUserByUsername(u))
            call.respond(HttpStatusCode.Created, 1)
        }

        // Read city
        post("/setting") {
            val settings = Settings(u,"s","d",Date(111111), Gender.Male, Frequency.ThreeTimesADay,
                Frequency.ThreeTimesADay, Frequency.ThreeTimesADay)
            val i = settingsService.insert(settings)
            println(i)
            println(settingsService.get(u))
            val settings2 = Settings(u,"s","d", Date(111111), Gender.Female, Frequency.ThreeTimesADay,
                Frequency.ThreeTimesADay, Frequency.ThreeTimesADay)
            println(settingsService.update(settings2))
            println(settingsService.get(u))
        }
    
        // Update city
        put("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<City>()
            cityService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        get("/sleep"){
            println("ssd")
            val t = sleepStatisticService.getSleepStatisticInterval("d", Timestamp(111), Timestamp(222))
            println(t)
            val f = sleepStatisticService.addSleepData("d", Timestamp(150), 11.2F,3)
            println(f)
            val g = sleepStatisticService.getSleepStatisticInterval("d", Timestamp(111), Timestamp(222))
            println(g)
            call.respond(HttpStatusCode.OK,g)
        }
        // Delete city
        delete("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            cityService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}



fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    println(embedded)
    if (embedded) {
        log.info("Using embedded H2 database for testing; replace this flag to use postgres")
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = environment.config.property("postgres.url").getString()
        log.info("Connecting to postgres database at $url")
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()

        return DriverManager.getConnection(url, user, password)
    }
}
fun Application.connectToClickHouse(): Connection {
    val url = environment.config.property("clickhouse.url").getString() // Убедитесь, что ClickHouse запущен на этом адресе
    Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
    return DriverManager.getConnection("jdbc:clickhouse://172.18.0.4:8123","default","")
}