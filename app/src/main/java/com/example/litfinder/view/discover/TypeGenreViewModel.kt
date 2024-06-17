package com.example.litfinder.view.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.DataItemtype
import com.example.litfinder.remote.repository.TypeGenreRepository
import kotlinx.coroutines.launch

class TypeGenreViewModel(tokenProvider: () -> String) : ViewModel() {
    private val genreRepository: TypeGenreRepository = TypeGenreRepository(tokenProvider)
    private val _genres = MutableLiveData<List<DataItemtype>?>()
    val genres: LiveData<List<DataItemtype>?> get() = _genres

    init {
        fetchGenres()
    }

    fun fetchGenres() {
        genreRepository.getGenres { genres ->
            _genres.postValue(genres)
        }
    }
}
