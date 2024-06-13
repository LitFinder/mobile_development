package com.example.litfinder.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.R
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _navigateToBookPreference = MutableLiveData<Boolean>()
    val navigateToBookPreference: LiveData<Boolean>
        get() = _navigateToBookPreference

    fun registerUser(name: String, username: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                when (val registerResponse = repository.register(name, username, email, password)) {
                    is ApiResponseStatus.Success -> {

                        _toastMessage.value = R.string.register_success
                        val token = registerResponse.data.token ?: ""
                        val id = registerResponse.data.data?.id ?: ""
                        val bio = registerResponse.data.data?.bio ?: ""
                        val imageProfile = registerResponse.data.data?.imageProfile ?: ""
                        if (token.isNotEmpty() && id.isNotEmpty()) {
                            val user = User(email, id, token, true, username, name, bio, imageProfile)
                            repository.saveSession(user)
                            _navigateToBookPreference.value = true
                        }

                    }

                    is ApiResponseStatus.Error -> {
                        _toastMessage.value = R.string.email_in_use
                    }

                    ApiResponseStatus.Loading -> TODO()
                }
            } catch (e: HttpException) {
                _toastMessage.value = R.string.registration_failed
            } catch (e: Exception) {
                _toastMessage.value = R.string.registration_failed
            } finally {
                _isLoading.value = false
            }
        }
    }
}
