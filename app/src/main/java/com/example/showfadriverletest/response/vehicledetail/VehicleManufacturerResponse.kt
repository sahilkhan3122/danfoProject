package com.example.showfadriverletest.response.vehicledetail

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class VehicleManufacturerResponse(

    @field:SerializedName("year_list")
    val yearList: List<String>,

    @field:SerializedName("data")
    val data: List<DataItem>,

    ) : BaseResponse() {
    data class DataItem(

        @field:SerializedName("vehicle_model")
		val vehicleModel: List<VehicleModelItem>? = null,

        @field:SerializedName("id")
		val id: String? = null,

        @field:SerializedName("manufacturer_name")
		val manufacturerName: String? = null,

        @field:SerializedName("type")
		val type: String? = null,

        @field:SerializedName("status")
		val status: String? = null,
	){

    data class VehicleModelItem(

		@field:SerializedName("vehicle_type_id")
		val vehicleTypeId: String? = null,

		@field:SerializedName("vehicle_type_name")
		var vehicleTypeName: String? = null,

		@field:SerializedName("vehicle_type_model_name")
		var vehicleTypeModelName: String? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("vehicle_type_manufacturer_id")
		val vehicleTypeManufacturerId: String? = null,

		@field:SerializedName("status")
		val status: String? = null,
	)
	}
}



