package ru.itmo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.DayOfWeek
import java.time.LocalDateTime

enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class SleepDataPiece(
    val timestamp: LocalDateTime,
    val pulse: Int,
    val sleepPhase: SleepPhase,
)

typealias SleepData = List<SleepDataPiece>

enum class Weekday {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun;

    companion object {
        fun of(week: DayOfWeek) = when (week) {
            DayOfWeek.MONDAY -> Mon
            DayOfWeek.TUESDAY -> Tue
            DayOfWeek.WEDNESDAY -> Wed
            DayOfWeek.THURSDAY -> Thu
            DayOfWeek.FRIDAY -> Fri
            DayOfWeek.SATURDAY -> Sat
            DayOfWeek.SUNDAY -> Sun
        }
    }
}

data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)

typealias WeekdaySleepDistribution = List<WeekdaySleep>
