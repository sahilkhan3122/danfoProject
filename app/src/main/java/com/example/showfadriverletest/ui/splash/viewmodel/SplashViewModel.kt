package com.example.showfadriverletest.ui.splash.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.init.InitResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService
    ) : BaseViewModel<Any>(){

    private val initResponseObserver: MutableLiveData<Resource<InitResponse>> = MutableLiveData()

    fun getInitObservable(): LiveData<Resource<InitResponse>> {
        return initResponseObserver
    }

    fun init(driverId: String) {
        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<InitResponse>
            viewModelScope.launch {
                try {
                    initResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                            response =
                                networkService.init(ApiConstant.init+"/"+driverId)
                            PrintLog.e("showfa", "$response")
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            initResponseObserver.value = baseDataSource.getResult(true) { this }
                            Log.e(TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            initResponseObserver.value = Resource(
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
                            initResponseObserver.value = Resource(
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
                    initResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }

    }
}
