package com.example.litfinder.remote.api


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//    @Parcelize
//    data class User(
//        var id: Int = -1,
//        var token: String? = null,
//        var name: String? = null,
//        var username: String? = null
//    ): Parcelable
    
data class User(
    val email: String,
    val id: String,
    val token: String,
    val isLogin: Boolean = false,
    val username: String,
    val name: String,
    val bio: String,
    val imageProfile: String,
    val password: String
)
