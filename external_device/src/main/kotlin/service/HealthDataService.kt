package org.example.service

import org.example.model.SleepPhase
import org.example.dto.HealthDataDto
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class HealthDataService {
    fun generateData(lastSync: Instant?): List<HealthDataDto> {
        val startTime = lastSync ?: Instant.now().minus(8, ChronoUnit.HOURS)
        val endTime = Instant.now()
        val data = mutableListOf<HealthDataDto>()

        var currentTime = startTime
        while (currentTime.isBefore(endTime)) {
            val heartRate = generateHeartRate(currentTime)
            val sleepPhase = determineSleepPhase(heartRate)
            data.add(
                    HealthDataDto(
                            timestamp = currentTime.toString(),
                            heartRate = heartRate,
                            sleepPhase = sleepPhase.name
                    )
            )
            currentTime = currentTime.plus(10, ChronoUnit.MINUTES)  // Интервал 10 минут
        }

        return data
    }

    private fun generateHeartRate(timestamp: Instant): Int {
        val hour = timestamp.atZone(java.time.ZoneId.systemDefault()).hour
        return when {
            hour in 6..21 -> Random.nextInt(60, 100)  // Бодрствование
            else -> when (Random.nextInt(0, 100)) {
                in 0..30 -> Random.nextInt(30, 40)   // Глубокий сон
                in 31..60 -> Random.nextInt(40, 50)   // Поверхностный сон
                in 61..80 -> Random.nextInt(50, 60)   // Дремота
                else -> Random.nextInt(50, 70)       // REM-сон
            }
        }
    }

    private fun determineSleepPhase(heartRate: Int): SleepPhase {
        return when {
            heartRate > 60 -> SleepPhase.AWAKE
            heartRate in 50..60 -> SleepPhase.LIGHT_SLEEP
            heartRate in 40..49 -> SleepPhase.DEEP_SLEEP
            else -> SleepPhase.REM_SLEEP
        }
    }
}
