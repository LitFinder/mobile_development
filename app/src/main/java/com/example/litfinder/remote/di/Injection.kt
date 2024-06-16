package com.example.litfinder.remote.di

import android.content.Context
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.pref.SettingPreferences
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.pref.dataStore
import com.example.litfinder.remote.pref.dataStoreTheme
import com.example.litfinder.remote.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val settingPref = SettingPreferences.getInstance(context.dataStoreTheme)
        return Repository.getInstance(apiService, pref, settingPref)
    }
}