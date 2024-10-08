package com.example.showfadriverletest.response.pasttrip

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class PastTripResponse(

    @field:SerializedName("data")
	val data: PastTripResponse? = null,

    ):BaseResponse()
{

data class DataItem(

	@field:SerializedName("corporate_driver_type")
	val corporateDriverType: String? = null,

	@field:SerializedName("driver_id")
	val driverId: String? = null,

	@field:SerializedName("payment_url")
	val paymentUrl: String? = null,

	@field:SerializedName("reference_id")
	val referenceId: String? = null,

	@field:SerializedName("customer_saving_wallet_amount")
	val customerSavingWalletAmount: Any? = null,

	@field:SerializedName("ambulance_package_id")
	val ambulancePackageId: String? = null,

	@field:SerializedName("corporate_company_amount")
	val corporateCompanyAmount: String? = null,

	@field:SerializedName("total_equipment_price")
	val totalEquipmentPrice: Any? = null,

	@field:SerializedName("booking_fee")
	val bookingFee: String? = null,

	@field:SerializedName("discount")
	val discount: String? = null,

	@field:SerializedName("saving_wallet_amount")
	val savingWalletAmount: String? = null,

	@field:SerializedName("payment_response")
	val paymentResponse: String? = null,

	@field:SerializedName("package_type")
	val packageType: String? = null,

	@field:SerializedName("jambopay_order_id")
	val jambopayOrderId: Any? = null,

	@field:SerializedName("tips")
	val tips: String? = null,

	@field:SerializedName("booking_note")
	val bookingNote: Any? = null,

	@field:SerializedName("cron_status")
	val cronStatus: String? = null,

	@field:SerializedName("pin")
	val pin: String? = null,

	@field:SerializedName("on_the_way")
	val onTheWay: String? = null,

	@field:SerializedName("estimated_fare")
	val estimatedFare: String? = null,

	@field:SerializedName("is_corporate_driver")
	val isCorporateDriver: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("cancel_time")
	val cancelTime: String? = null,

	@field:SerializedName("rent_type")
	val rentType: String? = null,

	@field:SerializedName("duration_fare")
	val durationFare: String? = null,

	@field:SerializedName("pickup_location")
	val pickupLocation: String? = null,

	@field:SerializedName("waiting_time")
	val waitingTime: String? = null,

	@field:SerializedName("customer_first_name")
	val customerFirstName: String? = null,

	@field:SerializedName("customer_profile_image")
	val customerProfileImage: String? = null,

	@field:SerializedName("promocode")
	val promocode: String? = null,

	@field:SerializedName("tax")
	val tax: String? = null,

	@field:SerializedName("driver_wallet_percentage")
	val driverWalletPercentage: String? = null,

	@field:SerializedName("card_id")
	val cardId: String? = null,

	@field:SerializedName("driver_amount")
	val driverAmount: Any? = null,

	@field:SerializedName("booking_equipments")
	val bookingEquipments: List<Any?>? = null,

	@field:SerializedName("fix_rate_id")
	val fixRateId: String? = null,

	@field:SerializedName("payment_type")
	val paymentType: String? = null,

	@field:SerializedName("distance_fare")
	val distanceFare: String? = null,

	@field:SerializedName("pickup_date_time")
	val pickupDateTime: String? = null,

	@field:SerializedName("pickup_time")
	val pickupTime: String? = null,

	@field:SerializedName("request_code")
	val requestCode: String? = null,

	@field:SerializedName("fare_increase_id")
	val fareIncreaseId: String? = null,

	@field:SerializedName("corporate_company_id")
	val corporateCompanyId: String? = null,

	@field:SerializedName("dropoff_location")
	val dropoffLocation: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("equipment_id")
	val equipmentId: Any? = null,

	@field:SerializedName("vehicle_name")
	val vehicleName: String? = null,

	@field:SerializedName("company_amount")
	val companyAmount: String? = null,

	@field:SerializedName("vehicle_plate_number")
	val vehiclePlateNumber: String? = null,

	@field:SerializedName("booking_from")
	val bookingFrom: String? = null,

	@field:SerializedName("distance")
	val distance: String? = null,

	@field:SerializedName("vehicle_model")
	val vehicleModel: String? = null,

	@field:SerializedName("company_vehicle_type_id")
	val companyVehicleTypeId: String? = null,

	@field:SerializedName("request_by_cron")
	val requestByCron: String? = null,

	@field:SerializedName("tips_status")
	val tipsStatus: String? = null,

	@field:SerializedName("other_company_amount")
	val otherCompanyAmount: Any? = null,

	@field:SerializedName("cancellation_charge")
	val cancellationCharge: String? = null,

	@field:SerializedName("cancele_reason")
	val canceleReason: String? = null,

	@field:SerializedName("customer_last_name")
	val customerLastName: String? = null,

	@field:SerializedName("is_changed_payment_type")
	val isChangedPaymentType: String? = null,

	@field:SerializedName("no_of_passenger")
	val noOfPassenger: String? = null,

	@field:SerializedName("fare_increase")
	val fareIncrease: String? = null,

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

	@field:SerializedName("vehicle_type_name")
	val vehicleTypeName: String? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("dropoff_lat")
	val dropoffLat: String? = null,

	@field:SerializedName("payment_status")
	val paymentStatus: String? = null,

	@field:SerializedName("booking_time")
	val bookingTime: String? = null,

	@field:SerializedName("base_fare")
	val baseFare: String? = null,

	@field:SerializedName("customer_saving_wallet_percentage")
	val customerSavingWalletPercentage: String? = null,

	@field:SerializedName("dropoff_time")
	val dropoffTime: String? = null,

	@field:SerializedName("request_response")
	val requestResponse: Any? = null,

	@field:SerializedName("vehicle_type_id")
	val vehicleTypeId: String? = null,

	@field:SerializedName("sub_total")
	val subTotal: String? = null,

	@field:SerializedName("booking_type")
	val bookingType: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("request_data")
	val requestData: Any? = null,

	@field:SerializedName("trip_duration")
	val tripDuration: String? = null,

	@field:SerializedName("arrived_time")
	val arrivedTime: String? = null,

	@field:SerializedName("request_id")
	val requestId: String? = null
)
}