package com.example.litfinder.remote.repository

import android.util.Log
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.api.DataItemtype
import com.example.litfinder.remote.api.TypeGenreResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TypeGenreRepository(private val tokenProvider: () -> String) {
    private val genreService: ApiService = ApiConfig.getApiBook(tokenProvider)

    fun getGenres(callback: (List<DataItemtype>?) -> Unit) {
        genreService.getGenres().enqueue(object : Callback<TypeGenreResponse> {
            override fun onResponse(call: Call<TypeGenreResponse>, response: Response<TypeGenreResponse>) {
                Log.d("GenreRepository", "onResponse: $response")
                if (response.isSuccessful) {
                    callback(response.body()?.data)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<TypeGenreResponse>, t: Throwable) {
                Log.e("GenreRepository", "onFailure: ", t)
                callback(null)
            }
        })
    }
}
