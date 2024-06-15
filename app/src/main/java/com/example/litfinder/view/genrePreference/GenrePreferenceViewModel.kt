package com.example.litfinder.view.genrePreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.GenreItem
import com.example.litfinder.remote.response.PostPreferenceResponse
import kotlinx.coroutines.launch

class GenrePreferenceViewModel(private val repository: Repository) : ViewModel() {
    private val _genres = MutableLiveData<List<GenreItem>>()
    val genres: LiveData<List<GenreItem>>
        get() = _genres

    private val _postResponse = MutableLiveData<PostPreferenceResponse>()
    val postResponse: LiveData<PostPreferenceResponse>
        get() = _postResponse

    private val _genreIds = MutableLiveData<List<Int>>()
    val genreIds: LiveData<List<Int>> get() = _genreIds

    suspend fun loadUserGenreIds(): List<Int> {
        return repository.getUserGenreIds()
    }


    fun addGenrePreference(genres: List<Int>) {
        viewModelScope.launch {
            try {
                val response = repository.addGenrePreference(genres)
                _postResponse.value = response
                Log.d("GenreViewModel", "Post Response: $response")
            } catch (e: Exception) {
                Log.e("GenreViewModel", "Error posting genre preference", e)
            }
        }
    }

    fun getGenres() {
        _genres.value = getManualGenres()
    }

    private fun getManualGenres(): List<GenreItem> {
        return listOf(
            GenreItem(11, "Fiction"),
            GenreItem(4, "Juvenile Fiction"),
            GenreItem(1, "Religion"),
            GenreItem(3, "History"),
            GenreItem(2, "Biography & Autobiography"),
            GenreItem(15, "Business & Economics"),
            GenreItem(16, "Juvenile Nonfiction"),
            GenreItem(31, "Computers"),
            GenreItem(35, "Social Science"),
            GenreItem(23, "Science"),
            GenreItem(24, "Cooking"),
            GenreItem(14, "Body, Mind & Spirit"),
            GenreItem(10, "Health & Fitness"),
            GenreItem(8, "Family & Relationships"),
            GenreItem(38, "Philosophy")
        )
    }
}
