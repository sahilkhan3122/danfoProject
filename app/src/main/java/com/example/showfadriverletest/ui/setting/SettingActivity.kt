package com.example.showfadriverletest.ui.setting

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivitySettingBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.ui.edit.editProfile.EditProfileActivity
import com.example.showfadriverletest.ui.edit.editVehicledetaile.EditVehicleDetailActivity
import com.example.showfadriverletest.ui.edit.editdocument.EditVehicleDocumentActivity
import com.example.showfadriverletest.ui.home.HomeViewModel
import com.example.showfadriverletest.ui.splash.SplashActivity
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.SnackbarUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding, HomeViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_setting
    override val bindingVariable: Int
        get() = BR.viewModel
    var lastname = ""
    private lateinit var enableOverlayLauncher: ActivityResultLauncher<Intent>
    private val DRAW_OVER_OTHER_APP_PERMISSION = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableOverlayLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { it -> }
        if (!Settings.canDrawOverlays(this)) {
            binding.llEnableOverlay.visibility = View.VISIBLE
        } else {
            binding.llEnableOverlay.visibility = View.GONE
        }

        binding.header.tvTitle.text = getString(R.string.settingss)
        binding.header.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        binding.llEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.llDeleteAccount.setOnClickListener {
            PopupDialog.logout(this@SettingActivity,"Are sure,You want to delete your account"){
                dataStore.getUserId.asLiveData().observe(this@SettingActivity) {
                    mViewModel.deleteAccountApiCall(it)
                }
            }
        }

    }

    override fun setUpObserver() {
        mViewModel.getDeleteAccountObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        lifecycleScope.launch {
                            dataStore.deleteAllPreferences()
                            Constants.Login = ""
                            Constants.Register = ""
                            startActivity(Intent(this@SettingActivity, SplashActivity::class.java))
                            finish()
                        }
                        SnackbarUtil.show(this, getString(R.string.delete_account_successful), Snackbar.LENGTH_LONG)
                    } else {
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    isInternetConnected = false
                }

                else -> {}
            }
        }
    }

    private fun setUserProfile() {
        binding.apply {

            dataStore.getLastName.asLiveData().observe(this@SettingActivity) {
                lastname = it.toString()
            }

            dataStore.getFirstName.asLiveData().observe(this@SettingActivity) {
                tvUserName.text = "$it $lastname"
            }

            dataStore.getMobileNo.asLiveData().observe(this@SettingActivity) {
                tvUserPhone.text = it.toString()
            }
            dataStore.getEmail.asLiveData().observe(this@SettingActivity) {
                tvUserEmail.text = it.toString()
            }
            dataStore.getProfileImage.asLiveData().observe(this@SettingActivity) {
                Glide.with(this@SettingActivity).load(ApiConstant.Image_URL.plus(it))
                    .into(binding.ivProfile)
            }
        }
    }

    fun vehicleDetail(view: View) {
        startActivity(Intent(this, EditVehicleDetailActivity::class.java))
    }

    fun vehicleDocument(view: View) {
        startActivity(Intent(this, EditVehicleDocumentActivity::class.java))
    }

    fun enableOverlay(view: View) {
        askForSystemOverlayPermission()
    }

    private fun askForSystemOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                    "package:$packageName"
                )
            )
            intent.putExtra("reqCode", DRAW_OVER_OTHER_APP_PERMISSION)
            enableOverlayLauncher.launch(intent)
        }
    }

    override fun onResume() {
        setUserProfile()
        super.onResume()
    }
}
