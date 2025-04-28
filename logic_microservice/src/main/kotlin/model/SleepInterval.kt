package ru.itmo.model

import java.time.LocalDateTime

data class SleepInterval(
    val username: String,
    val start: LocalDateTime,
    val end: LocalDateTime
)
