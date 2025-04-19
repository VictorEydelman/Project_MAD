package mad.project.service.postgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import java.sql.Time

@Serializable
data class BedTime(val id:Int = -1, @Contextual val time: Time, val remindMeToSleep: Boolean, val remindBeforeBad: Boolean)
class BedTimeService(private val connection: Connection){
    companion object {
        private const val CREATE_TABLE_BedTime =
            "CREATE TABLE IF NOT EXISTS bedtime (ID SERIAL PRIMARY KEY, time Time, remindMeToSleep Boolean, remindBeforeBad Boolean);"
        private const val SELECT_BEDTIME_BY_ID = "SELECT * FROM bedtime WHERE id = ?"
        private const val INSERT_BEDTIME = "INSERT INTO bedtime (time, remindMeToSleep, remindBeforeBad) VALUES (?, ?, ?)"
        private const val EXIST_USER = "SELECT count(*) FROM USERS WHERE username = ?"
        private const val FIND_BY_USERNAME_AND_PASSWORD = "SELECT count(*) FROM users WHERE username = ? and password = ?"
        private const val UPDATE_BEDTIME = "UPDATE bedtime SET time = ?, remindMeToSleep = ?, remindBeforeBad = ? WHERE id = ?"
        private const val DELETE_CITY = "DELETE FROM cities WHERE id = ?"

    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_BedTime)
    }
    fun save(bedTime: BedTime): Int{
        try {
            val statement = connection.prepareStatement(INSERT_BEDTIME, Statement.RETURN_GENERATED_KEYS)
            statement.setObject(1, bedTime.time)
            statement.setBoolean(2, bedTime.remindMeToSleep)
            statement.setBoolean(3, bedTime.remindBeforeBad)
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1)
            } else {
                throw Exception("Unable to retrieve the id")
            }
        } catch (e: SQLException){
            throw Exception(e)
        }
    }

    fun update(bedTime: BedTime){
        try {
            val statement = connection.prepareStatement(UPDATE_BEDTIME)
            statement.setObject(1, bedTime.time)
            statement.setBoolean(2, bedTime.remindMeToSleep)
            statement.setBoolean(3, bedTime.remindBeforeBad)
            statement.setInt(4,bedTime.id)

            statement.executeUpdate()
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }
    fun get(id: Int): BedTime{
        val statement = connection.prepareStatement(SELECT_BEDTIME_BY_ID)
        statement.setInt(1,id);
        val result = statement.executeQuery()

        if(result.next()){
            return BedTime(result.getInt("id"), result.getObject("time") as Time,result.getBoolean("remindMeToSleep"), result.getBoolean("remindBeforeBad"))
        } else{
            throw Exception("Нету настроек")
        }

    }
}