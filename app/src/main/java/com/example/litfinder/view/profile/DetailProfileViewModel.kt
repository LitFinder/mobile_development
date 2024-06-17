package com.example.litfinder.view.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.ChangeNameResponse
import com.example.litfinder.remote.response.ChangePhotoResponse
import com.example.litfinder.remote.response.PostPreferenceResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class DetailProfileViewModel(private val repository: Repository) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userBio = MutableLiveData<String>()
    val userBio: LiveData<String> get() = _userBio

    private val _changePasswordResult = MutableLiveData<Result<Unit>>()
    val changePasswordResult: LiveData<Result<Unit>> get() = _changePasswordResult

    private val _changeNameResponse = MutableLiveData<ChangeNameResponse>()
    val changeNameResponse: LiveData<ChangeNameResponse> get() = _changeNameResponse

    private val _changeBioResponse = MutableLiveData<ChangeNameResponse>()
    val changeBioResponse: LiveData<ChangeNameResponse> get() = _changeBioResponse


    private val _userPhotoUrl = MutableLiveData<String>()
    val userPhotoUrl: LiveData<String> get() = _userPhotoUrl

    init {
        refreshUserData()
    }

    private val _updatePictureResponse = MutableLiveData<ChangePhotoResponse>()
    val updatePictureResponse: LiveData<ChangePhotoResponse> get() = _updatePictureResponse

    private val _navigateToDetailProfile = MutableLiveData<Boolean>()
    val navigateToDetailProfile: LiveData<Boolean> get() = _navigateToDetailProfile

    fun updateProfilePicture(file: File) {
        viewModelScope.launch {
            try {
                val response = repository.updateUserProfilePicture(file)
                if (response.status == "success") {
                    Log.d("UpdateProfilePicture", "Profile picture updated successfully")
                    _navigateToDetailProfile.value = true
                } else {
                    Log.e("UpdateProfilePicture", "Failed to update profile picture. Status: ${response.status}")
                }
                _updatePictureResponse.value = response
            } catch (e: Exception) {
                Log.e("UpdateProfilePicture", "Failed to update profile picture", e)
                // Handle the error appropriately
            }
        }
    }


    fun refreshUserData() {
        viewModelScope.launch {
            val user = repository.getUser()
            _userName.value = user.name
            _userBio.value = if (user.bio.isEmpty()) "Bio masih kosong" else user.bio
            _userPhotoUrl.value = user.imageProfile ?: "" // If null, use empty string
        }
    }

    fun changeUserBio(newBio: String) {
        viewModelScope.launch {
            val response = repository.changeUserBio(newBio)
            _changeBioResponse.value = response
            if (response.status == "success") {
                repository.saveNewBio(newBio)
            }
        }
    }

    fun changeUserName(name: String) {
        viewModelScope.launch {
            try {
                val response = repository.changeUserName(name)
                _changeNameResponse.value = response
                if (response.status == "success") {
                    repository.saveNewName(name)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val savedPassword = repository.getCurrentPassword()
                if (currentPassword == savedPassword) {
                    if (currentPassword == newPassword) {
                        _changePasswordResult.value = Result.failure(Exception("Password Anda sudah sama seperti password sebelumnya"))
                    } else {
                        val result = repository.changePassword(newPassword)
                        repository.saveNewPassword(newPassword)
                        _changePasswordResult.value = Result.success(Unit)
                    }
                } else {
                    _changePasswordResult.value = Result.failure(Exception("Current password is incorrect"))
                }
            } catch (e: Exception) {
                _changePasswordResult.value = Result.failure(e)
            }
        }
    }


    init {
        viewModelScope.launch {
            val user = repository.getUser()
            _userName.value = user.name
            _userBio.value = user.bio
        }
    }
}