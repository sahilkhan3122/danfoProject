package com.example.showfadriverletest.network

import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.response.BaseResponse
import com.example.showfadriverletest.response.changeduty.ChangeDutyResponse
import com.example.showfadriverletest.response.chat.ChatResponse
import com.example.showfadriverletest.response.completetrip.CompleteTripResponse
import com.example.showfadriverletest.response.earning.EarningResponse
import com.example.showfadriverletest.response.editvehicleinfo.EditVehicleInfoResponse
import com.example.showfadriverletest.response.featuretrip.FeatureTripResponse
import com.example.showfadriverletest.response.init.InitResponse
import com.example.showfadriverletest.response.login.LoginResponse
import com.example.showfadriverletest.response.notificationlist.NotificationResponse
import com.example.showfadriverletest.response.pasttrip.TempResponse
import com.example.showfadriverletest.response.rating.RatingResponse
import com.example.showfadriverletest.response.register.RegisterResponse
import com.example.showfadriverletest.response.registerOtp.RegisterOtpResponse
import com.example.showfadriverletest.response.resendotp.ResendOtpResponse
import com.example.showfadriverletest.response.savingwallet.SavingWalletHistoryResponse
import com.example.showfadriverletest.response.subscription.PurchaseSubscriptionResponse
import com.example.showfadriverletest.response.subscription.SubscriptionResponse
import com.example.showfadriverletest.response.updatedata.EditProfileResponse
import com.example.showfadriverletest.response.updatedata.EditVehicleDocResponse
import com.example.showfadriverletest.response.uploaddoc.UploadDocsResponse
import com.example.showfadriverletest.response.vehicledetail.VehicleManufacturerResponse
import com.example.showfadriverletest.response.wallet.WalletHistoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Url

interface ApiService {

    /** init Api */
    @GET
    suspend fun init(@Url url: String): Response<InitResponse>

    /** Login Api */
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_LOGIN)
    suspend fun login(@FieldMap map: Map<String, String>): Response<LoginResponse>

    /** Register Api* */
    @Multipart
    @POST(ApiConstant.END_POINT_REGISTER)
    suspend fun register(
        @PartMap paramEditProfile: @JvmSuppressWildcards Map<String, RequestBody>,
        @Part image: MultipartBody.Part? = null,
    ): Response<RegisterResponse>

    /** ResendOtp Api */
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_RESEND_OTP)
    suspend fun resendOtp(@FieldMap map: Map<String, String>): Response<ResendOtpResponse>

    /** Register Otp Api*/
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_REGISTER_OTP)
    suspend fun registerOtp(@FieldMap map: Map<String, String>): Response<RegisterOtpResponse>

    /** vehicle_type_manufacturer_list Api*/
    @GET
    suspend fun vehicleManufacturer(@Url url: String): Response<VehicleManufacturerResponse>

    /** Upload Docs Api*/
    @Multipart
    @POST(ApiConstant.ENDPOINT_UPLOAD_DOC)
    suspend fun uploadDoc(
        @Part image: MultipartBody.Part? = null,
    ): Response<UploadDocsResponse>

    /** Past Trip Api */
    @GET
    suspend fun pastTrip(@Url url: String): Response<TempResponse>

    /** Feature Trip Api */
    @GET
    suspend fun futureTrip(@Url url: String): Response<FeatureTripResponse>

    /** Earning Api*/
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_EARNING)
    suspend fun earningReport(@FieldMap map: Map<String, String>): Response<EarningResponse>

    /**Support*/
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_SUPPORT)
    suspend fun support(@FieldMap map: Map<String, String>): Response<BaseResponse>

    /** Subscription Api */
    @GET
    suspend fun subscriptionList(@Url url: String): Response<SubscriptionResponse>

    /** subscription purchase Api */
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_SUBSCRIPTION_PURCHASE)
    suspend fun subscriptionPurchase(@FieldMap map: Map<String, String>): Response<PurchaseSubscriptionResponse>

    /** Notification Api */
    @GET
    suspend fun notificationList(@Url url: String): Response<NotificationResponse>

    /** Notification Clear Api */
    @GET
    suspend fun notionClearList(@Url url: String): Response<BaseResponse>

    /** Wallet History Api */
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_Wallet_history)
    suspend fun walletHistory(@FieldMap map: Map<String, String>): Response<WalletHistoryResponse>

    /** add Money*/

    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_ADD_MONEY)
    suspend fun addMoney(@FieldMap map: Map<String, String>): Response<BaseResponse>

    /**saving  Wallet History Api */
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_SAVING_WALLET)
    suspend fun savingWalletHistory(@FieldMap map: Map<String, String>): Response<SavingWalletHistoryResponse>

    /**update Profile Api*/
    @Multipart
    @POST(ApiConstant.END_POINT_EDIT_PROFILE)
    suspend fun editProfile(
        @PartMap paramEditProfile: @JvmSuppressWildcards Map<String, RequestBody>,
        @Part image: MultipartBody.Part? = null,
    ): Response<EditProfileResponse>

    /**update VehicleDetail APi*/
    @Multipart
    @POST(ApiConstant.END_POINT_EDIT_VEHICLE_INFO)
    suspend fun editVehicleDetail(@PartMap paramEditProfile: @JvmSuppressWildcards Map<String, RequestBody>): Response<EditVehicleInfoResponse>

    /**update VehicleDoc Api*/
    @Multipart
    @POST(ApiConstant.END_POINT_EDIT_VEHICLE_DOCUMENT)
    suspend fun editVehicleDocument(@PartMap paramEditProfile: @JvmSuppressWildcards Map<String, RequestBody>): Response<EditVehicleDocResponse>

    /** Logout APi*/
    @GET
    suspend fun logout(@Url url: String): Response<BaseResponse>

    /**  Delete Account Api*/
    @FormUrlEncoded
    @POST(ApiConstant.END_POINT_DELETE_ACCOUNT)
    suspend fun deleteAccount(@FieldMap map: Map<String, String>): Response<BaseResponse>

    @FormUrlEncoded
    @POST(ApiConstant.CHANGE_DUTY)
    suspend fun changeDuty(@FieldMap map: Map<String, String>): Response<ChangeDutyResponse>

    @FormUrlEncoded
    @POST(ApiConstant.CANCEL_TRIP)
    suspend fun cancelTrip(@FieldMap map: Map<String, String>): Response<BaseResponse>

    @FormUrlEncoded
    @POST(ApiConstant.COMPLETE_TRIP)
    suspend fun completeTrip(@FieldMap map: Map<String, String>): Response<CompleteTripResponse>

    @FormUrlEncoded
    @POST(ApiConstant.RATING)
    suspend fun reviewRating(@FieldMap map: Map<String, String>): Response<RatingResponse>


    @FormUrlEncoded
    @POST(ApiConstant.CHAT_TO_CUSTOMER)
    suspend fun chatApi(@FieldMap map: Map<String, String>): Response<ChatResponse>

    /** Logout APi*/
    @GET
    suspend fun chatHistory(@Url url: String): Response<ChatResponse>
}