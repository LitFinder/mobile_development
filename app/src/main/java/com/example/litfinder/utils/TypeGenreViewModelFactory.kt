package com.example.litfinder.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.view.discover.TypeGenreViewModel

class TypeGenreViewModelFactory(private val tokenProvider: () -> String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TypeGenreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TypeGenreViewModel(tokenProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}