package com.example.litfinder.remote.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("register")
    fun registerUser(@Body request: ApiResponse.RegisterRequest): Call<ApiResponse.RegisterResponse>

    @GET("book")
    fun getBooks(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): Call<BookResponse>

    @GET("genre")
    fun getGenres(): Call<TypeGenreResponse>

    @POST("bookself")
    fun getBookself(
        @Header("Authorization") token: String,
        @Body requestBody: BookselfRequest
    ): Call<BookshelfResponse>

    @POST("bookself/add")
    fun addBookToBookshelf(
        @Header("Authorization") token: String,
        @Body request: AddBookRequest
    ): Call<AddBookResponse>

    @POST("log")
    fun insertLogUser(
        @Header("Authorization") token: String,
        @Body request: AddBookRequest
    ): Call<AddBookResponse>

    @POST("bookself/update")
    fun updateBookToBookshelf(
        @Header("Authorization") token: String,
        @Body request: UpdateBookRequest
    ): Call<AddBookResponse>

    @POST("rating")
    fun getRatingBookshelf(
        @Header("Authorization") token: String,
        @Body request: RatingBookRequest
    ): Call<RatingResponse>

    @POST("recommendation")
    fun getRecommendations(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Body request: RecommendationRequest
    ): Call<BookResponse>
}