package com.example.showfadriverletest.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.hardware.GeomagneticField
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.showfadriverletest.R
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.ui.home.MapsActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*

class LocationService : Service(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var locationRequest: LocationRequest
    private var fusedLocationClient: FusedLocationProviderClient? = null

    companion object {
        private const val INTERVAL: Long = 1000 * 2
        private const val FASTEST_INTERVAL: Long = 2000
        private const val CHANNEL_ID = "LocationServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        initData()
        startLocationUpdates()
    }

    private fun initData() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        locationRequest.smallestDisplacement = 10f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient?.requestLocationUpdates(
            this.locationRequest,
            this.locationCallback, Looper.getMainLooper()
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private val notification: Notification
        get() {
            createNotificationChannel()
            val notificationIntent = Intent(this, MapsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(
                    resources.getString(com.example.showfadriverletest.R.string.app_name)
                        .plus(resources.getString(com.example.showfadriverletest.R.string.isRunning))
                    /*.plus(resources.getString(com.app.danfodriver.R.string.email_id))*/
                )
                .setContentText(resources.getString(com.example.showfadriverletest.R.string.tap_to_open))
                .setSmallIcon(com.example.showfadriverletest.R.drawable.notiback)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .build()
        }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, notification)
        }
        return START_STICKY
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation: Location? = locationResult.lastLocation

            Constants.latitude = currentLocation!!.latitude
            Constants.longitude = currentLocation.longitude
            Constants.location = currentLocation

            val field = GeomagneticField(
                currentLocation.latitude.toFloat(),
                currentLocation.longitude.toFloat(),
                currentLocation.altitude.toFloat(),
                System.currentTimeMillis()
            )

            // getDeclination returns degrees

            // getDeclination returns degrees
            Constants.mDeclination = field.declination
            //ConstantUtil.latitude = String.format("%.4f", currentLocation!!.latitude).toDouble()
            //ConstantUtil.longitude = String.format("%.4f", currentLocation.longitude).toDouble()

            Log.d("locationService current lat", Constants.latitude.toString())
            Log.d("locationService current longitude", Constants.longitude.toString())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        fusedLocationClient?.removeLocationUpdates(this.locationCallback)
        stopForeground(true)
        stopSelf()
        return super.onUnbind(intent)

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


}