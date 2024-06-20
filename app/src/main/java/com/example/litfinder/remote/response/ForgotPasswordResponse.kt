package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

    @field:SerializedName("kode")
    val kode: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)
