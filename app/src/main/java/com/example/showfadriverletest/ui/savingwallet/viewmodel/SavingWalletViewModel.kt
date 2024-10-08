package com.example.showfadriverletest.ui.savingwallet.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.savingwallet.SavingWalletHistoryResponse
import com.example.showfadriverletest.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SavingWalletViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService
) : BaseViewModel<Any>() {


    private val savibgWalletHistoryResponseObserver: MutableLiveData<Resource<SavingWalletHistoryResponse>> = MutableLiveData()
    fun getsavingWalletHistoryObservable(): LiveData<Resource<SavingWalletHistoryResponse>> {
        return savibgWalletHistoryResponseObserver
    }



    fun callSavingWalletHistoryApi(pageData: Int, uid: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<SavingWalletHistoryResponse>? = null

            val savingWalletHistoryRequest: MutableMap<String, String> = HashMap()
            savingWalletHistoryRequest[ApiConstant.DRIVER_ID] = uid
            savingWalletHistoryRequest[ApiConstant.PAGE] = pageData.toString()


            viewModelScope.launch {
                savibgWalletHistoryResponseObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.savingWalletHistory(savingWalletHistoryRequest)
                    Log.e("loginResponse------------------>", "$response")
                }

                withContext(Dispatchers.Main) {
                    response.run {
                        savibgWalletHistoryResponseObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    savibgWalletHistoryResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }

}