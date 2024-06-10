package com.example.litfinder.view.bookPreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.BookResponse
import kotlinx.coroutines.launch

class BookPreferenceViewModel(private val repository: Repository) : ViewModel() {
    private val _bookResponse = MutableLiveData<ApiResponseStatus<BookResponse>>()
    val bookResponse: LiveData<ApiResponseStatus<BookResponse>>
        get() = _bookResponse

    fun fetchBooks(limit: Int, page: Int) {
        viewModelScope.launch {
            _bookResponse.value = ApiResponseStatus.Loading
            _bookResponse.value = repository.getBooks(limit, page)
        }
    }
}