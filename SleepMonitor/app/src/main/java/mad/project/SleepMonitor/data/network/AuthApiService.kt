package mad.project.SleepMonitor.data.network

import mad.project.SleepMonitor.data.network.dto.AuthRequest
import mad.project.SleepMonitor.data.network.dto.AuthResponse
import mad.project.SleepMonitor.data.network.dto.CheckAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("auth/check")
    suspend fun checkAuth(@Header("Authorization") token: String): Response<CheckAuthResponse>
}