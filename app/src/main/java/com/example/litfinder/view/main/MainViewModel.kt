package com.example.litfinder.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.BookItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> get() = _logoutResult

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userBio = MutableLiveData<String>()
    val userBio: LiveData<String> get() = _userBio

    private val _userPhotoUrl = MutableLiveData<String>()
    val userPhotoUrl: LiveData<String> get() = _userPhotoUrl

    init {
        refreshUserData()
    }

    fun refreshUserData() {
        viewModelScope.launch {
            val user = repository.getUser()
            _userName.value = user.username
            _userBio.value = if (user.bio.isEmpty()) "Bio masih kosong" else user.bio
            _userPhotoUrl.value = user.imageProfile ?: "" // If null, use empty string
        }
    }

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logoutResult.value = true
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }

    val bookResponse: LiveData<PagingData<BookItem>> = repository.getBooks().cachedIn(viewModelScope)

    val recommendationResponse: LiveData<PagingData<BookItem>> = repository.getRecommendations().cachedIn(viewModelScope)

    val recommendationBasedReviewResponse: LiveData<PagingData<BookItem>> = repository.getRecommendationBasedReview().cachedIn(viewModelScope)

    fun postLog(bookId: Int) {
        viewModelScope.launch {
            try {
                repository.postLog(bookId)
                Log.d("POSTLOGBERHASIL", bookId.toString())
            } catch (e: Exception) {
                Log.d("POSTLOGGAGAL", e.toString())
            }
        }
    }
}
