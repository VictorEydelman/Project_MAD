package ru.itmo.model

import java.time.LocalTime

/**
 * Рекомендуемое время
 * @param asleepTime Время отхода ко сну
 * @param awakeTime Время пробуждения
 */
data class TimePreference(
    val asleepTime: LocalTime,
    val awakeTime: LocalTime,
)
