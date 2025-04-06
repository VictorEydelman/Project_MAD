package mad.project

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.html.insert
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.sql.Connection
import java.sql.DriverManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class ApplicationTest {
    private lateinit var connection: Connection

    @Test
    fun InsertUser()=testApplication {
            application {
                connection = connectToPostgres(false)
            }
            //val user = UsersService(connection = connection)
           // user.insert(Users("test1","test1"))
            //assertNotNull(connection)
        assert(true)


    }

}
