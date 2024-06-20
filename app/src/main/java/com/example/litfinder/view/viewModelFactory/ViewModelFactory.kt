package com.example.litfinder.view.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.remote.di.Injection
import com.example.litfinder.view.bookPreference.BookPreferenceViewModel
import com.example.litfinder.view.editPreference.EditPreferenceViewModel
import com.example.litfinder.view.forgotPassword.ForgotPasswordViewModel
import com.example.litfinder.view.genrePreference.GenrePreferenceViewModel
import com.example.litfinder.view.login.LoginViewModel
import com.example.litfinder.view.main.MainViewModel
import com.example.litfinder.view.profile.DetailProfileViewModel
import com.example.litfinder.view.register.RegisterViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(BookPreferenceViewModel::class.java) -> {
                BookPreferenceViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(GenrePreferenceViewModel::class.java) -> {
                GenrePreferenceViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(EditPreferenceViewModel::class.java) -> {
                EditPreferenceViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(DetailProfileViewModel::class.java) -> {
                DetailProfileViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> {
                ForgotPasswordViewModel(Injection.provideRepository(context)) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}