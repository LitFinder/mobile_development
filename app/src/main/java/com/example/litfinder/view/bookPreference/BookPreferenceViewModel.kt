package com.example.litfinder.view.bookPreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.response.BookItem
import kotlinx.coroutines.launch

class BookPreferenceViewModel() : ViewModel() {
    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> get() = _books

    private val apiService = ApiConfig.getApiService()
    private lateinit var userPreferences: UserPreferences

    fun getBooks() {
//        viewModelScope.launch {
//            try {
//                val user = userPreferences.getUser()
//                val token = user.token
//                Log.d("tokenbookpreference", "$token")
//                val response = apiService.getBooks("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwiaWF0IjoxNzE3OTQ1NjI0fQ.Og7y15H6tJSJgvy9m7_igQaeRIWKCYrvZ7_itZikvUU")
//                if (response.isSuccessful) {
//                    val bookResponse = response.body()
//                    bookResponse?.data?.forEach { bookItem ->
//                        Log.d("BookViewModel", "Title: ${bookItem?.title}, Author: ${bookItem?.authors}")
//                        // Add more logging as needed
//                    }
//                } else {
//                    Log.e("BookViewModel", "Failed to get books. Error: ${response.errorBody()}")
//                }
//            } catch (e: Exception) {
//                Log.e("BookViewModel", "Exception occurred: ${e.message}")
//            }
//        }
    }
}