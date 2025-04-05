package mad.project.Service

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class Gender {Men, Woman, Null}
class GenderService(private val connection: Connection){
    companion object {
        private const val CREATE_TYPE_GENDER =
            "DO $$\n" +
                    "BEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'gender') THEN\n" +
                    "        CREATE TYPE gender AS ENUM ('Men', 'Woman', 'Null');\n" +
                    "    END IF;\n" +
                    "END $$;\n"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TYPE_GENDER)
    }
}