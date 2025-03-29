package mad.project.Service

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class Gender {Men, Woman, Null}
class GenderService(private val connection: Connection){
    companion object {
        private const val CREATE_TYPE_GENDER =
            "CREATE TYPE IF NOT EXISTS GENDER AS ENUM ('Men', 'Woman', 'Null');"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_GENDER)
    }
}