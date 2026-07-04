package com.fixit.androidfront.data

import android.content.Context
import com.fixit.androidfront.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        val sessionManager = SessionManager(context)

        // Add Token if exists
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}

object ApiClient {
    // URL is injected at compile time via BuildConfig:
    //   debug   → http://10.0.2.2:3000/  (Android emulator → host localhost)
    //   release → https://fix-it-zcgs.onrender.com/
    private val BASE_URL get() = BuildConfig.BASE_URL

    fun getRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .apply {
                // Only attach the verbose body logger in debug builds
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuthService(context: Context): AuthService {
        return getRetrofit(context).create(AuthService::class.java)
    }

    fun getAppService(context: Context): AppService {
        return getRetrofit(context).create(AppService::class.java)
    }
}
