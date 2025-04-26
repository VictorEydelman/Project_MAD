package ru.itmo.keydb

import KeyDBClient
import io.ktor.http.*
import ru.itmo.dto.keydb.UserRequest
import ru.itmo.exception.StatusException
import ru.itmo.model.*
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime

object KeyDBAPI {

    private lateinit var keydb: KeyDBClient

    fun init(host: String, port: Int) {
        keydb = KeyDBClient(host, port)
    }

    fun close() {
        keydb.close()
    }

    suspend fun getUser(username: String): User? {
        //return User("string", "\$2a\$10\$psRAmWI6dkKEsovSmDTUEe5MPMtnw.hp329LlZ4y4jfWcmnbKGaii")
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

    suspend fun clearProfileTemporaries(username: String) {
        val res = keydb.sendRequest("clear-profile-temporaries", username, Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to clear temporaries", HttpStatusCode.InternalServerError)
    }

    suspend fun uploadSleepData(username: String, sleepData: SleepData) {
        val res = keydb.sendRequest("upload-sleep", UserRequest(username, sleepData), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to upload sleep data", HttpStatusCode.InternalServerError)
    }

    suspend fun makeSleepReport(username: String, period: String): Report {
//        return Report(
//            90,
//            LocalTime.now(),
//            LocalTime.now(),
//            Duration.ofHours(1),
//            10,
//            Duration.ofMinutes(5),
//            Duration.ofMinutes(5),
//            Duration.ofMinutes(5),
//            listOf(
//                SleepDataPiece(
//                    LocalDateTime.now(),
//                    10,
//                    SleepPhase.REM
//                )
//            ),
//            null
//        )
        val res = keydb.sendRequest("make-$period-report", username, Report::class.java)
        return res ?: throw StatusException("Unable to make sleep report", HttpStatusCode.InternalServerError)
    }

    suspend fun calculateRecommendedTimes(username: String): TimePreference {
        val res = keydb.sendRequest("calculate-recommended-times", username, TimePreference::class.java)
        return res ?: throw StatusException("Unable to calculate recommended times", HttpStatusCode.InternalServerError)
    }

}