package ru.itmo.keydb

import KeyDBClient
import io.ktor.http.*
import ru.itmo.dto.keydb.UserRequest
import ru.itmo.exception.StatusException
import ru.itmo.model.*

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
        val res = keydb.sendRequest("update-profile", UserRequest(username, profile), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to update profile", HttpStatusCode.InternalServerError)
    }

    suspend fun getProfile(username: String): Profile {
        val res = keydb.sendRequest("get-profile", username, Profile::class.java)
        return res ?: throw StatusException("Unable to get profile", HttpStatusCode.InternalServerError)
    }

    suspend fun uploadSleepData(username: String, sleepData: SleepData) {
        val res = keydb.sendRequest("upload-sleep", UserRequest(username, sleepData), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to upload sleep data", HttpStatusCode.InternalServerError)
    }

    suspend fun makeSleepReport(username: String, period: Period): Report {
        val res = keydb.sendRequest("make-sleep-report", UserRequest(username, period), Report::class.java)
        return res ?: throw StatusException("Unable to make sleep report", HttpStatusCode.InternalServerError)
    }

    suspend fun getLastSleepSession(username: String): SleepSession {
        val res = keydb.sendRequest("get-last-sleep", username, SleepSession::class.java)
        return res ?: throw StatusException("Unable to get last sleep session", HttpStatusCode.InternalServerError)
    }

    suspend fun calculateRecommendedAsleepTime(username: String, timePreference: TimePreference): TimePreference {
        val res = keydb.sendRequest("calculate-recommended-asleep-time", username, TimePreference::class.java)
        return res ?: throw StatusException("Unable to calculate recommended asleep time", HttpStatusCode.InternalServerError)
    }

}