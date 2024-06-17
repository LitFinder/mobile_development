package com.example.litfinder.remote.api

import com.example.litfinder.BuildConfig
import com.example.litfinder.BuildConfig.BASE_URL
import com.example.litfinder.remote.interceptor.AuthInterceptor
import com.example.litfinder.remote.interceptor.RetryInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

//    fun getApiService(): ApiService {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        return retrofit.create(ApiService::class.java)
//    }


    private const val BASE_URL = "http://34.27.235.243:1234/"

    fun getApiService(): ApiService {
        val loggingInterceptor =
            if (de.hdodenhof.circleimageview.BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getApiBook(tokenProvider: () -> String): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .addInterceptor(logging) // Menambahkan logging
            .addInterceptor(RetryInterceptor(3)) // Menambahkan retry mechanism, mencoba ulang hingga 3 kali
            .connectTimeout(30, TimeUnit.SECONDS) // waktu tunggu untuk koneksi
            .readTimeout(30, TimeUnit.SECONDS)    // waktu tunggu untuk membaca data
            .writeTimeout(30, TimeUnit.SECONDS)   // waktu tunggu untuk menulis data
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getApiBookBookself(tokenProvider: () -> String): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)  // Ganti dengan base URL yang benar
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}