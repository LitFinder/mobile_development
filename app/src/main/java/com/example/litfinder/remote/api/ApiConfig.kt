package com.example.litfinder.remote.api

import com.example.litfinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            if (de.hdodenhof.circleimageview.BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Set timeout koneksi
            .readTimeout(30, TimeUnit.SECONDS) // Set timeout pembacaan
            .writeTimeout(30, TimeUnit.SECONDS) // Set timeout penulisan
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}