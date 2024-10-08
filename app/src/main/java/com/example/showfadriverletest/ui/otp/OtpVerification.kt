package com.example.showfadriverletest.ui.otp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.component.showSuccessAlert
import com.example.showfadriverletest.databinding.ActivityOtpVarifyBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.ui.home.MapsActivity
import com.example.showfadriverletest.ui.otp.viewmodel.OtpNavigator
import com.example.showfadriverletest.ui.otp.viewmodel.ResendOtpViewModel
import com.example.showfadriverletest.ui.register.registerprofile.RegisterProfile
import com.example.showfadriverletest.util.PopupDialog.showDialogOfLocationPolicy
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OtpVerification : BaseActivity<ActivityOtpVarifyBinding, ResendOtpViewModel>(), OtpNavigator {
    override val layoutId: Int
        get() = R.layout.activity_otp_varify
    override val bindingVariable: Int
        get() = BR.viewModel

    var otp = ""
    var isTimer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTimer()
        onBackPressedDispatcher.addCallback(this, callback)
        binding.apply {
            dataStore.getOtp.asLiveData().observe(this@OtpVerification) {
                otp = it
                edtOtp.setText(otp)
            }

            dataStore.getMobileNo.asLiveData().observe(this@OtpVerification) {
                mViewModel.no = it
            }

            textviewLogin.setOnClickListener {
                if (validationOtp()) {
                    if (Constants.Login == "Login") {
                        startActivity(Intent(this@OtpVerification, MapsActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@OtpVerification, RegisterProfile::class.java))
                        finish()
                    }

                }
            }
        }
    }

    private fun startTimer() {
        object : CountDownTimer(60000, 1000) {
            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (isTimer) {
                    binding.textSignup.visibility = View.GONE
                    binding.txtTimer.visibility = View.VISIBLE
                    binding.txtTimer.text = "00:" + millisUntilFinished / 1000

                } else {
                    binding.txtTimer.text = "00:" + millisUntilFinished / 1000
                }
            }

            override fun onFinish() {
                binding.textSignup.visibility = View.VISIBLE
                binding.txtTimer.visibility = View.GONE
            }
        }.start()
    }

    override fun setUpObserver() {
        mViewModel.setNavigator(this)
        mViewModel.getResendOtpObservable().observe(this) {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        otp = it.data!!.otp.toString()
                        showSuccessAlert(it.message.toString())
                        binding.edtOtp.setText(otp)
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    showDialogOfLocationPolicy(this@OtpVerification, it.message.toString())
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.e("LOADING-----------------", "LOADING::${it.status}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.please_check_internet_connection))
                }

                else -> {
                }
            }
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            lifecycleScope.launch {
                Constants.Login = "LoginDenied"
                dataStore.setUserId("null")
            }
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            finish()
        }
    }

    override fun resend() {
        isTimer = true
        startTimer()
        mViewModel.callOtpResendApi()
    }

    private fun validationOtp(): Boolean {
        if (binding.edtOtp.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.please_enter_otp))
            return false
        } else if (binding.edtOtp.text.length < 6) {
            binding.root.showSnackBar(getString(R.string.please_enter_valid_otp))

            return false
        } else if (binding.edtOtp.text.toString() != otp) {
            binding.root.showSnackBar(getString(R.string.otp_doesn_t_match))
            return false
        }
        return true
    }


}