package com.example.showfadriverletest.ui.cardselect

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.databinding.ActivityCardSelectionBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.SnackbarUtil
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardSelection : BaseActivity<ActivityCardSelectionBinding, CardSelectionViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_card_selection
    override val bindingVariable: Int
        get() = BR.viewModel
    var amount = ""
    var title = ""
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            amount = intent.getStringExtra("amount").toString()
            title = intent.getStringExtra("title").toString()
            id = intent.getStringExtra("id").toString()
            Toast.makeText(this, amount + "$title", Toast.LENGTH_SHORT).show()
        }
        binding.llMPesa.setOnClickListener {
            mViewModel.addMoney(amount, id)
        }
        binding.header.tvTitle.text = getString(R.string.payment_methodd)
        binding.header.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }

    override fun setUpObserver() {
        mViewModel.getAddMoneyObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        PopupDialog.logout(this, it.message.toString()) {}
                    } else {
                        it.message?.let { it1 -> SnackbarUtil.show(this, it1) }
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    PopupDialog.logout(this, it.message.toString()) {}
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

                else -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}