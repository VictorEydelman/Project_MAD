package ru.itmo.keydb

import KeyDBClient
import io.ktor.http.*
import ru.itmo.exception.StatusException
import ru.itmo.model.User

object KeyDBAPI {

    suspend fun getUser(username: String): User? {
        return KeyDBClient.sendAwaitRequest("get-user", username)
    }

    suspend fun saveUser(user: User) {
        val res: Boolean = KeyDBClient.sendAwaitRequest("save-user", user)
        if (!res) throw StatusException("Unable to save user", HttpStatusCode.InternalServerError)
    }

}