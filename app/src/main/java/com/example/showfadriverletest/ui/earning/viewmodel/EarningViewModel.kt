package com.example.showfadriverletest.ui.earning.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.earning.EarningResponse
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class EarningViewModel @Inject constructor(var context: Context, var networkService: ApiService) :
    BaseViewModel<Any>() {

   var  type = ""
    var id = ""
    private val earningReportObserver: MutableLiveData<Resource<EarningResponse>> =
        MutableLiveData()

    fun getearningReportObservable(): LiveData<Resource<EarningResponse>> {
        return earningReportObserver
    }

    fun earningReportApi(strFromDate: String, toDate: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<EarningResponse>? = null
            val earningReportRequest: MutableMap<String, String> = HashMap()
            earningReportRequest[ApiConstant.TYPE] = type
            earningReportRequest[ApiConstant.DRIVERID] = id
            earningReportRequest[ApiConstant.FROMDATE] = strFromDate
            earningReportRequest[ApiConstant.TODATE] = toDate
            viewModelScope.launch {
                earningReportObserver.value = Resource.loading(null)
                withContext(Dispatchers.IO) {
                    response = networkService.earningReport(earningReportRequest)
                    Log.e("earningResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        earningReportObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    earningReportObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}