package mad.project.service.postgres

import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class Settings(val username: String, val name: String, val surname: String, @Contextual val birthday: Date, val gender: Gender, val physicalCondition: Frequency, val caffeineUsage: Frequency, val alcoholUsage: Frequency)
class SettingsService(private val connection: Connection){
    companion object {
        private const val CREATE_TABLE_SETTINGS =
            """CREATE TABLE IF NOT EXISTS SETTINGS (
                USERNAME VARCHAR(255) PRIMARY KEY,
                NAME VARCHAR(255),
                SURNAME VARCHAR(255),
                BIRTHDAY DATE,
                GENDER gender,
                PHYSICALCONDITION frequency,
                CAFFEINEUSAGE frequency,
                ALCOHOLUSAGE frequency
            );"""
        private const val SELECT_SETTING_BY_USERNAME = "SELECT * FROM SETTINGS WHERE USERNAME = ?"
        private const val INSERT_SETTING =
            "INSERT INTO SETTINGS (USERNAME, NAME, SURNAME, BIRTHDAY, GENDER, PHYSICALCONDITION, CAFFEINEUSAGE, ALCOHOLUSAGE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        private const val UPDATE_SETTING =
            "UPDATE SETTINGS SET NAME = ?, SURNAME = ?, BIRTHDAY = ?, GENDER = ?, PHYSICALCONDITION = ?, CAFFEINEUSAGE = ?, ALCOHOLUSAGE = ? WHERE USERNAME = ?"
        private const val DROP_TABLE = "DROP TABLE IF EXISTS SETTINGS"

    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(DROP_TABLE)
        statement.executeUpdate(CREATE_TABLE_SETTINGS)
    }

    suspend fun insert(settings: Settings): Boolean{
        try {
            val statement = connection.prepareStatement(INSERT_SETTING)
            statement.setString(1, settings.username)
            statement.setString(2, settings.name)
            statement.setString(3, settings.surname)
            statement.setObject(4, settings.birthday)
            statement.setObject(5, settings.gender.name,java.sql.Types.OTHER)
            statement.setObject(6, settings.physicalCondition.name,java.sql.Types.OTHER)
            statement.setObject(7, settings.caffeineUsage.name,java.sql.Types.OTHER)
            statement.setObject(8, settings.alcoholUsage.name,java.sql.Types.OTHER)
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            //return false
            throw Exception(e)
        }
    }
    suspend fun update(settings: Settings): Boolean{
        try {
            val statement = connection.prepareStatement(UPDATE_SETTING)
            statement.setObject(1, settings.birthday)
            statement.setString(2, settings.name)
            statement.setString(3, settings.surname)
            statement.setObject(4, settings.gender.name,java.sql.Types.OTHER)
            statement.setObject(5,settings.physicalCondition.name,java.sql.Types.OTHER)
            statement.setObject(6,settings.caffeineUsage.name,java.sql.Types.OTHER)
            statement.setObject(7,settings.alcoholUsage.name,java.sql.Types.OTHER)
            statement.setString(8,settings.username)
            statement.executeUpdate()
            return true
        } catch (e: SQLException){
            return false
            throw Exception(e)
        }
    }

    fun get(username: String): Settings{
        val statement = connection.prepareStatement(SELECT_SETTING_BY_USERNAME)
        statement.setString(1,username);
        val result = statement.executeQuery()

        if(result.next()){
            val user = result.getString("USERNAME")
            val name = result.getString("NAME")
            val surname = result.getString("SURNAME")
            val birthday = result.getObject("BIRTHDAY") as? Date ?: throw Exception("Неверный формат даты")
            val gender = result.getString("GENDER")?.let { Gender.valueOf(it) }
                ?: throw Exception("Неверное значение для пола")
            val physicalCondition = result.getString("PHYSICALCONDITION")?.let { Frequency.valueOf(it) }
                ?: throw Exception("Неверное значение для физического состояния")
            val caffeineUsage = result.getString("CaffeineUsage")?.let { Frequency.valueOf(it) }
                ?: throw Exception("Неверное значение для употребления кофеина")
            val alcoholUsage = result.getString("AlcoholUsage")?.let { Frequency.valueOf(it) }
                ?: throw Exception("Неверное значение для употребления алкоголя")

            return Settings(user, name, surname, birthday, gender, physicalCondition, caffeineUsage, alcoholUsage)
        } else{
            throw Exception("Нету настроек")
        }

    }
}