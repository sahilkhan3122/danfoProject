package com.example.showfadriverletest.fragment.viewmodel

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
import com.example.showfadriverletest.response.pasttrip.PastTripResponse
import com.example.showfadriverletest.response.pasttrip.TempResponse
import com.example.showfadriverletest.response.vehicledetail.VehicleManufacturerResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class PastViewModel @Inject constructor(var context: Context, var networkService: ApiService) :
    BaseViewModel<Any>() {

    var id = ""
    private val pastResponseObserver: MutableLiveData<Resource<TempResponse>> = MutableLiveData()

    fun getPastResponseObserver(): LiveData<Resource<TempResponse>> {
        return pastResponseObserver
    }


    fun callPastTripApi() {
        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<TempResponse>
            viewModelScope.launch {
                try {
                    pastResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                        response = networkService.pastTrip(ApiConstant.END_POINT_PAST_TRIP.plus(id))
                        PrintLog.e(
                            "TempResponse==========================>", "$response"
                        )
                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            pastResponseObserver.value = baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            pastResponseObserver.value = Resource(
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
                            pastResponseObserver.value = Resource(
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
                    pastResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}
