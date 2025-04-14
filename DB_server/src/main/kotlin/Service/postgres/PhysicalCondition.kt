package mad.project.Service.postgres

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class PhysicalCondition {OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null}
class PhysicalConditionService(private val connection: Connection){
    companion object {
        private const val CREATE_TYPE_PhysicalCondition =
            "DO $$\nBEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'physicalcondition') THEN\n" +
                    "        CREATE TYPE PhysicalCondition AS ENUM ('OnceADay', 'TwiceADay', 'ThreeTimesADay', 'OnceEveryTwoDays', 'TwiceAWeek', 'ThreeTimesAWeek', 'Rarely', 'Often', 'Never', 'Null');\n" +
                    "    END IF;\n" +
                    "END $$;\n"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_PhysicalCondition)
    }
}