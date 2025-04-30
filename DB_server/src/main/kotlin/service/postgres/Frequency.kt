package mad.project.service.postgres

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class Frequency  {OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null}
/**
 * Создаёт тип данных Frequency в бд postgres
 */
class FrequencyService(private val connection: Connection){
    /**
     * Запрос к бд postgres:
     * CREATE_TYPE_Frequency
     */
    companion object {
        private const val CREATE_TYPE_Frequency =
            "DO $$\nBEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'frequency') THEN\n" +
                    "        CREATE TYPE frequency AS ENUM ('OnceADay', 'TwiceADay', 'ThreeTimesADay', 'OnceEveryTwoDays', 'TwiceAWeek', 'ThreeTimesAWeek', 'Rarely', 'Often', 'Never', 'Null');\n" +
                    "    END IF;\n" +
                    "END $$;\n"
    }
    /**
     * Создание типа
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_Frequency)
    }
}