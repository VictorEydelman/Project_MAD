package mad.project

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.log
import io.ktor.server.html.insert
import io.ktor.server.testing.*
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
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.Time
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class ApplicationTest {
    val clickHouseConnection: Connection = connectToClickHouse()
    val dbConnection: Connection = connectToPostgres(false)
    val usersService = UsersService(dbConnection)
    val genderService = GenderService(dbConnection)
    val frequency = FrequencyService(dbConnection)
    val settingsService = SettingsService(dbConnection)
    val sleepStatisticService = SleepStatisticService(clickHouseConnection)
    val u ="h"
    @Test
    fun User() = testApplication {
        val user = Users(u,u)
        println(usersService.getUserByUsername(u))
        println(usersService.insert(user))
        assertEquals(usersService.getUserByUsername(u),user)

    }
    @Test
    fun Settings() = testApplication {
        val settings = Settings(u,"s","d", LocalDate.of(2024,1,1), Gender.Male, Frequency.ThreeTimesADay,
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
        settings2.gender= Gender.Female
        settings2.alarmTemporary=null
        val m = DataUser<Settings>(settings.username,settings2)
        println(settingsService.save(m))
        println(settingsService.get(u))
        println(settingsService.temporaryToNull(u))
    }

}
fun connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    println(embedded)
    if (embedded) {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val user = "s291485"
        val password = "qwe"

        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "s291485", "qwe")
    }
}
fun connectToClickHouse(): Connection {
    //val url = environment.config.property("clickhouse.url").getString() // Убедитесь, что ClickHouse запущен на этом адресе
    Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
    return DriverManager.getConnection("jdbc:clickhouse://localhost:8123","default","")
}