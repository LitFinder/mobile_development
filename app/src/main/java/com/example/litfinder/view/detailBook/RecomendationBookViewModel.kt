package com.example.litfinder.view.detailBook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.DataItem
import com.example.litfinder.remote.repository.BookselfRepository
import kotlinx.coroutines.launch

class RecomendationBookViewModel(tokenProvider: () -> String) : ViewModel() {
    private val bookselfRepository: BookselfRepository = BookselfRepository(tokenProvider)
    var currentPage = 1

    private val _bookRecomendation = MutableLiveData<List<DataItem>?>()
    val bookRecomendatiomItems: LiveData<List<DataItem>?> get() = _bookRecomendation

    fun fetchBooksrecomendation(limit: Int = 10, page: Int = currentPage, userId: Int = 0) {
        viewModelScope.launch {
            bookselfRepository.getBookRecomendation(userId, limit, page) { books ->
                if (page == 1) {
                    _bookRecomendation.postValue(books)
                } else {
                    val currentList = _bookRecomendation.value.orEmpty().toMutableList()
                    currentList.addAll(books.orEmpty())
                    _bookRecomendation.postValue(currentList)
                }
                currentPage = page // Update current page
            }
        }
    }
}