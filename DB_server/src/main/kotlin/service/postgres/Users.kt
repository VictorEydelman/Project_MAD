package mad.project.service.postgres

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
        private const val GET_USER = "SELECT * FROM USERS WHERE username = ?"
        private const val FIND_BY_USERNAME_AND_PASSWORD = "SELECT count(*) FROM users WHERE username = ? and password = ?"
        private const val UPDATE_CITY = "UPDATE cities SET name = ?, population = ? WHERE id = ?"
        private const val DROP_TABLE = "DROP TABLE IF EXISTS users"

    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(DROP_TABLE)
        statement.executeUpdate(CREATE_TABLE_USERS)
    }

    fun userNotExist(username: String): Boolean {
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

    fun insert(user: Users): Boolean {
        if(userNotExist(user.username)){
            try {
                val statement = connection.prepareStatement(INSERT_USER)
                statement.setString(1, user.username)
                statement.setString(2, user.password)
                statement.executeUpdate()
                return true
            } catch (e: SQLException){
                return false
                //throw Exception("Ошибка с бд")
            }
        } else{
            return false
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

    fun getUserByUsername(username: String): Users? {
        try{
            val statement = connection.prepareStatement(GET_USER);
            statement.setString(1,username)
            val result = statement.executeQuery()
            if(result.next()){
                return Users(result.getString("username"), result.getString("password"))
            } else{
                return null
            }
        } catch (e: SQLException){
            throw Exception("Ошибка с бд")
        }
    }
}