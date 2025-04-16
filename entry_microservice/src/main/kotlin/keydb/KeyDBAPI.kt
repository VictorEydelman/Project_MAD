package ru.itmo.keydb

import KeyDBClient
import io.ktor.http.*
import ru.itmo.dto.keydb.ProfileUpdateRequest
import ru.itmo.dto.keydb.SleepUploadRequest
import ru.itmo.exception.StatusException
import ru.itmo.model.Profile
import ru.itmo.model.SleepData
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
        if (res == null || !res) throw StatusException("Unable to save user", HttpStatusCode.InternalServerError)
    }

    suspend fun updateProfile(username: String, profile: Profile) {
        val res = keydb.sendRequest("update-profile", ProfileUpdateRequest(username, profile), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to update profile", HttpStatusCode.InternalServerError)
    }

    suspend fun getProfile(username: String): Profile {
        return keydb.sendRequest("get-profile", username, Profile::class.java)
            ?: throw StatusException("Unable to get profile", HttpStatusCode.InternalServerError)
    }

    suspend fun uploadSleepData(username: String, sleepData: SleepData) {
        val res = keydb.sendRequest("upload-sleep", SleepUploadRequest(username, sleepData), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to upload sleep data", HttpStatusCode.InternalServerError)
    }

}