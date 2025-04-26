package mad.project.SleepMonitor.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import mad.project.SleepMonitor.data.mapper.toDomain
import mad.project.SleepMonitor.data.network.AnalyticsApiService
import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.domain.repository.AnalyticsRepository
import mad.project.SleepMonitor.util.Resource

import kotlinx.coroutines.delay


class AnalyticsRepositoryImpl(
    private val apiService: AnalyticsApiService
) : AnalyticsRepository {

    override suspend fun getDailyReport(): Resource<Report> {
        delay(500)
        println("--- USING MOCKED DATA (Daily) in AnalyticsRepositoryImpl ---")
        val mockResponse = createMockReportResponseForDay()
        val domainReport: Report? = mockResponse.data?.toDomain()
        return if (domainReport != null) Resource.Success(domainReport)
        else Resource.Error("Failed to process mocked daily data.")
    }

    override suspend fun getWeeklyReport(): Resource<Report> {
        delay(700) // Имитация задержки
        println("--- USING MOCKED DATA (Weekly) in AnalyticsRepositoryImpl ---")
        val mockResponse = createMockReportResponseForWeek() // <<< Вызываем мок для НЕДЕЛИ
        val domainReport: Report? = mockResponse.data?.toDomain()
        return if (domainReport != null) Resource.Success(domainReport)
        else Resource.Error("Failed to process mocked weekly data.")
    }

    override suspend fun getAllTimeReport(): Resource<Report> {
        delay(900) // Имитация задержки
        println("--- USING MOCKED DATA (All Time - using Weekly mock) ---")
        // Для All Time пока тоже используем недельный мок
        val mockResponse = createMockReportResponseForWeek() // <<< Вызываем мок для НЕДЕЛИ
        val domainReport: Report? = mockResponse.data?.toDomain()
        return if (domainReport != null) Resource.Success(domainReport)
        else Resource.Error("Failed to process mocked all time data.")
    }

    /*
    --- Будущая реальная реализация (пример для Daily) ---
    override suspend fun getDailyReport(): Resource<Report> {
        return try {
             // Тут будет вызов apiService.getDailyReport() и обработка ответа
             // как в старом методе, но без request body
            val response = apiService.getDailyReport()
             if (response.isSuccessful) {
                 val body = response.body()
                 if (body != null && body.success && body.data != null) {
                     val domainReport = body.data.toDomain()
                     if (domainReport != null) Resource.Success(domainReport)
                     else Resource.Error("Failed to process daily report data.")
                 } else {
                     Resource.Error(body?.message ?: "API returned failure for daily report.")
                 }
             } else {
                 Resource.Error("HTTP Error ${response.code()} for daily report")
             }
        } catch (e: Exception) {
             // Обработка исключений (IOException, HttpException и т.д.)
            Resource.Error("Network or unexpected error getting daily report: ${e.message}")
        }
    }
    */
}