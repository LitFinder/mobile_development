package com.example.litfinder.remote.api

import com.google.gson.annotations.SerializedName

data class AddBookRequest(
    val user_id: Int,
    val book_id: Int
)

data class RecommendationRequest(
    val user_id: Int
)

data class UpdateBookRequest(
    val bookself_id: Int,
    val status: String,
    val book_id: Int,
    val user_id: Int,
    val profileName: String?,
    val reviewHelpfulness: String,
    val reviewScore: Int,
    val reviewSummary: String,
    val reviewText: String
)

data class RatingBookRequest(
    val book_id: Int,
)

data class AddBookResponse(
    val status: String,
    val message: String
)
