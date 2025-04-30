package mad.project.SleepMonitor.data.network

import mad.project.SleepMonitor.data.network.dto.SimpleResponse
import mad.project.SleepMonitor.domain.model.SleepData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SleepApiService {
    @POST("sleep/upload")
    fun addSleep(@Body request: SleepData?): Response<SimpleResponse>
}