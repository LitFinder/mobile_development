package com.example.litfinder.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamentalsubmission2.data.remote.retrofit.ApiConfig
import com.example.litfinder.remote.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<ApiResponse.RegisterResponse>()
    val registerResponse: LiveData<ApiResponse.RegisterResponse> = _registerResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun registerUser(registerRequest: ApiResponse.RegisterRequest) {
        val apiService = ApiConfig.getApiService()
        apiService.registerUser(registerRequest).enqueue(object : Callback<ApiResponse.RegisterResponse> {
            override fun onResponse(call: Call<ApiResponse.RegisterResponse>, response: Response<ApiResponse.RegisterResponse>) {
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    _errorMessage.value = "Failed to register"
                }
            }

            override fun onFailure(call: Call<ApiResponse.RegisterResponse>, t: Throwable) {
                _errorMessage.value = t.message
            }
        })
    }
}