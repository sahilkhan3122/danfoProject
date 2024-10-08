package com.example.showfadriverletest.ui.login

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.common.Constants.flagPermission
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.component.showSuccessAlert
import com.example.showfadriverletest.databinding.ActivityLoginBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.ui.login.viewmodel.LoginNavigator
import com.example.showfadriverletest.ui.login.viewmodel.LoginViewModel
import com.example.showfadriverletest.ui.otp.OtpVerification
import com.example.showfadriverletest.ui.register.RegisterOtpActivity
import com.example.showfadriverletest.ui.termscondition.TermsOfService
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.CommonFun.statusBar
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.PopupDialog.collectData
import com.example.showfadriverletest.util.SnackbarUtil
import com.example.showfadriverletest.view.showSnackBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {
    override val layoutId: Int
        get() = R.layout.activity_login
    override val bindingVariable: Int
        get() = BR.viewModel

    var otp = ""
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocationLat: Double? = null
    private var currentLocationLng: Double? = null
    private var isPostNotificationGranted = false
    private var flagLocationDenied = ""
    private var flagLocationStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        statusBar(this)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@LoginActivity)
        activityLauncher()
        onBackPressedDispatcher.addCallback(this@LoginActivity, callback)
        lifecycleScope.launch {
            lifecycleScope.launch {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPostNotification()
                }
            }
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            isPostNotificationGranted = true
        } else {
            CommonFun.showSetting(
                this, getString(R.string.please_turn_on_postnotification_permission)
            )
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            finishAffinity()
        }
    }

    override fun setUpObserver() {
        mViewModel.setNavigator(this)
        mViewModel.getLoginObservable().observe(this) { it ->

                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        myDialog.hide()
                        if(it.data?.status==true){
                            showSuccessAlert(it.message!!)
                            Handler(Looper.myLooper()!!).postDelayed({
                                lifecycleScope.launch {
                                    otp =
                                        it.data!!.otp.toString()/* USERID = it.data.data!!.id.toString()*/
                                    lifecycleScope.launch {
                                        dataStore.setApiKey(it.data.data!!.xApiKey.toString())
                                        dataStore.setFirstName(it.data.data.firstName.toString())
                                        dataStore.setLastName(it.data.data.lastName.toString())
                                        dataStore.setMobileNo(it.data.data.mobileNo.toString())
                                        dataStore.setEmail(it.data.data.email.toString())
                                        dataStore.setGender(it.data.data.gender.toString())
                                        dataStore.setCarType(it.data.data.carType.toString())
                                        dataStore.setOwnerName(it.data.data.ownerName.toString())
                                        dataStore.setOwnerEmail(it.data.data.ownerEmail.toString())
                                        dataStore.setOwnerMobileNo(it.data.data.ownerMobileNo.toString())
                                        dataStore.setDob(it.data.data.dob.toString())
                                        dataStore.setProfileImage(it.data.data.profileImage.toString())
                                        dataStore.setUserId(it.data.data.id.toString())
                                        dataStore.setHasApiKey("true")
                                        dataStore.setPlateNumber(it.data.data.vehicleInfo?.get(0)!!.plateNumber.toString())
                                        dataStore.setColor(it.data.data.vehicleInfo[0]!!.vehicleColor.toString())
                                        dataStore.setLeftImage(it.data.data.vehicleInfo[0]!!.carLeft.toString())
                                        dataStore.setRightImage(it.data.data.vehicleInfo[0]!!.carRight.toString())
                                        dataStore.setFrontImage(it.data.data.vehicleInfo[0]!!.carFront.toString())
                                        dataStore.setBackImage(it.data.data.vehicleInfo[0]!!.carBack.toString())
                                        dataStore.setInsideImage(it.data.data.vehicleInfo[0]!!.carInterior.toString())
                                        dataStore.setNationalIdNo(it.data.data.driverDocs!!.nationalIdNumber.toString())
                                        dataStore.setNationalIdImage(it.data.data.driverDocs.nationalIdImage!!.toString())
                                        dataStore.setDriverLicenceImage(it.data.data.driverDocs.driverLicenceImage!!.toString())
                                        dataStore.setDriverLicenceExpiry(it.data.data.driverDocs.driverLicenceExpDate!!.toString())
                                        dataStore.setNTSABudgeExpiry(it.data.data.driverDocs.psvBadgeExpDate!!.toString())
                                        dataStore.setNtsaBadgeImage(it.data.data.driverDocs.psvBadgeImage!!.toString())
                                        dataStore.setGoodConductImage(it.data.data.driverDocs.policeClearanceCerti!!.toString())
                                        dataStore.setVehicleLogBookImage(it.data.data.driverDocs.vehicleLogBookImage!!.toString())
                                        dataStore.setNtsaInspectionImage(it.data.data.driverDocs.ntsaInspectionImage!!.toString())
                                        dataStore.setNTSAInspectionExpiry(it.data.data.driverDocs.ntsaExpDate!!.toString())
                                        dataStore.setComprehensiveImage(it.data.data.driverDocs.psvComprehensiveInsurance!!.toString())
                                        dataStore.setPsvComprehensiveExpiry(it.data.data.driverDocs.psvComprehensiveInsuranceExpDate!!.toString())
                                    }
                                    dataStore.getFcmToken.asLiveData().observe(this@LoginActivity) {
                                        deviceToken = it.toString()
                                        ApiConstant.token = it.toString()
                                    }
                                    dataStore.getUserId.asLiveData().observe(this@LoginActivity) {
                                        ApiConstant.userid = it.toString()
                                    }
                                    lifecycleScope.launch {
                                        dataStore.setOtp(otp)
                                        dataStore.setMobileNo(binding.edtMobileNumber.text.toString())
                                        Constants.Login = "Login"
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                OtpVerification::class.java
                                            )
                                        )
                                        overridePendingTransition(
                                            R.anim.enter_from_right,
                                            R.anim.exit_to_left
                                        )
                                        finish()
                                    }
                                }
                            }, 2000)
                        }else{
                                showFailAlert(it.message!!)
                        }

                    }

                    Resource.Status.ERROR -> {
                        myDialog.hide()
                        showFailAlert(it.message!!)
                    }

                    Resource.Status.LOADING -> {
                        myDialog.show()
                    }

                    Resource.Status.NO_INTERNET_CONNECTION -> {
                        myDialog.hide()
                        binding.root.showSnackBar(getString(R.string.no_internet_connection))
                    }

                    else -> {
                        showFailAlert(it.message!!)
                    }
                }

        }
    }

    private suspend fun checkPostNotification() {
        val job = lifecycleScope.launch {
            checkPostPermission()
            delay(1000L)
        }
        job.join()
    }

    private fun checkPostPermission() {
        when (PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this@LoginActivity, Manifest.permission.POST_NOTIFICATIONS
            ),
            -> {
                Log.e(TAG, "onCreate: PERMISSION GRANTED")
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        }
    }

    private fun activityLauncher() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    flagLocationStatus = true
                    getCurrentLocation()
                } else {
                    flagLocationStatus = false
                    flagLocationDenied = "deniedLocation"
                    binding.root.showSnackBar(getString(R.string.please_turn_on_your_location))
                    CommonFun.showSetting(
                        this@LoginActivity, getString(R.string.please_turn_on_location_permission)
                    )
                }
            }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            return
        } else {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                val location = task.result
                Log.d("LOCATION", "location==>$location")
                if (location != null) {
                    currentLocationLat = location.latitude
                    currentLocationLng = location.longitude
                    Log.d(TAG, "LAT---->${location.latitude}")
                    Log.d(TAG, "LNG---->${location.latitude}")
                    flagPermission = true
                }
            }
        }
    }


    override fun onLogin() {
        if (binding.edtMobileNumber.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.empty_mobile_number))
        } else {
            if (flagLocationStatus) {
                mViewModel.userNumber = binding.edtMobileNumber.text.toString()
                mViewModel.callLoginApi(deviceToken, currentLocationLat, currentLocationLng)
            } else {
                PopupDialog.logout(
                    this,
                    getString(R.string.collects_location_data_to_enable_for_real_time_tracking_of_driver_even_when_the_app_is_in_background_or_not_in_use)
                )
                {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    override fun onRegister() {
        startActivity(Intent(this@LoginActivity, RegisterOtpActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        finish()
    }

    fun termsOfService(view: View) {
        ApiConstant.ISCHECKED_TERM_OF_SERVICE = true
        ApiConstant.ISCHECKED_PRIVACY_POLICY = false
        startActivity(Intent(this@LoginActivity, TermsOfService::class.java))
    }

    fun privacyPolicy(view: View) {
        ApiConstant.ISCHECKED_TERM_OF_SERVICE = false
        ApiConstant.ISCHECKED_PRIVACY_POLICY = true
        startActivity(Intent(this@LoginActivity, TermsOfService::class.java))
    }

    fun register(view: View) {
        startActivity(Intent(this@LoginActivity, RegisterOtpActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        finish()
    }
}


