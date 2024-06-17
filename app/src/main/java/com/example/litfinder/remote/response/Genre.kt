package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class Genre(
	val id: Int,
	val name: String,
	val createdAt: String,
	val updatedAt: String
)