package com.example.litfinder.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litfinder.remote.repository.Repository
import kotlinx.coroutines.launch

class DetailProfileViewModel(private val repository: Repository) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userBio = MutableLiveData<String>()
    val userBio: LiveData<String> get() = _userBio

    init {
        viewModelScope.launch {
            val user = repository.getUser()
            _userName.value = user.name
            _userBio.value = user.bio
        }
    }
}