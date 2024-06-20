package com.example.litfinder.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.view.discover.BookViewModel

class BookViewModelFactory(private val tokenProvider: () -> String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            return BookViewModel(tokenProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




