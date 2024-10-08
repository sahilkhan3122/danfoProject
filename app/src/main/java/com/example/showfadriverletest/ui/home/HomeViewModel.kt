package com.example.showfadriverletest.ui.home

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.BaseResponse
import com.example.showfadriverletest.response.changeduty.ChangeDutyResponse
import com.example.showfadriverletest.response.completetrip.CompleteTripResponse
import com.example.showfadriverletest.response.rating.RatingResponse
import com.example.showfadriverletest.service.TAG
import com.example.showfadriverletest.ui.login.viewmodel.LoginNavigator
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<LoginNavigator>() {

    var lat = ""
    var lng = ""
    var bookingId = ""

    /**logout account*/

    private val logoutResponseObserver: MutableLiveData<Resource<BaseResponse>> =
        MutableLiveData()

    fun getLogoutObserver(): LiveData<Resource<BaseResponse>> {
        return logoutResponseObserver
    }

    fun callLogoutApi(id: String) {
        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<BaseResponse>
            viewModelScope.launch {
                try {
                    logoutResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                        response = networkService.logout(
                            ApiConstant.LOGOUT_END_POINT.plus(id).plus("/").plus(ApiConstant.token)
                        )
                        PrintLog.e(
                            "TempResponse==========================>",
                            "$response"
                        )
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            logoutResponseObserver.value =
                                baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            logoutResponseObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                502
                            )
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            logoutResponseObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage.let { "- " + e.cause },
                                500
                            )
                        }
                    }
                }

            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    logoutResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }

    /**changeDuty*/

    private val changeDutyObserver: MutableLiveData<Resource<ChangeDutyResponse>> =
        MutableLiveData()

    fun getChangeDutyObserval(): LiveData<Resource<ChangeDutyResponse>> {
        return changeDutyObserver
    }

    fun changeDutyApiCall(id: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<ChangeDutyResponse>?

            val changeDutyRequest: MutableMap<String, String> = HashMap()
            changeDutyRequest[ApiConstant.DRIVER_ID] = id
            changeDutyRequest[ApiConstant.LAT] = lat
            changeDutyRequest[ApiConstant.LNG] = lng

            viewModelScope.launch {
                changeDutyObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.changeDuty(changeDutyRequest)
                    Log.e("changeDutyRequestResponse------------------>", "$response")
                }
                withContext(Dispatchers.Main) {
                    response.run {
                        changeDutyObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    changeDutyObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }


    /**cancel Trip*/
    private val cancelTripResponseObserver: MutableLiveData<Resource<BaseResponse>> =
        MutableLiveData()

    fun getCancelTripObserver(): LiveData<Resource<BaseResponse>> {
        return cancelTripResponseObserver
    }

    fun cancelTripAPiCall(bookingId: String,resone:String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<BaseResponse>? = null

            val cancelTripRequest: MutableMap<String, String> = HashMap()
            cancelTripRequest[ApiConstant.BOOKING_ID] = bookingId
            cancelTripRequest[ApiConstant.CANCEL_RESON] = resone

            viewModelScope.launch {
                cancelTripResponseObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.cancelTrip(cancelTripRequest)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        cancelTripResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    cancelTripResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }


    /**complete Trip*/

    private val completeTripObserver: MutableLiveData<Resource<CompleteTripResponse>> =
        MutableLiveData()

    fun getCompleteTripObserver(): LiveData<Resource<CompleteTripResponse>> {
        return completeTripObserver
    }
    fun completeTripApiCall(
        id: String,
        completeTripDropOffLat: String,
        completeTripDropOffLng: String,
        distance: String
    ) {
        if (NetworkUtils.isNetworkConnected(context)) {

            completeTripObserver.value = Resource.loading()
            lateinit var response: Response<CompleteTripResponse>
            val completeTripRequest: MutableMap<String, String> = HashMap()

            completeTripRequest[ApiConstant.BOOKING_ID] = id
            completeTripRequest[ApiConstant.DROP_OF_LAT] = completeTripDropOffLat
            // ConstantUtil.latitude.toString()
            completeTripRequest[ApiConstant.DROP_OF_LNG] = completeTripDropOffLng
            completeTripRequest[ApiConstant.DISTANCE] = distance
            // ConstantUtil.longitude.toString()

            Log.e("completeTripRequest", "completeTripRequest => $completeTripRequest")

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        response = networkService.completeTrip(completeTripRequest)
                        PrintLog.e("CompleteTrippResponse", "$response")

                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            completeTripObserver.value =
                                baseDataSource.getResult(true) { this }
                            PrintLog.e(TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            completeTripObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                502
                            )
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            completeTripObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                500
                            )
                        }
                    }
                }
            }

        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    completeTripObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }


    /**Rating Customer*/

    /**complete Trip*/

    private val reviewRatingObserver:MutableLiveData<Resource<RatingResponse>> = MutableLiveData()

    fun getReviewRatingObserver() :LiveData<Resource<RatingResponse>>{
        return reviewRatingObserver
    }

    fun ratingResponseApi(bookingId: String, description: String, rating: String) {
        if (NetworkUtils.isNetworkConnected(context)) {

            reviewRatingObserver.value = Resource.loading()
            lateinit var response: Response<RatingResponse>

            val ratingRequest: MutableMap<String, String> = HashMap()
            ratingRequest[ApiConstant.BOOKING_ID] =bookingId
            ratingRequest[ApiConstant.RATING_CUST] =description
            ratingRequest[ApiConstant.COMMENT] = rating
            // ConstantUtil.longitude.toString()

            Log.e("ratingRequest", "ratingRequest => $ratingRequest")

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        response = networkService.reviewRating(ratingRequest)
                        PrintLog.e("CompleteTrippResponse", "$response")

                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            reviewRatingObserver.value =
                                baseDataSource.getResult(true) { this }
                            PrintLog.e(TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            reviewRatingObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                502
                            )
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            reviewRatingObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                500
                            )
                        }
                    }
                }
            }

        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    completeTripObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }


    /**delete account*/

    private val deleteAccountObserver: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()

    fun getDeleteAccountObservable(): LiveData<Resource<BaseResponse>> {
        return deleteAccountObserver
    }

    fun deleteAccountApiCall(id: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<BaseResponse>? = null

            val deleteAccountRequest: MutableMap<String, String> = HashMap()
            deleteAccountRequest[ApiConstant.DRIVER_ID] = id

            viewModelScope.launch {
                deleteAccountObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.deleteAccount(deleteAccountRequest)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        deleteAccountObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    deleteAccountObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}
