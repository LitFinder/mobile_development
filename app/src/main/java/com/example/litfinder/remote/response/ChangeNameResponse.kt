package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class ChangeNameResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("newData")
    val newData: ChangeNameResult? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class ChangeNameResult(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("imageProfile")
    val imageProfile: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)
