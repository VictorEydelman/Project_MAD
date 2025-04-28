package mad.project

import KeyDBClient
import com.fasterxml.jackson.core.type.TypeReference
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
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
import mad.project.service.postgres.SettingWithOutUser
import mad.project.service.postgres.Settings
import mad.project.service.postgres.SettingsService
import mad.project.service.postgres.Users
import mad.project.service.postgres.UsersService
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class DataUser<T> (val username: String, val data: T)

/**
 *
 * Идёт вызов подключения к базам данных postgres и clickhouse, для этого исользуются методы:
 * Application.connectToClickHouse и Application.connectToPostgres
 *
 * По подключённым базам данных мы подключаем таблицы и типы данных:
 * 1)К postgres:
 * Users, Gendel, Frequency, Settings
 * 2)К clickhouse:
 * SleepStatistic
 *
 * Подключение к keyDB через KeyDBClient
 *
 * Подключение каналов subscribe для клиента keyDB
 * 1)Для Users:
 *   - save-user:
 *      - Описание: Сохраняет пользователя.
 *      - Параметры: Users
 *      - Ответ: Boolean
 *   - get-user:
 *      - Описание: Получает пользователя по имени.
 *      - Параметры: username: String
 *      - Ответ: Users
 * 2)Для Setting:
 *   - update-profile:
 *      - Описание: Сохраняет или обновляет настройки пользователя.
 *      - Параметры: SettingUser
 *      - Ответ: Boolean
 *   - get-profile:
 *      - Описание: Получает настройки пользователя по имени.
 *      - Параметры: username: String
 *      - Ответ: SettingWithOutUser
 *   - temporary-NULL-profile:
 *      - Описание: Обнуление временных значений alarm и bedTime.
 *      - Параметры: username: String
 *      - Ответ: Boolean
 * 3)Для SleepStatistic:
 *   - save-sleepStatistic:
 *      - Описание: Сохраняет статистику сна.
 *      - Параметры: SleepStatistic
 *      - Ответ: Boolean
 *   - get-sleepStatistic-Interval:
 *      - Описание: Получает статистику сна за указанный интервал.
 *      - Параметры: SleepInterval
 *      - Ответ: List<SleepStatistic>
 */
fun Application.configureDatabases() {
    val dbConnection: Connection = connectToPostgres()
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
        keyDBClient.subscribeWithResponse("clear-profile-temporaries", String::class.java, { user->
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

            val settings = Settings(u,"s","d", LocalDate.of(2024,1,1), Gender.Male, Frequency.ThreeTimesADay,
                Frequency.ThreeTimesADay, Frequency.ThreeTimesADay, Alarm(time = Time(222), alarm = true),
                Alarm(time = Time(223), alarm = true),
                BedTime(time = Time(21231), remindBeforeBad = true, remindMeToSleep = false),
                BedTime(time = Time(21231), remindBeforeBad = true, remindMeToSleep = false))
            val s= DataUser<Settings>(settings.username,settings)
            val i = settingsService.save(s)
            println(i)
            val settings3: SettingWithOutUser = settingsService.get(u)
            val settings2: Settings = settings
            settings2.alarmTemporary= settings3.alarmTemporary
            settings2.alarmRecurring=settings3.alarmRecurring
            settings2.bedTimeRecurring=settings3.bedTimeRecurring
            settings2.bedTimeTemporary=settings3.bedTimeTemporary

            println(settings3)
            settings2.gender= Gender.Female
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

/**
 * Подключение к базе данных postgres
 */
fun Application.connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")
    val url = environment.config.property("postgres.url").getString()
    log.info("Connecting to postgres database at $url")
    val user = environment.config.property("postgres.user").getString()
    val password = environment.config.property("postgres.password").getString()
    return DriverManager.getConnection(url, user, password)
}

/**
 * Подключение к базе данных ClickHouse
 */
fun Application.connectToClickHouse(): Connection {
    val url = environment.config.property("clickhouse.url").getString() // Убедитесь, что ClickHouse запущен на этом адресе
    Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
    return DriverManager.getConnection(url,"default","")
}