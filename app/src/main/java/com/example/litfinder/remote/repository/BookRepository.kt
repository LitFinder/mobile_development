package com.example.litfinder.remote.repository

import android.util.Log
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.api.BookResponse
import com.example.litfinder.remote.api.DataItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepository(private val tokenProvider: () -> String) {
    private val bookService: ApiService = ApiConfig.getApiBook(tokenProvider)

    fun getBooks(limit: Int = 10, page: Int = 1, search: String? = null, callback: (List<DataItem>?) -> Unit) {
        bookService.getBooks(limit, page, search).enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                Log.d("BookRepository", "onResponse: $response")
                if (response.isSuccessful) {
                    callback(response.body()?.data)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Log.e("BookRepository", "onFailure: ", t)
                callback(null)
            }
        })
    }
}



