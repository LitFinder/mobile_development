package com.example.litfinder.remote.pref

import android.content.Context
import com.example.litfinder.remote.api.User

class UserPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): User {
        val user = User()
        user.token = preferences.getString(TOKEN, "")

        return user
    }

    fun logout() {
        val editor = preferences.edit()
        editor.remove(TOKEN)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val IS_LOGIN = "is_login"
    }
}