package mad.project.service.clickhouse

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class SleepStatistic(val username: String, @Contextual val timestamp: Timestamp, val pulse: Float, val sleepPhase: Int)
class SleepStatisticService(val connection: Connection){
    companion object{
        val createTableSleepStatistic = """
            CREATE TABLE IF NOT EXISTS SleepStatistic (
                username String,
                timestamp DateTime,
                pulse Float32,
                sleep_phase Enum8('drowsiness' = 1, 'shallow' = 2, 'deep' = 3, 'fast ' = 4, 'wakefulness' = 5)
            ) ENGINE = MergeTree()
            ORDER BY (username, timestamp)
        """.trimIndent()
    }
    init {
        connection.createStatement().execute(createTableSleepStatistic)
    }
    fun getSleepStatisticInterval(username: String, start: Timestamp, end: Timestamp){
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val yesterday = now.minusDays(1).format(formatter)

        val query = """
            SELECT * FROM SleepStatistic 
            WHERE username = $username 
            AND timestamp BETWEEN '$start' AND '$end' 
            ORDER BY timestamp
        """.trimIndent()

        val resultSet = connection.createStatement().executeQuery(query)
        val results = mutableListOf<Map<String, Any>>()
        while (resultSet.next()) {
            val sleepDataMap = mutableMapOf<String, Any>()
            sleepDataMap["username"] = resultSet.getInt("username")
            sleepDataMap["timestamp"] = resultSet.getTimestamp("timestamp")
            sleepDataMap["pulse"] = resultSet.getFloat("pulse")
            sleepDataMap["sleep_phase"] = resultSet.getInt("sleep_phase")

            results.add(sleepDataMap)
        }
    }
    fun addSleepData(username: String, timestamp: Timestamp, pulse: Float, sleepPhase: Int): Boolean {
        var preparedStatement: PreparedStatement? = null
        return try {
            val query = """
            INSERT INTO SleepStatistic (username, timestamp, pulse, sleep_phase) 
            VALUES (?, ?, ?, ?)
        """.trimIndent()

            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, username)
            preparedStatement.setObject(2, timestamp)
            preparedStatement.setFloat(3,pulse)
            preparedStatement.setInt(4, sleepPhase)

            return preparedStatement.executeUpdate() > 0
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            preparedStatement?.close()
        }
    }
}