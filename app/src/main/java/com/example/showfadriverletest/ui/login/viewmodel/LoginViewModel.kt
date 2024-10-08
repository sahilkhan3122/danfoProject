package com.example.showfadriverletest.ui.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant.DEVICE_TOKEN
import com.example.showfadriverletest.common.ApiConstant.DEVICE_TYPE
import com.example.showfadriverletest.common.ApiConstant.LAT
import com.example.showfadriverletest.common.ApiConstant.LNG
import com.example.showfadriverletest.common.ApiConstant.USER_NAME
import com.example.showfadriverletest.response.login.LoginResponse
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.util.NetworkUtils
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<LoginNavigator>() {

    private val loginResponseObserver: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun getLoginObservable(): LiveData<Resource<LoginResponse>> {
        return loginResponseObserver
    }

    var latlngLiveData: MutableLiveData<LatLng> = MutableLiveData()

    fun setLatlngLiveData(latlng: LatLng) {
        latlngLiveData.value = latlng
    }


    var userNumber = ""

    fun callLoginApi(deviceToken: String?, currentLocationLat: Double?, currentLocationLng: Double?) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<LoginResponse>? = null

            val loginRequest: MutableMap<String, String> = HashMap()
            loginRequest[USER_NAME] = userNumber
            loginRequest[LAT] = currentLocationLat.toString()
            loginRequest[LNG] = currentLocationLat.toString()
            loginRequest[DEVICE_TYPE] = context.getString(R.string.android)
            loginRequest[DEVICE_TOKEN] = deviceToken.toString()

            viewModelScope.launch {
                 loginResponseObserver.value = Resource.loading(null)
                withContext(Dispatchers.IO) {
                    response = networkService.login(loginRequest)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        loginResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    loginResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}