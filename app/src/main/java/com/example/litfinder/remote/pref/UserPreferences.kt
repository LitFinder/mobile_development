package com.example.litfinder.remote.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
            "Saving session - Email: ${user.email}, Token: ${user.token}, IsLogin: ${user.isLogin}, ID: ${user.id}"
        )
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[ID_KEY] = user.id
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            Log.d(
                "UserPreference",
                "Email: ${preferences[EMAIL_KEY]}, Token: ${preferences[TOKEN_KEY]}, IsLogin: ${preferences[IS_LOGIN_KEY]}, ID: ${preferences[ID_KEY]}"
            )
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[ID_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,

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
                "Email: ${preferences[EMAIL_KEY]}, Token: ${preferences[TOKEN_KEY]}, IsLogin: ${preferences[IS_LOGIN_KEY]}, ID: ${preferences[ID_KEY]}"
            )
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[ID_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,

            )
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map {
            it[ID_KEY] ?: ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val ID_KEY = stringPreferencesKey("id")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
