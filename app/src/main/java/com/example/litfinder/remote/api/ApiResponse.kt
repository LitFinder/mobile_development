package com.example.litfinder.remote.api

class ApiResponse {

    data class RegisterRequest(
        val name: String,
        val username: String,
        val email: String,
        val password: String
    )

    data class RegisterResponse(
        val success: Boolean,
        val message: String
    )

}