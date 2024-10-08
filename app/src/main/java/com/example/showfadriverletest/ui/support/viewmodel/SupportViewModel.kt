package com.example.showfadriverletest.ui.support.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.BaseResponse
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SupportViewModel @Inject constructor(var context: Context, var networkService: ApiService) :
    BaseViewModel<Any>() {

    var subject = ""
    var issue = ""
    var id = ""

    private val supportResponseObserver: MutableLiveData<Resource<BaseResponse>> =
        MutableLiveData()

    fun getSupportResponseObserver(): LiveData<Resource<BaseResponse>> {
        return supportResponseObserver
    }


    fun callSupportApi() {

        if (NetworkUtils.isNetworkConnected(context)) {

            if (NetworkUtils.isNetworkConnected(context)) {
                var response: Response<BaseResponse>? = null

                val supportRequest: MutableMap<String, String> = HashMap()
                supportRequest["user_id"] = id
                supportRequest["ticket_title"] = subject
                supportRequest["description"] = issue


                viewModelScope.launch {
                    supportResponseObserver.value = Resource.loading(null)

                    withContext(Dispatchers.IO) {
                        response = networkService.support(supportRequest)
                        Log.e("loginResponse------------------>", "$response")
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            supportResponseObserver.value = baseDataSource.getResult { this!! }
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        supportResponseObserver.value = Resource.noInternetConnection(null)
                    }
                }
            }
        }
    }

}