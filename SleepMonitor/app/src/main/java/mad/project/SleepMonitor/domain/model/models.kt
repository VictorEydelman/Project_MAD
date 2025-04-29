package mad.project.SleepMonitor.domain.model
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

enum class SleepPhase {
    AWAKE, REM, LIGHT, DEEP, UNKNOWN
}

enum class Weekday {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun
}

data class Alarm(
    val time: LocalTime,
    val alarm: Boolean,
)

data class BedTime(
    val time: LocalTime,
    val remindMeToSleep: Boolean,
    val remindBeforeBad: Boolean,
)
data class TimePreference(
    val asleepTime: LocalTime,
    val awakeTime: LocalTime,
)
data class User(
    val username: String,
    val password: String,
)

data class Report(
    val quality: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val totalSleep: Duration?,
    val awakenings: Int?,
    val avgAwake: Duration?,
    val avgAsleep: Duration?,
    val avgToFallAsleep: Duration?,
    val data: SleepData?,
    val distribution: WeekdaySleepDistribution?,
)

data class SleepDataPiece(
    val timestamp: Instant,
    val pulse: Int,
    val phase: SleepPhase,
)

typealias SleepData = List<SleepDataPiece>

data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)

typealias WeekdaySleepDistribution = List<WeekdaySleep>