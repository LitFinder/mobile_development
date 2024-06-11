package com.example.litfinder.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.response.LoginResponse
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.pagingSource.BookPagingSource
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.remote.response.BookResponse
import com.example.litfinder.remote.response.PostBookResponse
import com.example.litfinder.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun saveSession(user: User) {
        userPreferences.saveSession(user)
    }

    suspend fun login(email: String, password: String): Flow<ApiResponseStatus<LoginResponse>> = flow {
        try {
            val loginResponse = ApiConfig.getApiService().login(email, password).execute()
            if (loginResponse.isSuccessful) {
                val responseBody = loginResponse.body()
                if (responseBody != null) {
                    if (responseBody.data != null) {
                        val user = User(responseBody.data.email ?: "", responseBody.token ?: "")
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
                val registerResponse = apiService.register(name,username, email, password)
                ApiResponseStatus.Success(registerResponse)
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during registration: ${e.message}")
                e.printStackTrace()
                ApiResponseStatus.Error("Registration failed")
            }
        }
    }

    suspend fun getBooks(limit: Int, page: Int): ApiResponseStatus<BookResponse> {
        return try {
            val token = userPreferences.getToken().first()
            Log.d("GetBooks", "Token: $token") // Log token sebelum pemanggilan API
            val bookResponse = apiService.getBooks(token, limit, page)
            Log.d("GetBooks", "Book Response: $bookResponse") // Log book response setelah pemanggilan API
            ApiResponseStatus.Success(bookResponse)
        } catch (e: Exception) {
            val errorMessage = "Failed to get books: ${e.message}"
            Log.e("GetBooks", errorMessage) // Log error jika terjadi exception
            ApiResponseStatus.Error(errorMessage)
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

    fun getBooks(): LiveData<PagingData<BookItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                BookPagingSource(userPreferences, apiService)
            }
        ).liveData
    }

    suspend fun addBookPreference(userId: Int, books: List<Int>): PostBookResponse {
        val token = userPreferences.getToken().first()
        return apiService.addBookPreference(token, userId, books)
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreferences)
            }.also { instance = it }
    }
}