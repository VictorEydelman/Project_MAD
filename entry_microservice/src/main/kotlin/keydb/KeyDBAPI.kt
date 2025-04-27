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

/**
 * Объект API для отправки запросов другим микросервисам через KeyDB
 */
object KeyDBAPI {

    private lateinit var keydb: KeyDBClient

    fun init(host: String, port: Int) {
        keydb = KeyDBClient(host, port)
    }

    fun close() {
        keydb.close()
    }

    /**
     * Получить пользователя по никнейму у микросервиса DB
     * DB должен прослушивать канал "get-user"
     */
    suspend fun getUser(username: String): User? {
        //return User("string", "\$2a\$10\$psRAmWI6dkKEsovSmDTUEe5MPMtnw.hp329LlZ4y4jfWcmnbKGaii")
        return keydb.sendRequest("get-user", username, User::class.java)
    }

    /**
     * Сохранить пользователя в микросервис DB
     * DB должен прослушивать канал "save-user"
     */
    suspend fun saveUser(user: User) {
        val res = keydb.sendRequest("save-user", user, Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to save user", HttpStatusCode.InternalServerError)
    }

    /**
     * Обновить профиль [Profile] пользователя в DB
     * DB должен прослушивать канал "update-profile"
     */
    suspend fun updateProfile(username: String, profile: Profile) {
        val res = keydb.sendRequest("update-profile", UserRequest(username, profile), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to update profile", HttpStatusCode.InternalServerError)
    }

    /**
     * Получить профиль [Profile] пользователя из DB
     * DB должен прослушивать канал "get-profile"
     */
    suspend fun getProfile(username: String): Profile {
        val res = keydb.sendRequest("get-profile", username, Profile::class.java)
        return res ?: throw StatusException("Unable to get profile", HttpStatusCode.InternalServerError)
    }

    /**
     * Очистить временные настройки пользователя в DB.
     * Очищает временный будильник [Profile.alarmTemporary] и время отхода ко сну [Profile.bedTimeTemporary]
     * DB должен прослушивать канал "clear-profile-temporaries"
     */
    suspend fun clearProfileTemporaries(username: String) {
        val res = keydb.sendRequest("clear-profile-temporaries", username, Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to clear temporaries", HttpStatusCode.InternalServerError)
    }

    /**
     * Загрузить массив данных [SleepData] сна пользователя в DB
     * DB должен прослушивать канал "upload-sleep"
     */
    suspend fun uploadSleepData(username: String, sleepData: SleepData) {
        val res = keydb.sendRequest("upload-sleep", UserRequest(username, sleepData), Boolean::class.java)
        if (res == null || !res) throw StatusException("Unable to upload sleep data", HttpStatusCode.InternalServerError)
    }

    /**
     * Создать отчет [Report] по сну через Logic за один из трех периодов
     * Logic должен прослушивать каналы: "make-daily-report", "make-weekly-report", "make-all-time-report"
     * @param period Пероид сна: "daily" | "weekly" | "all-time"
     */
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

    /**
     * Рассчитать рекомендумое время отхода ко сну и просыпания [TimePreference] через Logic
     * Logic должен прослушивать канал "calculate-recommended-times"
     */
    suspend fun calculateRecommendedTimes(username: String): TimePreference {
        val res = keydb.sendRequest("calculate-recommended-times", username, TimePreference::class.java)
        return res ?: throw StatusException("Unable to calculate recommended times", HttpStatusCode.InternalServerError)
    }

}