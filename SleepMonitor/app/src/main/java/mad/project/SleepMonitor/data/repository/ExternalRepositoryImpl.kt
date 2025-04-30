package mad.project.SleepMonitor.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mad.project.SleepMonitor.data.network.ExternalApiService
import mad.project.SleepMonitor.data.network.SleepApiService
import mad.project.SleepMonitor.data.network.dto.SimpleResponse
import mad.project.SleepMonitor.domain.model.ExternalSleepData
import mad.project.SleepMonitor.domain.model.SleepData
import mad.project.SleepMonitor.domain.model.SleepDataPiece
import mad.project.SleepMonitor.domain.model.SleepPhase
import mad.project.SleepMonitor.util.Resource
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExternalRepositoryImpl(private val externalApiService: ExternalApiService, private val sleepApiService: SleepApiService) {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun getSleepData(): Resource<SleepData> {
        return try {
            val response = externalApiService.getSleepData()
            Log.i("f", response.toString())
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.i("b","3.5")
                    val sleep: MutableList<SleepDataPiece> = mutableListOf()
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME //
                    for (i in body.data!!) {
                        sleep.add(SleepDataPiece(LocalDateTime.parse(i.timestamp, formatter), i.pulse!!, SleepPhase.valueOf(i.sleepPhase!!.uppercase())))
                    }
                    Resource.Success(sleep)
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error("Error: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("HttpException: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("IOException: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }

    fun addSleepData(sleepData: SleepData): Result<mad.project.SleepMonitor.domain.model.SimpleResponse> {
        try {
            val response = sleepApiService.addSleep(sleepData)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Result.success(mad.project.SleepMonitor.domain.model.SimpleResponse(body.success,body.message))
                }
                return Result.failure(Exception("Login failed"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                return Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }

    }
}