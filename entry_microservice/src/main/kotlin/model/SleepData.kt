package ru.itmo.model

import java.time.Instant

enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}

data class SleepDataPiece(
    val timestamp: Instant,
    val pulse: Int,
    val phase: SleepPhase,
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
