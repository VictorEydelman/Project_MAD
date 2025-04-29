package mad.project.SleepMonitor.data.repository

import android.content.Context
import mad.project.SleepMonitor.data.mapper.toDomain
import mad.project.SleepMonitor.data.network.AuthApiService
import mad.project.SleepMonitor.data.network.dto.AuthRequest
import mad.project.SleepMonitor.domain.model.User
import androidx.core.content.edit
import mad.project.SleepMonitor.domain.repository.AuthRepository
import org.json.JSONObject

class AuthRepositoryImpl(private val authApi: AuthApiService, private val context: Context) : AuthRepository
{
    override suspend fun login(authRequest: AuthRequest): Result<User> {
        try {
            val response = authApi.login(authRequest)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse?.success == true) {
                    val authUser = authResponse.toDomain(authRequest.password)
                    if (authUser != null) {
                        saveToken(authResponse.token)
                        return Result.success(authUser)
                    }
                }
                return Result.failure(Exception("Login failed"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = getErrorMessageFromJson(errorBody)
                return Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun register(authRequest: AuthRequest): Result<User> {
        try {
            val response = authApi.register(authRequest)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse?.success == true) {
                    val authUser = authResponse.toDomain(authRequest.password)
                    if (authUser != null) {
                        saveToken(authResponse.token)
                        return Result.success(authUser)
                    }
                }
                return Result.failure(Exception("Registration failed"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = getErrorMessageFromJson(errorBody)
                return Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun check(token: String, storedPassword: String?): Result<User> {
        return try {
            val response = authApi.checkAuth("Bearer $token")
            if (response.isSuccessful) {
                val checkResponse = response.body()
                if (checkResponse?.success == true) {
                    val user = checkResponse.toDomain(storedPassword)
                    if (user != null) {
                        return Result.success(user)
                    }
                }
                Result.failure(Exception("Authentication check failed"))
            } else {
                Result.failure(Exception("Authentication check failed with HTTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveToken(token: String) {
        val prefs = context.getSharedPreferences("SleepMonitorPrefs", Context.MODE_PRIVATE)
        prefs.edit() { putString("auth_token", token) }
    }

    private fun getErrorMessageFromJson(errorBody: String): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.optString("message", "Unknown error")
        } catch (e: Exception) {
            "Error parsing the error message"
        }
    }
}