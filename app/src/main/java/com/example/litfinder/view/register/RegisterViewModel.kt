package com.example.litfinder.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _navigateToBookPreference = MutableLiveData<Boolean>()
    val navigateToBookPreference: LiveData<Boolean>
        get() = _navigateToBookPreference

//    fun registerUser(name: String, username: String, email: String, password: String) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                when (val registerResponse = repository.register(name, username, email, password)) {
//                    is ApiResponseStatus.Success -> {
//                        val message = registerResponse.data.message
//                        if (message == "Register failed") {
//                            _toastMessage.value =
//                                "Email is already in use. Please use a different email."
//                        } else {
//                            _toastMessage.value = "Registration successful"
//                            val token = registerResponse.data.token ?: ""
//                            if (token.isNotEmpty()) {
//                                val user = User(email, token)
//                                saveSession(user)
//                                _navigateToBookPreference.value = true
//                            }
//                        }
//                    }
//
//                    is ApiResponseStatus.Error -> {
//                        _toastMessage.value = registerResponse.errorMessage ?: "Registration failed"
//                    }
//
//                    ApiResponseStatus.Loading -> TODO()
//                }
//            } catch (e: HttpException) {
//                _toastMessage.value = "Registration failed: ${e.message()}"
//            } catch (e: Exception) {
//                _toastMessage.value = "Registration failed"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun saveSession(user: User) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}