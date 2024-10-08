package com.example.showfadriverletest.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseFragment
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.FragmentPastBinding
import com.example.showfadriverletest.fragment.viewmodel.PastViewModel
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.pasttrip.TempResponse
import com.example.showfadriverletest.ui.mytrip.adapter.PastTripAdapter
import com.example.showfadriverletest.ui.tripdetail.TripDetailActivity
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PastFragment : BaseFragment<FragmentPastBinding, PastViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_past
    override val bindingVariable: Int
        get() = BR.viewModel
    private var list: ArrayList<TempResponse.DataItem> = ArrayList()
    private lateinit var layoutManager: LayoutManager
    override fun setupObservable() {
        viewModel.setNavigator(this)
        viewModel.getPastResponseObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d(ContentValues.TAG, "on success=>${it.data!!.status}")
                    myDialog.hide()
                    if(it.data?.status==true){
                    if (it.data.data.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                    } else {
                        binding.tvNoDataFound.visibility = View.GONE
                        list.addAll(it.data.data)
                        val pastAdapter = PastTripAdapter(requireContext(), list) { position,timeStamp ->
                            lifecycleScope.launch {
                                dataStore.setRatingImage(it.data.data[position].customerProfileImage.toString())
                                dataStore.setCustomerFirstName(it.data.data[position].customerFirstName.toString())
                                dataStore.setCustomerLastName(it.data.data[position].customerLastName.toString())
                            }
                            val intent = Intent(requireContext(), TripDetailActivity::class.java)
                            intent.putExtra("pickUpAddress", it.data.data[position].pickupLocation)
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
                            intent.putExtra("serviceType", it.data.data[position].vehicleName)
                            requireContext().startActivity(intent)
                        }
                        binding.pastTripRv.layoutManager = layoutManager
                        binding.pastTripRv.adapter = pastAdapter
                    }}else{
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
                    binding.root.showSnackBar(getString(R.string.please_check_internet_connection))
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                }
                else -> {}
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore.getUserId.asLiveData().observe(requireActivity()) {
            viewModel.id = it.toString()
        }
        initRv()
    }
    private fun initRv() {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.callPastTripApi()
    }
}
