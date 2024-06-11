package com.example.litfinder.view.bookPreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.pagingSource.BookPagingSource
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.remote.response.BookResponse
import com.example.litfinder.remote.response.PostBookResponse
import kotlinx.coroutines.launch

class BookPreferenceViewModel(private val repository: Repository) : ViewModel() {
    val bookResponse: LiveData<PagingData<BookItem>> =
        repository.getBooks().cachedIn(viewModelScope)


    private val _postResponse = MutableLiveData<PostBookResponse>()
    val postResponse: LiveData<PostBookResponse>
        get() = _postResponse

    fun addBookPreference(userId: Int, books: List<Int>) {
        viewModelScope.launch {
            val response = repository.addBookPreference(userId, books)
            _postResponse.value = response

            // Log response
            Log.d("BookViewModel", "Response: $response")
        }
    }

}

//class BookPreferenceViewModel(private val repository: Repository) : ViewModel() {
//    private val _bookResponse = MutableLiveData<ApiResponseStatus<BookResponse>>()
//    val bookResponse: LiveData<ApiResponseStatus<BookResponse>>
//        get() = _bookResponse
//
//    fun fetchBooks(limit: Int, page: Int) {
//        viewModelScope.launch {
//            _bookResponse.value = ApiResponseStatus.Loading
//            _bookResponse.value = repository.getBooks(limit, page)
//        }
//    }
//}