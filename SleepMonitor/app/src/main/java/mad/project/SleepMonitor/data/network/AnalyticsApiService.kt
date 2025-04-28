package mad.project.SleepMonitor.data.network

import mad.project.SleepMonitor.data.network.dto.ReportResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface AnalyticsApiService {
    @GET("/sleep/daily-report")
    suspend fun getDailyReport(): Response<ReportResponseDto>

    @GET("/sleep/weekly-report")
    suspend fun getWeeklyReport(): Response<ReportResponseDto>

    @GET("/sleep/all-time-report")
    suspend fun getAllTimeReport(): Response<ReportResponseDto>
}
