package mad.project.SleepMonitor.data.network // Или другой подходящий пакет

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private val HARDCODED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzQ1ODU2ODUwLCJleHAiOjE3NDU4Nzg0NTB9.smdbNMEpN3IFsjQaLQGS_TeKfErP0ShiIt5bIpM-L5A"

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