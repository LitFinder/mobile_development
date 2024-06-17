package com.example.litfinder.remote.api

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BookselfRequest(
	@SerializedName("user_id") val userId: Int,
	@SerializedName("filter") val filter: String? = "all"
)

@Parcelize
data class BookshelfResponse(

	@field:SerializedName("data")
	val data: List<DataItemBookShelf>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable


@Parcelize
data class Rating(

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
) : Parcelable

@Parcelize
data class DataItemBookShelf(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("rating_id")
	val ratingId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("book")
	val book: List<BookItem?>? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("book_id")
	val bookId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable


@Parcelize
data class BookItem(

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
) : Parcelable
