package mad.project.SleepMonitor.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import mad.project.SleepMonitor.data.mapper.toDomain
import mad.project.SleepMonitor.data.network.AnalyticsApiService
import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.domain.repository.AnalyticsRepository
import mad.project.SleepMonitor.util.Resource
import retrofit2.HttpException
import java.io.IOException

class AnalyticsRepositoryImpl(
    private val apiService: AnalyticsApiService
) : AnalyticsRepository {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun getDailyReport(): Resource<Report> {
        try {
            val response = apiService.getDailyReport()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    val domainReport = body.data.toDomain()
                    if (domainReport != null) {
                        return Resource.Success(domainReport)
                    }
                }
            }
        } catch (e: HttpException) {
            // Ошибка HTTP, переход к моку
        } catch (e: IOException) {
            // Ошибка сети, переход к моку
        } catch (e: Exception) {
            // Другая ошибка, переход к моку
        }


        return try {
            val mockResponse = createMockReportResponseForDay()
            val domainReport: Report? = mockResponse.data?.toDomain()
            if (domainReport != null) {
                Resource.Success(domainReport)
            } else {
                Resource.Error("Failed to process mocked daily data.")
            }
        } catch (mockE: Exception) {
            Resource.Error("Failed to process mocked daily data: ${mockE.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun getWeeklyReport(): Resource<Report> {
        try {
            val response = apiService.getWeeklyReport()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    val domainReport = body.data.toDomain()
                    if (domainReport != null) {
                        return Resource.Success(domainReport)
                    }
                }
            }
        } catch (e: HttpException) {
            // Ошибка HTTP, переход к моку
        } catch (e: IOException) {
            // Ошибка сети, переход к моку
        } catch (e: Exception) {
            // Другая ошибка, переход к моку
        }

        return try {
            val mockResponse = createMockReportResponseForWeek()
            val domainReport: Report? = mockResponse.data?.toDomain()
            if (domainReport != null) {
                Resource.Success(domainReport)
            } else {
                Resource.Error("Failed to process mocked weekly data.")
            }
        } catch (mockE: Exception) {
            Resource.Error("Failed to process mocked weekly data: ${mockE.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun getAllTimeReport(): Resource<Report> {
        try {
            val response = apiService.getAllTimeReport()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    val domainReport = body.data.toDomain()
                    if (domainReport != null) {
                        return Resource.Success(domainReport)
                    }
                }
            }
        } catch (e: HttpException) {
            // Ошибка HTTP, переход к моку
        } catch (e: IOException) {
            // Ошибка сети, переход к моку
        } catch (e: Exception) {
            // Другая ошибка, переход к моку
        }

        return try {
            val mockResponse = createMockReportResponseForWeek()
            val domainReport: Report? = mockResponse.data?.toDomain()
            if (domainReport != null) {
                Resource.Success(domainReport)
            } else {
                Resource.Error("Failed to process mocked all time data.")
            }
        } catch (mockE: Exception) {
            Resource.Error("Failed to process mocked all time data: ${mockE.message}")
        }
    }
}