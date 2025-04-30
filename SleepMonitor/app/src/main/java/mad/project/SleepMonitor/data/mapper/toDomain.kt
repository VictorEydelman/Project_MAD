package mad.project.SleepMonitor.data.mapper


import mad.project.SleepMonitor.data.network.dto.ProfileData
import mad.project.SleepMonitor.data.network.dto.AuthRequest
import mad.project.SleepMonitor.data.network.dto.AuthResponse
import mad.project.SleepMonitor.data.network.dto.CheckAuthResponse

import mad.project.SleepMonitor.data.network.dto.ReportDataDto
import mad.project.SleepMonitor.data.network.dto.SleepDataPieceDto
import mad.project.SleepMonitor.data.network.dto.TimePreferenceDto
import mad.project.SleepMonitor.data.network.dto.WeekdaySleepDto
import mad.project.SleepMonitor.domain.model.*
import java.time.*
import java.time.format.DateTimeParseException
import kotlin.math.roundToInt


fun ReportDataDto.toDomain(): Report? {
    val quality = this.quality
    val startTime = LocalTime.parse(this.startTime)
    val endTime = LocalTime.parse(this.endTime)

    val totalSleepDuration = Duration.ofSeconds(this.totalSleepSec.toLong())
    val awakeningsCount = this.awakenings
    val avgAwakeDuration = Duration.ofSeconds(this.avgAwakeSec.toLong())
    val avgAsleepDuration = Duration.ofSeconds(this.avgAsleepSec.toLong())
    val avgToFallAsleepDuration = Duration.ofSeconds(this.avgToFallAsleepSec.toLong())

    val mappedSleepData: SleepData? = this.data
        ?.mapNotNull { it.toDomain() }
        ?.sortedBy { it.timestamp }

    if (mappedSleepData.isNullOrEmpty()) {
        println("Warning: No valid sleep data pieces found in the report.")
    }

    val mappedDistribution: WeekdaySleepDistribution? = this.distribution
        ?.mapNotNull { it.toDomain() }

    if (mappedDistribution.isNullOrEmpty()) {
        println("Warning: No valid week distribution found in the report.")
    }

//    val reportStartTime: LocalTime? = mappedSleepData?.firstOrNull()?.timestamp?.atZone(ZoneId.systemDefault())?.toLocalTime()
//    val reportEndTime: LocalTime? = mappedSleepData?.lastOrNull()?.timestamp?.atZone(ZoneId.systemDefault())?.toLocalTime()
//    val calculatedQuality: Int = calculateSleepQuality(totalSleepDuration, awakeningsCount)
//    val calculatedDistribution: WeekdaySleepDistribution? = calculateWeekdayDistribution(mappedSleepData)

    return Report(
        quality = quality,
        startTime = startTime,
        endTime = endTime,
        totalSleep = totalSleepDuration,
        awakenings = awakeningsCount,
        avgAwake = avgAwakeDuration,
        avgAsleep = avgAsleepDuration,
        avgToFallAsleep = avgToFallAsleepDuration,
        data = mappedSleepData,
        distribution = mappedDistribution
    )
}

fun SleepDataPieceDto.toDomain(): SleepDataPiece? {
    val instant = try {
        this.timestamp?.let { LocalDateTime.parse(it) }
    } catch (e: DateTimeParseException) {
        println("Error parsing timestamp: ${this.timestamp}, error: ${e.message}")
        null
    }

    val phase = try {
        this.sleepPhase?.let { SleepPhase.valueOf(it.uppercase()) } ?: SleepPhase.UNKNOWN
    } catch (e: IllegalArgumentException) {
        println("Error parsing phase: ${this.sleepPhase}, error: ${e.message}")
        SleepPhase.UNKNOWN
    }

    return if (instant != null && this.pulse != null) {
        SleepDataPiece(
            timestamp = instant,
            pulse = this.pulse,
            sleepPhase = phase
        )
    } else {
        null
    }
}
fun WeekdaySleepDto.toDomain(): WeekdaySleep? {
    val week = try {
        Weekday.valueOf(this.weekday)
    } catch (e: IllegalArgumentException) {
        println("Error parsing weekday: ${this.weekday}, error: ${e.message}")
        return null
    }
    return WeekdaySleep(week, this.asleepHours)
}

fun TimePreferenceDto.toDomain(): TimePreference? {
    return try {
        TimePreference(
            asleepTime = LocalTime.parse(asleepTime),
            awakeTime = LocalTime.parse(awakeTime)
        )
    } catch (e: DateTimeParseException) {
        println("Error parsing times: ${asleepTime} ${awakeTime}, error: ${e.message}")
        null
    }
}

// --- Mappers for Authentication (Login/Register) ---
fun AuthRequest.toDomain(): User? {
    if (username.isBlank()) {
        println("Error: Username is empty or blank")
        return null
    }
    if (password.isBlank() || password.length < 6) {
        println("Error: Password is empty or too short")
        return null
    }
    return User(
        username = username,
        password = password
    )
}

fun AuthResponse.toDomain(storedPassword: String): User? {
    if (!success) {
        println("Error: Authentication failed for username: $username")
        return null
    }
    if (username.isBlank()) {
        println("Error: Invalid username in AuthResponse")
        return null
    }
    return User(
        username = username,
        password = storedPassword
    )
}

fun CheckAuthResponse.toDomain(storedPassword: String?): User? {
    if (!success || username.isNullOrBlank()) {
        println("Error: Check authentication failed or no valid username")
        return null
    }
    if (storedPassword.isNullOrBlank()) {
        println("Warning: No stored password available for CheckAuth")
        return null
    }
    return User(
        username = username,
        password = storedPassword
    )
}

private fun calculateSleepQuality(totalSleep: Duration?, awakenings: Int?): Int {
    if (totalSleep == null) return 0

    val hoursSlept = totalSleep.toHours().toInt()
    val awakeningsCount = awakenings ?: 0

    var score = 0
    score += when {
        hoursSlept >= 9 -> 50
        hoursSlept >= 7 -> 40
        hoursSlept >= 5 -> 25
        else -> 10
    }

    score += when {
        awakeningsCount == 0 -> 40
        awakeningsCount <= 2 -> 30
        awakeningsCount <= 5 -> 15
        else -> 5
    }

    return (score * 1.1).roundToInt().coerceIn(0, 100)
}

private fun calculateWeekdayDistribution(sleepData: SleepData?): WeekdaySleepDistribution? {
    if (sleepData.isNullOrEmpty()) return null

    val sleepByDay = sleepData
        .groupBy { it.timestamp.atZone(ZoneId.systemDefault()).dayOfWeek }
        .mapValues { (_, pieces) -> calculateTotalSleepDurationForPieces(pieces) }

    val distribution = DayOfWeek.values().map { javaDayOfWeek ->
        val domainWeekday = mapJavaDayToDomainWeekday(javaDayOfWeek)
        val duration = sleepByDay[javaDayOfWeek] ?: Duration.ZERO
        val hours = duration.toMillis() / (1000.0 * 60 * 60)
        WeekdaySleep(weekday = domainWeekday, asleepHours = hours)
    }

    return distribution
}


private fun calculateTotalSleepDurationForPieces(pieces: List<SleepDataPiece>): Duration {
    var totalDuration = Duration.ZERO
    val sortedPieces = pieces.sortedBy { it.timestamp }

    for (i in 0 until sortedPieces.size - 1) {
        if (sortedPieces[i].sleepPhase != SleepPhase.AWAKE) {
            val durationBetweenPoints = Duration.between(sortedPieces[i].timestamp, sortedPieces[i + 1].timestamp)
            if (durationBetweenPoints.abs().toMinutes() < 30) {
                totalDuration = totalDuration.plus(durationBetweenPoints)
            }
        }
    }
    return totalDuration
}

private fun mapJavaDayToDomainWeekday(javaDayOfWeek: DayOfWeek): Weekday {
    return when (javaDayOfWeek) {
        DayOfWeek.MONDAY -> Weekday.Mon
        DayOfWeek.TUESDAY -> Weekday.Tue
        DayOfWeek.WEDNESDAY -> Weekday.Wed
        DayOfWeek.THURSDAY -> Weekday.Thu
        DayOfWeek.FRIDAY -> Weekday.Fri
        DayOfWeek.SATURDAY -> Weekday.Sat
        DayOfWeek.SUNDAY -> Weekday.Sun
    }
}

fun ProfileData.toDomain(): Profile {
    return Profile(
        name = this.name,
        surname = this.surname,
        birthday = this.birthday,
        gender = this.gender,
        physicalCondition = this.physicalCondition,
        caffeineUsage = this.caffeineUsage,
        alcoholUsage = this.alcoholUsage,
        alarmRecurring = this.alarmRecurring,
        alarmTemporary = this.alarmTemporary,
        bedTimeRecurring = this.bedTimeRecurring,
        bedTimeTemporary = this.bedTimeTemporary
    )
}

