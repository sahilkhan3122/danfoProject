package com.example.showfadriverletest.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.showfadriverletest.ui.login.viewmodel.LoginViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class LocationUtil(var context: Context,var viewModel: ViewModel) {
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    // Initializing other items
    // from layout file
    var PERMISSION_ID = 44
    lateinit var homeVM: LoginViewModel// if permissions aren't available,


    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        this.homeVM = homeVM
    }
    // request for permissions
// Toast.makeText(context, "LOCATION : " + location.getLatitude() + "   " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    /*latitudeTextView.setText(location.getLatitude() + "");
longitTextView.setText(location.getLongitude() + "");*/// getting last
    // location from
    // FusedLocationClient
    // object
// check if location is enabled
    // check if permissions are given
    @get:SuppressLint("MissingPermission")
    val lastLocation: Unit
        get() {
            try {// check if permissions are given
                if (checkPermissions()) {

                    // check if location is enabled
                    if (isLocationEnabled) {

                        // getting last
                        // location from
                        // FusedLocationClient
                        // object
                        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                            val location = task.result
                            if (location == null) {
                                requestNewLocationData()
                            } else {
                                Log.e(
                                    "MAP",
                                    "Lat : " + location.latitude + "  lng : " + location.longitude
                                )

                                // Toast.makeText(context, "LOCATION : " + location.getLatitude() + "   " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                                homeVM.setLatlngLiveData(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                                /*latitudeTextView.setText(location.getLatitude() + "");
                                  longitTextView.setText(location.getLongitude() + "");*/
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please turn on" + " your location...",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    }
                } else {
                    // if permissions aren't available,
                    // request for permissions
                    requestPermissions()
                }
            } catch (e: Exception) {
                Log.e(TAG, "$")
            }
        }


    fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            if (mLastLocation != null) {
                Log.e(
                    "MAP",
                    "From Result - Lat : " + mLastLocation.latitude + "  lng : " + mLastLocation.longitude
                )
            }

            //  Toast.makeText(context, "From LocationResult : " + mLastLocation.getLatitude() + "  " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            if (mLastLocation != null) {
                homeVM.setLatlngLiveData(LatLng(mLastLocation.latitude, mLastLocation.longitude))
            }
            /* latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");*/
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:

        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            (context as Activity), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

  /*   fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }*/

    // method to check
    // if location is enabled
    private val isLocationEnabled: Boolean
        private get() {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }



}
