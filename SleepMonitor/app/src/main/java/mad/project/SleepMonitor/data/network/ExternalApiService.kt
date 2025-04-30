package mad.project.SleepMonitor.data.network

import mad.project.SleepMonitor.data.network.dto.ReportResponseDto
import mad.project.SleepMonitor.domain.model.ExternalSleepData
import mad.project.SleepMonitor.domain.model.SleepData
import retrofit2.Response
import retrofit2.http.GET

interface ExternalApiService {
    @GET("/data")
    fun getSleepData(): Response<mad.project.SleepMonitor.data.network.dto.SleepData>
}