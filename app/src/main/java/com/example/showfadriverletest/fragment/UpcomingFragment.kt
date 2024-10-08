package com.example.showfadriverletest.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseFragment
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.FragmentUpcomingBinding
import com.example.showfadriverletest.fragment.viewmodel.UpcomingTripViewModel
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.featuretrip.FeatureTripResponse
import com.example.showfadriverletest.ui.mytrip.adapter.UpcomingTripAdapter
import com.example.showfadriverletest.ui.tripdetail.TripDetailActivity
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UpcomingFragment : BaseFragment<FragmentUpcomingBinding, UpcomingTripViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_upcoming
    override val bindingVariable: Int
        get() = BR.viewModel
    private var list: ArrayList<FeatureTripResponse.DataItem> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var uid: String

    override fun setupObservable() {
        viewModel.setNavigator(this)
        viewModel.getFeatureResponseObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        if (it.data?.data!!.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                        } else {
                            binding.tvNoDataFound.visibility = View.GONE
                            list.addAll(it.data.data)
                            val upcomingAdapter =
                                UpcomingTripAdapter(requireContext(), list) { position, timeStamp ->

                                    lifecycleScope.launch {
                                        dataStore.setRatingImage(it.data.data[position].customerProfileImage.toString())
                                        dataStore.setCustomerFirstName(it.data.data[position].customerFirstName.toString())
                                        dataStore.setCustomerLastName(it.data.data[position].customerLastName.toString())
                                    }

                                    val intent =
                                        Intent(requireContext(), TripDetailActivity::class.java)
                                    intent.putExtra(
                                        "pickUpAddress",
                                        it.data.data[position].pickupLocation
                                    )
                                    intent.putExtra(
                                        "dropOffAddress",
                                        it.data.data[position].dropoffLocation
                                    )
                                    intent.putExtra("pickupLat", it.data.data[position].pickupLat)
                                    intent.putExtra("pickupLag", it.data.data[position].pickupLng)
                                    intent.putExtra("dropLat", it.data.data[position].dropoffLat)
                                    intent.putExtra("dropLng", it.data.data[position].dropoffLng)
                                    intent.putExtra("tripTime", timeStamp)
                                    intent.putExtra("kilometer", it.data.data[position].distance)
                                    intent.putExtra("id", it.data.data[position].id)
                                    intent.putExtra(
                                        "savingWallet",
                                        it.data.data[position].savingWalletAmount
                                    )
                                    intent.putExtra("totalPay", it.data.data[position].grandTotal)
                                    intent.putExtra(
                                        "serviceType",
                                        it.data.data[position].vehicleName
                                    )
                                    requireContext().startActivity(intent)
                                }
                            binding.upcomingRv.layoutManager = layoutManager
                            binding.upcomingRv.adapter = upcomingAdapter
                        }
                    } else {
                        activity.showFailAlert(it.message!!)
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
                        } else {
                            Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                        }
                    }

                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.please_check_internet_connection))
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                }

                else -> {}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            getUserId()
        }
    }

    private suspend fun getUserId() {
        val job = lifecycleScope.launch {
            dataStore.getUserId.asLiveData().observe(requireActivity()) {
                uid = it.toString()
            }
            delay(100L)
        }
        job.join()
        initRv()
    }

    private fun initRv() {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.callFeatureTripApi(uid)
    }
}