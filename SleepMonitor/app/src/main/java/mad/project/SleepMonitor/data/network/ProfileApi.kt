package mad.project.SleepMonitor.data.network

import mad.project.SleepMonitor.data.network.dto.ProfileResponse
import mad.project.SleepMonitor.data.network.dto.UpdateProfileRequest
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.POST


interface ProfileApi {

    @GET("/api/v1/profile/get")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("/api/v1/profile/update")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<Unit>
}
