package com.example.litfinder.remote.pref

import android.content.Context
import com.example.litfinder.remote.api.User

class UserPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.putInt(ID, value.id)
        editor.putString(NAME, value.name)
        editor.putString(USERNAME, value.username)
        editor.apply()
    }

    fun getUser(): User {
        val user = User()
        user.token = preferences.getString(TOKEN, "")
        user.id = preferences.getInt(ID, -1)
        user.name = preferences.getString(NAME, "")
        user.username = preferences.getString(USERNAME, "")
        return user
    }

    fun logout() {
        val editor = preferences.edit()
        editor.remove(TOKEN)
        editor.remove(ID)
        editor.remove(NAME)
        editor.remove(USERNAME)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val ID = "id"
        private const val NAME = "name"
        private const val USERNAME = "username"
    }
}

