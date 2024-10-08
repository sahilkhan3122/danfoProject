@file:Suppress("DEPRECATION")

package com.example.showfadriverletest.ui.wallet

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivityWalletBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.wallet.WalletHistoryResponse
import com.example.showfadriverletest.ui.cardselect.CardSelection
import com.example.showfadriverletest.ui.wallet.adapter.WalletAdapter
import com.example.showfadriverletest.ui.wallet.viewmodel.WalletViewModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.view.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Objects

@AndroidEntryPoint
class WalletActivity : BaseActivity<ActivityWalletBinding, WalletViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_wallet
    override val bindingVariable: Int
        get() = BR.viewModel
    var isLoading = false
    var pageData: Int = 1
    private var list: ArrayList<WalletHistoryResponse.DataItem> = ArrayList()
    private lateinit var layoutManager: LayoutManager
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var uid: String
    var isScroll = false
    var currentItem = 0
    var totalItem = 0
    var scrollItem = 0
    var lastItemPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            getUserId()
        }
        binding.header.tvTitle.text = getString(R.string.wallet)
        binding.header.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }

    private suspend fun getUserId() {
        val job = lifecycleScope.launch {
            dataStore.getUserId.asLiveData().observe(this@WalletActivity) {
                uid = it
                mViewModel.uid = uid
            }
            delay(100L)
        }
        job.join()

        initRv(pageData)
    }

    @SuppressLint("SetTextI18n")
    override fun setUpObserver() {
        mViewModel.setNavigator(this)
        mViewModel.getwalletHistoryObservable().observe(this) {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        if (it.data!!.data!!.isEmpty()) {
                            binding.rvTransferHistory.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                        } else {
                            Log.d(
                                ContentValues.TAG,
                                "on success=>${it.data!!.status}"
                            )
                            binding.apply {
                                tvWalletBallence.text = "KES :${it.data.walletBalance.toString()}"
                                tvWalletUpdate.text =
                                    "Last Updated:${it.data.lastWalletUpdateTime.toString()}"
                            }
                            list.addAll(it.data.data!!)
                            walletAdapter = WalletAdapter(this@WalletActivity, list) { position ->
                                Toast.makeText(
                                    this@WalletActivity, "position$position", Toast.LENGTH_SHORT
                                ).show()
                            }
                            binding.rvTransferHistory.layoutManager = layoutManager
                            binding.rvTransferHistory.adapter = walletAdapter
                            binding.rvTransferHistory.layoutManager?.scrollToPosition(totalItem)
                            binding.rvTransferHistory.addOnScrollListener(object :
                                RecyclerView.OnScrollListener() {
                                override fun onScrolled(
                                    recyclerView: RecyclerView,
                                    dx: Int,
                                    dy: Int,
                                ) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    // This method will be called when the RecyclerView is scrolled.
                                    // You can implement your custom logic here.
                                }

                                override fun onScrollStateChanged(
                                    recyclerView: RecyclerView,
                                    newState: Int,
                                ) {
                                    super.onScrollStateChanged(recyclerView, newState)
                                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                        isScroll = true
                                        val layoutManager =
                                            recyclerView.layoutManager as LinearLayoutManager
                                        currentItem = layoutManager.childCount
                                        totalItem = layoutManager.itemCount
                                        scrollItem = layoutManager.findFirstVisibleItemPosition()
                                        lastItemPos = layoutManager.findLastVisibleItemPosition()
                                        if (isScroll && (currentItem + scrollItem == totalItem)) {
                                            isScroll = false
                                            myDialog.show()
                                            Handler(Looper.myLooper()!!).postDelayed({
                                                    if (it.data.data.isEmpty()) {
                                                        list.clear()
                                                        walletAdapter.notifyDataSetChanged()
                                                        showFailAlert("No Data Found")
                                                        binding.rvTransferHistory.visibility =
                                                            View.GONE
                                                        binding.tvNoDataFound.visibility =
                                                            View.VISIBLE
                                                    } else {
                                                        pageData += 1
                                                        myDialog.hide()
                                                        initRv(pageData)
                                                        walletAdapter.notifyDataSetChanged()
                                                    }
                                            }, 1000)
                                        }
                                    }
                                }
                            })
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

    private fun initRv(pageData: Int) {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mViewModel.callWalletHistoryApi(pageData, uid)
    }

    fun walletMenu(view: View) {
        when (view.id) {
            R.id.ivAddMoney -> {
                openCloseAddMoney()
            }

            R.id.ivSendMoney -> {
                openCloseSendMoney()
            }

            R.id.ivWithdraw -> {
                Toast.makeText(this, "withdraw", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun openCloseSendMoney() {
        var bsSendMoneyDialog = BottomSheetDialog(this@WalletActivity, R.style.TransparentDialog)
        bsSendMoneyDialog.setContentView(R.layout.dialog_send_money)
        var tv_bt_send_money = bsSendMoneyDialog.findViewById<TextView>(R.id.tv_bt_send_money)
        var etEnterAmount = bsSendMoneyDialog.findViewById<EditText>(R.id.etEnterAmount)
        var etPhoneNumber = bsSendMoneyDialog.findViewById<EditText>(R.id.etPhoneNumber)
        var ivCloseSendMoneyDialog =
            bsSendMoneyDialog.findViewById<ImageView>(R.id.ivCloseSendMoneyDialog)
        bsSendMoneyDialog.show()
    }

    private fun openCloseAddMoney() {
        val bsAddMoneyDialog = BottomSheetDialog(this, R.style.TransparentDialog)
        bsAddMoneyDialog.setContentView(R.layout.dialog_add_money)
        var tv_bt_add_money = bsAddMoneyDialog.findViewById<TextView>(R.id.tv_bt_add_money)
        var ivCloseAddMoneyDialog =
            bsAddMoneyDialog.findViewById<ImageView>(R.id.ivCloseAddMoneyDialog)
        var etEnterAddAmount = bsAddMoneyDialog.findViewById<EditText>(R.id.etEnterAddAmount)

        ivCloseAddMoneyDialog!!.setOnClickListener(View.OnClickListener { view: View? -> bsAddMoneyDialog.dismiss() })

        tv_bt_add_money!!.setOnClickListener(View.OnClickListener { view: View? ->
            var amount = etEnterAddAmount!!.getText().toString()
            if (TextUtils.isEmpty(amount)) {
                Toast.makeText(this, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT)
                    .show()
            } else {
                startActivity(
                    Intent(this, CardSelection::class.java).putExtra(
                        "amount", amount
                    ).putExtra("title", getString(R.string.proceed_to_pay)).putExtra("id", uid)
                )
                bsAddMoneyDialog.dismiss()
            }
        })
        bsAddMoneyDialog.show()
    }
}