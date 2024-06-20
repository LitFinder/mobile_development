package com.example.litfinder.remote.api

data class User(
    val email: String,
    val id: Int,
    val token: String,
    val isLogin: Boolean = false,
    val username: String,
    val name: String,
    val bio: String,
    val imageProfile: String,
    val password: String
)
