package com.elite.gemini.chatbot.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object responsible for creating and managing Retrofit instances
 * for communicating with the Gemini API.
 */
object RetrofitClient {
    
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Set logging level to BODY for detailed request/response logs
        // Consider reducing to HEADERS or NONE in production builds
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val geminiApiService: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}
