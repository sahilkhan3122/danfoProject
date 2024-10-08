package com.example.showfadriverletest.ui.register.registerotpviewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.registerOtp.RegisterOtpResponse
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


class RegisterOtpViewModel @Inject constructor(var context: Context,var networkService: ApiService): BaseViewModel<RegisterOtpNavigator>()  {

    private val registerOtpObserver: MutableLiveData<Resource<RegisterOtpResponse>> = MutableLiveData()

    fun getRegisterOtpObservable(): LiveData<Resource<RegisterOtpResponse>> {
        return registerOtpObserver
    }

    var userNumber = ""

    var email = ""


    fun registerOtpApi(){
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<RegisterOtpResponse>?

            val registerOtpRequest: MutableMap<String, String> = HashMap()
            registerOtpRequest[ApiConstant.EMAIL] = email
            registerOtpRequest[ApiConstant.REGISTER_MOBILE_NO] = userNumber
            viewModelScope.launch {
                registerOtpObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.registerOtp(registerOtpRequest)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        registerOtpObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    registerOtpObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}