package com.example.litfinder.remote.api

data class User(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
