package mad.project.Service

import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException

@Serializable
data class Users(val username: String, val password: String)
class UsersService(private val connection: Connection){
    companion object {
        private const val CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS USERS (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255));"
        private const val SELECT_CITY_BY_ID = "SELECT name, population FROM cities WHERE id = ?"
        private const val INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)"
        private const val EXIST_USER = "SELECT count(*) FROM USERS WHERE username = ?"
        private const val FIND_BY_USERNAME_AND_PASSWORD = "SELECT count(*) FROM users WHERE username = ? and password = ?"
        private const val UPDATE_CITY = "UPDATE cities SET name = ?, population = ? WHERE id = ?"
        private const val DELETE_CITY = "DELETE FROM cities WHERE id = ?"

    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_USERS)
    }

    suspend fun userExist(username: String): Boolean {

        try{
            val statement = connection.prepareStatement(EXIST_USER)
            statement.setString(1,username)
            val e=statement.executeQuery();
            if (e.next() && e.getInt(1) == 1) {
                return false
                //throw Exception("Пользователь с таким именем уже существует.")
            }
            return true
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
            //throw Exception("Пользователь с таким именем уже существует.")
        }
    }

    suspend fun insert(users: Users){
        if(userExist(users.username)){
            try {
                val statement = connection.prepareStatement(INSERT_USER)
                statement.setString(1, users.username)
                statement.setString(2, users.password)
                statement.executeUpdate()
            } catch (e: SQLException){
                throw Exception("Ошибка с бд")
            }
        }
    }

    suspend fun findByUsernameAndPassword(users: Users): Boolean{
        try {
            val statement = connection.prepareStatement(FIND_BY_USERNAME_AND_PASSWORD)
            statement.setString(1, users.username)
            statement.setString(2, users.password)
            val exist = statement.executeQuery()
            if (exist.next() && exist.getInt(1) == 1) {
                return false
                //throw Exception("Пользователь с таким именем уже существует.")
            }
            return true
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }

}