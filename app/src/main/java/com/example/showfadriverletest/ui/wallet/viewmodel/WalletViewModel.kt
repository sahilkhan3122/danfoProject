package com.example.showfadriverletest.ui.wallet.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.wallet.WalletHistoryResponse
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class WalletViewModel  @Inject constructor(
    var context: Context,
    var networkService: ApiService
) : BaseViewModel<Any>() {
    var uid = ""
    var amount = ""
    private val walletHistoryResponseObserver: MutableLiveData<Resource<WalletHistoryResponse>> = MutableLiveData()

    fun getwalletHistoryObservable(): LiveData<Resource<WalletHistoryResponse>> {
        return walletHistoryResponseObserver
    }

    fun callWalletHistoryApi(pageData: Int,uid:String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<WalletHistoryResponse>? = null
            val walletHistoryRequest: MutableMap<String, String> = HashMap()
            walletHistoryRequest[ApiConstant.DRIVER_ID] = uid
            walletHistoryRequest[ApiConstant.PAGE] = pageData.toString()
            walletHistoryRequest[ApiConstant.FROM_DATE] = ""
            walletHistoryRequest[ApiConstant.TO_DATE] = ""
            walletHistoryRequest[ApiConstant.PAYMENT_TYPE] = ""
            walletHistoryRequest[ApiConstant.TRANSACTION_TYPE] = ""

            viewModelScope.launch {
                walletHistoryResponseObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.walletHistory(walletHistoryRequest)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        walletHistoryResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    walletHistoryResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }

}