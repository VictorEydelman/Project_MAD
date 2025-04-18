package ru.itmo.model

import java.time.LocalTime

data class TimePreference(
    val asleep_time: LocalTime?,
    val awake_time: LocalTime?,
)
