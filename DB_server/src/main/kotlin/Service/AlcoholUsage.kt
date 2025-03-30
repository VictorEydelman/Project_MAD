package mad.project.Service

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class AlcoholUsage {OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null}
class AlcoholUsageService(private val connection: Connection){
    companion object {
        private const val CREATE_TYPE_ALCOHOL =
            "CREATE TYPE AlcoholUsage AS ENUM ('OnceADay', 'TwiceADay', 'ThreeTimesADay', 'OnceEveryTwoDays', 'TwiceAWeek', 'ThreeTimesAWeek', 'Rarely', 'Often', 'Never', 'Null');"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_ALCOHOL)
    }
}