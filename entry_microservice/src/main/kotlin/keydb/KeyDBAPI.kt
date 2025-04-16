package ru.itmo.keydb

import KeyDBClient
import io.ktor.http.*
import ru.itmo.exception.StatusException
import ru.itmo.model.User

object KeyDBAPI {

    private lateinit var keydb: KeyDBClient

    fun init(host: String, port: Int) {
        keydb = KeyDBClient(host, port)
    }

    fun close() {
        keydb.close()
    }

    suspend fun getUser(username: String): User? {
        return keydb.sendRequest("get-user", username, User::class.java)
    }

    suspend fun saveUser(user: User) {
        val res = keydb.sendRequest("save-user", user, Boolean::class.java)
        if (res==null || !res) throw StatusException("Unable to save user", HttpStatusCode.InternalServerError)
    }

}