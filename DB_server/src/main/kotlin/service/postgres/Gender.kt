package mad.project.service.postgres

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
enum class Gender {Male, Female, Null}

/**
 * Создаёт тип данных gender в бд postgres
 */
class GenderService(private val connection: Connection){
    /**
     * Запрос к бд postgres:
     * CREATE_TYPE_GENDER
     * DROP
     */
    companion object {
        private const val CREATE_TYPE_GENDER =
            "DO $$\n" +
                    "BEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'gender') THEN\n" +
                    "        CREATE TYPE gender AS ENUM ('Male', 'Female', 'Null');\n" +
                    "    END IF;\n" +
                    "END $$;\n"
        private const val DROP= "DROP TYPE IF EXISTS gender CASCADE;"
    }

    /**
     * Создание типа
     * Возможно ещё её удаление
     */
    init {
        val statement = connection.createStatement()
       // statement.executeUpdate(DROP)
        statement.executeUpdate(CREATE_TYPE_GENDER)
    }
}