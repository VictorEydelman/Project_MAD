package mad.project.Service

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class CaffeineUsage {OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null}
class CaffeineUsageService(private val connection: Connection){
    companion object {
        private const val CREATE_TYPE_CAFFEINE =
            "CREATE TYPE IF NOT EXISTS CaffeineUsage AS ENUM ('OnceADay', 'TwiceADay', 'ThreeTimesADay', 'OnceEveryTwoDays', 'TwiceAWeek', 'ThreeTimesAWeek', 'Rarely', 'Often', 'Never', 'Null');"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_CAFFEINE)
    }
}