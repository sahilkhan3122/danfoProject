package com.example.showfadriverletest.ui.otp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.response.resendotp.ResendOtpResponse
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ResendOtpViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<OtpNavigator>() {


    var resendOtp = ""
    var no = ""
    private val otpResendResponseObserver: MutableLiveData<Resource<ResendOtpResponse>> =
        MutableLiveData()

    fun getResendOtpObservable(): LiveData<Resource<ResendOtpResponse>> {
        return otpResendResponseObserver
    }

    fun callOtpResendApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<ResendOtpResponse>? = null

            val resandOtpRequest: MutableMap<String, String> = HashMap()
            resandOtpRequest[ApiConstant.MOBILE_NO] = no

            viewModelScope.launch {
                otpResendResponseObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.resendOtp(resandOtpRequest)
                    Log.e("resandOtpResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        otpResendResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    otpResendResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }


    }
}