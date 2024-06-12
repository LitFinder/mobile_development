package com.example.litfinder.view.bookPreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.remote.response.PostPreferenceResponse
import kotlinx.coroutines.launch

class BookPreferenceViewModel(private val repository: Repository) : ViewModel() {



    private val _searchQuery = MutableLiveData<String>()


    init {
        _searchQuery.value = ""
    }

    val bookResponse: LiveData<PagingData<BookItem>> = _searchQuery.switchMap { query ->
        repository.getBooks(query).cachedIn(viewModelScope)
    }



    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }




    private val _postResponse = MutableLiveData<PostPreferenceResponse>()
    val postResponse: LiveData<PostPreferenceResponse>
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