package com.example.showfadriverletest.response.notificationlist

import com.google.gson.annotations.SerializedName

data class NotificationClearResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
