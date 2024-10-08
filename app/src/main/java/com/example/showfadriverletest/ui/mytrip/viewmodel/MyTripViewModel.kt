package com.example.showfadriverletest.ui.mytrip.viewmodel

import android.content.Context
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.network.ApiService
import javax.inject.Inject

class MyTripViewModel  @Inject constructor(
    var context: Context,
    var networkService: ApiService
) : BaseViewModel<Any>(){

}