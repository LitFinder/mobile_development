package com.example.litfinder.utils

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

fun isValidEmail(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}




