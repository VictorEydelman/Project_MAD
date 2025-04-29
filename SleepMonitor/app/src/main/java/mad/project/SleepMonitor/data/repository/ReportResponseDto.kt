package mad.project.SleepMonitor.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import mad.project.SleepMonitor.data.network.dto.ReportDataDto
import mad.project.SleepMonitor.data.network.dto.ReportResponseDto
import mad.project.SleepMonitor.data.network.dto.SleepDataPieceDto
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun createMockReportResponseForDay(): ReportResponseDto {
    val now = Instant.now()
    val startTime = now.minus(1, ChronoUnit.DAYS)
        .truncatedTo(ChronoUnit.DAYS)
        .plus(23, ChronoUnit.HOURS)
        .plus(Random.nextInt(0, 30).toLong(), ChronoUnit.MINUTES)

    val endTime = startTime.plus(Random.nextInt(7, 9).toLong(), ChronoUnit.HOURS)
        .plus(Random.nextInt(0, 60).toLong(), ChronoUnit.MINUTES)

    val sleepDataPoints = mutableListOf<SleepDataPieceDto>()
    var currentTime = startTime
    var nightTotalSleep = Duration.ZERO
    var nightTotalAwake = Duration.ZERO
    var nightAwakenings = 0
    var lastPhase = "UNKNOWN"

    fun addPoint(time: Instant, phase: String, pulseRange: IntRange) {
        sleepDataPoints.add(
            SleepDataPieceDto(
                timestamp = DateTimeFormatter.ISO_INSTANT.format(time),
                sleepPhase = phase,
                pulse = Random.nextInt(pulseRange.first, pulseRange.last + 1)
            )
        )
        if (lastPhase != "AWAKE" && phase == "AWAKE") {
            nightAwakenings++
        }
        lastPhase = phase
    }


    addPoint(currentTime, "AWAKE", 70..80)
    val timeToFallAsleep = Duration.ofMinutes(Random.nextLong(5, 20))
    nightTotalAwake = nightTotalAwake.plus(timeToFallAsleep)
    currentTime = currentTime.plus(timeToFallAsleep)

    while (currentTime.isBefore(endTime)) {
        val phaseDurationMinutes: Long
        val nextPhase: String
        val pulseRange: IntRange

        when (Random.nextInt(4)) {
            0 -> { nextPhase = "DEEP"; phaseDurationMinutes = Random.nextLong(30, 70); pulseRange = 48..60 }
            1 -> { nextPhase = "REM"; phaseDurationMinutes = Random.nextLong(20, 50); pulseRange = 65..78 }
            2 -> { nextPhase = "AWAKE"; phaseDurationMinutes = Random.nextLong(2, 8); pulseRange = 70..85 }
            else -> { nextPhase = "LIGHT"; phaseDurationMinutes = Random.nextLong(25, 60); pulseRange = 55..70 }
        }

        addPoint(currentTime, nextPhase, pulseRange)
        val duration = Duration.ofMinutes(phaseDurationMinutes)

        if (nextPhase == "AWAKE") nightTotalAwake = nightTotalAwake.plus(duration)
        else nightTotalSleep = nightTotalSleep.plus(duration)
        currentTime = currentTime.plus(duration)

        if (currentTime.isAfter(endTime)) {
            val overshoot = Duration.between(endTime, currentTime)
            if (sleepDataPoints.isNotEmpty()) sleepDataPoints.removeLast()
            addPoint(endTime, "AWAKE", 75..85)
            if (lastPhase == "AWAKE") nightTotalAwake = nightTotalAwake.minus(overshoot).coerceAtLeast(Duration.ZERO)
            else nightTotalSleep = nightTotalSleep.minus(overshoot).coerceAtLeast(Duration.ZERO)
            break
        }
    }

    // Агрегированные значения
    val totalSleepSec = nightTotalSleep.toSeconds()
    val avgAsleepSec = totalSleepSec
    val avgAwakeSec = if (nightAwakenings > 0) nightTotalAwake.dividedBy(nightAwakenings.toLong()).toSeconds() else Duration.ofMinutes(2).toSeconds()
    val awakeningsCount = nightAwakenings

    return ReportResponseDto(
        success = true,
        message = null,
        data = ReportDataDto(
            totalSleepSec = totalSleepSec.toDouble(),
            avgAsleepSec = avgAsleepSec.toDouble(),
            avgAwakeSec = avgAwakeSec.toDouble(),
            awakenings = awakeningsCount,
            data = sleepDataPoints,
            quality = (Math.random() * 100).toInt(),
            startTime = LocalTime.now().minusSeconds(totalSleepSec).toString(),
            endTime = LocalTime.now().toString(),
            avgToFallAsleepSec = avgAwakeSec.toDouble(),
            distribution = null
        )
    )
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun createMockReportResponseForWeek(): ReportResponseDto {
    val now = Instant.now()
    val allSleepDataPoints = mutableListOf<SleepDataPieceDto>()
    var totalSleepDurationAggregated = Duration.ZERO
    var totalAwakeDurationAggregated = Duration.ZERO
    var totalAwakeningsAggregated = 0
    val numberOfDays = 7

    for (dayIndex in 0 until numberOfDays) {
        val nightStartTimeBase = now.minus((numberOfDays - 1 - dayIndex).toLong(), ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS)
            .plus(Random.nextInt(21, 24).toLong(), ChronoUnit.HOURS)
            .plus(Random.nextInt(0, 60).toLong(), ChronoUnit.MINUTES)

        val nightDurationHours = Random.nextDouble(6.0, 9.0)
        val nightEndTime = nightStartTimeBase.plusMillis((nightDurationHours * 60 * 60 * 1000).toLong())

        var currentTime = nightStartTimeBase
        var nightTotalSleep = Duration.ZERO
        var nightTotalAwake = Duration.ZERO
        var nightAwakenings = 0
        var lastPhase = "UNKNOWN"

        fun addNightPoint(time: Instant, phase: String, pulseRange: IntRange) {
            allSleepDataPoints.add(
                SleepDataPieceDto(
                    timestamp = DateTimeFormatter.ISO_INSTANT.format(time),
                    sleepPhase = phase,
                    pulse = Random.nextInt(pulseRange.first, pulseRange.last + 1)
                )
            )
            if (lastPhase != "AWAKE" && phase == "AWAKE") {
                nightAwakenings++
            }
            lastPhase = phase
        }

        addNightPoint(currentTime, "AWAKE", 70..80)
        val timeToFallAsleep = Duration.ofMinutes(Random.nextLong(5, 15))
        nightTotalAwake = nightTotalAwake.plus(timeToFallAsleep)
        currentTime = currentTime.plus(timeToFallAsleep)

        while (currentTime.isBefore(nightEndTime)) {
            val phaseDurationMinutes: Long
            val nextPhase: String
            val pulseRange: IntRange

            when (Random.nextInt(4)) {
                0 -> { nextPhase = "DEEP"; phaseDurationMinutes = Random.nextLong(30, 70); pulseRange = 48..60 }
                1 -> { nextPhase = "REM"; phaseDurationMinutes = Random.nextLong(20, 50); pulseRange = 65..78 }
                2 -> { nextPhase = "AWAKE"; phaseDurationMinutes = Random.nextLong(2, 8); pulseRange = 70..85 }
                else -> { nextPhase = "LIGHT"; phaseDurationMinutes = Random.nextLong(25, 60); pulseRange = 55..70 }
            }
            addNightPoint(currentTime, nextPhase, pulseRange)
            val duration = Duration.ofMinutes(phaseDurationMinutes)
            if (nextPhase == "AWAKE") nightTotalAwake = nightTotalAwake.plus(duration) else nightTotalSleep = nightTotalSleep.plus(duration)
            currentTime = currentTime.plus(duration)

            if (currentTime.isAfter(nightEndTime)) {
                val overshoot = Duration.between(nightEndTime, currentTime)
                if (allSleepDataPoints.isNotEmpty()) allSleepDataPoints.removeLast()
                addNightPoint(nightEndTime, "AWAKE", 75..85)
                if (lastPhase == "AWAKE") nightTotalAwake = nightTotalAwake.minus(overshoot).coerceAtLeast(Duration.ZERO)
                else nightTotalSleep = nightTotalSleep.minus(overshoot).coerceAtLeast(Duration.ZERO)
                break
            }
        }
        totalSleepDurationAggregated = totalSleepDurationAggregated.plus(nightTotalSleep)
        totalAwakeDurationAggregated = totalAwakeDurationAggregated.plus(nightTotalAwake)
        totalAwakeningsAggregated += nightAwakenings
    }

    val avgSleepPerNightMillis = if (numberOfDays > 0) totalSleepDurationAggregated.dividedBy(numberOfDays.toLong()).toSeconds() else 0
    val avgAwakeDurationPerAwakeningMillis = if (totalAwakeningsAggregated > 0) totalAwakeDurationAggregated.dividedBy(totalAwakeningsAggregated.toLong()).toSeconds() else Duration.ofMinutes(5).toSeconds()
    val avgAwakeningsPerNight = if (numberOfDays > 0) totalAwakeningsAggregated / numberOfDays else 0

    return ReportResponseDto(
        success = true,
        message = null,
        data = ReportDataDto(
            totalSleepSec = avgSleepPerNightMillis.toDouble(),
            avgAsleepSec = avgSleepPerNightMillis.toDouble(),
            avgAwakeSec = avgAwakeDurationPerAwakeningMillis.toDouble(),
            awakenings = avgAwakeningsPerNight,
            data = allSleepDataPoints,
            quality = (Math.random() * 100).toInt(),
            startTime = LocalTime.now().minusSeconds(avgSleepPerNightMillis).toString(),
            endTime = LocalTime.now().toString(),
            avgToFallAsleepSec = avgAwakeDurationPerAwakeningMillis.toDouble(),
            distribution = null
        )
    )
}
