package com.example.showfadriverletest.ui.support

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivitySupportBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.ui.support.viewmodel.SupportViewModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SupportActivity : BaseActivity<ActivitySupportBinding, SupportViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_support
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            header.tvTitle.text = getString(R.string.supporttt)
            header.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }

            tvSave.setOnClickListener {
                if (binding.subject.text.isEmpty()) {
                    binding.root.showSnackBar(getString(R.string.please_enter_subject))
                } else if (binding.etIssue.text.isEmpty()) {
                    binding.root.showSnackBar(getString(R.string.please_enter_descriptionn))
                } else {
                    dataStore.getUserId.asLiveData().observe(this@SupportActivity) {
                        viewModel!!.id = it.toString()
                    }
                    mViewModel.callSupportApi()
                }
            }
        }
    }

    override fun setUpObserver() {
        mViewModel.getSupportResponseObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {

                        it.message?.let { it1 ->
                            PopupDialog.logout(this, it1) {
                                binding.subject.text.clear()
                                binding.etIssue.text.clear()
                                onBackPressedDispatcher.onBackPressed()
                            }
                        }
                        /* Log.d(ContentValues.TAG, "on success=>${it.data!!.status}")
                         val builder = AlertDialog.Builder(this).create()
                         val view = layoutInflater.inflate(R.layout.my_dialog_class, null)
                         val button = view.findViewById<TextView>(R.id.Ok)
                         val message = view.findViewById<TextView>(R.id.message)
                         message.text = it.data.message
                         builder.setView(view)
                         button.setOnClickListener {
                             binding.subject.text.clear()
                             binding.etIssue.text.clear()
                             onBackPressedDispatcher.onBackPressed()
                             overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                             builder.dismiss()
                         }
                         builder.setCanceledOnTouchOutside(false)
                         builder.show()*/
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
}