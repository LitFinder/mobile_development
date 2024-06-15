package com.example.litfinder.view.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(val repository: Repository) : ViewModel() {
    private var _verificationCodeSent = MutableLiveData<Boolean>()
    val verificationCodeSent: LiveData<Boolean> get() = _verificationCodeSent

    private var _codeVerified = MutableLiveData<Boolean>()
    val codeVerified: LiveData<Boolean> get() = _codeVerified

    private val _passwordChangeStatus = MutableLiveData<ApiResponseStatus<Boolean>>()
    val passwordChangeStatus: LiveData<ApiResponseStatus<Boolean>> get() = _passwordChangeStatus

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            _passwordChangeStatus.postValue(ApiResponseStatus.Loading)
            try {
                val response = repository.changePassword(newPassword)
                if (response.message != null) {
                    repository.saveNewPassword(newPassword)
                    _passwordChangeStatus.postValue(ApiResponseStatus.Success(true))
                } else {
                    _passwordChangeStatus.postValue(ApiResponseStatus.Error("Password change failed"))
                }
            } catch (e: Exception) {
                _passwordChangeStatus.postValue(ApiResponseStatus.Error(e.message ?: "An error occurred"))
            }
        }
    }

    fun sendVerificationCode(email: String) {
        viewModelScope.launch {
            repository.saveEmail(email)
            _verificationCodeSent.value = repository.sendVerificationCode(email)
        }
    }

    fun verifyCode(code: String) {
        _codeVerified.value = repository.verifyCode(code)
    }

    suspend fun getToken(): String? {
        return repository.getToken()
    }

}