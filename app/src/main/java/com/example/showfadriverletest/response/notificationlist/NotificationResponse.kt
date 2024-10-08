package com.example.showfadriverletest.response.notificationlist

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,


	):BaseResponse(){

data class DataItem(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("read_status_user")
	val readStatusUser: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
}