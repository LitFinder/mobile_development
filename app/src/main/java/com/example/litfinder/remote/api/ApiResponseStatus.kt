package com.example.litfinder.remote.api

sealed class ApiResponseStatus<out R> private constructor() {
    data class Success<out T>(val data: T) : ApiResponseStatus<T>()
    data class Error(val errorMessage: String) : ApiResponseStatus<Nothing>()
    object Loading : ApiResponseStatus<Nothing>()
}