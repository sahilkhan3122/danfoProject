package com.example.showfadriverletest.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.ApiConstant.FlagMapDirectionState
import com.example.showfadriverletest.common.ApiConstant.GPS_STATUS
import com.example.showfadriverletest.common.ApiConstant.PIN
import com.example.showfadriverletest.common.ApiConstant.STATUS_REQUEST_ACCEPT
import com.example.showfadriverletest.common.ApiConstant.STATUS_REQUEST_TRAVELING
import com.example.showfadriverletest.common.ApiConstant.bookingRequestSlide
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.common.Constants.PASSANGER_NUMBER
import com.example.showfadriverletest.common.Constants.SOS_NUMBER
import com.example.showfadriverletest.common.Constants.latitude
import com.example.showfadriverletest.common.Constants.longitude
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.component.showSuccessAlert
import com.example.showfadriverletest.databinding.ActivityMapsBinding
import com.example.showfadriverletest.di.app.MyApplication
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.BaseResponse
import com.example.showfadriverletest.response.OnAcceptBookingResponse
import com.example.showfadriverletest.response.OnForwardBookingRequest
import com.example.showfadriverletest.response.OnInitResponse
import com.example.showfadriverletest.response.canceltrip.CancelModel
import com.example.showfadriverletest.response.drawermodel.DrawerModel
import com.example.showfadriverletest.response.init.BookingInfo
import com.example.showfadriverletest.response.init.CustomerInfo
import com.example.showfadriverletest.service.ConnectionLiveData
import com.example.showfadriverletest.service.LocationService
import com.example.showfadriverletest.service.TAG
import com.example.showfadriverletest.ui.WelcomeActivity
import com.example.showfadriverletest.ui.chat.ChatActivity
import com.example.showfadriverletest.ui.completetripRating.CompleteTripActivity
import com.example.showfadriverletest.ui.earning.EarningActivity
import com.example.showfadriverletest.ui.edit.editProfile.EditProfileActivity
import com.example.showfadriverletest.ui.home.adapter.CancelTripAdapter
import com.example.showfadriverletest.ui.home.adapter.DrawerAdapter
import com.example.showfadriverletest.ui.mytrip.MyTripActivity
import com.example.showfadriverletest.ui.notification.NotificationActivity
import com.example.showfadriverletest.ui.savingwallet.SavingWalletActivity
import com.example.showfadriverletest.ui.setting.SettingActivity
import com.example.showfadriverletest.ui.splash.SplashActivity
import com.example.showfadriverletest.ui.subscription.SubscriptionActivity
import com.example.showfadriverletest.ui.support.SupportActivity
import com.example.showfadriverletest.ui.termscondition.TermsOfService
import com.example.showfadriverletest.ui.wallet.WalletActivity
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.DateUtils
import com.example.showfadriverletest.util.DateUtils.USA_FORMAT_DATE
import com.example.showfadriverletest.util.GpsUtil
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.PopupDialog.collectData
import com.example.showfadriverletest.util.PopupDialog.initPopup
import com.example.showfadriverletest.util.PopupDialog.showDialogOfLocationPolicy
import com.example.showfadriverletest.util.PopupDialog.socketAleartDailog
import com.example.showfadriverletest.util.PopupDialog.socketDailog
import com.example.showfadriverletest.util.SnackbarUtil
import com.example.showfadriverletest.util.gone
import com.example.showfadriverletest.util.mapparam.MapData
import com.example.showfadriverletest.util.setGlideImage
import com.example.showfadriverletest.util.showMsgDialog
import com.example.showfadriverletest.util.visible
import com.example.showfadriverletest.view.showSnackBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

@AndroidEntryPoint
class MapsActivity : BaseActivity<ActivityMapsBinding, HomeViewModel>(), OnMapReadyCallback {
    override val layoutId: Int
        get() = R.layout.activity_maps
    override val bindingVariable: Int
        get() = BR.viewModel

    private var mMap: GoogleMap? = null
    private var isOpen = false
    private var cancellationAdapter: CancelTripAdapter? = null
    private var cancellationModel =
        ArrayList<CancelModel>()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var serviceRegistered = false
    private var latlang: LatLng? = null
    private val option = MarkerOptions()
    private var carMarker: Marker? = null
    var driverId = ""
    var resone = ""
    lateinit var locationService: LocationService
    private var mSocket: Socket? = null
    private var locationPermission = false
    private var repeatJobUpdateDriverLocation: Job? = null
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var gpsUtils: GpsUtil
    var handlerAnimateMarker: Handler? = null
    private val handlerHeatMap = Handler(Looper.getMainLooper())
    private val handlerUpdateLatLang = Handler(Looper.getMainLooper())
    var delay = 1000
    private var currentLocationLat: Double? = null
    private var currentLocationLng: Double? = null
    private lateinit var drawableModel: ArrayList<DrawerModel>
    var firstRequestId = ""
    var bookingType = ""
    var bookingId = ""
    var driverName = ""
    private lateinit var connectionLiveData: ConnectionLiveData

    ////for Passcode back press
    private val PERMISSION_REQUEST_CODE = 1
    private val REQUEST_CALL_PHONE = 3
    private val DRAW_OVER_OTHER_APP_PERMISSION = 99
    private val PERMISSION_REQUEST_CODE_BACKGROUND_PERMISSION = 2
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val overlayPermissionRequestCode = 123
    private var startMarker = MarkerOptions()
    var destinationMarker = MarkerOptions()
    private var runnableInitWaitingTime: Runnable? = null
    private var runnableUpdateLoc: Runnable? = null
    private var runnableHeatMap: Runnable? = null

    private var repeatJobLiveTracking: Job? = null
    var acceptGsonData: OnAcceptBookingResponse? = null
    var gsonDataCancel: OnInitResponse.Data? = null
    var initGsonData: OnInitResponse? = null
    var acceptRideTimer: CountDownTimer? = null
    var waitingTimerInit: CountDownTimer? = null
    var waitingTimer: CountDownTimer? = null
    var bsCustomerRequestBehavior: BottomSheetBehavior<*>? = null
    var cancelReasonBehaviour: BottomSheetBehavior<*>? = null
    var driverStatusDialogBehaviour: BottomSheetBehavior<*>? = null
    var isRequestArrived = false
    var isRequestArrivedFirst = false
    var isPlaying = false
    var notifySound: MediaPlayer? = null
    var isFirstTime = true
    var arriveSlide = 0
    var distance = "4"
    var customerId = ""
    var completeTripDropOffLat = ""
    var completeTripDropOffLng = ""
    var dropOffLat = ""
    var dropOffLng = ""
    var gpsStatus = ""

    @SuppressLint("MissingInflatedId", "RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonFun.hideStatusBar(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationService = LocationService()
        gpsUtils = GpsUtil(this)
        /* checkGpsStatus()*/
        setDrawerHeaderImage()
        activityLauncher()
        checkStatus()
        connectSocket()
        cancelTripFun()
        setBottomSheetBehaviour()



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    getLastLocation()
                }
            }
        }

        dataStore.getUserId.asLiveData().observe(this@MapsActivity) { driverId = it }
        Log.e(TAG, "onCreate initGsonData: ${ApiConstant.initGsonData}")
        onBackPressedDispatcher.addCallback(this@MapsActivity, callback)
        binding.driverStatus.resetCurrentLocation.setOnClickListener { getLastLocation() }
        binding.drawableItems.logout.setOnClickListener { logout() }
        binding.driverStatus.switchOnline.setOnCheckedChangeListener { _, isChecked ->
            if (!isLocationEnabled()) {
                Toast.makeText(this, "please enable location", Toast.LENGTH_SHORT).show()
                binding.driverStatus.switchOnline.isChecked = false
                binding.driverStatus.tvDriverStatus.setText(R.string.you_re_offline)
            } else {
                if (driverId != "" && driverId.isNotEmpty()) {
                    onSwitchChange()
                }
            }

        }
        binding.bsCustomerRequestDialog.ivRedirectionToGoogleMap.setOnClickListener { v: View? ->
            if (FlagMapDirectionState == "AcceptState") {
                setMapDirection(latitude, longitude)
            }
            if (FlagMapDirectionState == "StartTrip") {
                setMapDirection(
                    dropOffLat.toDouble(), dropOffLng.toDouble()
                )
            }
        }
        binding.bsCustomerRequestDialog.slideSeek.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean,
            ) {
                // Here, you can update UI or perform actions as the SeekBar progresses
                setSliderTextAlpha(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This is called when the user starts touching the SeekBar

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // This is called when the user stops touching the SeekBar
                if (seekBar?.progress == 100) {
                    if (bookingRequestSlide == 1) {
                        bookingRequestSlide = 2
                        acceptRideFunction()
                        seekBar.progress = 0
                        stopMusic()
                    } else if (bookingRequestSlide == 2) {
                        seekBar.progress = 0
                        ApiConstant.bookingRequestSlide = 3
                        arriveDriverFunction()
                    } else if (bookingRequestSlide == 3) {
                        startTripFun()
                        binding.driverStatus.clStartRideRootStatus.gone()
                        bookingRequestSlide = 4
                        seekBar.progress = 0
                    } else if (bookingRequestSlide == 4) {/*api call complete trip*/
                        initPopup(
                            this@MapsActivity,
                            getString(R.string.are_you_sure_you_want_to_complete_trip)
                        ) {
                            mViewModel.completeTripApiCall(
                                bookingId, completeTripDropOffLat, completeTripDropOffLng, distance
                            )
                        }
                        binding.bsCustomerRequestDialog.slideText.setText(getString(R.string.slide_to_accept))
                        bookingRequestSlide = 1
                        binding.bsCustomerRequestDialog.tvWaitingTime.visible()/* acceptRideTimer!!.cancel()*/
                        binding.bsCustomerRequestDialog.ivCancelRide.visible()
                        seekBar.progress = 0
                    }
                }
            }
        })
        binding.apply {
            Drawer.setOnClickListener {
                if (!isOpen) {
                    drawerLayout.openDrawer(Gravity.LEFT)
                    isOpen = true
                } else {
                    isOpen = false
                    drawerLayout.closeDrawer(Gravity.LEFT)
                }
            }
            drawableItems.header.setOnClickListener {
                startActivity(Intent(this@MapsActivity, EditProfileActivity::class.java))
            }
        }
        setDrawerList()
        initMap()
    }



    private fun checkGpsStatus() {
        gpsUtils.turnGPSOn(object : GpsUtil.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                if (isGPSEnabled) {
                    gpsStatus = "enable"
                    getCurrentLocation()
                } else {
                    gpsUtils.openLocationSettings()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ApiConstant.GPS_REQUEST) {
            if (resultCode == RESULT_OK) {
                GPS_STATUS = 1
                if (GPS_STATUS == 1) {
                    gpsStatus = "enable"
                    getCurrentLocation()
                }
            } else {
                GPS_STATUS == 0
                gpsStatus = "disable"
                /*  checkGpsStatus()*/
            }
        }
    }

    private fun setBottomSheetBehaviour() {
        val customerRequestDialog = binding.bsCustomerRequestDialog.bsCustomerRequestDialog
        bsCustomerRequestBehavior = BottomSheetBehavior.from(customerRequestDialog)
        bsCustomerRequestBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        val cancelResonDialog = binding.cancelResonDialog.bsCancelReason
        cancelReasonBehaviour = BottomSheetBehavior.from(cancelResonDialog)
        cancelReasonBehaviour!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @SuppressLint("MissingInflatedId")
    override fun setUpObserver() {
        /** logout api */
        mViewModel.getLogoutObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        binding.drawerLayout.closeDrawer(Gravity.LEFT)
                        showSuccessAlert(it.data.message)
                        Handler(Looper.myLooper()!!).postDelayed({
                            lifecycleScope.launch {
                                dataStore.deleteAllPreferences()
                                Constants.Login = ""
                                Constants.Register = ""
                                startActivity(
                                    Intent(
                                        this@MapsActivity, WelcomeActivity::class.java
                                    )
                                )
                                finishAffinity()
                            }
                        }, 2000)
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                            it.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(it.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                }
                            }
                        }
                    }
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    isInternetConnected = false
                }

                else -> {}
            }
        }
        /**changeDuty api*/
        mViewModel.getChangeDutyObserval().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        if (it.data.duty == "online") {
                            animateDriverStatus()
                            showSuccessAlert(getString(R.string.you_re_online))
                            connectSocket()
                            bindService()
                            // startService(Intent(applicationContext, FloatingViewService::class.java))
                            updateDriverLocationSocketCall()
                            binding.driverStatus.tvDriverStatus.text =
                                getString(R.string.you_re_online)
                        } else {
                            animateDriverStatus()
                            showFailAlert(getString(R.string.you_re_offline))
                            binding.driverStatus.tvDriverStatus.text =
                                getString(R.string.you_re_offline)
                            if (repeatJobUpdateDriverLocation != null) {
                                repeatJobUpdateDriverLocation!!.cancel()
                                repeatJobUpdateDriverLocation = null
                            }
                            if (isServiceRunningInForeground(LocationService::class.java)) {
                                unbindService(sc)
                            }
                            // stopService(Intent(applicationContext, FloatingViewService::class.java))
                            disconnectSocket()
                        }
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    if (it.code != 403) {
                        collectData(this@MapsActivity, it.message.toString()) {
                            binding.driverStatus.switchOnline.isChecked = false
                            binding.driverStatus.tvDriverStatus.setText(R.string.you_re_offline)
                            showFailAlert(it.message.toString())
                        }
                    } else {
                        lifecycleScope.launch {
                            if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                                it.message.let { message ->
                                    if (CommonFun.checkIsConnectionReset(it.code)) {
                                        getString(R.string.no_internet)
                                    } else {
                                        message
                                    }
                                }
                            }
                        }
                    }
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    binding.driverStatus.switchOnline.isChecked = false
                    binding.driverStatus.tvDriverStatus.setText(R.string.you_re_offline)
                    showFailAlert(getString(R.string.please_check_internet_connection))
                    isInternetConnected = false
                }

                else -> {}
            }
        }
        /**delete account api*/
        mViewModel.getDeleteAccountObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        lifecycleScope.launch {
                            dataStore.deleteAllPreferences()
                            Constants.Login = ""
                            Constants.Register = ""
                            startActivity(Intent(this@MapsActivity, SplashActivity::class.java))
                            finish()
                        }
                        SnackbarUtil.show(this, "delete account full", Snackbar.LENGTH_LONG)
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                            it.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(it.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    isInternetConnected = false
                }

                else -> {}
            }
        }
        /**cancel Trip api*/
        mViewModel.getCancelTripObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        binding.cancelResonDialog.cancelReson.visibility = View.GONE
                        binding.driverStatus.clStartRideRootStatus.visible()
                        socketDailog(this, it.message!!)

                        if (mMap != null) {
                            mMap!!.clear()
                            getCurrentLocation()
                        }
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                            it.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(it.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                    showFailAlert(it.message!!)
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    binding.root.showSnackBar(getString(R.string.no_internet_connection))
                }

                else -> {}
            }
        }
        /**completeTrip api*/
        mViewModel.getCompleteTripObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        Log.e("CompleteTripResponse: ", it.data.data.toString())
                        binding.bsCustomerRequestDialog.bsCustomerRequestDialog.gone()
                        binding.driverStatus.clStartRideRootStatus.visible()
                        if (mMap != null) {
                            mMap!!.clear()
                            getCurrentLocation()
                        }/*  var duration = formatMilliseconds(it?.data.data?.duration)*/
                        val (minutes, remainingSeconds) = convertSecondsStringToMinutesAndSeconds(it?.data.data?.duration!!)
                        val intent = Intent(this, CompleteTripActivity::class.java)
                        intent.putExtra("passangerImage", it.data?.data?.passengerPhoto)
                        intent.putExtra("bookingId", it.data?.data?.id)
                        intent.putExtra("passangerName", it?.data?.data?.passengerFullName)
                        intent.putExtra("passangerRating", it?.data?.data?.passengerRating)
                        intent.putExtra(
                            "tripTime", minutes.toString() + "m" + remainingSeconds.toString() + "s"
                        )
                        intent.putExtra("waitingTime", it?.data?.data?.waitingTime)
                        intent.putExtra("distance", it?.data?.data?.distance)
                        intent.putExtra("waitingFee", it?.data?.data?.waitingTimeCharge)
                        intent.putExtra("earning", it?.data?.data?.grandTotal)
                        intent.putExtra(
                            "savingWalletAmount", it?.data?.data?.customerSavingWalletAmount
                        )
                        startActivity(intent)
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                            it.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(it.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                    showFailAlert(it.message!!)
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    binding.root.showSnackBar(getString(R.string.no_internet_connection))
                }

                else -> {}
            }
        }/* mViewModel.getReviewRatingObserver().observe(this) {
             when (it.status) {
                 Resource.Status.SUCCESS -> {
                     myDialog.hide()

                     if (mMap != null) {
                         mMap!!.clear()
                         binding.bsCustomerRating.bsRating.gone()
                     }

                     initPopup(this, it.message!!) {
                         getCurrentLocation()
                     }
                 }

                 Resource.Status.ERROR -> {
                     myDialog.hide()
                     Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                     lifecycleScope.launch {
                         if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                             it.message?.let { message ->
                                 if (CommonFun.checkIsConnectionReset(it.code)) {
                                     getString(R.string.no_internet)
                                 } else {
                                     message
                                 }
                             }
                         }
                     }
                     showFailAlert(it.message!!)
                 }

                 Resource.Status.LOADING -> {
                     myDialog.show()
                     Log.d(ContentValues.TAG, "loading=>${it.message}")
                 }

                 Resource.Status.NO_INTERNET_CONNECTION -> {
                     myDialog.hide()
                     Log.d(ContentValues.TAG, "no internet=>${it.message}")
                     binding.root.showSnackBar(getString(R.string.no_internet_connection))
                 }

                 else -> {}
             }
         }*/
    }

    private fun animateDriverStatus() {
        val rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 1000 // Animation duration in milliseconds
        binding.driverStatus.tvDriverStatus.startAnimation(rotateAnimation)
    }

    private fun checkStatus() {
        if (ApiConstant.initGsonData != null) {
            if (ApiConstant.initGsonData!!.bookingInfo!!.driverDuty == "1") {
                firstRequestId = ""
                binding.driverStatus.switchOnline.isChecked = true
                bindService()
                updateDriverLocationSocketCall()
                binding.driverStatus.tvDriverStatus.text = getString(R.string.you_re_online)
            } else {
                binding.driverStatus.switchOnline.isChecked = false
                binding.driverStatus.tvDriverStatus.text = getString(R.string.you_re_offline)/*     if (mSocket!!.connected()) {
                     if (isServiceRunningInForeground(LocationService::class.java)) {
                         unbindService(sc)
                     }
                     disconnectSocket()
                 }*/
            }
            when (ApiConstant.initGsonData?.bookingInfo?.status) {
                // if booking status is in accepted state
                STATUS_REQUEST_ACCEPT -> {
                    FlagMapDirectionState = "AcceptState"
                    if (ApiConstant.initGsonData?.bookingInfo?.arrivedTime == null || ApiConstant.initGsonData?.bookingInfo?.arrivedTime == "") {
                        binding.bsCustomerRequestDialog.bsCustomerRequestDialog.visible()
                        binding.driverStatus.clStartRideRootStatus.gone()
                        bookingRequestSlide = 2
                        Log.e("killApp Accept gson Data: ", ApiConstant.initGsonData.toString())
                        if (ApiConstant.initGsonData != null) {
                            try {
                                setMapPolyLine(
                                    ApiConstant.initGsonData?.bookingInfo?.pickupLat.toString(),
                                    ApiConstant.initGsonData?.bookingInfo?.pickupLng.toString(),
                                    ApiConstant.initGsonData?.bookingInfo?.driverInfo?.lat.toString(),
                                    ApiConstant.initGsonData?.bookingInfo?.driverInfo?.lng.toString()
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "onAcceptBookingRequest = ${e.message}")
                            }
                        }
                        val dataBookingInfo = ApiConstant.initGsonData?.bookingInfo
                        val dataCustomerInfo = dataBookingInfo?.customerInfo
                        bookingId = ApiConstant.initGsonData?.bookingInfo?.id.toString()
                        driverName =
                            ApiConstant.initGsonData?.bookingInfo?.customerInfo?.firstName.toString()
                        driverId = ApiConstant.initGsonData?.bookingInfo?.driverInfo?.id.toString()
                        customerId = ApiConstant.initGsonData?.bookingInfo?.customerId.toString()
                        val img = ApiConstant.BASE_URL + dataCustomerInfo?.profileImage
                        setPreferenceArriveData(dataBookingInfo, dataCustomerInfo, img)
                        stopMusic()
                    } else if (ApiConstant.initGsonData?.bookingInfo?.arrivedTime!!.isNotEmpty()) {
                        FlagMapDirectionState = "StartTrip"
                        bookingId = ApiConstant.initGsonData?.bookingInfo?.id.toString()
                        dropOffLat = ApiConstant.initGsonData?.bookingInfo?.dropoffLat.toString()
                        dropOffLng = ApiConstant.initGsonData?.bookingInfo?.dropoffLng.toString()
                        binding.bsCustomerRequestDialog.bsCustomerRequestDialog.visible()
                        binding.bsCustomerRequestDialog.ivRedirectionToGoogleMap.gone()
                        PIN = ApiConstant.initGsonData?.bookingInfo?.pin.toString()
                        bookingRequestSlide = 3
                        setMapPolyLine(
                            ApiConstant.initGsonData?.bookingInfo?.pickupLat.toString(),
                            ApiConstant.initGsonData?.bookingInfo?.pickupLng.toString(),
                            ApiConstant.initGsonData?.bookingInfo?.driverInfo?.lat.toString(),
                            ApiConstant.initGsonData?.bookingInfo?.driverInfo?.lng.toString()
                        )
                        val dataBookingInfo = ApiConstant.initGsonData?.bookingInfo
                        val dataCustomerInfo = dataBookingInfo?.customerInfo
                        val img = ApiConstant.BASE_URL + dataCustomerInfo?.profileImage
                        dataBookingInfo?.let {
                            setPreferenceArriveSlideData(
                                it, dataCustomerInfo, img
                            )
                        }
                    } else {
                    }
                }

                STATUS_REQUEST_TRAVELING -> {
                    FlagMapDirectionState = "StartTrip"
                    binding.driverStatus.clStartRideRootStatus.gone()
                    bookingId = ApiConstant.initGsonData?.bookingInfo?.id.toString()
                    binding.bsCustomerRequestDialog.bsCustomerRequestDialog.visible()
                    PIN = ApiConstant.initGsonData?.bookingInfo?.pin.toString()
                    bookingRequestSlide = 4
                    val dataBookingInfo = ApiConstant.initGsonData?.bookingInfo
                    val dataCustomerInfo = dataBookingInfo?.customerInfo
                    val img = ApiConstant.BASE_URL + dataCustomerInfo?.profileImage
                    dataBookingInfo?.let {
                        setPreferenceCompleteSlideData(
                            it, dataCustomerInfo, img
                        )
                    }
                    completeTripDropOffLat = ApiConstant.initGsonData?.bookingInfo?.dropoffLat!!
                    completeTripDropOffLng = ApiConstant.initGsonData?.bookingInfo?.dropoffLng!!
                    dropOffLat = ApiConstant.initGsonData?.bookingInfo?.dropoffLat.toString()
                    dropOffLng = ApiConstant.initGsonData?.bookingInfo?.dropoffLng.toString()
                    try {
                        setMapPolyLine(
                            ApiConstant.initGsonData?.bookingInfo?.pickupLat,
                            ApiConstant.initGsonData?.bookingInfo?.pickupLng,
                            ApiConstant.initGsonData?.bookingInfo?.dropoffLat,
                            ApiConstant.initGsonData?.bookingInfo?.dropoffLng
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "onAcceptBookingRequest = ${e.message}")
                    }
                }
            }
        }
    }

    private fun setPreferenceCompleteSlideData(
        dataBookingInfo: BookingInfo,
        dataCustomerInfo: CustomerInfo?,
        img: String,
    ) {
        binding.bsCustomerRequestDialog.apply {
            tvTripAmount.text = "KES:~$".plus(dataBookingInfo!!.estimatedFare)
            tvCustomerName.text = dataCustomerInfo?.firstName
            tvCustomerRating.text = dataCustomerInfo!!.rating
            tvPaymentType.text = getString(R.string.pyment_type).plus(dataBookingInfo!!.paymentType)
            pickupData.tvPickupLocation.text = dataBookingInfo.pickupLocation
            pickupData.tvDropOffLocation.text =
                dataBookingInfo.dropoffLocation/*  userPreferences?.isShowForwardDialog = "true"*/
            setGlideImage(img, ivCustomerProfile, progressBar)
            tvTime.text = DateUtils.timeStampToUsaDate(
                dataBookingInfo?.bookingTime.toString(), USA_FORMAT_DATE
            )
            tvWaitingTime.gone()
            llRideAmountDetails.gone()
            llCustomerContact.gone()
            ivCancelRide.gone()
            slideText.setText(getString(R.string.complete_trip))
            setSliderTextAlpha(0)
        }
    }

    private fun setPreferenceArriveSlideData(
        dataBookingInfo: BookingInfo,
        dataCustomerInfo: CustomerInfo?,
        img: String,
    ) {
        binding.bsCustomerRequestDialog.apply {
            tvTripAmount.text = "KES:~$".plus(dataBookingInfo!!.estimatedFare)
            tvCustomerName.text = dataCustomerInfo?.firstName
            tvCustomerRating.text = dataCustomerInfo!!.rating
            tvPaymentType.text = getString(R.string.pyment_type).plus(dataBookingInfo!!.paymentType)
            pickupData.tvPickupLocation.text = dataBookingInfo.pickupLocation
            pickupData.tvDropOffLocation.text =
                dataBookingInfo.dropoffLocation/*  userPreferences?.isShowForwardDialog = "true"*/
            setGlideImage(img, ivCustomerProfile, progressBar)
            tvTime.text = DateUtils.timeStampToUsaDate(
                dataBookingInfo?.bookingTime.toString(), USA_FORMAT_DATE
            )
            tvWaitingTime.gone()
            llRideAmountDetails.gone()
            llCustomerContact.visible()
            ivCancelRide.gone()
            slideText.setText(getString(R.string.start_trip))
            setSliderTextAlpha(0)
        }
    }

    private fun setPreferenceArriveData(
        dataBookingInfo: BookingInfo?,
        dataCustomerInfo: CustomerInfo?,
        img: String,
    ) {
        binding.bsCustomerRequestDialog.apply {
            tvTripAmount.text = "KES:~$".plus(dataBookingInfo!!.estimatedFare)
            tvCustomerName.text = dataCustomerInfo?.firstName
            tvCustomerRating.text = dataCustomerInfo!!.rating
            tvPaymentType.text = getString(R.string.pyment_type).plus(dataBookingInfo!!.paymentType)
            pickupData.tvPickupLocation.text = dataBookingInfo.pickupLocation
            pickupData.tvDropOffLocation.text =
                dataBookingInfo.dropoffLocation/*  userPreferences?.isShowForwardDialog = "true"*/
            setGlideImage(img, ivCustomerProfile, progressBar)
            tvTime.text = DateUtils.timeStampToUsaDate(
                dataBookingInfo?.bookingTime.toString(), USA_FORMAT_DATE
            )
            tvWaitingTime.gone()
            llRideAmountDetails.gone()
            llCustomerContact.visible()
            ivCancelRide.gone()
            slideText.setText(getString(R.string.arrived))
            setSliderTextAlpha(0)
            /* if(carMarker==null){
                     setMapPolyLine(
                         ApiConstant.initGsonData!!.bookingInfo!!.pickupLat,
                         ApiConstant.initGsonData!!.bookingInfo!!.pickupLng,
                         ApiConstant.initGsonData!!.bookingInfo!!.driverInfo!!.lat.toString(),
                         ApiConstant.initGsonData!!.bookingInfo!!.driverInfo!!.lng.toString(),
                     )
                 }else{
                     carMarker!!.remove()
                 }*/
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, overlayPermissionRequestCode)
    }

    private fun requestPermissionBackgroundPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), PERMISSION_REQUEST_CODE_BACKGROUND_PERMISSION
                )
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), PERMISSION_REQUEST_CODE_BACKGROUND_PERMISSION
                )
            }
        }
    }

    private fun hasOverlayPermission(): Boolean {
        // Check if the device is running Android M (6.0) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the overlay permission is granted
            return Settings.canDrawOverlays(this)
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("call", "onRequestPermissionsResult() 11 = $requestCode")
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestPermissionBackgroundPermission()
                    Log.e("call", "onRequestPermissionsResult() 22 = ")
                } else {
                    PopupDialog.logout(
                        this, resources.getString(R.string.permission_denied_for_location)
                    ) {
                        Handler().postDelayed({
                            PopupDialog.logout(
                                this,
                                resources.getString(R.string.permission_denied_for_location_settings)
                            ) {
                                Handler().postDelayed({
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                                    intent.data = Uri.parse("package:$packageName")
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                    startActivityForResult(intent, 1000)
                                    finish()
                                }, 400)
                            }
                        }, 400)
                    }
                }
            }

            1000, PERMISSION_REQUEST_CODE_BACKGROUND_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("call", "onRequestPermissionsResult() 22 = ")
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    ) {
                        PopupDialog.logout(
                            this, resources.getString(R.string.permission_denied_for_location)
                        ) {}

                    } else {
                        PopupDialog.logout(
                            this, resources.getString(R.string.permission_denied_for_location)
                        ) {
                            Handler().postDelayed({
                                PopupDialog.logout(
                                    this,
                                    resources.getString(R.string.permission_denied_for_location_settings)
                                ) {
                                    Handler().postDelayed({
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                                        intent.data = Uri.parse("package:$packageName")
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                        startActivityForResult(intent, 1000)
                                        finish()
                                    }, 400)
                                }
                                //requestPermission();
                            }, 400)
                        }
                    }
                }
            }

            REQUEST_CALL_PHONE -> {}
        }
    }

    /** <--------------------------------------------------------------   driver duty on or off  ---------------------------------------------------------------> */
    private fun onSwitchChange() {
        mViewModel.changeDutyApiCall(driverId)
    }

    /** <--------------------------------------------------------------   Socket on  ---------------------------------------------------------------> */

    private val onConnect: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Log.e(TAG, "onConnect:")
        }
    }
    private val onUpdateDriverLatLong: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Log.e(TAG, "onUpdateDriverLatLong = ${args[0]}")
        }
    }
    private val onReceiveBookingRequest: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Log.e(TAG, "onForwardBookingRequest = ${args[0]}")
            bookingRequestSlide = 1

            if (args[0].toString()
                    .isNotEmpty() && firstRequestId.isEmpty() && firstRequestId != null && firstRequestId != gson.fromJson(
                    args[0].toString(), OnForwardBookingRequest::class.java
                ).bookingInfo?.id.toString()
            ) {

                try {
                    notifySound = MediaPlayer.create(this, R.raw.pick_and_go)
                    if (notifySound != null) {
                        notifySound?.start()/*    notifySound!!.isLooping = true*/
                    }
                } catch (e: Exception) {
                    if (e.localizedMessage != null) {
                        showFailAlert(e.localizedMessage.toString())
                        Log.e(TAG, "Exception notifySound: ${e.localizedMessage}")
                    }
                }
                val gsonData =
                    gson.fromJson(args[0].toString(), OnForwardBookingRequest::class.java)
                initGsonData = gson.fromJson(args[0].toString(), OnInitResponse::class.java)
                Log.e(TAG, "forwardBookinResponse: $gsonData")
                Log.e(TAG, "inResponse: $initGsonData")
                PASSANGER_NUMBER = gsonData.bookingInfo!!.customerInfo!!.mobileNo.toString()
                firstRequestId = gsonData.bookingInfo?.id.toString()
                driverName = gsonData?.bookingInfo?.customerInfo?.firstName.toString()
                bookingId = gsonData.bookingInfo?.id.toString()
                bookingType = gsonData.bookingInfo?.bookingType.toString()
                lifecycleScope.launch {
                    mViewModel.bookingId = bookingId
                    dataStore.setBookingId(bookingId)
                    dataStore.setBookingType(bookingType)
                }
                Log.e(TAG, "onForwardBookingRequest in: $firstRequestId")
                if (gsonData != null) {
                    binding.bsCustomerRequestDialog.bsCustomerRequestDialog.visible()
                    acceptRideTimerStart()
                    binding.driverStatus.clStartRideRootStatus.gone()
                    setBottomSheetCustomerRequestDialog(gsonData)
                }
            } else {
                if (firstRequestId != gson.fromJson(
                        args[0].toString(), OnForwardBookingRequest::class.java
                    ).bookingInfo?.id.toString()
                ) {
                    firstRequestId = gson.fromJson(
                        args[0].toString(), OnForwardBookingRequest::class.java
                    ).bookingInfo?.id.toString()

                    lifecycleScope.launch {
                        dataStore.setBookingId(firstRequestId)
                    }
                    val i = JSONObject()
                    if (firstRequestId != null) {
                        i.put(ApiConstant.driverId, driverId)
                        i.put(ApiConstant.bookingId, firstRequestId)
                        Log.e(
                            TAG,
                            "onForwardBookingRequest emit forwardBookingRequestToAnotherDriver :  $i"
                        )
                        firstRequestId = ""
                        binding.driverStatus.clStartRideRootStatus.visible()
                        mSocket?.emit(ApiConstant.forwardBookingRequestToAnotherDriver, i)

                    }
                    Log.e(TAG, "onForwardBookingRequest out: $firstRequestId")
                }

            }

        }
    }
    private val onAcceptBookingRequest: Emitter.Listener = Emitter.Listener { args ->
        Log.e(TAG, "onAcceptBookingRequest = ${args[0]}")
        runOnUiThread {
            Constants.IS_NOTIFICATION_ACCEPTED_TRIP = true
            firstRequestId = ""
            if (args[0].toString().isNotEmpty()) {
                stopMusic()
                acceptGsonData =
                    gson.fromJson(args[0].toString(), OnAcceptBookingResponse::class.java)
                bookingId = acceptGsonData?.bookingInfo?.id.toString()
                driverId = acceptGsonData?.bookingInfo?.driverInfo?.id.toString()
                customerId = acceptGsonData?.bookingInfo?.customerId.toString()
                if (acceptGsonData != null) {
                    try {
                        setMapPolyLine(
                            acceptGsonData?.bookingInfo?.pickupLat,
                            acceptGsonData?.bookingInfo?.pickupLng,
                            latitude.toString(),
                            longitude.toString()
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "onAcceptBookingRequest = ${e.message}")
                    }
                }
            }

        }
    }
    private val onCancelBookingBeforeAccept: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Toast.makeText(this, "cancel by customer", Toast.LENGTH_SHORT).show()
            if (mSocket!!.connected()) {
                try {
                    val json = JSONObject(args[0].toString())
                    Log.e(TAG, "onCancelBookingBeforeAccept() json:- $json")
                    stopMusic()
                    binding.driverStatus.clStartRideRootStatus.visible()
                    binding.bsCustomerRequestDialog.bsCustomerRequestDialog.gone()
                    showFailAlert(json.getString("message").trim { it <= ' ' })
                    showFailAlert(json.getString("message").trim { it <= ' ' })
                } catch (e: Exception) {
                    Log.e(TAG, "Exception : ${e.message}")
                }
            }
        }
    }
    private val onArrivedPickUpLocation: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Log.e(TAG, "onArrivedPickUpLocation = ${args[0]}")
            acceptGsonData = gson.fromJson(args[0].toString(), OnAcceptBookingResponse::class.java)
            PIN = acceptGsonData!!.bookingInfo.pin.toString()
            Log.e(TAG, "PIN----------------: $PIN")
            binding.bsCustomerRequestDialog.ivRedirectionToGoogleMap.gone()

        }
    }
    private val onStartTrip: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Log.e("onStartTrip", "onStartTrip = ${args[0]}")/*complete Trip Api param*/
            completeTripDropOffLat = acceptGsonData?.bookingInfo?.dropoffLat.toString()
            dropOffLat = acceptGsonData?.bookingInfo?.dropoffLat.toString()
            completeTripDropOffLng = acceptGsonData?.bookingInfo?.dropoffLng.toString()
            dropOffLng = acceptGsonData?.bookingInfo?.dropoffLng.toString()
            distance = acceptGsonData?.bookingInfo?.distanceFare.toString()
            acceptGsonData = gson.fromJson(args[0].toString(), OnAcceptBookingResponse::class.java)
            if (mMap != null) {
                mMap!!.clear()
                binding.bsCustomerRequestDialog.ivRedirectionToGoogleMap.visible()
                setMapPolyLine(
                    acceptGsonData!!.bookingInfo.pickupLat,
                    acceptGsonData!!.bookingInfo.pickupLng,
                    acceptGsonData!!.bookingInfo.dropoffLat,
                    acceptGsonData!!.bookingInfo.dropoffLng
                )
            }
        }
    }
    private val cancelTrip: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            firstRequestId = ""
            Log.e(TAG, "cancelTrip = ${args[0]}")
            if (mMap != null) {
                mMap!!.clear()
            }
            val gsonData = gson.fromJson(args[0].toString(), BaseResponse::class.java)
            showFailAlert(gsonData.message.toString())
            bookingRequestSlide = 1
            binding.bsCustomerRequestDialog.slideText.setText(getString(R.string.slide_to_accept))
            binding.driverStatus.clStartRideRootStatus.visible()
            binding.bsCustomerRequestDialog.bsCustomerRequestDialog.gone()
            binding.bsCustomerRequestDialog.apply {
                tvWaitingTime.visible()
                llRideAmountDetails.visible()
                llCustomerContact.gone()
                ivCancelRide.visible()
            }
            binding.bsCustomerVerifyOtp.bsVerifyCustomer.gone()
        }
    }
    private val onDisconnect: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread {
            Log.e(TAG, "onDisconnect = ${args[0]}")

        }
    }

    /** <--------------------------------------------------------------   *socket function  ---------------------------------------------------------------> */
    private fun startTripFun() {
        openVerifyCustomerOtp()
    }

    private fun openVerifyCustomerOtp() {
        binding.bsCustomerRequestDialog.bsCustomerRequestDialog.gone()
        binding.bsCustomerVerifyOtp.bsVerifyCustomer.visible()
        binding.bsCustomerVerifyOtp.apply {
            btSubmitOTP.setOnClickListener {
                if (etEnterCustomerOTP.text.toString() == PIN) {
                    binding.bsCustomerVerifyOtp.bsVerifyCustomer.gone()
                    binding.bsCustomerRequestDialog.bsCustomerRequestDialog.visible()
                    binding.bsCustomerRequestDialog.llCustomerContact.gone()
                    binding.bsCustomerRequestDialog.slideText.setText(getString(R.string.complete_trip))
                    etEnterCustomerOTP.text.clear()
                    setSliderTextAlpha(0)
                    emitStartTripSocketCall()
                } else {
                    showFailAlert(getString(R.string.invalid_pin))
                }
            }
        }
    }

    private fun cancelTripFun() {
        binding.bsCustomerRequestDialog.tvCancel.setOnClickListener {
            /* val viewDialogBottomSheet =
                 activity.layoutInflater.inflate(R.layout.cancel_reson_item, null)
             viewDialogBottomSheet.visibility = View.VISIBLE*/
            binding.cancelResonDialog.cancelReson.visibility = View.VISIBLE
            if (bookingId != null) {

                cancellationModel = arrayListOf()
                cancellationModel.add(
                    CancelModel(
                        false,
                        getString(R.string.driver_delay)
                    )
                )
                cancellationModel.add(
                    CancelModel(
                        false,
                        getString(R.string.changedtheplan)
                    )
                )
                cancellationModel.add(
                    CancelModel(
                        false,
                        getString(R.string.longpickuptime)
                    )
                )
                cancellationModel.add(
                    CancelModel(
                        false,
                        getString(R.string.nolonginterest)
                    )
                )

                cancellationModel.add(
                    CancelModel(
                        false,
                        getString(R.string.other)
                    )
                )

                val recyclerView = binding.cancelResonDialog.cancellationItemRecyclerView

                layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                cancellationAdapter =
                    CancelTripAdapter(this, cancellationModel) { position, cancelmodel ->

                        Log.d("list", "checkbox==>${cancelmodel.isCheck}")
                        Toast.makeText(
                            this@MapsActivity,
                            "${cancelmodel.textCancellationName}",
                            Toast.LENGTH_SHORT
                        ).show()
                        resone = cancelmodel.textCancellationName
                        var builder = StringBuilder()
                        builder.append(resone)
                        resone = builder.toString()
                        Toast.makeText(this@MapsActivity, "${resone}", Toast.LENGTH_SHORT).show()
                    }

                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = cancellationAdapter
                binding.cancelResonDialog.btnGoBack.setOnClickListener {
                    binding.cancelResonDialog.cancelReson.visibility = View.GONE
                }
                binding.cancelResonDialog.btnCancelRide.setOnClickListener {
                    mViewModel.cancelTripAPiCall(bookingId, resone)
                }
            }
        }
    }

    private fun repeatJobUpdateDriverLocation(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                try {
                    emitDriverLocationUpdate()

                } catch (e: Exception) {
                    Log.e("repeatJobUpdateDriverLocation Exception", "$e")
                }
                delay(timeInterval)
            }
        }
    }

    private fun setBottomSheetCustomerRequestDialog(gsonData: OnForwardBookingRequest) {
        binding.bsCustomerRequestDialog.apply {
            val dataBookingInfo = gsonData.bookingInfo
            val dataCustomerInfo = dataBookingInfo?.customerInfo
            val img = ApiConstant.Image_URL + dataCustomerInfo?.profileImage

            tvTripAmount.text = "KES:~$".plus(dataBookingInfo!!.estimatedFare)
            tvCustomerName.text = dataCustomerInfo?.firstName
            tvCustomerRating.text = dataCustomerInfo!!.rating
            tvPaymentType.text = getString(R.string.pyment_type).plus(dataBookingInfo!!.paymentType)
            pickupData.tvPickupLocation.text = dataBookingInfo.pickupLocation
            pickupData.tvDropOffLocation.text =
                dataBookingInfo.dropoffLocation/*  userPreferences?.isShowForwardDialog = "true"*/
            setGlideImage(img, ivCustomerProfile, progressBar)
            tvTime.text = DateUtils.timeStampToUsaDate(
                dataBookingInfo.bookingTime.toString(), USA_FORMAT_DATE
            )
        }

    }

    private fun setMapDirection(latitude: Double, longitude: Double) {
        val directionsBuilder =
            Uri.Builder().scheme("https").authority("www.google.com").appendPath("maps")
                .appendPath("dir").appendPath("").appendQueryParameter("api", "1")
                .appendQueryParameter(
                    "destination", "${latitude},${longitude}"
                )
        startActivity(Intent(Intent.ACTION_VIEW, directionsBuilder.build()))
    }

    private fun arriveDriverFunction() {
        binding.bsCustomerRequestDialog.slideText.setText(getString(R.string.start_trip))
        emitArrivedAtPickupLocation()
    }

    private fun acceptRideFunction() {
        binding.bsCustomerRequestDialog.apply {
            acceptRideTimer?.cancel()
            tvWaitingTime.gone()
            llRideAmountDetails.gone()
            llCustomerContact.visible()
            ivCancelRide.gone()
            slideText.setText(getString(R.string.arrived))
            emitAcceptBookingSocketCall()
            setSliderTextAlpha(0)
            binding.driverStatus.clStartRideRootStatus.gone()
        }
    }

    private fun stopMusic() {
        try {
            if (notifySound != null) {
                notifySound?.stop()
                notifySound = null
                notifySound?.release()
                notifySound?.isLooping = false
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "stopMusic: ${e.message}")
        }
    }

    private fun setSliderTextAlpha(progress: Int) {
        val remaining = 100 - progress
        val getAlpha = remaining.toFloat() / 100
        binding.bsCustomerRequestDialog.slideText.setAlpha(getAlpha)
    }

    private fun connectSocket() {
        try {
            val app: MyApplication = application as MyApplication
            mSocket = app.getMSocket()
            if (mSocket != null) {
                mSocket?.connect()
                mSocket?.on(Socket.EVENT_CONNECT, onConnect)
                mSocket?.on(ApiConstant.SOCKET_ON_UPDATE_DRIVER_LAT_LONG, onUpdateDriverLatLong)
                mSocket?.on(ApiConstant.SOCKET_ON_BOOKING_REQUEST_BOOK_NOW, onReceiveBookingRequest)
                mSocket?.on(
                    ApiConstant.SOCKET_ON_CANCEL_BOOKING_BEFORE_ACCEPT, onCancelBookingBeforeAccept
                )
                mSocket?.on(ApiConstant.SOCKET_ON_CANCEL_TRIP, cancelTrip)
                mSocket?.on(ApiConstant.SOCKET_ON_ACCEPT_BOOKING_BOOK_NOW, onAcceptBookingRequest)
                mSocket?.on(
                    ApiConstant.SOCKET_ON_ARRIVED_AT_PICKUP_LOCATION, onArrivedPickUpLocation
                )
                mSocket?.on(ApiConstant.SOCKET_ON_BOOKING_INFO_BOOK_NOW_START_TRIP, onStartTrip)
                mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                if (mSocket?.connected() == true) {
                    Log.e(TAG, "Socket Connected")
                    connectUserEmit()
                } else {
                    IO.Options().reconnection = true
                    Log.e(TAG, "Socket not connected, reconnecting....")
                }
            }, delay * 2.toLong())
        } catch (e: Exception) {
            Log.e(TAG, "connectSocket:Exception ${e.message}")
        }
    }

    private fun connectUserEmit() {
        if (mSocket?.connected() == true) {
            val j = JSONObject()
            j.put(ApiConstant.DRIVER_ID, driverId)
            Log.e(TAG, "emit connect user parameter $j")
            mSocket?.emit(ApiConstant.ConnectUser, j)
        }
    }

    private fun disconnectSocket() {
        try {
            if (mSocket?.connected() == true) {
                mSocket?.off(Socket.EVENT_CONNECT, onConnect)
                mSocket?.off(
                    ApiConstant.SOCKET_ON_UPDATE_DRIVER_LAT_LONG, onUpdateDriverLatLong
                )
                mSocket?.off(
                    ApiConstant.SOCKET_ON_BOOKING_REQUEST_BOOK_NOW, onReceiveBookingRequest
                )
                mSocket?.off(
                    ApiConstant.SOCKET_ON_CANCEL_BOOKING_BEFORE_ACCEPT, onCancelBookingBeforeAccept
                )
                mSocket?.off(
                    ApiConstant.SOCKET_ON_ACCEPT_BOOKING_BOOK_NOW, onAcceptBookingRequest
                )
                mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
            }

        } catch (e: Exception) {
            Log.e(TAG, "disconnectSocket: Exception ${e.message}")
        }
    }

    private fun acceptRideTimerStart() {
        if (acceptRideTimer != null) {
            acceptRideTimer = null
        }
        acceptRideTimer = object : CountDownTimer(35000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.bsCustomerRequestDialog.tvWaitingTime.text =
                    "00:" + (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                firstRequestId = ""
                binding.bsCustomerRequestDialog.bsCustomerRequestDialog.gone()
                try {
                    if (notifySound != null) {
                        notifySound!!.stop()
                        notifySound!!.release()
                        notifySound = null
                    }
                } catch (e: Exception) {
                    if (e.localizedMessage != null) {
                        e.localizedMessage?.let { showFailAlert(it) }
                        Log.e(TAG, "Exception notifySound: ${e.localizedMessage}")
                    }
                }
            }
        }
        (acceptRideTimer as CountDownTimer).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        bookingRequestSlide = 0
        try {
            if (repeatJobUpdateDriverLocation != null) {
                repeatJobUpdateDriverLocation?.cancel()

                repeatJobUpdateDriverLocation = null
            }
            notifySound!!.release()
            disconnectSocket()
        } catch (e: Exception) {
            Log.e(TAG, "onDestroy: ${e.message}")
        }
    }

    /** <--------------------------------------------------------------   socket emit function  ---------------------------------------------------------------> */
    private fun emitDriverLocationUpdate() {

        if (mSocket != null && mSocket!!.connected()) {
            val obj = JSONObject()
            try {
                obj.put(ApiConstant.SOCKET_PARAM_DRIVER_ID, driverId)
                obj.put(ApiConstant.SOCKET_PARAM_DRIVER_LAT, latitude)
                obj.put(ApiConstant.SOCKET_PARAM_DRIVER_LONG, longitude)
                obj.put(ApiConstant.DEVICE_TOKEN, deviceToken)
                mSocket?.emit(ApiConstant.SOCKET_EMIT_UPDATE_DRIVER_LAT_LONG, obj)
                Log.e(TAG, "emitDriverLocationUpdate: $obj")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateDriverLocationSocketCall() {
        repeatJobUpdateDriverLocation = if (repeatJobUpdateDriverLocation == null) {
            repeatJobUpdateDriverLocation(delay * 5.toLong())
        } else {
            repeatJobUpdateDriverLocation?.cancel()
            null
        }
    }

    fun forwardRequestToAnotherDriver(view: View) {
        emitForwardRequestToAnotherDriver()
        firstRequestId = ""
        acceptRideTimer?.cancel()
        binding.driverStatus.clStartRideRootStatus.visible()
        binding.bsCustomerRequestDialog.bsCustomerRequestDialog.gone()
        Log.d(TAG, "onCancelClick: ")
        try {
            if (notifySound != null) {
                notifySound!!.release()
                notifySound = null
            }
        } catch (e: Exception) {
            if (e.localizedMessage != null) {
                socketAleartDailog(this, e.localizedMessage.toString())
                Log.e(TAG, "Exception notifySound: ${e.localizedMessage.toString()}")
            }
        }
    }

    private fun emitForwardRequestToAnotherDriver() {
        val i = JSONObject()
        if (driverId != null && bookingId != null) {
            i.put(ApiConstant.driverId, driverId)
            i.put(ApiConstant.bookingId, bookingId)
            Log.e(TAG, "emit forwardBookingRequestToAnotherDriver :  $i")
            mSocket?.emit(ApiConstant.forwardBookingRequestToAnotherDriver, i)
            binding.driverStatus.clStartRideRootStatus.visible()
        }
    }

    private fun emitAcceptBookingSocketCall() {
        stopMusic()
        FlagMapDirectionState = "AcceptState"
        Handler(Looper.myLooper()!!).postDelayed({
            val i = JSONObject()
            if (userId != null && bookingId != null && bookingType != null) {
                i.put(ApiConstant.driverId, driverId)
                i.put(ApiConstant.bookingId, bookingId)
                i.put(ApiConstant.bookingType, bookingType)
                Log.e(TAG, "emit acceptBookingRequest :  $i")
                mSocket?.emit(ApiConstant.acceptBookingRequest, i)
            }
        }, 0)
    }

    private fun emitArrivedAtPickupLocation() {
        val i = JSONObject()
        if (bookingId != null) {
            i.put(ApiConstant.bookingId, bookingId)
            Log.e(TAG, "emit arrivedAtPickupLocation :  $i")
            mSocket?.emit(ApiConstant.arrivedAtPickupLocation, i)
        }
    }

    private fun emitStartTripSocketCall() {
        FlagMapDirectionState = "StartTrip"
        Handler(Looper.myLooper()!!).postDelayed({
            val i = JSONObject()
            if (bookingId != null) {
                i.put(ApiConstant.bookingId, bookingId)
                Log.e(TAG, "emit startTrip :  $i")
                mSocket?.emit(ApiConstant.startTrip, i)
            }
        }, 2000)
    }

    private fun bindService() {
        serviceRegistered = true
        val i = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(i)
        } else {
            startService(i)
        }/*  updateLocation()*/
        bindService(i, sc, BIND_AUTO_CREATE)
    }

    private fun unBindService() {
        this.stopService(Intent(this, LocationService::class.java))
        this.unbindService(sc)
        if (carMarker != null) {
            carMarker!!.remove()
        }
    }

    private val sc: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

    private fun updateLocation() {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                try {
                    animateMarker()
                    handlerUpdateLatLang.postDelayed(this, delay * 10.toLong())
                } catch (e: Exception) {
                    Log.d(TAG, "Exception == ${e.printStackTrace()}")
                }
            }
        }
        handlerUpdateLatLang.postDelayed(runnable, 0)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun animateMarker() {/* val option = MarkerOptions()
         val bitmapDraw: BitmapDrawable =
             resources.getDrawable(R.drawable.black_car) as BitmapDrawable

         val startMarker = Bitmap.createScaledBitmap(
             bitmapDraw.bitmap,
             70,
             100,
             true
         )*/
        if (latitude != 0.0 && longitude != 0.0) {
            val pos = LatLng(latitude, longitude)
            option.position(pos).anchor(0.5f, 0.5f)
            if (carMarker != null) {
                carMarker?.remove()
            }/*     carMarker = mMap!!.addMarker(
                     option.position(pos)
                         .icon(
                             getMarkerIconFromDrawable(
                                 ContextCompat.getDrawable(
                                     this,
                                     R.drawable.cab_dummy_car
                                 )!!
                             )
                         )
                 )*/
            latlang = LatLng(latitude, longitude)
            latitude = latlang!!.latitude
            longitude = latlang!!.longitude
            placeMarker(latlang!!)
        }

        if (handlerAnimateMarker == null) {
            handlerAnimateMarker = Handler(Looper.getMainLooper())
            handlerAnimateMarker?.postDelayed(object : Runnable {
                override fun run() {
                    handlerAnimateMarker?.postDelayed(this, delay * 3.toLong())
                }
            }, 0)
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun logout() {
        PopupDialog.logout(this, getString(R.string.are_yor_sure_you_want_to_logoutt)) {
            dataStore.getUserId.asLiveData().observe(this@MapsActivity) {
                mViewModel.callLogoutApi(it)
            }
        }
    }

    private fun activityLauncher() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    locationPermission = true
                    getLastLocation()
                    /*  getCurrentLocation()*/
                }
            }
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    private fun getLastLocation() {
        if (locationPermission) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener() { task ->
                    val location = task.result
                    Log.d("LOCATION", "location==>$location")

                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        currentLocationLat = location.latitude
                        currentLocationLng = location.longitude
                        latlang = LatLng(currentLocationLat!!, currentLocationLng!!)
                        latitude = location.latitude
                        longitude = location.longitude
                        mViewModel.lat = location.latitude.toString()
                        mViewModel.lng = location.longitude.toString()
                        animateMarker()
                        Log.d(ContentValues.TAG, "LAT---->${location.latitude}")
                        Log.d(ContentValues.TAG, "LNG---->${location.latitude}")
                    }
                }
            } else {

                activity.showMsgDialog(
                    activity.resources.getString(R.string.location_permissions_needed),
                    activity.resources.getString(R.string.open_setting),
                    { _, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }, "Cancel",
                    { _, _ ->

                    }
                )
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, locationCallback, Looper.myLooper()
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            this?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                val location = task.result
                Log.d("LOCATION", "location==>$location")
                if (location != null) {
                    currentLocationLat = location.latitude
                    currentLocationLng = location.longitude
                    latlang = LatLng(currentLocationLat!!, currentLocationLng!!)
                    latitude = location.latitude
                    longitude = location.longitude
                    mViewModel.lat = location.latitude.toString()
                    mViewModel.lng = location.longitude.toString()
                    animateMarker()
                    Log.d(ContentValues.TAG, "LAT---->${location.latitude}")
                    Log.d(ContentValues.TAG, "LNG---->${location.latitude}")

                }
            }
        }
    }

    private fun placeMarker(latlang: LatLng) {
        val markerOptions = MarkerOptions().position(latlang).title("$latlang").snippet("")
            .icon(bitmapFromVector(applicationContext, R.drawable.car_red))
        if (mMap != null) {
            carMarker = mMap!!.addMarker(
                option.position(latlang).icon(
                    getMarkerIconFromDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.car_red
                        )!!
                    )
                )
            )
            val cameraPosition = CameraPosition.Builder()
                .target(latlang) // Specify the center point of the map
                .bearing(90.0.toFloat()) // Set the rotation angle
                .zoom(16f) // Set the desired zoom level
                .build()
            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null)
            /* mMap!!.animateCamera(
                 CameraUpdateFactory.newLatLngZoom(
                     latlang, 19f
                 )
             )*/
        }
    }

    private fun getMarkerIconFromDrawable(
        drawable: Drawable,
        height: Int = 110,
        width: Int = 60,
    ): BitmapDescriptor? {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun bitmapFromVector(applicationContext: Context?, icCar: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(applicationContext!!, icCar)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun initMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setDrawerHeaderImage() {
        dataStore.getProfileImage.asLiveData().observe(this@MapsActivity) {
            Glide.with(this).load(ApiConstant.Image_URL.plus(it))
                .into(binding.drawableItems.headerImage)
        }
        dataStore.getFirstName.asLiveData().observe(this@MapsActivity) {
            binding.drawableItems.txtFirstName.text = it.toString()
        }
        dataStore.getLastName.asLiveData().observe(this@MapsActivity) {
            binding.drawableItems.txtLastName.text = it.toString()
        }
    }

    private fun setDrawerList() {
        drawableModel = arrayListOf()
        drawableModel.add(DrawerModel(R.drawable.ic_menu_history, "Trip History"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_earnings, "Earning"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_subscription, "Membership"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_wallet, "Wallet"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_wallet, "Saving Wallet"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_settings, "Settings"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_support, "Support"))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_privacy_policy, "legal"))

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val drawerAdapter = DrawerAdapter(this, drawableModel) { position ->

            when (position) {
                0 -> {
                    startActivity(Intent(this, MyTripActivity::class.java))
                }

                1 -> {
                    startActivity(Intent(this, EarningActivity::class.java))
                }

                2 -> {
                    startActivity(Intent(this, SubscriptionActivity::class.java))
                }

                3 -> {
                    startActivity(Intent(this, WalletActivity::class.java))
                }

                4 -> {
                    startActivity(Intent(this, SavingWalletActivity::class.java))
                }

                5 -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                }

                6 -> {
                    startActivity(Intent(this, SupportActivity::class.java))
                }

                7 -> {
                    startActivity(Intent(this, TermsOfService::class.java))
                }
            }
        }
        binding.drawableItems.drawerRecyclerView.layoutManager = layoutManager
        binding.drawableItems.drawerRecyclerView.adapter = drawerAdapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
    }

    fun notificationList(view: View) {
        startActivity(Intent(this, NotificationActivity::class.java))
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            finishAffinity()
        }
    }

    private fun isServiceRunningInForeground(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return service.foreground
            }
        }
        return false
    }

    fun sosClick(view: View) {
        if (SOS_NUMBER.isNotEmpty() && SOS_NUMBER != "") {
            callSos(applicationContext, SOS_NUMBER)
        } else {
            SnackbarUtil.show(this, "SOS not found", Snackbar.LENGTH_LONG)
        }
    }

    private fun callSos(context: Context, SosMobNumber: String) {
        Log.e(TAG, "callSos() SosMobNumber:- $SosMobNumber")
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$SosMobNumber"))
        startActivity(dialIntent)
    }

    private fun setMapPolyLine(
        pickupLat: String?,
        pickupLng: String?,
        dropoffLat: String?,
        dropoffLng: String?,
    ) {

        val startLocation = LatLng(pickupLat!!.toDouble(), pickupLng!!.toDouble())
        val destinationLocation = LatLng(
            dropoffLat!!.toDouble(), dropoffLng!!.toDouble()
        )/*mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))*/

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 25f)) ?: ""
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 25f)) ?: ""
        Log.d(ContentValues.TAG, "PICKLAT====>${startLocation}")
        Log.d(ContentValues.TAG, "DROPLat====>${destinationLocation}")

        val builder = LatLngBounds.Builder()
        builder.include(startLocation)
        builder.include(destinationLocation)

        startMarker.position(startLocation)
            .icon(resizeBitmap(R.drawable.car_red.toString(), 60, 110)?.let { it1 ->
                BitmapDescriptorFactory.fromBitmap(
                    it1
                )
            })
        mMap?.addMarker(startMarker.position(startLocation))

        destinationMarker.position(destinationLocation)
            .icon(resizeBitmap(R.drawable.live_marker_img.toString(), 210, 210)?.let { it2 ->
                BitmapDescriptorFactory.fromBitmap(
                    it2
                )
            })
        mMap?.addMarker(destinationMarker.position(destinationLocation))

        if (carMarker != null) {
            carMarker!!.remove()
            startMarker.visible(true)
            destinationMarker.visible(true)
        }
        val url = getDirectionURL(
            startLocation, destinationLocation, "AIzaSyAHoxA9mAOwiilUUwLfauECc7SrJSNwywM"
        )
        GetDirection(url).execute()
        Log.e("LOCATION", "curruntLatLng: $startLocation")

    }


    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret"
    }

    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(getColor(R.color.black))
            }
            mMap!!.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    @SuppressLint("DiscouragedApi")
    private fun resizeBitmap(drawableName: String?, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                drawableName, "drawable", packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    fun callToCustomer(view: View) {
        callSos(this, PASSANGER_NUMBER)
    }

    fun chatToCustomer(view: View) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("bookingId", bookingId)
        intent.putExtra("driverId", driverId)
        intent.putExtra("customerId", customerId)
        intent.putExtra("driverName", driverName)
        startActivity(intent)
    }

    fun formatMilliseconds(milliseconds: String?): String {

        val totalSeconds = milliseconds?.toLong()?.div(1000)
        val minutes = totalSeconds?.div(60)
        val seconds = totalSeconds?.rem(60)
        return "${minutes.toString()}:${seconds.toString()}"
    }

    fun convertSecondsStringToMinutesAndSeconds(secondsString: String): Pair<Long, Long> {
        val seconds = secondsString.toLong()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return Pair(minutes, remainingSeconds)
    }

    /*     fun OnTheWayTrip(bookingId: String?) {
    `             Toast.makeText(this, "OnTheWayTrip booking", Toast.LENGTH_SHORT).show()
         }

         fun StartTrip(bookingId: String?) {
             Toast.makeText(this, "StartTrip booking", Toast.LENGTH_SHORT).show()
         }

         fun CompleteTrip(bookingId: String?) {
             Toast.makeText(this, "Com0l0eteTrip booking", Toast.LENGTH_SHORT).show()
         }

         fun AcceptedTrip(bookingId: String?) {
             runOnUiThread(Runnable {
                 Handler().postDelayed({
                     if (!Constants.IS_SOCKET_ACCEPTED_TRIP) {
                         if (MyApplication().getCurrentActivity() === this@MapsActivity) {
                             Constants.IS_NOTIFICATION_ACCEPTED_TRIP = true
                             checkStatus()
                         }
                     }
                     Constants.IS_SOCKET_ACCEPTED_TRIP = false
                 }, 3000)
             })
         }

         fun ArrivedTrip(bookingId: String?) {
             Toast.makeText(this, "ArrivedTrip", Toast.LENGTH_SHORT).show()
         }

         fun CancelTrip(bookingId: String?) {
             Toast.makeText(this, "cancel booking", Toast.LENGTH_SHORT).show()
         }*/
}