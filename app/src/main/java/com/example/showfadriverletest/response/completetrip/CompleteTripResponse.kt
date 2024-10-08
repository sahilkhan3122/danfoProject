package com.example.showfadriverletest.response.completetrip

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class CompleteTripResponse(

    @field:SerializedName("data")
    val data: Data? = null,
) : BaseResponse() {

    data class Data(

        @field:SerializedName("driver_photo")
        val driverPhoto: String? = null,

        @field:SerializedName("payment_url")
        val paymentUrl: String? = null,

        @field:SerializedName("driver_id")
        val driverId: String? = null,

        @field:SerializedName("trip_status")
        val tripStatus: String? = null,

        @field:SerializedName("distance")
        val distance: String? = null,

        @field:SerializedName("waiting_time")
        val waitingTime: String? = null,

        @field:SerializedName("customer_saving_wallet_amount")
        val customerSavingWalletAmount: String? = null,

        @field:SerializedName("passenger_full_name")
        val passengerFullName: String? = null,

        @field:SerializedName("passenger_rating")
        val passengerRating: String? = null,

        @field:SerializedName("payment_status")
        val paymentStatus: Boolean? = null,

        @field:SerializedName("driver_full_name")
        val driverFullName: String? = null,

        @field:SerializedName("driver_rating")
        val driverRating: String? = null,

        @field:SerializedName("driver_wallet_percentage")
        val driverWalletPercentage: String? = null,

        @field:SerializedName("customer_saving_wallet_percentage")
        val customerSavingWalletPercentage: String? = null,

        @field:SerializedName("driver_amount")
        val driverAmount: String? = null,

        @field:SerializedName("duration")
        val duration: String? = null,

        @field:SerializedName("payment_type")
        val paymentType: String? = null,

        @field:SerializedName("passenger_photo")
        val passengerPhoto: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("waiting_time_charge")
        val waitingTimeCharge: String? = null,

        @field:SerializedName("driver_saving_wallet_amount")
        val driverSavingWalletAmount: String? = null,

        @field:SerializedName("grand_total")
        val grandTotal: String? = null,

        @field:SerializedName("customer_id")
        val customerId: String? = null,
    )

}
