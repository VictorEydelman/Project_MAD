package mad.project

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.log
import io.ktor.server.html.insert
import io.ktor.server.testing.*
import mad.project.service.clickhouse.SleepStatisticService
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class ApplicationTest {
    /*val dbConnection: Connection = connectToPostgres(false)
    val clickHouseConnection: Connection = connectToClickHouse()
    val usersService = UsersService(dbConnection)
    val genderService = GenderService(dbConnection)
    val frequency = FrequencyService(dbConnection)
    val settingsService = SettingsService(dbConnection)
    val sleepStatisticService = SleepStatisticService(clickHouseConnection)
    @Test
    fun User() = testApplication {
        val user = Users("s","s")
        println(usersService.getUserByUsername("s"))
        println(usersService.insert(user))
        assertEquals(usersService.getUserByUsername("s"),user)
    }
    @Test
    fun Settings() = testApplication {
        val settings = Settings("s",Date(111111), Gender.Male, Frequency.ThreeTimesADay,
            Frequency.ThreeTimesADay, Frequency.ThreeTimesADay)
        val i =settingsService.insert(settings)
        println(i)
        println(settingsService.get("s"))
        val settings2 = Settings(i,"s",Date(111111), Gender.Female, Frequency.ThreeTimesADay,
            Frequency.ThreeTimesADay, Frequency.ThreeTimesADay)
        println(settingsService.update(settings2))
        assertEquals(settingsService.get(i),settings2)
    }*/

}
fun connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    println(embedded)
    if (embedded) {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = "jdbc:postgresql://db:5432/studs"
        val user = "s291485"
        val password = "qwe"

        return DriverManager.getConnection(url, user, password)
    }
}
fun connectToClickHouse(): Connection {
    //val url = environment.config.property("clickhouse.url").getString() // Убедитесь, что ClickHouse запущен на этом адресе
    Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
    return DriverManager.getConnection("jdbc:clickhouse://172.18.0.4:8123","default","")
}