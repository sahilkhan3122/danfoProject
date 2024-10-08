package com.example.showfadriverletest.response

import com.google.gson.annotations.SerializedName

data class OnForwardBookingRequest(

    @field:SerializedName("booking_info")
    val bookingInfo: BookingInfo? = null
) : BaseResponse() {

    data class BookingInfo(

        @field:SerializedName("driver_id")
        val driverId: String? = null,

        @field:SerializedName("reference_id")
        val referenceId: String? = null,

        @field:SerializedName("booking_fee")
        val bookingFee: String? = null,

        @field:SerializedName("discount")
        val discount: String? = null,

        @field:SerializedName("payment_response")
        val paymentResponse: String? = null,

        @field:SerializedName("tips")
        val tips: String? = null,

        @field:SerializedName("estimated_distance")
        val estimatedDistance: String? = null,

        @field:SerializedName("pin")
        val pin: String? = null,

        @field:SerializedName("on_the_way")
        val onTheWay: String? = null,

        @field:SerializedName("forward_type")
        val forwardType: String? = null,

        @field:SerializedName("estimated_fare")
        val estimatedFare: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("rent_type")
        val rentType: String? = null,

        @field:SerializedName("duration_fare")
        val durationFare: String? = null,

        @field:SerializedName("pickup_location")
        val pickupLocation: String? = null,

        @field:SerializedName("waiting_time")
        val waitingTime: String? = null,

        @field:SerializedName("promocode")
        val promocode: String? = null,

        @field:SerializedName("tax")
        val tax: String? = null,

        @field:SerializedName("card_id")
        val cardId: String? = null,

        @field:SerializedName("driver_amount")
        val driverAmount: String? = null,

        @field:SerializedName("fix_rate_id")
        val fixRateId: String? = null,

        @field:SerializedName("payment_type")
        val paymentType: String? = null,

        @field:SerializedName("distance_fare")
        val distanceFare: String? = null,

        @field:SerializedName("pickup_date_time")
        val pickupDateTime: String? = null,

        @field:SerializedName("_id")
        val _id: String? = null,

        @field:SerializedName("pickup_time")
        val pickupTime: String? = null,

        @field:SerializedName("request_code")
        val requestCode: String? = null,

        @field:SerializedName("fare_increase_id")
        val fareIncreaseId: String? = null,

        @field:SerializedName("dropoff_location")
        val dropoffLocation: String? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("company_amount")
        val companyAmount: String? = null,

        @field:SerializedName("distance")
        val distance: String? = null,

        @field:SerializedName("tips_status")
        val tipsStatus: String? = null,

        @field:SerializedName("cancellation_charge")
        val cancellationCharge: String? = null,

        @field:SerializedName("cancele_reason")
        val canceleReason: String? = null,

        @field:SerializedName("is_changed_payment_type")
        val isChangedPaymentType: String? = null,

        @field:SerializedName("no_of_passenger")
        val noOfPassenger: String? = null,

        @field:SerializedName("fare_increase")
        val fareIncrease: String? = null,

        @field:SerializedName("customer_info")
        val customerInfo: CustomerInfo? = null,

        @field:SerializedName("waiting_time_charge")
        val waitingTimeCharge: String? = null,

        @field:SerializedName("grand_total")
        val grandTotal: String? = null,

        @field:SerializedName("cancel_by")
        val cancelBy: String? = null,

        @field:SerializedName("pickup_lat")
        val pickupLat: String? = null,

        @field:SerializedName("accept_time")
        val acceptTime: String? = null,

        @field:SerializedName("dropoff_lng")
        val dropoffLng: String? = null,

        @field:SerializedName("extra_charge")
        val extraCharge: String? = null,

        @field:SerializedName("pickup_lng")
        val pickupLng: String? = null,

        @field:SerializedName("dropoff_lat")
        val dropoffLat: String? = null,

        @field:SerializedName("payment_status")
        val paymentStatus: String? = null,

        @field:SerializedName("booking_time")
        val bookingTime: String? = null,

        @field:SerializedName("base_fare")
        val baseFare: String? = null,

        @field:SerializedName("estimated_trip_duration")
        val estimatedTripDuration: String? = null,

        @field:SerializedName("vehicle_type")
        val vehicleType: VehicleType? = null,

        @field:SerializedName("dropoff_time")
        val dropoffTime: String? = null,

        @field:SerializedName("vehicle_type_id")
        val vehicleTypeId: String? = null,

        @field:SerializedName("sub_total")
        val subTotal: String? = null,

        @field:SerializedName("booking_type")
        val bookingType: String? = null,

        @field:SerializedName("customer_id")
        val customerId: String? = null,

        @field:SerializedName("note")
        val note: String? = null,

        @field:SerializedName("trip_duration")
        val tripDuration: String? = null,

        @field:SerializedName("arrived_time")
        val arrivedTime: String? = null,

        @field:SerializedName("request_id")
        val requestId: String? = null
    )
}


data class VehicleType(

    @field:SerializedName("booking_fee")
    val bookingFee: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("per_km_charge")
    val perKmCharge: String? = null,

    @field:SerializedName("capacity")
    val capacity: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("driver_cancellation_fee")
    val driverCancellationFee: String? = null,

    @field:SerializedName("base_charge")
    val baseCharge: String? = null,

    @field:SerializedName("customer_cancellation_fee")
    val customerCancellationFee: String? = null,

    @field:SerializedName("commission")
    val commission: String? = null,

    @field:SerializedName("unselect_image")
    val unselectImage: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("per_minute_charge")
    val perMinuteCharge: String? = null,

    @field:SerializedName("extra_charge")
    val extraCharge: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("charge")
    val charge: String? = null,

    @field:SerializedName("bulk_mile_rate")
    val bulkMileRate: String? = null,

    @field:SerializedName("free_cancallation_time")
    val freeCancallationTime: String? = null,

    @field:SerializedName("sort")
    val sort: String? = null,

    @field:SerializedName("trash")
    val trash: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("base_km")
    val baseKm: String? = null,

    @field:SerializedName("waiting_time_per_min_charge")
    val waitingTimePerMinCharge: String? = null,

    @field:SerializedName("base")
    val base: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class CustomerInfo(

    @field:SerializedName("co_miles_balance")
    val coMilesBalance: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("co_miles_exp_date")
    val coMilesExpDate: String? = null,

    @field:SerializedName("mobile_no")
    val mobileNo: String? = null,

    @field:SerializedName("rating")
    val rating: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("device_type")
    val deviceType: String? = null,

    @field:SerializedName("old_device_token")
    val oldDeviceToken: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("profile_image")
    val profileImage: String? = null,

    @field:SerializedName("user_type")
    val userType: String? = null,

    @field:SerializedName("social_type")
    val socialType: String? = null,

    @field:SerializedName("transaction_password")
    val transactionPassword: String? = null,

    @field:SerializedName("qr_code")
    val qrCode: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("remember_token")
    val rememberToken: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("company_id")
    val companyId: String? = null,

    @field:SerializedName("lng")
    val lng: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("wallet_balance")
    val walletBalance: String? = null,

    @field:SerializedName("miles_balance")
    val milesBalance: String? = null,

    @field:SerializedName("trash")
    val trash: String? = null,

    @field:SerializedName("social_id")
    val socialId: String? = null,

    @field:SerializedName("old_device_type")
    val oldDeviceType: String? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("company_name")
    val companyName: String? = null,

    @field:SerializedName("miles_exp_date")
    val milesExpDate: String? = null,

    @field:SerializedName("device_token")
    val deviceToken: String? = null,

    @field:SerializedName("referral_code")
    val referralCode: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)