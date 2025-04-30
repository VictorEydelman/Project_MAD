package mad.project.SleepMonitor.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8090/api/v1/"
    private const val EXTERNAL_DEVICE_URL = "http://10.0.2.2:8909/"

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var externalRetrofit: Retrofit

    private val gson = GsonBuilder()
        .serializeNulls()
        .create()

    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) {
            return
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(context.applicationContext)

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        externalRetrofit = Retrofit.Builder()
            .baseUrl(EXTERNAL_DEVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        isInitialized = true
    }

    private fun ensureInitialized() {
        if (!isInitialized) {
            throw IllegalStateException("RetrofitInstance must be initialized before use. Call initialize(context) first.")
        }
    }

    val analyticsApi: AnalyticsApiService by lazy {
        ensureInitialized()
        retrofit.create(AnalyticsApiService::class.java)
    }

    val profileApi: ProfileApi by lazy {
        ensureInitialized()
        retrofit.create(ProfileApi::class.java)
    }

    val authApi: AuthApiService by lazy {
        ensureInitialized()
        retrofit.create(AuthApiService::class.java)
    }

    val externalApi: ExternalApiService by lazy{
        Retrofit.Builder()
            .baseUrl(EXTERNAL_DEVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ExternalApiService::class.java)
    }

    val sleepApi: SleepApiService by lazy {
        ensureInitialized()
        retrofit.create(SleepApiService::class.java)
    }
}