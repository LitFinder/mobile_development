package com.example.litfinder.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.api.LoginResponse
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.pagingSource.BookPagingSource
import com.example.litfinder.remote.pagingSource.RecommendationBasedReviewPagingSource
import com.example.litfinder.remote.pagingSource.RecommendationPagingSource
import com.example.litfinder.remote.pref.SettingPreferences
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.remote.response.ChangeNameResponse
import com.example.litfinder.remote.response.ChangePhotoResponse
import com.example.litfinder.remote.response.GenreResponse
import com.example.litfinder.remote.response.PostChangePasswordResponse
import com.example.litfinder.remote.response.PostLogResponse
import com.example.litfinder.remote.response.PostPreferenceResponse
import com.example.litfinder.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val settingPreferences: SettingPreferences
) {
    private var verificationCode: String? = null

    suspend fun saveSession(user: User) {
        userPreferences.saveSession(user)
    }

    suspend fun login(email: String, password: String): Flow<ApiResponseStatus<LoginResponse>> =
        flow {
            try {
                val loginResponse = ApiConfig.getApiService().login(email, password).execute()
                if (loginResponse.isSuccessful) {
                    val responseBody = loginResponse.body()
                    if (responseBody != null) {
                        if (responseBody.data != null) {
                            val user = User(
                                email = email,
                                id = responseBody.data.id ?: 0,
                                token = responseBody.token ?: "",
                                isLogin = true,
                                username = responseBody.data.username ?: "",
                                name = responseBody.data.name ?: "",
                                bio = responseBody.data.bio ?: "",
                                imageProfile = responseBody.data.imageProfile ?: "",
                                password = password
                            )
                            saveSession(user)
                            emit(ApiResponseStatus.Success(responseBody))
                        } else {
                            emit(ApiResponseStatus.Error(responseBody.message ?: "Login failed"))
                        }
                    } else {
                        emit(ApiResponseStatus.Error("Empty response body"))
                    }
                } else {
                    val errorBody = loginResponse.errorBody()?.string()
                    emit(ApiResponseStatus.Error("Failed to login: $errorBody"))
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during login: ${e.message}")
                e.printStackTrace()
                emit(ApiResponseStatus.Error("Login failed!"))
            }
        }.flowOn(Dispatchers.IO)


    suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String
    ): ApiResponseStatus<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val registerResponse = apiService.register(name, username, email, password)
                ApiResponseStatus.Success(registerResponse)
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during registration: ${e.message}")
                e.printStackTrace()
                ApiResponseStatus.Error("Registration failed")
            }
        }
    }

    fun getSession(): Flow<User> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    suspend fun getUser(): User {
        return userPreferences.getUser().first()
    }

    fun getBooks(searchQuery: String? = null): LiveData<PagingData<BookItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BookPagingSource(userPreferences, apiService, searchQuery) }
        ).liveData.map {
            it.map {
                it
            }
        }
    }


    suspend fun addBookPreference(books: List<Int>): PostPreferenceResponse {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()
        Log.d("BookViewModel", "Token: $token")
        Log.d("BookViewModel", "UserId: $userId")
        Log.d("BookViewModel", "books: $books")
        return apiService.addBookPreference(token, userId, books)
    }

    suspend fun getGenres(): GenreResponse {
        val token = userPreferences.getToken().first()
        return apiService.getGenres(token)
    }

    suspend fun addGenrePreference(genres: List<Int>): PostPreferenceResponse {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()
        Log.d("BookViewModel", "Token: $token")
        Log.d("BookViewModel", "UserId: $userId")
        Log.d("BookViewModel", "books: $genres")
        return apiService.addGenrePreference(token, userId, genres)
    }

    suspend fun changePassword(newPassword: String): PostChangePasswordResponse {
        val email = userPreferences.getEmail().first()
        return apiService.changePassword(email, newPassword)
    }

    suspend fun saveNewPassword(newPassword: String) {
        userPreferences.savePassword(newPassword)
    }

    suspend fun saveEmail(email: String) {
        userPreferences.saveEmail(email)
    }

    suspend fun getCurrentPassword(): String {
        return userPreferences.getPassword().first()
    }

    suspend fun sendVerificationCode(email: String): Boolean {
        val response = apiService.forgotPassword(email)
        verificationCode = response.kode
        return verificationCode != null
    }

    fun verifyCode(code: String): Boolean {
        return verificationCode == code
    }

    suspend fun getToken(): String? {
        return userPreferences.getToken().firstOrNull()
    }

    suspend fun getUserGenreIds(): List<Int> {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()
        val response = apiService.getUserGenre(token, userId)
        return response.data.map { it.id!! }
    }

    suspend fun changeUserName(name: String): ChangeNameResponse {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()
        return apiService.changeName(token, userId, name)
    }

    suspend fun saveNewName(newName: String) {
        userPreferences.saveName(newName)
    }

    suspend fun changeUserBio(bio: String): ChangeNameResponse {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()
        return apiService.changeBio(token, userId, bio)
    }

    suspend fun saveNewBio(newBio: String) {
        userPreferences.saveBio(newBio)
    }

    suspend fun updateUserProfilePicture(file: File): ChangePhotoResponse {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val picture = MultipartBody.Part.createFormData("picture", file.name, requestFile)
        val userIdRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), userId.toString())

        val response = apiService.updateUserProfilePicture(token, userIdRequestBody, picture)

        Log.d("API_RESPONSE", response.toString())
        if (response.status == "success") {
            val newImageUrl = response.newImage
            if (newImageUrl != null) {
                userPreferences.saveImageProfile(newImageUrl)
                Log.d("NEWIMAGE", newImageUrl)
            }
        }

        return response
    }

    fun getThemeSetting(): Flow<Boolean> {
        return settingPreferences.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    fun getRecommendations(): LiveData<PagingData<BookItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                val userId = runBlocking { userPreferences.getUserId().first().toInt() }
                RecommendationPagingSource(userPreferences, apiService, userId)
            }
        ).liveData.map {
            it.map {
                it
            }
        }
    }

    fun getRecommendationBasedReview(): LiveData<PagingData<BookItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                val userId = runBlocking { userPreferences.getUserId().first().toInt() }
                RecommendationBasedReviewPagingSource(userPreferences, apiService, userId)
            }
        ).liveData.map {
            it.map {
                it
            }
        }
    }

    suspend fun postLog(bookId: Int): PostLogResponse {
        val token = userPreferences.getToken().first()
        val userId = userPreferences.getUserId().first().toInt()

        Log.d("PostLog", "Posting log for bookId: $bookId, userId: $userId, token: $token")

        return apiService.postLog(token, userId, bookId)
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
            settingPreferences: SettingPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreferences, settingPreferences)
            }.also { instance = it }
    }
}