package mad.project.SleepMonitor.domain.model
import com.google.gson.annotations.SerializedName
import mad.project.SleepMonitor.data.network.dto.AlarmData
import mad.project.SleepMonitor.data.network.dto.BedTimeData
import mad.project.SleepMonitor.util.Resource
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
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
    val timestamp: LocalDateTime,
    val pulse: Int,
    val sleepPhase: SleepPhase,
)

data class Profile(
    val name: String,
    val surname: String,
    val birthday: String,
    val gender: String,
    val physicalCondition: String,
    val caffeineUsage: String,
    val alcoholUsage: String,
    val alarmRecurring: AlarmData?,
    val alarmTemporary: AlarmData?,
    val bedTimeRecurring: BedTimeData?,
    val bedTimeTemporary: BedTimeData?
)


typealias SleepData = List<SleepDataPiece>

data class ExternalSleepData(
    val success: Boolean,
    val data: SleepData
)

data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)
data class SimpleResponse(
    val success: Boolean,
    val message: String?,
)
typealias WeekdaySleepDistribution = List<WeekdaySleep>