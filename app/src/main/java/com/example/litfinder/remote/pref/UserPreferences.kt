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
            "Saving session - Email: ${user.email}, Token: ${user.token}, IsLogin: ${user.isLogin}, ID: ${user.id}, Username: ${user.username}, Name: ${user.name}, Bio: ${user.bio}, ImageProfile: ${user.imageProfile}, Password: ${user.password}"
        )
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[ID_KEY] = user.id
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[USERNAME_KEY] = user.username
            preferences[NAME_KEY] = user.name
            preferences[BIO_KEY] = user.bio
            preferences[IMAGE_PROFILE_KEY] = user.imageProfile
            preferences[PASSWORD_KEY] = user.password
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            Log.d(
                "UserPreference",
                "Email: ${preferences[EMAIL_KEY]}, Token: ${preferences[TOKEN_KEY]}, IsLogin: ${preferences[IS_LOGIN_KEY]}, ID: ${preferences[ID_KEY]}, Username: ${preferences[USERNAME_KEY]}, Name: ${preferences[NAME_KEY]}, Bio: ${preferences[BIO_KEY]}, ImageProfile: ${preferences[IMAGE_PROFILE_KEY]}, Password: ${preferences[PASSWORD_KEY]}"
            )
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[ID_KEY] ?: 0,
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[USERNAME_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[BIO_KEY] ?: "",
                preferences[IMAGE_PROFILE_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: ""
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
                "Email: ${preferences[EMAIL_KEY]}, Token: ${preferences[TOKEN_KEY]}, IsLogin: ${preferences[IS_LOGIN_KEY]}, ID: ${preferences[ID_KEY]}, Username: ${preferences[USERNAME_KEY]}, Name: ${preferences[NAME_KEY]}, Bio: ${preferences[BIO_KEY]}, ImageProfile: ${preferences[IMAGE_PROFILE_KEY]}"
            )
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[ID_KEY] ?: 0,
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[USERNAME_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[BIO_KEY] ?: "",
                preferences[IMAGE_PROFILE_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: ""
            )
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    fun getUserId(): Flow<Int> {
        return dataStore.data.map {
            it[ID_KEY] ?: 0
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map {
            it[EMAIL_KEY] ?: ""
        }
    }

    fun getPassword(): Flow<String> {
        return dataStore.data.map {
            it[PASSWORD_KEY] ?: ""
        }
    }

    suspend fun savePassword(newPassword: String) {
        dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = newPassword
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun saveName(newName: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = newName
        }
    }

    suspend fun saveBio(newBio: String) {
        dataStore.edit { preferences ->
            preferences[BIO_KEY] = newBio
        }
    }

    suspend fun saveImageProfile(newImageUrl: String) {
        dataStore.edit { preferences ->
            preferences[IMAGE_PROFILE_KEY] = newImageUrl
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val ID_KEY = intPreferencesKey("id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val NAME_KEY = stringPreferencesKey("name")
        private val BIO_KEY = stringPreferencesKey("bio")
        private val IMAGE_PROFILE_KEY = stringPreferencesKey("imageProfile")
        private val PASSWORD_KEY = stringPreferencesKey("password")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
