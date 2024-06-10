package com.example.litfinder.remote.api

import com.example.litfinder.remote.response.BookResponse
import com.example.litfinder.remote.response.GenreResponse
import com.example.litfinder.remote.response.LoginResponse
import com.example.litfinder.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("genre")
    suspend fun getGenres(): Response<GenreResponse>

//    @GET("/book")
//    @Headers("Content-Type:application/json")
//    suspend fun getBooks(
//        @Header("Authorization") token: String,
//        @Query("limit") limit: Int = 10,
//        @Query("page") page: Int = 1,
//        @Query("search") search: String? = null
//    ): BookResponse

    @GET("/book")
    suspend fun getBooks(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): BookResponse

}