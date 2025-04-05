package mad.project.Service

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class CaffeineUsage {OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null}
class CaffeineUsageService(private val connection: Connection){
    companion object {
        private const val CREATE_TYPE_CAFFEINE =
            "DO $$\nBEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'caffeineusage') THEN\n" +
                    "        CREATE TYPE CaffeineUsage AS ENUM ('OnceADay', 'TwiceADay', 'ThreeTimesADay', 'OnceEveryTwoDays', 'TwiceAWeek', 'ThreeTimesAWeek', 'Rarely', 'Often', 'Never', 'Null');\n" +
                    "    END IF;\n" +
                    "END $$;\n"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_CAFFEINE)
    }
}