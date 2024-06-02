package com.example.fundamentalsubmission2.data.remote.retrofit

import com.example.litfinder.remote.api.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    fun registerUser(@Body request: ApiResponse.RegisterRequest): Call<ApiResponse.RegisterResponse>
}