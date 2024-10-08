package com.example.showfadriverletest.ui.subscription

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivitySubscriptionBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.subscription.SubscriptionResponse
import com.example.showfadriverletest.ui.subscription.adapter.SubscriptionAdapter
import com.example.showfadriverletest.ui.subscription.viewmodel.SubscriptionViewModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.CommonFun.checkIsConnectionReset
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.view.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionActivity : BaseActivity<ActivitySubscriptionBinding, SubscriptionViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_subscription
    override val bindingVariable: Int
        get() = BR.viewModel

    private var list: ArrayList<SubscriptionResponse.SubscriptionItem> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var subscriptionId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activityHeader.tvTitle.text = getString(R.string.subscription)
        binding.activityHeader.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        initRv()
    }

    override fun setUpObserver() {
        mViewModel.getSubscriptionObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "on success=>${it.data!!.status}")
                    Log.d(ContentValues.TAG, "on success=>${it.data.message}")

                    if (it.data?.status == true) {

                        if (it.data.subscription!!.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                        } else {
                            list.addAll(it.data.subscription)
                            val subscriptionAdapter = SubscriptionAdapter(this, list) { pos ->
                                subscriptionId = list[pos].id.toString()
                                openCloseSelectPaymentType(subscriptionId)
                            }
                            binding.rvSubscription.layoutManager = layoutManager
                            binding.rvSubscription.adapter = subscriptionAdapter
                            myDialog.hide()
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
                                if (checkIsConnectionReset(it.code)) {
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
                    binding.root.showSnackBar(getString(R.string.please_check_internet_connection))
                    isInternetConnected = false
                }
                else -> {}
            }
        }


        mViewModel.getSubscriptionPurchaseObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if(it.data!!.status){
                        PopupDialog.logout(this, it.message.toString()) {}
                    }else{
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    PopupDialog.logout(
                        this@SubscriptionActivity,
                        it.message.toString()
                    ) {}
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.no_internet_connection))
                }

                else -> {
                }

            }
        }
    }

    private fun openCloseSelectPaymentType(subscriptionId: String) {
        val bsAddMoneyDialog = BottomSheetDialog(this, R.style.TransparentDialog)
        bsAddMoneyDialog.setContentView(R.layout.dialog_select_payment)

        val llWallet = bsAddMoneyDialog.findViewById<LinearLayout>(R.id.llWallet)
        val llMPesa = bsAddMoneyDialog.findViewById<LinearLayout>(R.id.llMPesa)
        val llJambopay = bsAddMoneyDialog.findViewById<LinearLayout>(R.id.llJambopay)
        val ivCloseAddMoneyDialog =
            bsAddMoneyDialog.findViewById<ImageView>(R.id.ivCloseAddMoneyDialog)

        llWallet!!.visibility = View.VISIBLE
        llMPesa!!.visibility = View.VISIBLE
        llJambopay!!.visibility = View.GONE

        llWallet!!.setOnClickListener { view: View? ->
            bsAddMoneyDialog.dismiss()
            mViewModel.purchaseSubscription(subscriptionId, Constants.PAYMENT_METHOD_WALLET)
        }

        llMPesa!!.setOnClickListener { view: View? ->
            bsAddMoneyDialog.dismiss()
            mViewModel.purchaseSubscription(subscriptionId, Constants.PAYMENT_METHOD_M_PESA)
        }

        llJambopay!!.setOnClickListener { view: View? ->
            bsAddMoneyDialog.dismiss()
            /*   callSubscriptionActive(subscriptionId, Constants.PAYMENT_METHOD_JAMBOPAY)*/
        }

        ivCloseAddMoneyDialog!!.setOnClickListener { view: View? -> bsAddMoneyDialog.dismiss() }

        bsAddMoneyDialog.show()
    }

    private fun initRv() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dataStore.getUserId.asLiveData().observe(this@SubscriptionActivity) {
            mViewModel.id = it.toString()
        }
        mViewModel.subscriptionApi()
    }
}