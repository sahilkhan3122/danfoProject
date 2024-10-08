package com.example.showfadriverletest.ui.notification

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivityNotificationBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.notificationlist.NotificationResponse
import com.example.showfadriverletest.ui.notification.adapter.NotificationAdapter
import com.example.showfadriverletest.ui.notification.viewmodel.NotificationViewModel
import com.example.showfadriverletest.util.CommonFun.checkIsConnectionReset
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.gone
import com.example.showfadriverletest.util.visible
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationBinding, NotificationViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_notification
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var notificationAdapter: NotificationAdapter
    private var list: ArrayList<NotificationResponse.DataItem> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            onBackPressedDispatcher.addCallback(this@NotificationActivity, callback)
            lifecycleScope.launch {
                getUserId()
            }
            icHeader.tvClearNotification.visibility = View.VISIBLE
            icHeader.tvTitle.text = getString(R.string.notification)

            icHeader.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
            icHeader.tvClearNotification.setOnClickListener {
                mViewModel.notificationListClearApi(uid)
            }
        }
    }

    override fun setUpObserver() {
        /** notification list api */
        mViewModel.getNotificationObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        if (it.data!!.data!!.isEmpty() || it.data.data == null) {
                            binding.tvNoDataFound.visible()
                            binding.rvNotification.gone()
                        } else {
                            binding.tvNoDataFound.gone()
                            binding.rvNotification.visible()
                            list.addAll(it.data.data)
                            notificationAdapter = NotificationAdapter(this, list) { position ->
                                Toast.makeText(this, "position$position", Toast.LENGTH_SHORT).show()
                            }
                            binding.rvNotification.layoutManager = layoutManager
                            binding.rvNotification.adapter = notificationAdapter
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
                                if (checkIsConnectionReset(it.code)) {
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
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    binding.root.showSnackBar(R.string.no_internet.toString())
                    isInternetConnected = false
                }

                else -> {}
            }
        }

        /**clear notification api*/
        mViewModel.getNotificationClearObservable().observe(this) { it ->
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    it.data!!.let {
                        if (it.status) {
                            PopupDialog.logout(
                                this@NotificationActivity, it.message
                            ) {
                                list.clear()
                                binding.rvNotification.adapter?.notifyDataSetChanged()
                                binding.rvNotification.gone()
                                binding.tvNoDataFound.visible()
                            }
                        } else {
                            showFailAlert(it.message!!)
                        }
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
                    binding.tvNoDataFound.visibility = View.VISIBLE
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    binding.root.showSnackBar(R.string.no_internet.toString())
                    isInternetConnected = false
                }

                else -> {
                    Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun getUserId() {
        val job = lifecycleScope.launch {
            dataStore.getUserId.asLiveData().observe(this@NotificationActivity) {
                uid = it.toString()
            }
            delay(100L)
        }
        job.join()
        initRv()
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            finish()
        }
    }

    private fun initRv() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mViewModel.notificationListApi(uid)
    }

}