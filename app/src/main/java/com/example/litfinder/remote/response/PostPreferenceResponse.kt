package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class PostPreferenceResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: String? = null
)
