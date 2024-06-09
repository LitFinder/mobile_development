package com.example.litfinder.remote.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.litfinder.remote.api.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: User) {
        Log.d(
            "UserPreference",
            "Saving session - Email: ${user.email}, Token: ${user.token}, IsLogin: ${user.isLogin}"
        )
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            Log.d(
                "UserPreference",
                "Email: ${preferences[EMAIL_KEY]}, Token: ${preferences[TOKEN_KEY]}, IsLogin: ${preferences[IS_LOGIN_KEY]}"
            )
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            Log.d(
                "UserPreference",
                "Email: ${preferences[EMAIL_KEY]}, Token: ${preferences[TOKEN_KEY]}, IsLogin: ${preferences[IS_LOGIN_KEY]}")
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

//class UserPreferences(context: Context) {
//    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//
//    fun setUser(value: User) {
//        val editor = preferences.edit()
//        editor.putString(TOKEN, value.token)
//        editor.apply()
//    }
//
//    fun getUser(): User {
//        val user = User()
//        user.token = preferences.getString(TOKEN, "")
//
//        return user
//    }
//
//    fun logout() {
//        val editor = preferences.edit()
//        editor.remove(TOKEN)
//        editor.apply()
//    }
//
//    companion object {
//        private const val PREFS_NAME = "user_pref"
//        private const val TOKEN = "token"
//        private const val IS_LOGIN = "is_login"
//    }
//}