package com.example.litfinder.remote.repository

import android.util.Log
import com.example.litfinder.BuildConfig
import com.example.litfinder.remote.api.AddBookRequest
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.api.BookselfRequest
import com.example.litfinder.remote.api.BookshelfResponse
import com.example.litfinder.remote.api.DataItemBookShelf
import com.example.litfinder.remote.api.DataItemRating
import com.example.litfinder.remote.api.RatingBookRequest
import com.example.litfinder.remote.api.RatingResponse
import com.example.litfinder.remote.api.RecommendationRequest
import com.example.litfinder.remote.api.UpdateBookRequest
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.remote.response.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class BookselfRepository(private val tokenProvider: () -> String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun getBookRecomendation(userId: Int, limit: Int = 10, page: Int = 1, callback: (List<BookItem>?) -> Unit) {
        val requestBody = RecommendationRequest(userId)
        apiService.getRecommendations(tokenProvider(), limit, page, requestBody).enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    Log.d("responseBookRecomendation", "onResponse: ${response.body()}")
                    callback(response.body()?.data)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getBookself(userId: Int, filter: String?, callback: (List<DataItemBookShelf>?) -> Unit) {
        val requestBody = BookselfRequest(userId, filter)
        apiService.getBookself(tokenProvider(), requestBody).enqueue(object : Callback<BookshelfResponse> {
            override fun onResponse(call: Call<BookshelfResponse>, response: Response<BookshelfResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.data)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<BookshelfResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    suspend fun getBookReviews(bookId: Int): Response<RatingResponse> {
        val request = RatingBookRequest(book_id = bookId)
        return apiService.getRatingBookshelf(tokenProvider(), request).awaitResponse()
    }

    fun getRatingBookself(bookseflId: Int, callback: (List<DataItemRating>?) -> Unit) {
        val requestBody = RatingBookRequest(bookseflId)
        apiService.getRatingBookshelf(tokenProvider(), requestBody).enqueue(object : Callback<RatingResponse> {
            override fun onResponse(call: Call<RatingResponse>, response: Response<RatingResponse>) {
                Log.d("resRating", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    callback(response.body()?.data)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    suspend fun addBookToBookshelf(userId: Int, bookId: Int): Pair<Boolean, String> {
        val request = AddBookRequest(userId, bookId)
        return try {
            val response = apiService.addBookToBookshelf(tokenProvider(), request).awaitResponse()
            if (response.isSuccessful) {
                Pair(true, response.body()?.message ?: "Book added successfully")
            } else {
                Pair(false, response.errorBody()?.string() ?: "Failed to add book")
            }
        } catch (e: Exception) {
            Pair(false, "Error: ${e.message}")
        }
    }

    suspend fun insertLogUser(userId: Int, bookId: Int): Pair<Boolean, String> {
        val request = AddBookRequest(userId, bookId)
        return try {
            val response = apiService.insertLogUser(tokenProvider(), request).awaitResponse()
            if (response.isSuccessful) {
                Pair(true, response.body()?.message ?: "Insert Log successfully")
            } else {
                Pair(false, response.errorBody()?.string() ?: "Failed to add book")
            }
        } catch (e: Exception) {
            Pair(false, "Error: ${e.message}")
        }
    }

    suspend fun updateBookToBookshelf(
        bookselfId: Int,
        status: String
    ): Pair<Boolean, String> {
        val request = UpdateBookRequest(
            bookself_id = bookselfId,
            status = status,
            book_id = -1, // or any default value, if not needed in this case
            user_id = -1, // or any default value, if not needed in this case
            profileName = "",
            reviewHelpfulness = "",
            reviewScore = 0,
            reviewSummary = "",
            reviewText = ""
        )
        return try {
            val response = apiService.updateBookToBookshelf(tokenProvider(), request).awaitResponse()
            if (response.isSuccessful) {
                Pair(true, response.body()?.message ?: "Bookself has been updated")
            } else {
                Pair(false, response.errorBody()?.string() ?: "Failed to update bookself")
            }
        } catch (e: Exception) {
            Pair(false, "Error: ${e.message}")
        }
    }

    suspend fun updateBookToBookshelfWithReview(
        bookselfId: Int,
        status: String,
        bookId: Int,
        userId: Int,
        profileName: String?,
        reviewHelpfulness: String,
        reviewScore: Int,
        reviewSummary: String,
        reviewText: String
    ): Pair<Boolean, String> {
        val request = UpdateBookRequest(
            bookself_id = bookselfId,
            status = status,
            book_id = bookId,
            user_id = userId,
            profileName = profileName,
            reviewHelpfulness = reviewHelpfulness,
            reviewScore = reviewScore,
            reviewSummary = reviewSummary,
            reviewText = reviewText
        )
        return try {
            val response = apiService.updateBookToBookshelf(tokenProvider(), request).awaitResponse()
            if (response.isSuccessful) {
                Pair(true, response.body()?.message ?: "Bookself has been updated")
            } else {
                Pair(false, response.errorBody()?.string() ?: "Failed to update bookself")
            }
        } catch (e: Exception) {
            Pair(false, "Error: ${e.message}")
        }
    }



}



