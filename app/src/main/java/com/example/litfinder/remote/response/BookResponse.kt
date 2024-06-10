package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class BookResponse(

	@field:SerializedName("data")
	val data: List<BookItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BookItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("previewLink")
	val previewLink: String? = null,

	@field:SerializedName("rating")
	val rating: List<RatingItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("publisher")
	val publisher: String? = null,

	@field:SerializedName("ratingsCount")
	val ratingsCount: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("publishedDate")
	val publishedDate: String? = null,

	@field:SerializedName("categories")
	val categories: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("authors")
	val authors: String? = null,

	@field:SerializedName("infoLink")
	val infoLink: String? = null
)

data class RatingItem(

	@field:SerializedName("profileName")
	val profileName: String? = null,

	@field:SerializedName("reviewScore")
	val reviewScore: Int? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("price")
	val price: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("book_id")
	val bookId: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("reviewHelpfulness")
	val reviewHelpfulness: String? = null,

	@field:SerializedName("reviewSummary")
	val reviewSummary: String? = null,

	@field:SerializedName("reviewTime")
	val reviewTime: Int? = null,

	@field:SerializedName("reviewText")
	val reviewText: String? = null
)
