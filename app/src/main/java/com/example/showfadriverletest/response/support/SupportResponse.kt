package com.example.showfadriverletest.response.support

import com.google.gson.annotations.SerializedName

data class SupportResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
