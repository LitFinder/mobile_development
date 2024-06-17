package com.example.litfinder.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetry: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var tryCount = 0

        while (response == null && tryCount < maxRetry) {
            try {
                response = chain.proceed(request)
            } catch (e: Exception) {
                tryCount++
                if (tryCount >= maxRetry) {
                    throw e
                }
            }
        }
        return response ?: throw IOException("Maximum retry attempts reached")
    }
}