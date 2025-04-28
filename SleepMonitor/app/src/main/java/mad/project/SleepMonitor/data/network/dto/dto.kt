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