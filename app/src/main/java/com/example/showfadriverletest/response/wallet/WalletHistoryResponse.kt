package com.example.showfadriverletest.response.wallet

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class WalletHistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("last_wallet_update_time")
	val lastWalletUpdateTime: String? = null,

	@field:SerializedName("wallet_balance")
	val walletBalance: Int? = null,

	@field:SerializedName("is_jambopay_wallet_setup")
	val isJambopayWalletSetup: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("view_link")
	val viewLink: String? = null,

	):BaseResponse(){

data class DataItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("reference_id")
	val referenceId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("transaction_type")
	val transactionType: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("debit_account")
	val debitAccount: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("payment_type")
	val paymentType: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("admin_paid")
	val adminPaid: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
}
