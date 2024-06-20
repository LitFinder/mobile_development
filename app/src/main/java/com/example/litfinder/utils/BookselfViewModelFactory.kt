package com.example.litfinder.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.view.bookshelf.BookselfViewModel

class BookselfViewModelFactory(private val tokenProvider: () -> String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookselfViewModel::class.java)) {
            return BookselfViewModel(tokenProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
