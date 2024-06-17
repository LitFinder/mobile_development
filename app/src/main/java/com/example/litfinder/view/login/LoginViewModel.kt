package com.example.litfinder.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.response.LoginResponse
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun login(email: String, password: String) {
        _isLoading.value = true
        _isSuccess.value = false

        val client = ApiConfig.getApiService().login(email,password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                    _isSuccess.value = true
                } else {
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        _errorMessage.postValue("LOGIN ERROR : $errorMessages")
                    }
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, e: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
