package com.example.litfinder.remote.api

import com.example.litfinder.remote.response.Genre
import com.example.litfinder.remote.response.GenreResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("register")
    fun registerUser(@Body request: ApiResponse.RegisterRequest): Call<ApiResponse.RegisterResponse>

//    @GET("genre")
//    fun getGenres(): Call<List<Genre>>

    @GET("genre")
    suspend fun getGenres(): Response<GenreResponse>
}