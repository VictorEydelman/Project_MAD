package mad.project

import KeyDBClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import mad.project.Service.clickhouse.SleepStatisticService
import mad.project.Service.postgres.AlcoholUsageService
import mad.project.Service.postgres.CaffeineUsageService
import mad.project.Service.postgres.GenderService
import mad.project.Service.postgres.PhysicalConditionService
import mad.project.Service.postgres.SettingsService
import java.sql.Connection
import java.sql.DriverManager
import mad.project.Service.postgres.Users
import mad.project.Service.postgres.UsersService

fun Application.configureDatabases() {
    println("f")
    val dbConnection: Connection = connectToPostgres(embedded = false)
    val clickHouseConnection: Connection = connectToClickHouse()
    val cityService = CityService(dbConnection)
    val usersService = UsersService(dbConnection)
    val genderService = GenderService(dbConnection)
    val alcoholUsageService = AlcoholUsageService(dbConnection)
    val caffeineUsageService = CaffeineUsageService(dbConnection)
    val physicalConditionService = PhysicalConditionService(dbConnection)
    val settingsService = SettingsService(dbConnection)
    val sleepStatisticService = SleepStatisticService(clickHouseConnection)
    val keyDBClient = KeyDBClient()
    val user = mad.project.UsersService

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
        post("/citie") {
            println("cds")
            val user = call.receive<Users>()
            usersService.insert(user)

            println(usersService.findByUsernameAndPassword(user))
            //val settings = Settings.
            //settingsService.insert()
            call.respond(HttpStatusCode.Created, 1)
        }

        // Read city
        get("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val city = cityService.read(id)
                call.respond(HttpStatusCode.OK, city)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    
        // Update city
        put("/cities/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<City>()
            cityService.update(id, user)
            call.respond(HttpStatusCode.OK)
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