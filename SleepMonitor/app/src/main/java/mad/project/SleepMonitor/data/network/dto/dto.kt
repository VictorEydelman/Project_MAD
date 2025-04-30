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
    @SerializedName("avg_asleep") val avgAsleepMillis: Long?,
    @SerializedName("avg_awake") val avgAwakeMillis: Long?,
    @SerializedName("total_sleep") val totalSleepMillis: Long?,

    @SerializedName("awakenings") val awakenings: Int?,
    @SerializedName("data") val data: List<SleepDataPieceDto>?
)

data class SleepDataPieceDto(
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("pulse") val pulse: Int?,
    @SerializedName("phase") val phase: String?
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
)
