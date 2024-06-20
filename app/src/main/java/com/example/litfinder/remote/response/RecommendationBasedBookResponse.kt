package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationBasedBookResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RecommendationItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("publisher")
	val publisher: String? = null,

	@field:SerializedName("ratingsCount")
	val ratingsCount: Int? = null,

	@field:SerializedName("publishedDate")
	val publishedDate: String? = null,

	@field:SerializedName("categories")
	val categories: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("authors")
	val authors: String? = null,

	@field:SerializedName("infoLink")
	val infoLink: String? = null,

	@field:SerializedName("previewLink")
	val previewLink: String? = null
)

data class Data(

	@field:SerializedName("recommendation")
	val recommendation: List<RecommendationItem> = emptyList(),

	@field:SerializedName("fromCategory")
	val fromCategory: List<FromCategoryItem> = emptyList()
)

data class FromCategoryItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("previewLink")
	val previewLink: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("publisher")
	val publisher: String? = null,

	@field:SerializedName("ratingsCount")
	val ratingsCount: Int? = null,

	@field:SerializedName("publishedDate")
	val publishedDate: String? = null,

	@field:SerializedName("categories")
	val categories: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("authors")
	val authors: String? = null,

	@field:SerializedName("infoLink")
	val infoLink: String? = null
)
