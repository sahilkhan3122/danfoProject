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
import com.example.showfadriverletest.response.featuretrip.FeatureTripResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class UpcomingTripViewModel @Inject constructor(var context: Context, var networkService: ApiService) : BaseViewModel<Any>()  {
    private val featureResponseObserver: MutableLiveData<Resource<FeatureTripResponse>> =
        MutableLiveData()

    fun getFeatureResponseObserver(): LiveData<Resource<FeatureTripResponse>> {
        return featureResponseObserver
    }

    fun callFeatureTripApi(uid: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            lateinit var response: Response<FeatureTripResponse>
            viewModelScope.launch {
                try {
                    featureResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                        response = networkService.futureTrip(ApiConstant.END_POINT_FEATURE_TRIP.plus(uid))
                        PrintLog.e(
                            "TempResponse==========================>",
                            "$response"
                        )
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            featureResponseObserver.value =
                                baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            featureResponseObserver.value = Resource(
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
                            featureResponseObserver.value = Resource(
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
                    featureResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}
