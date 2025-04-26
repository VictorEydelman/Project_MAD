package ru.itmo.model

import java.time.LocalTime

data class TimePreference(
    val asleepTime: LocalTime,
    val awakeTime: LocalTime,
)
