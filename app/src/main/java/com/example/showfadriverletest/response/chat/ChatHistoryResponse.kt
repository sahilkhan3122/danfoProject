package com.example.showfadriverletest.response.chat

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class ChatHistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	):BaseResponse(){
	data class DataItem(
		@SerializedName("viewType")
		val viewType: Int = 0,

		@field:SerializedName("booking_id")
		val bookingId: String? = null,

		@field:SerializedName("sender_type")
		val senderType: String? = null,

		@field:SerializedName("receiver_id")
		val receiverId: String? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("message")
		val message: String? = null,

		@field:SerializedName("sender_id")
		val senderId: String? = null,

		@field:SerializedName("receiver_type")
		val receiverType: String? = null
	)
}


