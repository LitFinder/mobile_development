package com.example.litfinder.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.response.LoginResponse
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun login(email: String, password: String): LiveData<ApiResponseStatus<LoginResponse>> {
        val result = MutableLiveData<ApiResponseStatus<LoginResponse>>()

        viewModelScope.launch {
            repository.login(email, password).collect { response ->
                result.value = response
            }
        }

        return result
    }
}