package com.example.litfinder.view.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.BookResponse
import com.example.litfinder.remote.api.DataItem
import com.example.litfinder.remote.repository.BookRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel(tokenProvider: () -> String) : ViewModel() {
    private val bookRepository: BookRepository = BookRepository(tokenProvider)
    var currentPage = 1

    private val _books = MutableLiveData<List<DataItem>?>()
    val books: LiveData<List<DataItem>?> get() = _books

    fun fetchBooks(limit: Int = 10, page: Int = currentPage, search: String? = null) {
        viewModelScope.launch {
            bookRepository.getBooks(limit, page, search) { books ->
                if (page == 1) {
                    _books.postValue(books)
                } else {
                    val currentList = _books.value.orEmpty().toMutableList()
                    currentList.addAll(books.orEmpty())
                    _books.postValue(currentList)
                }
                currentPage = page // Update current page
            }
        }
    }
}



//class BookViewModel(token: String) : ViewModel() {
//    private val bookRepository: BookRepository = BookRepository(ApiConfig.getApiService(token))
//
//    private val _books = MutableLiveData<List<DataItem>?>()
//    val books: LiveData<List<DataItem>?> get() = _books
//
//    fun fetchBooks(limit: Int = 10, page: Int = 1, search: String? = null) {
//        bookRepository.getBooks(limit, page, search) { books ->
//            _books.postValue(books)
//        }
//    }
//
//    private val _dataListUser = MutableLiveData<List<DataItem>?>()
//    val dataListUser: MutableLiveData<List<DataItem>?> = _dataListUser
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
//
//    fun searchUserGithub(data: String) {
//        ApiConfig.getApiService().getBooks().enqueue(object : Callback<BookResponse> {
//            override fun onResponse(
//                call: Call<BookResponse>,
//                response: Response<BookResponse>
//            ) {
//                if (response.isSuccessful) {
//                    _loading.value = false
//                    _dataListUser.value = response.body()?.data
//                } else {
//                    _loading.value = false
//                    _errorMessage.value = "Request unsuccessful: ${response.code()}"
//                }
//            }
//
//            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
//                _loading.value = false
//                _errorMessage.value = t.localizedMessage ?: "Unknown error occurred"
//            }
//        })
//    }
//}





