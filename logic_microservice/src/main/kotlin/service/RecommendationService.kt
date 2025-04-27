package ru.itmo.service

import KeyDBClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itmo.model.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

class RecommendationService(private val keydb: KeyDBClient) {

    suspend fun calculateRecommendedTimes(username: String): TimePreference = withContext(Dispatchers.IO) {
        val now = LocalDateTime.now()
        val stats: SleepData? = keydb.sendRequest(
            channel = "get-sleepStatistic-Interval",
            message = SleepInterval(username, now.minusMonths(1), now)
        )

        val sleepData = stats!!
//        val sleepData = stats.map { map ->
//            SleepDataPiece(
//                timestamp = LocalDateTime.parse(map["timestamp"] as String),
//                pulse = (map["pulse"] as Double).toInt(),
//                sleepPhase = SleepPhase.valueOf(map["sleepPhase"] as String)
//            )
//        }

        // Логика расчета рекомендаций
        val avgSleepTime = calculateAverageSleepTime(sleepData)
        val mostCommonSleepTime = calculateMostCommonSleepHour(sleepData)

        TimePreference(
            asleepTime = LocalTime.of(mostCommonSleepTime, 0),
            awakeTime = LocalTime.of((mostCommonSleepTime + avgSleepTime).toInt() % 24, 0)
        )
    }

    private fun calculateAverageSleepTime(data: SleepData): Double {
        if (data.isEmpty()) return 8.0

        val byDays = data.groupBy { it.timestamp.toLocalDate() }
        val sleepTimes = mutableListOf<Double>()

        for ((_, dayData) in byDays) {
            val totalSleep = calculateTotalSleep(dayData).toHours().toDouble()
            if (totalSleep > 0) {
                sleepTimes.add(totalSleep)
            }
        }

        if (sleepTimes.isEmpty()) return 8.0
        return sleepTimes.average()
    }

    private fun calculateMostCommonSleepHour(data: SleepData): Int {
        if (data.isEmpty()) return 22

        // Группируем начало сна по часам
        val sleepStarts = mutableListOf<Int>()
        val asleepPhases = listOf(SleepPhase.REM, SleepPhase.LIGHT, SleepPhase.DEEP, SleepPhase.DROWSY)

        val byDays = data.groupBy { it.timestamp.toLocalDate() }

        for ((_, dayData) in byDays) {
            val sorted = dayData.sortedBy { it.timestamp }

            for (i in sorted.indices) {
                val current = sorted[i]
                if (current.sleepPhase in asleepPhases &&
                    (i == 0 || sorted[i-1].sleepPhase == SleepPhase.AWAKE)) {
                    sleepStarts.add(current.timestamp.hour)
                }
            }
        }

        if (sleepStarts.isEmpty()) return 22

        // Находим наиболее частый час засыпания
        return sleepStarts.groupBy { it }
            .maxByOrNull { it.value.size }?.key ?: 22
    }

    private fun calculateTotalSleep(data: SleepData): Duration {
        if (data.isEmpty()) return Duration.ZERO

        val asleepPhases = listOf(SleepPhase.REM, SleepPhase.LIGHT, SleepPhase.DEEP, SleepPhase.DROWSY)
        val sleepPeriods = mutableListOf<Duration>()
        var sleepStart: LocalDateTime? = null

        // Группируем данные по дням
        val byDays = data.groupBy { it.timestamp.toLocalDate() }

        for ((_, dayData) in byDays) {
            val sorted = dayData.sortedBy { it.timestamp }

            for (i in sorted.indices) {
                val current = sorted[i]

                if (current.sleepPhase in asleepPhases && sleepStart == null) {
                    sleepStart = current.timestamp
                } else if (current.sleepPhase == SleepPhase.AWAKE && sleepStart != null) {
                    sleepPeriods.add(Duration.between(sleepStart, current.timestamp))
                    sleepStart = null
                }
            }

            // Добавляем последний период сна, если он не закончился бодрствованием
            if (sleepStart != null) {
                sleepPeriods.add(Duration.between(sleepStart, sorted.last().timestamp))
                sleepStart = null
            }
        }

        return sleepPeriods.fold(Duration.ZERO) { acc, duration -> acc + duration }
    }
}