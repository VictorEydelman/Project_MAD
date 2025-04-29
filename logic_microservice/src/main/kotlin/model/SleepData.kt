package ru.itmo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class SleepDataPiece(
    val username: String,
    val timestamp: LocalDateTime,
    val pulse: Int,
    val sleepPhase: SleepPhase,
)

typealias SleepData = List<SleepDataPiece>

enum class Weekday {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun
}

data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)

typealias WeekdaySleepDistribution = List<WeekdaySleep>
