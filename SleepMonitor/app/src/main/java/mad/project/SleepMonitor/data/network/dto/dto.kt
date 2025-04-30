package mad.project.SleepMonitor.data.network.dto
import com.google.gson.annotations.SerializedName

// --- Request DTO ---
data class ReportRequest(
    // null если "All time"
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?
)

// --- Response DTOs ---
data class ReportResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ReportDataDto? // Делаем nullable на случай !success
)

data class ReportDataDto(
    @SerializedName("quality") val quality: Int,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("totalSleep") val totalSleepSec: Double,
    @SerializedName("awakenings") val awakenings: Int,
    @SerializedName("avgAwake") val avgAwakeSec: Double,
    @SerializedName("avgAsleep") val avgAsleepSec: Double,
    @SerializedName("avgToFallAsleepSec") val avgToFallAsleepSec: Double,
    @SerializedName("data") val data: List<SleepDataPieceDto>?,
    @SerializedName("distribution") val distribution: List<WeekdaySleepDto>?
)

data class SleepDataPieceDto(
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("pulse") val pulse: Int?,
    @SerializedName("sleepPhase") val sleepPhase: String?
)
data class WeekdaySleepDto(
    @SerializedName("weekday") val weekday: String,
    @SerializedName("asleepHours") val asleepHours: Double,
)

data class ProfileResponse(
    @SerializedName("data") val data: ProfileData?,
    @SerializedName("message") val message: String?,
    @SerializedName("success") val success: Boolean
)

data class AlarmData(
    @SerializedName("time") val time: String,
    @SerializedName("alarm") val alarm: Boolean
)

data class BedTimeData(
    @SerializedName("time") val time: String,
    @SerializedName("remindMeToSleep") val remindMeToSleep: Boolean,
    @SerializedName("remindBeforeBad") val remindBeforeBad: Boolean
)

data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("gender") val gender: String?,
    @SerializedName("physicalCondition") val physicalCondition: String?,
    @SerializedName("caffeineUsage") val caffeineUsage: String?,
    @SerializedName("alcoholUsage") val alcoholUsage: String?,
    @SerializedName("alarmRecurring") val alarmRecurring: AlarmData,
    @SerializedName("alarmTemporary") val alarmTemporary: AlarmData,
    @SerializedName("bedTimeRecurring") val bedTimeRecurring: BedTimeData,
    @SerializedName("bedTimeTemporary") val bedTimeTemporary: BedTimeData
)

data class ProfileData(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("gender") val gender: String?,
    @SerializedName("physicalCondition") val physicalCondition: String?,
    @SerializedName("caffeineUsage") val caffeineUsage: String?,
    @SerializedName("alcoholUsage") val alcoholUsage: String?,
    @SerializedName("alarmRecurring") val alarmRecurring: AlarmData,
    @SerializedName("alarmTemporary") val alarmTemporary: AlarmData,
    @SerializedName("bedTimeRecurring") val bedTimeRecurring: BedTimeData,
    @SerializedName("bedTimeTemporary") val bedTimeTemporary: BedTimeData

//----- Request DTO --------
data class AuthRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

// --- Response DTOs ---
data class AuthResponse(
    @SerializedName("username") val username: String,
    @SerializedName("token") val token: String,
    @SerializedName("success") val success: Boolean
)
data class CheckAuthResponse(
    @SerializedName("username") val username: String?,
    @SerializedName("success") val success: Boolean
)
