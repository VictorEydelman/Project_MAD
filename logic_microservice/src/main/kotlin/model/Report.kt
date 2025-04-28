package ru.itmo.model

import java.time.Duration
import java.time.LocalTime

data class Report(
    val quality: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val totalSleep: Duration,
    val awakenings: Int,
    val avgAwake: Duration,
    val avgAsleep: Duration,
    val avgToFallAsleep: Duration,
    val data: SleepData?,
    val distribution: WeekdaySleepDistribution?,
)
