package com.example.litfinder.view.genrePreference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.repository.GenreRepository
import com.example.litfinder.remote.response.DataItem
import com.example.litfinder.remote.response.Genre
import com.example.litfinder.remote.response.GenreResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenrePreferenceViewModel : ViewModel() {
    private val _genreNames = MutableLiveData<List<String>?>(null)
    val genreNames: LiveData<List<String>?> get() = _genreNames

    private val apiService: ApiService = ApiConfig.getApiService()

    fun fetchGenres() {
        viewModelScope.launch {
            try {
                val response = apiService.getGenres()
                if (response.isSuccessful) {
                    val genreResponse = response.body()
                    val names = genreResponse?.data?.mapNotNull { it.name }
                    _genreNames.value = names
                    Log.d("GenreViewModel", "Data received: $names")
                } else {
                    Log.e("GenreViewModel", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("GenreViewModel", "Exception: ${e.message}")
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
