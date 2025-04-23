package mad.project

import KeyDBClient
import com.fasterxml.jackson.core.type.TypeReference
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import mad.project.service.clickhouse.SleepInterval
import mad.project.service.clickhouse.SleepStatistic
import java.sql.Connection
import java.sql.DriverManager
import mad.project.service.clickhouse.SleepStatisticService
import mad.project.service.postgres.Alarm
import mad.project.service.postgres.BedTime
import mad.project.service.postgres.Frequency
import mad.project.service.postgres.FrequencyService
import mad.project.service.postgres.Gender
import mad.project.service.postgres.GenderService
import mad.project.service.postgres.Settings
import mad.project.service.postgres.SettingsService
import mad.project.service.postgres.Users
import mad.project.service.postgres.UsersService
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime

fun Application.configureDatabases() {
    println("f")
    val dbConnection: Connection = connectToPostgres(embedded = false)
    val clickHouseConnection: Connection = connectToClickHouse()
    val usersService = UsersService(dbConnection)
    val genderService = GenderService(dbConnection)
    val frequency = FrequencyService(dbConnection)
    val settingsService = SettingsService(dbConnection)
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
    launch {
        val requestType = object : TypeReference<DataUser<Settings>>() {}
        keyDBClient.subscribeWithResponse("update-profile", requestType, { setting_user: DataUser<Settings> ->
            settingsService.save(setting_user)})
    }
    launch {
        keyDBClient.subscribeWithResponse("get-profile", String::class.java, { user->
            settingsService.get(user)
        })
    }
    launch {
        keyDBClient.subscribeWithResponse("temporary-NULL-profile", String::class.java, { user->
            settingsService.temporaryToNull(user)
        })
    }
    launch {
        keyDBClient.subscribeWithResponse("get-sleepStatistic-Interval",
            SleepInterval::class.java, { sleepInterval->
            sleepStatisticService.getSleepStatisticInterval(sleepInterval)
        })
    }
    launch {
        val requestType = object : TypeReference<DataUser<List<SleepStatistic>>>() {}
        keyDBClient.subscribeWithResponse("upload-sleep",
            requestType, { sleepStatistic: DataUser<List<SleepStatistic>> ->
            sleepStatisticService.addSleepData(sleepStatistic)
        })
    }
    routing {

        val u = "k"
        post("/user") {
            val user = Users(u,u)
            println(usersService.getUserByUsername(u))
            println(usersService.insert(user))
            println(usersService.getUserByUsername(u))
            call.respond(HttpStatusCode.Created, 1)
        }

        post("/setting") {

            val settings = Settings(u,"s","d", LocalDate.of(2024,1,1), Gender.male, Frequency.ThreeTimesADay,
                Frequency.ThreeTimesADay, Frequency.ThreeTimesADay, Alarm(time = Time(222), alarm = true),
                Alarm(time = Time(223), alarm = true),
                BedTime(time = Time(21231), remindBeforeBad = true, remindMeToSleep = false),
                BedTime(time = Time(21231), remindBeforeBad = true, remindMeToSleep = false))
            val s= DataUser<Settings>(settings.username,settings)
            val i = settingsService.save(s)
            println(i)
            val settings3: SettingWithOutUser = settingsService.get(u)
            val settings2: Settings =settings
            settings2.alarmTemporary=settings3.alarmTemporary
            settings2.alarmRecurring=settings3.alarmRecurring
            settings2.bedTimeRecurring=settings3.bedTimeRecurring
            settings2.bedTimeTemporary=settings3.bedTimeTemporary

            println(settings3)
            settings2.gender= Gender.female
            settings2.alarmTemporary=null
            val m = DataUser<Settings>(settings.username,settings2)
            println(settingsService.save(m))
            println(settingsService.get(u))
            println(settingsService.temporaryToNull(u))
        }

        post("/sleep"){
            val sleepStatistic = SleepStatistic(u, LocalDateTime.of(2024,4,20,11,1,6),2.1F,"DEEP")
            val sleepStatistic2 = SleepStatistic(u, LocalDateTime.of(2024,4,20,11,1,10),2.3F,"LIGHT")
            val s: List<SleepStatistic> = listOf(sleepStatistic,sleepStatistic2)
            val d = DataUser<List<SleepStatistic>>(u,s)
            println(sleepStatisticService.addSleepData(d))
            //println(sleepStatisticService.addSleepData(sleepStatistic2))
            val sleepInterval = SleepInterval(u, LocalDateTime.of(2024,4,20,11,1,4), LocalDateTime.of(2024,4,20,11,1,15))
            val list = sleepStatisticService.getSleepStatisticInterval(sleepInterval)
            for (i in list){
                println(i)
            }

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
    return DriverManager.getConnection(url,"default","")
}