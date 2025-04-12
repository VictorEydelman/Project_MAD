package mad.project.Service.clickhouse

import java.sql.Connection


class SleepStatisticService(val connection: Connection){
    companion object{
        val createTableSleepStatistic = """
            CREATE TABLE IF NOT EXISTS SleepStatistic (
                username String,
                timestamp DateTime,
                pulse UInt32,
                sleep_phase Enum8('drowsiness' = 1, 'shallow' = 2, 'deep' = 3, 'fast ' = 4, 'wakefulness' = 5)
            ) ENGINE = MergeTree()
            ORDER BY (username, timestamp)
        """.trimIndent()
    }
    init {
        connection.createStatement().execute(createTableSleepStatistic)
    }
}