package mad.project.SleepMonitor.data.repository

import android.util.Log
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
    fun getSleepData(): Resource<SleepData> {
        try {
            val response = externalApiService.getSleepData()
            Log.i("f",response.toString())
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val sleep: MutableList<SleepDataPiece> = mutableListOf()
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    for(i in body.data){
                        val localDateTime: LocalDateTime = LocalDateTime.parse(i.timestamp, formatter)
                        sleep.add(SleepDataPiece(localDateTime,i.pulse!!,SleepPhase.valueOf(i.sleepPhase!!.uppercase())))
                    }
                    return Resource.Success(sleep)
                }
            }
        } catch (e: HttpException) {
            throw Exception(e)
        } catch (e: IOException) {
            throw Exception(e)
        } catch (e: Exception) {
            throw Exception(e)
        }
        throw Exception("Error")
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