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
data class Alarm(val id: Int = -1, @Contextual val time: Time, val alarm: Boolean)
/**
 * Класс для работы с таблицей Alarm в базе данных postgres
 */
class AlarmService(private val connection: Connection){
    /**
     * Запросы к таблице:
     * CREATE_TABLE_ALARM
     * SELECT_ALARM_BY_ID
     * INSERT_Alarm
     * UPDATE_ALARM
     */
    companion object {
        private const val CREATE_TABLE_ALARM =
            "CREATE TABLE IF NOT EXISTS ALARM (ID SERIAL PRIMARY KEY, time Time, alarm Boolean);"
        private const val SELECT_ALARM_BY_ID = "SELECT * FROM alarm WHERE id = ?"
        private const val INSERT_Alarm = "INSERT INTO alarm (time, alarm) VALUES (?, ?)"
        private const val UPDATE_ALARM = "UPDATE alarm SET time = ?, alarm = ? WHERE id = ?"
    }

    /**
     * Создание таблицы
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_ALARM)
    }

    /**
     * Сохраняет alarm
     */
    fun save(alarm: Alarm?): Int{
        try {
            val statement = connection.prepareStatement(INSERT_Alarm, Statement.RETURN_GENERATED_KEYS)
            alarm?.let { statement.setObject(1, it.time) }
            alarm?.let { statement.setBoolean(2, it.alarm) }
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1)
            } else {
                throw Exception("Unable to retrieve the id")
            }
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }

    /**
     * Обновляет alarm
     */
    fun update(alarm: Alarm?){
        try {
            val statement = connection.prepareStatement(UPDATE_ALARM)
            alarm?.let { statement.setObject(1, it.time) }
            alarm?.let { statement.setBoolean(2, it.alarm) }
            alarm?.let { statement.setInt(3,it.id) }

            statement.executeUpdate()
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }

    /**
     * Возвращает Alarm по id
     */
    fun get(id: Int): Alarm{
        val statement = connection.prepareStatement(SELECT_ALARM_BY_ID)
        statement.setInt(1,id);
        val result = statement.executeQuery()

        if(result.next()){
            return Alarm(result.getInt("id"), result.getObject("time") as Time,result.getBoolean("alarm"))
        } else{
            throw Exception("Нету настроек")
        }

    }
}