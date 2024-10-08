package com.example.showfadriverletest.ui.invoice

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.databinding.ActivityInvoiceBinding
import com.example.showfadriverletest.ui.invoice.viewmodel.InvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceActivity : BaseActivity<ActivityInvoiceBinding, InvoiceViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_invoice
    override val bindingVariable: Int
        get() = BR.viewModel

    var pickUpAddress: String? = null
    var dropOffAddress: String? = null
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            header.tvTitle.text = getString(R.string.trip_details)
            header.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }

        if (intent != null) {
            pickUpAddress = intent.getStringExtra("pickUpAddress")
            dropOffAddress = intent.getStringExtra("dropOffAddress")
            pickLat = intent.getStringExtra("pickupLat").toString()
            pickLng = intent.getStringExtra("pickupLag").toString()
            dropLat = intent.getStringExtra("dropLat").toString()
            dropLng = intent.getStringExtra("dropLng").toString()
            tripTime = intent.getStringExtra("tripTime").toString()
            kilometer = intent.getStringExtra("kilometer").toString()
            id = intent.getStringExtra("id").toString()
            totalPay = intent.getStringExtra("totalPay").toString()
            saving = intent.getStringExtra("savingWallet").toString()
            serviceType = intent.getStringExtra("serviceType").toString()

            setInvoiceData()
        }
    }

    override fun setUpObserver() {}

    private fun setInvoiceData() {
        binding.apply {
            pickupDropOff.tvPickupLocation.text = pickUpAddress
            pickupDropOff.tvDropOffLocation.text = dropOffAddress
            trpiDetail.tvTripDuration.text = tripTime
            trpiDetail.tvGrandTotal.text = kilometer
            tvTripTime.text = tripTime
            tvSavingWalletAmount.text = "$$saving"
            tvTotalPayable.text = "$$totalPay"
            tvServiceType.text = serviceType
        }
    }
}