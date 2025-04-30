package mad.project.SleepMonitor.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    companion object {
        const val PREFS_NAME = "SleepMonitorPrefs"
        const val AUTH_TOKEN_KEY = "auth_token"
    }

    private fun getToken(): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(AUTH_TOKEN_KEY, null)?.takeIf { it.isNotBlank() }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        if (originalRequest.header("Authorization") == null) {
            val token = getToken()
            if (token != null) {
                builder.header("Authorization", "Bearer $token")
            } else {
            }
        }

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}