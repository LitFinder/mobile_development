package com.example.litfinder.view.bookshelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.DataItemBookShelf
import com.example.litfinder.remote.api.DataItemRating
import com.example.litfinder.remote.repository.BookselfRepository
import kotlinx.coroutines.launch

class BookselfViewModel(tokenProvider: () -> String) : ViewModel() {
    private val bookselfRepository: BookselfRepository = BookselfRepository(tokenProvider)
    private val _bookselfItems = MutableLiveData<List<DataItemBookShelf>?>()
    val bookselfItems: LiveData<List<DataItemBookShelf>?> get() = _bookselfItems
    private val _getBookselfItems = MutableLiveData<List<DataItemRating>?>()
    val getBookselfItems: LiveData<List<DataItemRating>?> get() = _getBookselfItems
    private val _addBookResult = MutableLiveData<Pair<Boolean, String>>()
    private val _insertLogUser = MutableLiveData<Pair<Boolean, String>>()
    private val _reviewScores = MutableLiveData<IntArray>()
    private val _averageRating = MutableLiveData<Float>()
    val averageRating: LiveData<Float> get() = _averageRating
    private val _totalReviews = MutableLiveData<Int>()
    val totalReviews: LiveData<Int> get() = _totalReviews
    private val _ratingCounts = MutableLiveData<IntArray>()
    val ratingCounts: LiveData<IntArray> get() = _ratingCounts
    val reviewScores: LiveData<IntArray> = _reviewScores
    val addBookResult: LiveData<Pair<Boolean, String>> get() = _addBookResult

    fun fetchBookself(userId: Int, filter: String? = "all") {
        bookselfRepository.getBookself(userId, filter) { items ->
            _bookselfItems.postValue(items)
        }
    }

    fun addBookToBookshelf(userId: Int, bookId: Int) {
        viewModelScope.launch {
            val result = bookselfRepository.addBookToBookshelf(userId, bookId)
            _addBookResult.postValue(result)
        }
    }

    fun insertLogUser(userId: Int, bookId: Int) {
        viewModelScope.launch {
            val result = bookselfRepository.insertLogUser(userId, bookId)
            _insertLogUser.postValue(result)
        }
    }

    fun updateBookToBookshelf(bookselfId: Int, status: String) {
        viewModelScope.launch {
            val result = bookselfRepository.updateBookToBookshelf(bookselfId, status)
            _addBookResult.postValue(result)
        }
    }

    fun updateBookToBookshelfWithReview(
        bookselfId: Int,
        status: String,
        bookId: Int,
        userId: Int,
        profileName: String?,
        reviewHelpfulness: String,
        reviewScore: Int,
        reviewSummary: String,
        reviewText: String
    ) {
        viewModelScope.launch {
            val result = bookselfRepository.updateBookToBookshelfWithReview(
                bookselfId,
                status,
                bookId,
                userId,
                profileName,
                reviewHelpfulness,
                reviewScore,
                reviewSummary,
                reviewText
            )
            _addBookResult.postValue(result)
        }
    }

    fun getReviewScores(bookId: Int) {
        viewModelScope.launch {
            val response = bookselfRepository.getBookReviews(bookId)
            if (response.isSuccessful) {
                val reviews = response.body()?.data ?: emptyList()
                val scoreCounts = IntArray(5)
                var totalScore = 0
                reviews.forEach { review ->
                    val score = review.reviewScore
                    if (score != null) {
                        if (score in 1..5) {
                            scoreCounts[5 - score]++  // Reverse the indexing
                            totalScore += score
                        }
                    }
                }
                val averageRating = if (reviews.isNotEmpty()) totalScore.toFloat() / reviews.size else 0f
                _averageRating.postValue(averageRating)
                _totalReviews.postValue(reviews.size)
                _ratingCounts.postValue(scoreCounts)
            } else {
                _averageRating.postValue(0f)
                _totalReviews.postValue(0)
                _ratingCounts.postValue(IntArray(5))
            }
        }
    }

    fun getBookToBookshelf(bookselfId: Int) {
        bookselfRepository.getRatingBookself(bookselfId) { items ->
            _getBookselfItems.postValue(items)
        }
    }
}




