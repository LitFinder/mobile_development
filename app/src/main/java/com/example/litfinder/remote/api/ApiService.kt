package com.example.litfinder.remote.api

import com.example.litfinder.remote.response.BookResponse
import com.example.litfinder.remote.response.ChangeNameResponse
import com.example.litfinder.remote.response.ChangePhotoResponse
import com.example.litfinder.remote.response.ForgotPasswordResponse
import com.example.litfinder.remote.response.GenreResponse
import com.example.litfinder.remote.response.GenreUserResponse
import com.example.litfinder.remote.response.PostChangePasswordResponse
import com.example.litfinder.remote.response.PostLogResponse
import com.example.litfinder.remote.response.PostPreferenceResponse
import com.example.litfinder.remote.response.RecommendationBasedBookResponse
import com.example.litfinder.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    fun registerUser(@Body request: ApiResponse.RegisterRequest): Call<ApiResponse.RegisterResponse>

    @GET("book")
    fun getBooks(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): Call<BookResponse>

    @FormUrlEncoded
    @POST("recommendation")
    fun getRecommendations(
        @Field("user_id") userId: Int,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): Call<BookResponse>

    @FormUrlEncoded
    @POST("recommendation/colabUser")
    fun getRecommendationsBasedReview(
        @Field("user_id") userId: Int,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): Call<BookResponse>

    @FormUrlEncoded
    @POST("recommendation/colabBook")
    fun getRecommendationsBasedBook(
        @Field("book_id") bookId: Int,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): Call<RecommendationBasedBookResponse>

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

    @FormUrlEncoded
    @POST("/profile/bio")
    suspend fun changeBio(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("bio") bio: String
    ): ChangeNameResponse

    @Multipart
    @POST("/profile/picture")
    suspend fun updateUserProfilePicture(
        @Header("Authorization") token: String,
        @Part("user_id") userId: RequestBody,
        @Part picture: MultipartBody.Part
    ): ChangePhotoResponse

    @FormUrlEncoded
    @POST("/recommendation")
    suspend fun getRecommendation(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Query("rating") rating: Boolean = true
    ): BookResponse

    @FormUrlEncoded
    @POST("/recommendation/colabUser")
    suspend fun getRecommendationBasedReview(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Query("rating") rating: Boolean = true
    ): BookResponse

    @FormUrlEncoded
    @POST("/log")
    suspend fun postLog(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("book_id") bookId: Int,
    ): PostLogResponse
}
