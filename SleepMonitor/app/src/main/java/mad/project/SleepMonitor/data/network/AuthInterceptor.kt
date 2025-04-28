package mad.project.SleepMonitor.data.network // Или другой подходящий пакет

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private val HARDCODED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdHJpbmciLCJpYXQiOjE3NDU2NjgzOTcsImV4cCI6MTc0NTY4OTk5N30.Ikw4uZOG_c6D9vUmLf_5JOtnGlnxbAYalaIMAAIInfs"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        if (HARDCODED_TOKEN.isNotBlank() && originalRequest.header("Authorization") == null) {
            builder.header("Authorization", "Bearer $HARDCODED_TOKEN")
        }

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}