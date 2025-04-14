package org.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class HealthDataDto(
        val timestamp: String,
        val heartRate: Int,
        val sleepPhase: String
)
