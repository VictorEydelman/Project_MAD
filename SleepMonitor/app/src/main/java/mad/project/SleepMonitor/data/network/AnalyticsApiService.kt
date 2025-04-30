package mad.project.SleepMonitor.data.network

import mad.project.SleepMonitor.data.network.dto.ReportResponseDto
import mad.project.SleepMonitor.data.network.dto.TimePreferenceResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface AnalyticsApiService {
    @GET("/api/v1/sleep/daily-report")
    suspend fun getDailyReport(): Response<ReportResponseDto>

    @GET("/api/v1/sleep/weekly-report")
    suspend fun getWeeklyReport(): Response<ReportResponseDto>

    @GET("/api/v1/sleep/all-time-report")
    suspend fun getAllTimeReport(): Response<ReportResponseDto>

    @GET("/api/v1/sleep/recommended-times")
    suspend fun getRecommendedTimes(): Response<TimePreferenceResponseDto>
}
