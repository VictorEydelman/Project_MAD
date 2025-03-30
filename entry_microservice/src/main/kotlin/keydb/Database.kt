package ru.itmo.keydb

import ru.itmo.exception.StatusException
import ru.itmo.model.User

object Database {

    fun <T> publish(action: String, payload: T? = null) {
        KeyDBClient.publish("database", KeyDBRequest(action, payload))
    }

    suspend fun getUser(username: String): User? {
        publish("get-user", username)
        return User("test", "KJKn+0rZAg4VGYNBo11JPeoqKI6L+P8mNK9Rv1+aRdU=")
    }

    suspend fun saveUser(user: User) {
        println(user)
    }

}
