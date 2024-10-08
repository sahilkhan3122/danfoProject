package com.example.showfadriverletest.ui.subscription.viewmodel

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
import com.example.showfadriverletest.response.subscription.PurchaseSubscriptionResponse
import com.example.showfadriverletest.response.subscription.SubscriptionResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class SubscriptionViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService
) : BaseViewModel<Any>() {

    var id = ""

    private val subscriptionResponseObserver: MutableLiveData<Resource<SubscriptionResponse>> = MutableLiveData()

    fun getSubscriptionObservable(): LiveData<Resource<SubscriptionResponse>> {
        return subscriptionResponseObserver
    }

    fun subscriptionApi() {
        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<SubscriptionResponse>
            viewModelScope.launch {
                try {
                    subscriptionResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                        response =
                            networkService.subscriptionList(ApiConstant.END_POINT_SUBSCRIPTION.plus("/").plus(id))
                        PrintLog.e("showfa", "$response")
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            subscriptionResponseObserver.value = baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            subscriptionResponseObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                502
                            )
                        }
                    }
                }catch (e: Exception) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            subscriptionResponseObserver.value = Resource(
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
                    subscriptionResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }

    private val subscriptionPurchaseResponseObserver: MutableLiveData<Resource<PurchaseSubscriptionResponse>> = MutableLiveData()

    fun getSubscriptionPurchaseObservable(): LiveData<Resource<PurchaseSubscriptionResponse>> {
        return subscriptionPurchaseResponseObserver
    }
    fun purchaseSubscription(subscriptionId: String, paymentMethodType: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<PurchaseSubscriptionResponse>? = null

            val subscriptionPurchase: MutableMap<String, String> = HashMap()
            subscriptionPurchase[ApiConstant.DRIVER_ID] = id
            subscriptionPurchase[ApiConstant.SUBSCRIPTION_ID] = subscriptionId
            subscriptionPurchase[ApiConstant.PAYMENT_TYPE] = paymentMethodType

            viewModelScope.launch {
                subscriptionPurchaseResponseObserver.value = Resource.loading(null)
                withContext(Dispatchers.IO) {
                    response = networkService.subscriptionPurchase(subscriptionPurchase)
                    Log.e("Response------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        subscriptionPurchaseResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    subscriptionPurchaseResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}