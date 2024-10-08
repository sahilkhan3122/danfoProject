package com.example.showfadriverletest.ui.register.registerprofile.viewmodel

import android.content.Context
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.network.ApiService
import javax.inject.Inject

class RegisterProfileModel  @Inject constructor(
    var context: Context,
    var networkService: ApiService
) : BaseViewModel<Any>() {
}