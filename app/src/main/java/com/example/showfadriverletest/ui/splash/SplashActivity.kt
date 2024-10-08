package com.example.showfadriverletest.ui.splash

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.Constants.PASSANGER_NUMBER
import com.example.showfadriverletest.common.Constants.SOS_NUMBER
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivitySplashBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.ui.WelcomeActivity
import com.example.showfadriverletest.ui.home.MapsActivity
import com.example.showfadriverletest.ui.splash.viewmodel.SplashViewModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.SnackbarUtil
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val bindingVariable: Int
        get() = BR.viewModel
    var driverId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonFun.hideStatusBar(this@SplashActivity)
        getDeviceToken()
        mViewModel.setNavigator(this@SplashActivity)
        getId()
    }

    private fun getId() {
        lifecycleScope.launch {
            val job = lifecycleScope.async {
                dataStore.getUserId.asLiveData().observe(this@SplashActivity) {
                    driverId = it
                    mViewModel.init(driverId)
                }
            }
            job.await()
        }
    }

    override fun setUpObserver() {
        mViewModel.getInitObservable().observe(this) { res ->
            when (res.status) {
                Resource.Status.SUCCESS -> {

                    if (res.data?.status == true) {
                        ApiConstant.initGsonData  = res.data
                        dataStore.getFcmToken.asLiveData().observe(this@SplashActivity) {
                            deviceToken = it.toString()
                            ApiConstant.token = it.toString()

                        }
                        SOS_NUMBER = res.data.sosNumber.toString()
                        Handler(Looper.myLooper()!!).postDelayed({
                            lifecycleScope.launch {
                                if (dataStore.getUserId.first()
                                        .isNotEmpty() && dataStore.getUserId.first() != "null"
                                ) {
                                    startActivity(
                                        Intent(
                                            this@SplashActivity,
                                            MapsActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    startActivity(
                                        Intent(this@SplashActivity, WelcomeActivity::class.java)
                                    )
                                    finish()
                                }
                            }
                        }, 2000)
                    } else{
                        showFailAlert(res.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    Log.d(ContentValues.TAG, "on error----------------=>${res.message}")
                    PopupDialog.logout(this, res!!.message.toString()) {
                        finish()
                    }
                }

                Resource.Status.LOADING -> {
                    Log.d(ContentValues.TAG, "loading=>${res.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    PopupDialog.initPopup(
                        this,
                        getString(R.string.no_internet_connection)
                    ) { finish() }
                    Log.d(ContentValues.TAG, "no internet=>${res.message}")
                }

                else -> {}
            }
        }
    }

    override fun onResume() {
        mViewModel.init(driverId)
        super.onResume()
    }
}
