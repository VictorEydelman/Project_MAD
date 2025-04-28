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