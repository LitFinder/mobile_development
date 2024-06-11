package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class PostBookResponse(

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("data")
	val data: String? = null
)
