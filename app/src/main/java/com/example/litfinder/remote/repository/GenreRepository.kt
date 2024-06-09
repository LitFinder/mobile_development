package com.example.litfinder.remote.repository

import android.util.Log
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.response.GenreResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

class GenreRepository(private val apiService: ApiService) {
    suspend fun getGenres(): Response<GenreResponse> {
        try {
            val response = apiService.getGenres()
            if (response.isSuccessful) {
                Log.d("GenreRepository", "Data received: ${response.body()}")
            } else {
                Log.e("GenreRepository", "Error: ${response.errorBody()}")
            }
            return response
        } catch (e: Exception) {
            Log.e("GenreRepository", "Exception: ${e.message}")
            return Response.error(500, ResponseBody.create("application/json".toMediaTypeOrNull(), "{}"))
        }
    }
}