package com.example.showfadriverletest.response.subscription

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class SubscriptionResponse(

	@field:SerializedName("driver_subscription")
	val driverSubscription: DriverSubscription? = null,

	@field:SerializedName("subscription")
	val subscription: List<SubscriptionItem>? = null,

	):BaseResponse()
{

data class SubscriptionItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class DriverSubscription(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
)
}