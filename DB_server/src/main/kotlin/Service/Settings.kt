package mad.project.Service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class Settings(val id: Int, val username: String, @Contextual val birthday: Date, val gender: Gender, val physicalCondition: PhysicalCondition, val caffeineUsage: CaffeineUsage, val alcoholUsage: AlcoholUsage)
class SettingsService(private val connection: Connection){
    companion object {
        private const val CREATE_TABLE_SETTINGS =
            "CREATE TABLE IF NOT EXISTS SETTINGS (ID SERIAL PRIMARY KEY, USERNAME VARCHAR(255), BIRTHDAY DATE, GENDER GENDER, PHYSICALCONDITION PHYSICALCONDITION, CaffeineUsage CaffeineUsage, AlcoholUsage AlcoholUsage);"
        private const val SELECT_Setting_BY_ID = "SELECT * FROM settings WHERE id = ?"
        private const val INSERT_USER = "INSERT INTO settings (username, birthday, gender, physicalcondition, caffeineusage, alcoholusage) VALUES (?, ?, ?, ?, ?)"
        private const val EXIST_USER = "SELECT count(*) FROM USERS WHERE username = ?"
        private const val FIND_BY_USERNAME_AND_PASSWORD = "SELECT count(*) FROM users WHERE username = ? and password = ?"
        private const val UPDATE_CITY = "UPDATE settings SET birthday = ?, gender = ?, physicalcondition = ?, caffeineusage = ?, alcoholusage = ?) WHERE id = ?"
        private const val DELETE_CITY = "DELETE FROM cities WHERE id = ?"

    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_SETTINGS)
    }

    suspend fun insert(settings: Settings): Int = withContext(Dispatchers.IO){
        try {
            val statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)
            statement.setObject(1, settings.birthday)
            statement.setObject(2, settings.gender)
            statement.setObject(3, settings.physicalCondition)
            statement.setObject(4, settings.caffeineUsage)
            statement.setObject(5, settings.alcoholUsage)
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getInt(1)
            } else {
                throw Exception("Unable to retrieve the id of the newly inserted city")
            }
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }
    suspend fun update(settings: Settings){
        try {
            val statement = connection.prepareStatement(INSERT_USER)
            statement.setObject(1, settings.birthday)
            statement.setObject(2, settings.gender)
            statement.setObject(3,settings.physicalCondition)
            statement.setObject(4,settings.caffeineUsage)
            statement.setObject(5,settings.alcoholUsage)
            statement.setInt(6,settings.id)
            statement.executeUpdate()
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }

    suspend fun get(id: Int): Settings{
        val statement = connection.prepareStatement(SELECT_Setting_BY_ID)
        statement.setInt(1,id);
        val result = statement.executeQuery()

        if(result.next()){
            val username =result.getString("USERNAME")
            return Settings(id,result.getString("USERNAME"), result.getObject("BIRTHDAY") as Date,
                result.getObject("GENDER") as Gender,
                result.getObject("PHYSICALCONDITION") as PhysicalCondition,
                result.getObject("CaffeineUsage") as CaffeineUsage,
                result.getObject("AlcoholUsage") as AlcoholUsage
            )
            //result null
        } else{
            throw Exception("Нету настроек")
        }

    }
}