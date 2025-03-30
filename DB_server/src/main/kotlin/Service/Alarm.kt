package mad.project.Service

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Time

@Serializable
data class Alarm(val id: Int, @Contextual val time: Time)
class AlarmService(private val connection: Connection){
    companion object {
        private const val CREATE_TABLE_ALARM =
            "CREATE TABLE IF NOT EXISTS ALARM (ID SERIAL PRIMARY KEY, Time Time);"
        private const val SELECT_CITY_BY_ID = "SELECT name, population FROM cities WHERE id = ?"
        private const val INSERT_USER = "INSERT INTO settings (username, birthday, gender, physicalcondition, caffeineusage, alcoholusage) VALUES (?, ?, ?, ?, ?)"
        private const val EXIST_USER = "SELECT count(*) FROM USERS WHERE username = ?"
        private const val FIND_BY_USERNAME_AND_PASSWORD = "SELECT count(*) FROM users WHERE username = ? and password = ?"
        private const val UPDATE_CITY = "UPDATE settings SET birthday = ?, gender = ?, physicalcondition = ?, caffeineusage = ?, alcoholusage = ?) WHERE id = ?"
        private const val DELETE_CITY = "DELETE FROM cities WHERE id = ?"

    }
    init {
        val statement = connection.createStatement()
       // statement.executeUpdate(CREATE_TABLE_SETTINGS)
    }

    suspend fun userExist(username: String): Boolean {

        try{
            val statement = connection.prepareStatement(EXIST_USER)
            statement.setString(1,username)
            val e=statement.executeQuery();
            if (e.next() && e.getInt(1) == 1) {
                return false
                //throw Exception("Пользователь с таким именем уже существует.")
            }
            return true
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
            //throw Exception("Пользователь с таким именем уже существует.")
        }
    }

    suspend fun insert(settings: Settings){
        try {
            val statement = connection.prepareStatement(INSERT_USER)
            //statement.setString(1, settings.birthday)
            statement.setObject(2, settings.gender)
            statement.setObject(3,settings.physicalCondition)
            statement.setObject(4,settings.caffeineUsage)
            statement.executeUpdate()
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }
    suspend fun update(settings: Settings){
        try {
            val statement = connection.prepareStatement(INSERT_USER)
            //statement.setString(1, settings.birthday)
            statement.setObject(2, settings.gender)

            statement.executeUpdate()
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }
}