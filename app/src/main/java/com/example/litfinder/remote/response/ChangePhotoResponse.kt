package com.example.litfinder.remote.response

import com.google.gson.annotations.SerializedName

data class ChangePhotoResponse(

	@field:SerializedName("newImage")
	val newImage: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
