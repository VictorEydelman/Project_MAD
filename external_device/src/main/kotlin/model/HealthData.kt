package org.example.model

import java.time.Instant

data class HealthData(
        val id: Long? = null,
        val timestamp: Instant,
        val heartRate: Int,
        val sleepPhase: SleepPhase
)

enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}
