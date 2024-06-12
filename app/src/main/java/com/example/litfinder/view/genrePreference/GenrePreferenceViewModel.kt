package com.example.litfinder.view.genrePreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiService
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
        viewModelScope.launch {
            try {
                val response = repository.getGenres()
                if (response.status == "success") {
                    _genres.value = response.data
                    Log.d("GenreViewModel", "Genres: ${response.data}")
                } else {
                    Log.e("GenreViewModel", "Failed to load genres")
                }
            } catch (e: Exception) {
                Log.e("GenreViewModel", "Error loading genres", e)
            }
        }
    }
}
