package com.example.showfadriverletest.ui.tripdetail

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.databinding.ActivityTripDetailBinding
import com.example.showfadriverletest.ui.invoice.InvoiceActivity
import com.example.showfadriverletest.ui.support.SupportActivity
import com.example.showfadriverletest.ui.tripdetail.viewmodel.TripDetailModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.mapparam.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request


@AndroidEntryPoint
class TripDetailActivity : BaseActivity<ActivityTripDetailBinding, TripDetailModel>(),
    OnMapReadyCallback {
    override val layoutId: Int
        get() = R.layout.activity_trip_detail
    override val bindingVariable: Int
        get() = BR.viewModel

    private var startLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    var map: GoogleMap? = null
    var destinationMarker = MarkerOptions()
    private var startMarker = MarkerOptions()
    var zoomed = true
    var pickUpAddress: String? = null
    var dropUpAddress: String? = null
    var pickLat = ""
    var pickLng = ""
    var dropLat = ""
    var dropLng = ""
    var tripTime = ""
    var kilometer = ""
    var id = ""
    var saving = ""
    var totalPay = ""
    var serviceType = ""
    var customerName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map123) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setImage()
        binding.include.tvTitle.text = getString(R.string.trip_details)
        binding.include.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        binding.tvNeedHelp.setOnClickListener {
            startActivity(Intent(this@TripDetailActivity, SupportActivity::class.java))
        }

        if (intent != null) {
            pickUpAddress = intent.getStringExtra("pickUpAddress")
            dropUpAddress = intent.getStringExtra("dropOffAddress")
            pickLat = intent.getStringExtra("pickupLat").toString()
            pickLng = intent.getStringExtra("pickupLag").toString()
            dropLat = intent.getStringExtra("dropLat").toString()
            dropLng = intent.getStringExtra("dropLng").toString()
            tripTime = intent.getStringExtra("tripTime").toString()
            kilometer = intent.getStringExtra("kilometer").toString()
            id = intent.getStringExtra("id").toString()
            saving = intent.getStringExtra("savingWallet").toString()
            totalPay = intent.getStringExtra("totalPay").toString()
            serviceType = intent.getStringExtra("serviceType").toString()

            startLocation = LatLng(pickLat!!.toDouble(), pickLng!!.toDouble())
            destinationLocation = LatLng(dropLat!!.toDouble(), dropLng!!.toDouble())

            Log.d(TAG, "data--->$pickUpAddress")
            Log.d(TAG, "data--->$dropUpAddress")
            Log.d(TAG, "data--->$pickLat")
            Log.d(TAG, "data--->$pickLng")
            Log.d(TAG, "data--->$dropLat")
            Log.d(TAG, "data--->$dropLng")

            binding.pickup.tvPickupLocation.text = pickUpAddress
            binding.pickup.tvDropOffLocation.text = dropUpAddress
            binding.tripPrice.tvTripDuration.text = tripTime
            binding.tripPrice.tvGrandTotal.text = kilometer
            binding.tvTripId.text = "ID :$id"

            binding.llInvoice.setOnClickListener {
                val intent = Intent(this, InvoiceActivity::class.java)
                intent.putExtra("pickUpAddress", pickUpAddress)
                intent.putExtra("dropOffAddress", dropUpAddress)
                intent.putExtra("pickupLat", pickLat)
                intent.putExtra("pickupLag", pickLng)
                intent.putExtra("dropLat", dropLat)
                intent.putExtra("dropLng", dropLng)
                intent.putExtra("tripTime", tripTime)
                intent.putExtra("kilometer", kilometer)
                intent.putExtra("id", id)
                intent.putExtra("savingWallet", saving)
                intent.putExtra("totalPay", totalPay)
                intent.putExtra("serviceType", serviceType)
                startActivity(intent)
            }
        }
    }

    override fun setUpObserver() {}

    fun resizeBitmap(drawableName: String?, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                drawableName, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    private fun setImage() {
        dataStore.getRatingImage.asLiveData().observe(this@TripDetailActivity) {
            Glide.with(this@TripDetailActivity).load(ApiConstant.Image_URL.plus(it))
                .into(binding.ivProfile)
        }

        dataStore.getCustomerFirstName.asLiveData().observe(this@TripDetailActivity) {
            customerName = it.toString()
        }
        dataStore.getCustomerLastName.asLiveData().observe(this@TripDetailActivity) {
            binding.tvUserName.text =customerName + it.toString()
        }

    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        setMapPolyLine()
    }

    private fun setMapPolyLine() {
        map!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(this.startLocation!!, 16f))
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(this.destinationLocation!!, 16f))
        Log.d(TAG, "PICKLAT====>${this.startLocation}")
        Log.d(TAG, "DROPLat====>${this.destinationLocation}")

        val builder = LatLngBounds.Builder()
        builder.include(startLocation!!)
        builder.include(destinationLocation!!)

        startMarker.position(startLocation!!).icon(resizeBitmap(R.drawable.live_marker_img.toString(), 100, 100)?.let { it1 ->
                BitmapDescriptorFactory.fromBitmap(
                    it1
                )
            })
        map?.addMarker(startMarker.position(startLocation!!))

        destinationMarker.position(destinationLocation!!).icon(resizeBitmap(R.drawable.img_marker_car.toString(), 72, 110)?.let { it1 ->
                BitmapDescriptorFactory.fromBitmap(
                    it1
                )
            })
        map?.addMarker(destinationMarker.position(destinationLocation!!))

        val url = startLocation?.let { destinationLocation?.let { it1 ->
            getDirectionURL(
                    it,
                    it1,
                    "AIzaSyAHoxA9mAOwiilUUwLfauECc7SrJSNwywM"
                )
            }
        }
        Log.e("LOCATION", "curruntLatLng: $startLocation")
        if (url != null) {
            GetDirection(url).execute()
        }
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
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
                lineoption.color(getColor(R.color.theme_color))
            }
            map!!.addPolyline(lineoption)
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
}