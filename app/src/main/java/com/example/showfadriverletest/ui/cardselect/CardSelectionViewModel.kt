package com.example.showfadriverletest.ui.cardselect

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
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class CardSelectionViewModel@Inject constructor(
    var context: Context,
    var networkService: ApiService
) : BaseViewModel<Any>() {

    private val addMoneyResponseObserver: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()

    fun getAddMoneyObservable(): LiveData<Resource<BaseResponse>> {
        return addMoneyResponseObserver
    }
    fun addMoney(amount: String, id: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<BaseResponse>? = null
            val addMoney: MutableMap<String, String> = HashMap()
            addMoney[ApiConstant.DRIVER_ID] = id
            addMoney[ApiConstant.PAYMENT_TYPE] = "m_pesa"
            addMoney[ApiConstant.AMOUNT] = amount

            viewModelScope.launch {
                addMoneyResponseObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.addMoney(addMoney)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        addMoneyResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    addMoneyResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}