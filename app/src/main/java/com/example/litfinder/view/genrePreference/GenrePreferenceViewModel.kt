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
import kotlinx.coroutines.launch

class GenrePreferenceViewModel (private val repository: Repository): ViewModel() {
    private val _genres = MutableLiveData<List<GenreItem>>()
    val genres: LiveData<List<GenreItem>>
        get() = _genres

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

//class GenrePreferenceViewModel : ViewModel() {
//    private val _genreNames = MutableLiveData<List<String>>()
//    val genreNames: LiveData<List<String>> get() = _genreNames
//
//    private val apiService: ApiService = ApiConfig.getApiService()
//
//    fun getGenreNames() {
//        apiService.getGenres().enqueue(object : Callback<List<Genre>> {
//            override fun onResponse(call: Call<List<Genre>>, response: Response<List<Genre>>) {
//                if (response.isSuccessful) {
//                    val genres = response.body()
//                    val genreNames = genres?.map { it.name } ?: emptyList()
//                    _genreNames.value = genreNames
//                    Log.d("GenrePreferenceViewModel", "API call successful. Genre Names: $genreNames")
//                } else {
//                    // Handle error response
//                    Log.e("GenrePreferenceViewModel", "API call failed. Error: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Genre>>, t: Throwable) {
//                // Handle network call failure
//                Log.e("GenrePreferenceViewModel", "API call failed. Error: ${t.message}")
//            }
//        })
//    }
//}
