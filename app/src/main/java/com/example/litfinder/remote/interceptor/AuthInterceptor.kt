package com.example.litfinder.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")  // Pastikan "Bearer " diikuti oleh spasi
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}
