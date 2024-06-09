package com.example.litfinder.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.api.LoginResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun login(email: String, password: String) {
        _isLoading.value = true
        _isSuccess.value = false

        // Logging the email and password before sending the login request
        Log.d(TAG, "Email: $email, Password: $password")

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                    _isSuccess.value = true

                    // Logging the successful response body
                    Log.d(TAG, "Login successful: ${response.body()}")
                } else {
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        _errorMessage.postValue("LOGIN ERROR : $errorMessages")

                        // Logging the error message
                        Log.e(TAG, "Login error: $errorMessages")
                    }
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, e: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message}")

                // Logging the failure message
                Log.e(TAG, "Login failed: ${e.message}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}