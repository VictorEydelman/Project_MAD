package ru.itmo.model

import java.time.Duration

data class Report(
    val total_sleep: Duration,
    val awakenings: Int,
    val avg_awake: Duration,
    val avg_asleep: Duration,
    val data: SleepData,
)
