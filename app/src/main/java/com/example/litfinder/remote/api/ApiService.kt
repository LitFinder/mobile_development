package com.example.litfinder.remote.api

import com.example.litfinder.remote.response.BookResponse
import com.example.litfinder.remote.response.ChangeNameResponse
import com.example.litfinder.remote.response.ForgotPasswordResponse
import com.example.litfinder.remote.response.GenreResponse
import com.example.litfinder.remote.response.GenreUserResponse
import com.example.litfinder.remote.response.LoginResponse
import com.example.litfinder.remote.response.PostChangePasswordResponse
import com.example.litfinder.remote.response.PostPreferenceResponse
import com.example.litfinder.remote.response.RegisterResponse
import retrofit2.Call
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

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("/book")
    suspend fun getBooks(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int,
        @Query("search") search: String? = null
    ): BookResponse

    @FormUrlEncoded
    @POST("/preference/book/add")
    suspend fun addBookPreference(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("books") books: List<Int>
    ): PostPreferenceResponse

    @GET("/genre")
    suspend fun getGenres(
        @Header("Authorization") token: String
    ): GenreResponse

    @FormUrlEncoded
    @POST("/preference/genre/add")
    suspend fun addGenrePreference(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("genres") genres: List<Int>
    ): PostPreferenceResponse

    @FormUrlEncoded
    @POST("/change-password")
    suspend fun changePassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): PostChangePasswordResponse

    @FormUrlEncoded
    @POST("/send-kode")
    suspend fun forgotPassword(
        @Field("email") email: String,
    ): ForgotPasswordResponse

    @FormUrlEncoded
    @POST("/preference/genre/user")
    suspend fun getUserGenre(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
    ): GenreUserResponse

    @FormUrlEncoded
    @POST("/profile/name")
    suspend fun changeName(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("name") name: String
    ): ChangeNameResponse
}
