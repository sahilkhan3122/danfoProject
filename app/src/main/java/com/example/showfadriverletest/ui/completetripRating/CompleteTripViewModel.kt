package com.example.showfadriverletest.ui.completetripRating

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.rating.RatingResponse
import com.example.showfadriverletest.service.TAG
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class CompleteTripViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {

    /**Rating Customer*/
    private val reviewRatingObserver: MutableLiveData<Resource<RatingResponse>> = MutableLiveData()

    fun getReviewRatingObserver() : LiveData<Resource<RatingResponse>> {
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
                    reviewRatingObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}