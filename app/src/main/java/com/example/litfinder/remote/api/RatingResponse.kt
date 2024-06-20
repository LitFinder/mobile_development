package com.example.litfinder.remote.api

import com.google.gson.annotations.SerializedName

data class RatingResponse(

    @field:SerializedName("data")
    val data: List<DataItemRating>? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemRating(

    @field:SerializedName("profileName")
    val profileName: String? = null,

    @field:SerializedName("reviewScore")
    val reviewScore: Int? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("book_id")
    val bookId: Int? = null,

    @field:SerializedName("reviewHelpfulness")
    val reviewHelpfulness: String? = null,

    @field:SerializedName("reviewSummary")
    val reviewSummary: String? = null,

    @field:SerializedName("reviewText")
    val reviewText: String? = null
)
