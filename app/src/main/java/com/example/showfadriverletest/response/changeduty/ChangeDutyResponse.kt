package com.example.showfadriverletest.response.changeduty

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class ChangeDutyResponse(
    @field:SerializedName("is_purchase_membership_error")
    val isPurchaseMembershipError: Boolean? = null,

    @field:SerializedName("duty")
    val duty: String? = null,

    @field:SerializedName("location")
    val location: Boolean? = null,
) : BaseResponse()
