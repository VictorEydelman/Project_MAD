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
