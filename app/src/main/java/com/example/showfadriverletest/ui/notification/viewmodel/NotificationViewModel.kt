package com.example.showfadriverletest.ui.notification.viewmodel

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
import com.example.showfadriverletest.response.notificationlist.NotificationResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {
    var id = ""

    private val notificationResponseObserver: MutableLiveData<Resource<NotificationResponse>> =
        MutableLiveData()

    fun getNotificationObservable(): LiveData<Resource<NotificationResponse>> {
        return notificationResponseObserver
    }

    fun notificationListApi(uid: String) {
        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<NotificationResponse>
            viewModelScope.launch {
                try {
                    notificationResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                        response =
                            networkService.notificationList(
                                ApiConstant.NOTIFICATION_END_POINT.plus(
                                    uid
                                )
                            )
                        PrintLog.e("showfa", "$response")
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            notificationResponseObserver.value =
                                baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            notificationResponseObserver.value = Resource(
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
                            notificationResponseObserver.value = Resource(
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
                    notificationResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }

    }


    /**clear notification*/

    private val notificationClearResponseObserver: MutableLiveData<Resource<BaseResponse>> =
        MutableLiveData()

    fun getNotificationClearObservable(): LiveData<Resource<BaseResponse>> {
        return notificationClearResponseObserver
    }

    fun notificationListClearApi(uid: String) {

        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<BaseResponse>
            viewModelScope.launch {
                try {
                    notificationClearResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {

                        response =
                            networkService.notionClearList(
                                ApiConstant.NOTIFICATION_CLEAR_END_POINT.plus(uid))
                        PrintLog.e("showfa", "$response")
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            notificationClearResponseObserver.value =
                                baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            notificationClearResponseObserver.value = Resource(
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
                            notificationClearResponseObserver.value = Resource(
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
                    notificationClearResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }

    }

}
