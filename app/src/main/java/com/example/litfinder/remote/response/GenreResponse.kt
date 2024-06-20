package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class GenreResponse(

    @field:SerializedName("data")
    val data: List<GenreItem> = emptyList(),

    @field:SerializedName("status")
    val status: String? = null
)

data class GenreItem(val id: Int, val name: String)
