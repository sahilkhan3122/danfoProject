package com.example.showfadriverletest.response.earning

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class EarningResponse(

    @field:SerializedName("current_date")
    val currentDate: String? = null,

    @field:SerializedName("earnings")
    val earnings: List<EarningsItem>? = null,

    @field:SerializedName("total_price")
    val totalPrice: String? = null,

    @field:SerializedName("total_tips")
    val totalTips: String? = null,

    @field:SerializedName("total_booking")
    val totalBooking: Int? = null,

    @field:SerializedName("total_time")
    val totalTime: Int? = null,

    @field:SerializedName("graph")
    val graph: List<Graph>? = null,
) : BaseResponse() {

    data class Graph(

        @field:SerializedName("Tue")
        val tue: Int? = null,

        @field:SerializedName("Wed")
        val wed: Int? = null,

        @field:SerializedName("Thu")
        val thu: Int? = null,

        @field:SerializedName("Sat")
        val sat: Int? = null,

        @field:SerializedName("Fri")
        val fri: Int? = null,

        @field:SerializedName("Mon")
        val mon: Int? = null,

        @field:SerializedName("Sun")
        val sun: Int? = null,
    )

    data class EarningsItem(

        @field:SerializedName("payment_type")
        val paymentType: String? = null,

        @field:SerializedName("booking_type")
        val bookingType: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("day")
        val day: String? = null,

        @field:SerializedName("driver_amount")
        val driverAmount: String? = null,

        @field:SerializedName("dropoff_time")
        val dropoffTime: String? = null,
        )
}