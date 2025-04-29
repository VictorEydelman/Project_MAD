package mad.project.SleepMonitor.data.network // Или другой подходящий пакет

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private val HARDCODED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdHJpbmciLCJpYXQiOjE3NDU5NTU2ODMsImV4cCI6MTc0NjU2MDQ4M30.46KqUdrua3c0n77Aznw95qOonepGmaQQ3bjJjkhoCCE"

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