package com.example.showfadriverletest.response.rating

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class RatingResponse(
	@field:SerializedName("your_rating")
	val yourRating: Any? = null,
):BaseResponse(){}
