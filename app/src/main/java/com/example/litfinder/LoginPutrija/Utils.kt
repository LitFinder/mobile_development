package com.example.litfinder.LoginPutrija

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

fun isValidEmail(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}




