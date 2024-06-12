package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class GenreResponse(

	@field:SerializedName("data")
	val data: List<GenreItem> = emptyList(),

	@field:SerializedName("status")
	val status: String? = null
)

data class GenreItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
