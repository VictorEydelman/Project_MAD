package ru.itmo.model

import java.time.Instant

data class SleepSession(
    val start_time: Instant,
    val end_time: Instant,
    val report: Report,
)
