package com.example.litfinder.view.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(val repository: Repository) : ViewModel() {
    private var _verificationCodeSent = MutableLiveData<Boolean>()
    val verificationCodeSent: LiveData<Boolean> get() = _verificationCodeSent

    private var _codeVerified = MutableLiveData<Boolean>()
    val codeVerified: LiveData<Boolean> get() = _codeVerified

    fun sendVerificationCode(email: String) {
        viewModelScope.launch {
            _verificationCodeSent.value = repository.sendVerificationCode(email)
        }
    }

    fun verifyCode(code: String) {
        _codeVerified.value = repository.verifyCode(code)
    }

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            repository.changePassword(newPassword)
        }
    }
}