package ru.itmo.model

import java.time.LocalDateTime

enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}

/**
 * Часть данных о сне пользователя
 * @param timestamp Временная отметка
 * @param pulse Пульс пользователя в данное время
 * @param sleepPhase Фаза сна в данное время
 */
data class SleepDataPiece(
    val timestamp: LocalDateTime,
    val pulse: Int,
    val sleepPhase: SleepPhase,
)

typealias SleepData = List<SleepDataPiece>

enum class Weekday {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun
}

/**
 * Часть данных о недельном распределении времени сна пользователя
 * @param weekday День недели
 * @param asleepHours Часов сна в этот день
 */
data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)

typealias WeekdaySleepDistribution = List<WeekdaySleep>
