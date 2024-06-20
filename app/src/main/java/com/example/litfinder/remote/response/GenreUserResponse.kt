package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class GenreUserResponse(

    @field:SerializedName("data")
    val data: List<GenreUser> = emptyList(),

    @field:SerializedName("status")
    val status: String? = null
)

data class GenreUser(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
