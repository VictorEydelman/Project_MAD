package mad.project.service.clickhouse

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mad.project.DataUser
import org.joda.time.DateTime
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class SleepStatistic(var username: String = "", @Contextual val timestamp: LocalDateTime, val pulse: Float, val sleepPhase: String)
@Serializable
data class SleepInterval(val username: String, @Contextual val start: LocalDateTime, @Contextual val end: LocalDateTime)

/**
 * Класс для работы с таблицей SleepStatistic в базе данных Clickhouse
 */
class SleepStatisticService(val connection: Connection){
    /**
     * Запросы к таблице:
     * drop
     * createTableSleepStatistic
     */
    companion object{
        val drop = """drop table IF EXISTS SleepStatistic"""
        val createTableSleepStatistic = """
            CREATE TABLE IF NOT EXISTS SleepStatistic (
                username String,
                timestamp DateTime,
                pulse Float32,
                sleep_phase Enum8('AWAKE' = 1, 'DROWSY' = 2, 'LIGHT' = 3, 'DEEP' = 4, 'REM' = 5)
            ) ENGINE = MergeTree()
            ORDER BY (username, timestamp)
        """.trimIndent()
    }
    /**
     * Создание таблицы
     * Возможно ещё её удаление
     */
    init {
        //connection.createStatement().execute(drop)
        connection.createStatement().execute(createTableSleepStatistic)
    }

    /**
     * Достать список данных о сне за опредлелённый интервал пользователя
     */
    fun getSleepStatisticInterval(sleepInterval: SleepInterval): List<SleepStatistic>{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val startFormat = sleepInterval.start.format(formatter)
        val endFormat = sleepInterval.end.format(formatter)

        val query = """
            SELECT * FROM SleepStatistic 
            WHERE username = '${sleepInterval.username}'
            AND timestamp BETWEEN '$startFormat' AND '$endFormat'
            ORDER BY timestamp
        """.trimIndent()

        val resultSet = connection.createStatement().executeQuery(query)
        val results = ArrayList<SleepStatistic>()
        while (resultSet.next()) {
            val timestamp: Timestamp = resultSet.getTimestamp("timestamp")
            val localDateTime = timestamp.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            val sleepStatistic = SleepStatistic(resultSet.getString("username"),
                localDateTime, resultSet.getFloat("pulse"),
                resultSet.getString("sleep_phase"))
            results.add(sleepStatistic)
        }
        return results
    }

    /**
     * Добавление списка данных по статистике сна пользователя
     */
    fun addSleepData(sleepStatistics: DataUser<List<SleepStatistic>>): Boolean {
        var error = true
        for (sleepStatistic in sleepStatistics.data) {
            sleepStatistic.username=sleepStatistics.username
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val timestamp = sleepStatistic.timestamp.format(formatter)
            var preparedStatement: PreparedStatement? = null
            try {
                val query = """
                    INSERT INTO SleepStatistic (username, timestamp, pulse, sleep_phase) 
                    VALUES (?, ?, ?, ?)
                """.trimIndent()

                preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, sleepStatistic.username)
                preparedStatement.setObject(2, timestamp)
                preparedStatement.setFloat(3, sleepStatistic.pulse)
                preparedStatement.setString(4, sleepStatistic.sleepPhase)

                error = preparedStatement.executeUpdate() > 0
                //return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                preparedStatement?.close()
            }
            if(!error){
                return false
            }
        }
        return true
    }
}