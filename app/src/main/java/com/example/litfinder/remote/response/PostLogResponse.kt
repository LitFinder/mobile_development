package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class PostLogResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
