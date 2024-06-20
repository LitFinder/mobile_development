package com.example.litfinder.view.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.repository.BookRepository
import com.example.litfinder.remote.response.BookItem
import kotlinx.coroutines.launch

class BookViewModel(tokenProvider: () -> String) : ViewModel() {
    private val bookRepository: BookRepository = BookRepository(tokenProvider)
    var currentPage = 1

    private val _books = MutableLiveData<List<BookItem>?>()
    val books: LiveData<List<BookItem>?> get() = _books

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

    private val _recommendation = MutableLiveData<List<BookItem>>()
    val recommendation: LiveData<List<BookItem>> get() = _recommendation

    fun fetchRecommendations(
        userId: Int,
        limit: Int = 10,
        page: Int = currentPage,
        search: String? = null
    ) {
        viewModelScope.launch {
            bookRepository.getRecommendations(userId, limit, page, search) { recommendation ->
                if (page == 1) {
                    _recommendation.postValue(recommendation!!)
                } else {
                    val currentList = _recommendation.value.orEmpty().toMutableList()
                    currentList.addAll(recommendation.orEmpty())
                    _recommendation.postValue(currentList)
                }
                currentPage = page
            }
        }
    }

    private val _recommendationBasedReview = MutableLiveData<List<BookItem>>()
    val recommendationBasedReview: LiveData<List<BookItem>> get() = _recommendationBasedReview

    fun fetchRecommendationsBasedReview(
        userId: Int,
        limit: Int = 10,
        page: Int = currentPage,
        search: String? = null
    ) {
        viewModelScope.launch {
            bookRepository.getRecommendationsBasedReview(
                userId,
                limit,
                page,
                search
            ) { recommendationBasedReview ->
                if (page == 1) {
                    _recommendationBasedReview.postValue(recommendationBasedReview!!)
                } else {
                    val currentList = _recommendationBasedReview.value.orEmpty().toMutableList()
                    currentList.addAll(recommendationBasedReview.orEmpty())
                    _recommendationBasedReview.postValue(currentList)
                }
                currentPage = page
            }
        }
    }

    private val _recommendationBasedBook = MutableLiveData<List<BookItem>>()
    val recommendationBasedBook: LiveData<List<BookItem>> get() = _recommendationBasedBook

    fun fetchRecommendationsBasedBook(
        userId: Int,
        limit: Int = 10,
        page: Int = currentPage,
        search: String? = null
    ) {
        viewModelScope.launch {
            bookRepository.getRecommendationsBasedBook(
                userId,
                limit,
                page,
                search
            ) { recommendationBasedBook ->
                if (page == 1) {
                    _recommendationBasedBook.postValue(recommendationBasedBook!!)
                } else {
                    val currentList = _recommendationBasedBook.value.orEmpty().toMutableList()
                    currentList.addAll(recommendationBasedBook.orEmpty())
                    _recommendationBasedBook.postValue(currentList)
                }
                currentPage = page
            }
        }
    }
}
