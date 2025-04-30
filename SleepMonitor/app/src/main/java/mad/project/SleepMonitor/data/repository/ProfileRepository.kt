package mad.project.SleepMonitor.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import mad.project.SleepMonitor.data.mapper.toDomain

import mad.project.SleepMonitor.data.network.ProfileApi
import mad.project.SleepMonitor.data.network.dto.ProfileResponse
import mad.project.SleepMonitor.data.network.dto.UpdateProfileRequest
import mad.project.SleepMonitor.domain.model.Profile
import mad.project.SleepMonitor.domain.model.Report
import mad.project.SleepMonitor.util.Resource
import retrofit2.HttpException
import java.io.IOException

class ProfileRepository(private val api: ProfileApi) {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun getProfile(): Profile? {
        try {
            val response = api.getProfile()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    val domainProfile = body.data.toDomain()
                    return domainProfile
                }
            }
        } catch (e: HttpException) {
            Log.e("ProfileRepository", "HttpException: ${e.code()} - ${e.message()}", e)

        } catch (e: IOException) {
            Log.e("ProfileRepository", "IOException (network issue): ${e.message}", e)

        } catch (e: Exception) {
            Log.e("ProfileRepository", "Unexpected error: ${e.message}", e)

        }


        return null;
    }

    suspend fun updateProfile(profile: UpdateProfileRequest): Boolean {
        return try {
            val response = api.updateProfile(profile)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}