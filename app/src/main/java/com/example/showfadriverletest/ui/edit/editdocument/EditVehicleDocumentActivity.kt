package com.example.showfadriverletest.ui.edit.editdocument

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivityEditVehicleDocumentBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.service.TAG
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.file.PathUtil
import com.example.showfadriverletest.util.pdfpath.PDFFile
import com.example.showfadriverletest.view.showSnackBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditVehicleDocumentActivity :
    BaseActivity<ActivityEditVehicleDocumentBinding, EditVehicleDocumentViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_edit_vehicle_document
    override val bindingVariable: Int
        get() = BR.viewModel

    var flag = ""
    private var flagInner = ""
    private var FLAG_CAMERA = "camera"
    private var FLAG_GALLARY = "gallary"
    private var FLAG_DOC = "document"
    private var requestCode = 0
    private var imageUriCamera: Uri? = null
    private var documentUri: Uri? = null
    private var imagePathUriGallary = ""
    private var CAMERA_PERMISSION_CODE = 101
    private val GALLERY_PERMISSION_CODE = 1
    private val PICK_IMAGE_REQUEST_CODE = 2
    private val DOCUMENT_PERMISSION_CODE = 12
    private val PICK_DOCUMENT_REQUEST_CODE = 3
    private var nationalId = ""
    private var tvNationalIdDocUri = ""
    private var tvDriverLicenceDocUri = ""
    private var tvNTSABudgeCertificateDocUri = ""
    private var tvPoliceReceiptDocUri = ""
    private var tvVehicleLogBookDocUri = ""
    private var tvNTSAInspectionDocUri = ""
    private var tvPSVComprehensiveInsuranceDocUri = ""
    private var flagNationalId = true
    private var flagDriverLicenceDoc = true
    private var flagNTSABudgeCertificateDoc = true
    private var flagPoliceReceiptDoc = true
    private var flagVehicleLogBookDoc = true
    private var flagNTSAInspectionDoc = true
    private var flagPSVComprehensiveInsuranceDoc = true
    private var flagDriverLicenceExpDate = true
    private var flagPSVComprehensiveInsuranceExpDate = true
    private var flagNSTAInspectionExpDate = true
    private var flagNTSABudgeCertificateExpDate = true
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private var READ_MEDIA_CODE = 102
    private var FLAG_NATIONAL_ID_DOC = "tvNationalIdDoc"
    private var FLAG_DRIVER_LICENCE_DOC = "tvDriverLicenceDoc"
    private var FLAG_NTSA_BUDGE_DOC = "tvNTSABudgeCertificateDoc"
    private var FLAG_POLICE_CARTI_DOC = "tvPoliceReceiptDoc"
    private var FLAG_VEHICLE_LOGBOOK_DOC = "tvVehicleLogBookDoc"
    private var FLAG_NTSA_INSPECTION_DOC = "tvNTSAInspectionDoc"
    private var FLAG_PSV_COMPRIHENSIVE_DOC = "tvPSVComprehensiveInsuranceDoc"
    private lateinit var cameraGallaryDocumentLauncher: ActivityResultLauncher<Intent>
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocationLat: Double? = null
    private var currentLocationLng: Double? = null
    private var isPostNotificationGranted = false
    private var LOCATION_CODE = 1


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            isPostNotificationGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_CODE
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Constants.selectInsideImage = false
        Constants.selectBackImage = false
        Constants.selectFrontImage = false
        Constants.selectLeftImage = false
        Constants.selectRightImage = false
        onBackPressedDispatcher.addCallback(this@EditVehicleDocumentActivity, callback)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        activityLauncher()
        lifecycleScope.launch {
            lifecycleScope.launch {
                getData()
            }
        }
        checkPostNotification()
        dataStore.getProfileImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            mViewModel.profileImage = it.toUri()
        }

        binding.activityHeader.tvTitle.text = getString(R.string.attach_documents)

        binding.activityHeader.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        cameraGallaryDocumentLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {

                if (requestCode == CAMERA_PERMISSION_CODE && it.resultCode == Activity.RESULT_OK) {
                    Log.d("data_information", "File name $imageUriCamera")
                    mViewModel.docFile = imageUriCamera
                    mViewModel.uploadDocsApi()

                } else if (requestCode == PICK_IMAGE_REQUEST_CODE && it.resultCode == RESULT_OK) {

                    val imageUriGallary = it.data?.data

                    imagePathUriGallary = PathUtil.getPath(this, imageUriGallary!!).toString()
                    val uri = Uri.fromFile(File(imagePathUriGallary))
                    mViewModel.docFile = uri
                    mViewModel.uploadDocsApi()
                    Log.d("data_information", "File name $uri")

                } else if (requestCode == PICK_DOCUMENT_REQUEST_CODE && it.resultCode == RESULT_OK) {
                    //documentUri = data?.data
                    try {
                        Log.d("data_information uri", it.data?.data.toString())
                        val uri: Uri = it.data?.data!!
                        val documentName = PDFFile(this).copyFile(uri, "")
                        documentUri = Uri.fromFile(File(documentName))
                        mViewModel.docFile = documentUri
                        mViewModel.uploadDocsApi()
                        Log.d("data_information", "File name --$documentName")
                    } catch (e: Exception) {
                        Log.d("data_information", "Error $e")
                    }
                }
            }

        binding.tvSave.setOnClickListener {

            nationalId = binding.etNationalID.text.toString()

            if (isPostNotificationGranted) {
                showSetting()
            } else if (flagNationalId) {
                binding.root.showSnackBar(getString(R.string.plz_upload_nationalid_doc))
            } else if (nationalId.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.please_enter_national_id))
            } else if (flagDriverLicenceDoc) {
                binding.root.showSnackBar(getString(R.string.plz_upload_driver_licence))
            } else if (flagDriverLicenceExpDate) {
                binding.root.showSnackBar(getString(R.string.plz_enter_driver_licence_expiry_date))
            } else if (flagNTSABudgeCertificateDoc) {
                binding.root.showSnackBar(getString(R.string.plz_upload_ntsabudge_certificate_doc))
            } else if (flagNTSABudgeCertificateExpDate) {
                binding.root.showSnackBar(getString(R.string.plz_enter_ntsabudgecertificate_expiry_date))
            } else if (flagPoliceReceiptDoc) {
                binding.root.showSnackBar(getString(R.string.plz_upload_good_conduct_certificate))
            } else if (flagVehicleLogBookDoc) {
                binding.root.showSnackBar(getString(R.string.plz_upload_vehicle_log_book_doc))
            } else if (flagNTSAInspectionDoc) {
                binding.root.showSnackBar(getString(R.string.plz_upload_ntsainspection_doc))
            } else if (flagNSTAInspectionExpDate) {
                binding.root.showSnackBar(getString(R.string.plz_enter_driver_ntsainspection_expirydate))
            } else if (flagPSVComprehensiveInsuranceDoc) {
                binding.root.showSnackBar(getString(R.string.plz_upload_vehicle_insurance_doc))
            } else if (flagPSVComprehensiveInsuranceExpDate) {
                binding.root.showSnackBar(getString(R.string.plz_enter_driver_comprehensiveinsurance_expiry_date))
            } else {
                mViewModel.nationalId = binding.etNationalID.text.toString()

                getCurrentLocation()
                mViewModel.editVehicleDocApi()
            }
        }
    }

    override fun setUpObserver() {

        /**Upload Doc Api*/
        mViewModel.getUploadDocsObservableDoc().observe(this) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    resource.data!!.let {
                        if (it.status) {
                            when (flag) {
                                FLAG_NATIONAL_ID_DOC -> {
                                    when (flagInner) {
                                        FLAG_CAMERA -> {
                                            tvNationalIdDocUri = it.url.toString()
                                            mViewModel.nationalIdIImage = it.url.toString()
                                            flagNationalId = false
                                            binding.tvNationalIdDoc.text =
                                                getString(R.string.national_id_doc_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvNationalIdDocUri = it.url.toString()
                                            Log.d("image---------", tvNationalIdDocUri)
                                            mViewModel.nationalIdIImage = it.url.toString()
                                            flagNationalId = false
                                            binding.tvNationalIdDoc.text =
                                                getString(R.string.national_id_doc_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvNationalIdDocUri = it.url.toString()
                                            mViewModel.nationalIdIImage = it.url.toString()
                                            flagNationalId = false
                                            binding.tvNationalIdDoc.text =
                                                getString(R.string.national_id_doc_uploaded)
                                        }

                                    }
                                }

                                FLAG_DRIVER_LICENCE_DOC -> {

                                    when (flagInner) {

                                        FLAG_CAMERA -> {
                                            tvDriverLicenceDocUri = it.url.toString()
                                            mViewModel.tvDriverLicenceDoc = it.url.toString()
                                            flagDriverLicenceDoc = false
                                            binding.tvDriverLicenceDoc.text =
                                                getString(R.string.driver_licence_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvDriverLicenceDocUri = it.url.toString()
                                            Log.d("image---------", tvDriverLicenceDocUri)
                                            mViewModel.tvDriverLicenceDoc = it.url.toString()
                                            flagDriverLicenceDoc = false
                                            binding.tvDriverLicenceDoc.text =
                                                getString(R.string.driver_licence_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvDriverLicenceDocUri = it.url.toString()
                                            mViewModel.tvDriverLicenceDoc = it.url.toString()
                                            flagDriverLicenceDoc = false
                                            binding.tvDriverLicenceDoc.text =
                                                getString(R.string.driver_licence_uploaded)
                                        }

                                    }
                                }


                                FLAG_NTSA_BUDGE_DOC -> {
                                    when (flagInner) {

                                        FLAG_CAMERA -> {
                                            tvNTSABudgeCertificateDocUri = it.url.toString()
                                            mViewModel.tvNTSABudgeCertificateDoc = it.url.toString()
                                            flagNTSABudgeCertificateDoc = false
                                            binding.tvNTSABudgeCertificateDoc.text =
                                                getString(R.string.ntsabudge_doc_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvNTSABudgeCertificateDocUri = it.url.toString()
                                            Log.d("image---------", tvNTSABudgeCertificateDocUri)
                                            mViewModel.tvNTSABudgeCertificateDoc = it.url.toString()
                                            flagNTSABudgeCertificateDoc = false
                                            binding.tvNTSABudgeCertificateDoc.text =
                                                getString(R.string.ntsabudge_doc_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvNTSABudgeCertificateDocUri = it.url.toString()
                                            mViewModel.tvNTSABudgeCertificateDoc = it.url.toString()
                                            flagNTSABudgeCertificateDoc = false
                                            binding.tvNtsaBudgeCertificate.text =
                                                getString(R.string.ntsabudge_doc_uploaded)
                                        }

                                    }
                                }

                                FLAG_POLICE_CARTI_DOC -> {
                                    when (flagInner) {

                                        FLAG_CAMERA -> {
                                            tvPoliceReceiptDocUri = it.url.toString()
                                            mViewModel.tvPoliceReceiptDoc = it.url.toString()
                                            flagPoliceReceiptDoc = false
                                            binding.tvPoliceReceiptDoc.text =
                                                getString(R.string.policereceiptdoc_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvPoliceReceiptDocUri = it.url.toString()
                                            Log.d("image---------", tvPoliceReceiptDocUri)
                                            mViewModel.tvPoliceReceiptDoc = it.url.toString()
                                            flagPoliceReceiptDoc = false
                                            binding.tvPoliceReceiptDoc.text =
                                                getString(R.string.policereceiptdoc_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvPoliceReceiptDocUri = it.url.toString()
                                            mViewModel.tvPoliceReceiptDoc = it.url.toString()
                                            flagPoliceReceiptDoc = false
                                            binding.tvPoliceReceiptDoc.text =
                                                getString(R.string.policereceiptdoc_uploaded)
                                        }
                                    }
                                }


                                FLAG_VEHICLE_LOGBOOK_DOC -> {
                                    when (flagInner) {

                                        FLAG_CAMERA -> {
                                            tvVehicleLogBookDocUri = it.url.toString()
                                            mViewModel.tvVehicleLogBookDoc = it.url.toString()
                                            flagVehicleLogBookDoc = false
                                            binding.tvVehicleLogBookDoc.text =
                                                getString(R.string.vehicledoc_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvVehicleLogBookDocUri = it.url.toString()
                                            Log.d("image---------", tvVehicleLogBookDocUri)
                                            mViewModel.tvVehicleLogBookDoc = it.url.toString()
                                            flagVehicleLogBookDoc = false
                                            binding.tvVehicleLogBookDoc.text =
                                                getString(R.string.vehicledoc_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvVehicleLogBookDocUri = it.url.toString()
                                            mViewModel.tvVehicleLogBookDoc = it.url.toString()
                                            flagVehicleLogBookDoc = false
                                            binding.tvVehicleLogBookDoc.text =
                                                getString(R.string.vehicledoc_uploaded)
                                        }
                                    }
                                }


                                FLAG_NTSA_INSPECTION_DOC -> {

                                    when (flagInner) {

                                        FLAG_CAMERA -> {
                                            tvNTSAInspectionDocUri = it.url.toString()
                                            mViewModel.tvNTSAInspectionDoc = it.url.toString()
                                            flagNTSAInspectionDoc = false
                                            binding.tvNTSAInspectionDoc.text =
                                                getString(R.string.ntsa_insoection_doc_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvNTSAInspectionDocUri = it.url.toString()
                                            Log.d("image---------", tvNTSAInspectionDocUri)
                                            mViewModel.tvNTSAInspectionDoc = it.url.toString()
                                            flagNTSAInspectionDoc = false
                                            binding.tvNTSAInspectionDoc.text =
                                                getString(R.string.ntsa_insoection_doc_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvNTSAInspectionDocUri = it.url.toString()
                                            mViewModel.tvNTSAInspectionDoc = it.url.toString()
                                            flagNTSAInspectionDoc = false
                                            binding.tvNTSAInspectionDoc.text =
                                                getString(R.string.ntsa_insoection_doc_uploaded)
                                        }
                                    }
                                }

                                FLAG_PSV_COMPRIHENSIVE_DOC -> {
                                    when (flagInner) {

                                        FLAG_CAMERA -> {
                                            tvPSVComprehensiveInsuranceDocUri = it.url.toString()
                                            mViewModel.tvPSVComprehensiveInsuranceDoc =
                                                it.url.toString()
                                            flagPSVComprehensiveInsuranceDoc = false
                                            binding.tvPSVComprehensiveInsuranceDoc.text =
                                                getString(R.string.psvcomprehensiveinsurancedoc_uploaded)
                                        }

                                        FLAG_GALLARY -> {
                                            tvPSVComprehensiveInsuranceDocUri = it.url.toString()
                                            Log.d(
                                                "image---------",
                                                tvPSVComprehensiveInsuranceDocUri
                                            )
                                            mViewModel.tvPSVComprehensiveInsuranceDoc =
                                                it.url.toString()
                                            flagPSVComprehensiveInsuranceDoc = false
                                            binding.tvPSVComprehensiveInsuranceDoc.text =
                                                getString(R.string.psvcomprehensiveinsurancedoc_uploaded)
                                        }

                                        FLAG_DOC -> {
                                            tvPSVComprehensiveInsuranceDocUri = it.url.toString()
                                            mViewModel.tvPSVComprehensiveInsuranceDoc =
                                                it.url.toString()
                                            flagPSVComprehensiveInsuranceDoc = false
                                            binding.tvPSVComprehensiveInsuranceDoc.text =
                                                getString(R.string.psvcomprehensiveinsurancedoc_uploaded)
                                        }

                                    }
                                }
                            }
                        } else {
                            showFailAlert(it.message!!)
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(
                                resource.code,
                                getString(R.string.session_expire)
                            )
                        ) {
                            resource.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(resource.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                    myDialog.hide()
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.d(ContentValues.TAG, "loading=>${resource.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.no_internet_connection))
                }

                else -> {}
            }
        }
        /**editDocApi*/
        mViewModel.getEditVehicleDocObservable().observe(this) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    resource.data!!.let {
                        if (it.status) {
                            myDialog.hide()
                            lifecycleScope.launch {
                                dataStore.setNationalIdNo(it.data!!.driverDocs!!.nationalIdNumber.toString())
                                dataStore.setNationalIdImage(it.data.driverDocs!!.nationalIdImage!!.toString())
                                dataStore.setDriverLicenceImage(it.data.driverDocs.driverLicenceImage!!.toString())
                                dataStore.setDriverLicenceExpiry(it.data.driverDocs.driverLicenceExpDate!!.toString())
                                dataStore.setNTSABudgeExpiry(it.data.driverDocs.psvBadgeExpDate!!.toString())
                                dataStore.setNtsaBadgeImage(it.data.driverDocs.psvBadgeImage!!.toString())
                                dataStore.setGoodConductImage(it.data.driverDocs.policeClearanceCerti!!.toString())
                                dataStore.setVehicleLogBookImage(it.data.driverDocs.vehicleLogBookImage!!.toString())
                                dataStore.setNtsaInspectionImage(it.data.driverDocs.ntsaInspectionImage!!.toString())
                                dataStore.setNTSAInspectionExpiry(it.data.driverDocs.ntsaExpDate!!.toString())
                                dataStore.setComprehensiveImage(it.data.driverDocs.psvComprehensiveInsurance!!.toString())
                                dataStore.setPsvComprehensiveExpiry(it.data.driverDocs.psvComprehensiveInsuranceExpDate!!.toString())
                            }

                            PopupDialog.editVehicleDetailPopup(
                                this@EditVehicleDocumentActivity,
                                resource.message!!
                            ) {
                                finish()
                            }
                        } else {
                            showFailAlert(it.message!!)
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(
                                resource.code,
                                getString(R.string.session_expire)
                            )
                        ) {
                            resource.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(resource.code)) {
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
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.no_internet_connection))
                }

                Resource.Status.UNKNOWN -> {
                    myDialog.hide()
                    activity.window.clearFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    if (resource.code == 502) {
                        binding.root.showSnackBar(getString(R.string.no_internet))
                    } else {
                        binding.root.showSnackBar(resource.message.toString())
                    }
                }

                else -> {}
            }
        }
    }

    private suspend fun getData() {
        val job = lifecycleScope.launch {
            setPrefillData()
            delay(500L)
        }
        job.join()
    }


    @SuppressLint("SimpleDateFormat")
    private fun setPrefillData() {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val current = formatter.format(date)

        dataStore.getNationalIdNo.asLiveData().observe(this@EditVehicleDocumentActivity) {
            binding.etNationalID.setText(it.toString())
        }
        dataStore.getNationalIdImage.asLiveData().observe(this@EditVehicleDocumentActivity) {

            if (it.isNotEmpty()) {
                tvNationalIdDocUri = it.toString()
                mViewModel.nationalIdIImage = it.toString()
                flagNationalId = false
                binding.tvNationalIdDoc.text = getString(R.string.national_id_doc_uploaded)
            } else {
                binding.tvNationalIdDoc.text = getString(R.string.attach_documents)
                binding.ivEditDriverLicence.visibility = View.GONE
                flagNationalId = true
            }
        }
        dataStore.getDriverLicenceImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                tvDriverLicenceDocUri = it.toString()
                mViewModel.tvDriverLicenceDoc = it.toString()
                flagDriverLicenceDoc = false
                binding.tvDriverLicenceDoc.text = getString(R.string.driver_licence_uploaded)
            } else {
                flagDriverLicenceDoc = true
                binding.tvDriverLicenceDoc.text = getString(R.string.attach_documents)
            }
        }
        dataStore.getNtsaBadgeImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                tvNTSABudgeCertificateDocUri = it.toString()
                mViewModel.tvNTSABudgeCertificateDoc = it.toString()
                flagNTSABudgeCertificateDoc = false
                binding.tvNTSABudgeCertificateDoc.text = getString(R.string.ntsabudge_doc_uploaded)
            } else {
                flagNTSABudgeCertificateDoc = true
                binding.tvNTSABudgeCertificateDoc.text = getString(R.string.attach_documents)
            }
        }

        dataStore.getGoodConductImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                tvPoliceReceiptDocUri = it.toString()
                mViewModel.tvPoliceReceiptDoc = it.toString()
                flagPoliceReceiptDoc = false
                binding.tvPoliceReceiptDoc.text = getString(R.string.policereceiptdoc_uploaded)
            } else {
                flagPoliceReceiptDoc = true
                binding.tvPoliceReceiptDoc.text = getString(R.string.attach_documents)
            }
        }
        dataStore.getVehicleLogBookImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                tvVehicleLogBookDocUri = it.toString()
                mViewModel.tvVehicleLogBookDoc = it.toString()
                flagVehicleLogBookDoc = false
                binding.tvVehicleLogBookDoc.text = getString(R.string.vehicledoc_uploaded)
            } else {
                flagVehicleLogBookDoc = true
                binding.tvVehicleLogBookDoc.text = getString(R.string.attach_documents)
            }
        }
        dataStore.getNtsaInspectionImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                tvNTSAInspectionDocUri = it.toString()
                mViewModel.tvNTSAInspectionDoc = it.toString()
                flagNTSAInspectionDoc = false
                binding.tvNTSAInspectionDoc.text = getString(R.string.ntsa_insoection_doc_uploaded)
            } else {
                flagNTSAInspectionDoc = true
                binding.tvNTSAInspectionDoc.text = getString(R.string.attach_documents)
            }
        }

        dataStore.getComprehensiveImage.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                tvPSVComprehensiveInsuranceDocUri = it.toString()
                mViewModel.tvPSVComprehensiveInsuranceDoc = it.toString()
                flagPSVComprehensiveInsuranceDoc = false
                binding.tvPSVComprehensiveInsuranceDoc.text =
                    getString(R.string.psvcomprehensiveinsurancedoc_uploaded)
            } else {
                flagPSVComprehensiveInsuranceDoc = true
                binding.tvPSVComprehensiveInsuranceDoc.text =
                    getString(R.string.attach_documents)
            }
        }


        dataStore.getNTSAInspectionExpiry.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                if (isValidDate(current, it.toString())) {
                    binding.llNtsaInspectionDocument.setBackgroundResource(R.drawable.bg_vehicle_details)
                    flagNSTAInspectionExpDate = false
                    binding.tvNTSAInspectionExpStatus.visibility = View.GONE
                    binding.tvNSTAInspectionExpDate.text = it.toString()
                    mViewModel.NTSAInspectionExp = it.toString()
                } else {
                    flagNSTAInspectionExpDate = true
                    binding.tvNTSAInspectionDoc.text =
                        getString(R.string.attach_documents)
                    binding.tvNTSAInspectionExpStatus.visibility = View.VISIBLE
                    binding.llNtsaInspectionDocument.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                }
            }

        }
        dataStore.getNTSABudgeExpiry.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                if (isValidDate(current, it.toString())) {
                    binding.llNtsaBudgeCertificateDocument.setBackgroundResource(R.drawable.bg_vehicle_details)
                    flagNTSABudgeCertificateExpDate = false
                    binding.tvNTSABudgeCertificateDocStatus.visibility = View.GONE
                    binding.tvNTSABudgeCertificateExpDate.text = it.toString()
                    mViewModel.NTSABudgeCetrificateExp = it.toString()
                } else {
                    flagNTSABudgeCertificateExpDate = true
                    binding.tvNTSABudgeCertificateDocStatus.visibility = View.VISIBLE
                    binding.tvNTSABudgeCertificateDoc.text =
                        getString(R.string.attach_documents)
                    binding.llNtsaBudgeCertificateDocument.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                }
            }
        }
        dataStore.getPsvComprehensiveExpiry.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                if (isValidDate(current, it.toString())) {
                    binding.tvPSVComprehensiveInsuranceExpDate.text = it.toString()
                    flagPSVComprehensiveInsuranceExpDate = false
                    binding.tvPSVComprehensiveInsuranceStatus.visibility = View.GONE
                    binding.llPsvComprehensiveInsuranceDocument.setBackgroundResource(R.drawable.bg_vehicle_details)
                    mViewModel.PsvComprehensiveInsuranceExp = it.toString()
                } else {
                    flagPSVComprehensiveInsuranceExpDate = true
                    binding.tvPSVComprehensiveInsuranceDoc.text =
                        getString(R.string.attach_documents)
                    binding.tvPSVComprehensiveInsuranceStatus.visibility = View.VISIBLE
                    binding.llPsvComprehensiveInsuranceDocument.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                }
            }
        }
        /** set prefill expiry and validation*/
        dataStore.getDriverLicenceExpiry.asLiveData().observe(this@EditVehicleDocumentActivity) {
            if (it.isNotEmpty()) {
                if (isValidDate(current, it.toString())) {
                    binding.tvDriverLicenceExpDate.text = it.toString()
                    binding.llDriverLicenceDocument.setBackgroundResource(R.drawable.bg_vehicle_details)
                    binding.tvDriverLicenceDocStatus.visibility = View.GONE
                    mViewModel.driverLicenceExp = it.toString()
                    flagDriverLicenceExpDate = false
                } else {
                    binding.llDriverLicenceDocument.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                    flagDriverLicenceExpDate = true
                    binding.tvDriverLicenceDocStatus.visibility = View.VISIBLE
                    binding.tvDriverLicenceDoc.text =
                        getString(R.string.attach_documents)

                }
            }
        }
        dataStore.getUserId.asLiveData().observe(this@EditVehicleDocumentActivity) {
            mViewModel.id = it.toString()
        }
    }

    private fun showSetting(): Boolean {
        val showRationale =
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!showRationale) {
            val deniedDialog = androidx.appcompat.app.AlertDialog.Builder(this)

            deniedDialog.setTitle(R.string.permission_denied)
            deniedDialog.setMessage("Please turn on PostNotification")
            deniedDialog.setPositiveButton(
                R.string.settings
            ) { _, _ ->
                showPermissionDeniedDialog()
            }
            deniedDialog.setCancelable(false)
            deniedDialog.show()
        }
        return false
    }

    private fun showPermissionDeniedDialog() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package", this.packageName, null
        )
        intent.data = uri
        startActivity(intent)
    }

    private fun checkPostNotification() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ),
            -> {
                Log.e(ContentValues.TAG, "onCreate: PERMISSION GRANTED")
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        }
    }

    private fun activityLauncher() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getCurrentLocation()
                } else {
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.showfa_require_to_location_permission)
                    )
                    binding.root.showSnackBar(getString(R.string.please_turn_on_your_location))
                }
            }
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                val location = task.result
                Log.d("LOCATION", "location==>$location")
                if (location != null) {
                    currentLocationLat = location.latitude
                    currentLocationLng = location.longitude
                    mViewModel.Lat = location.latitude.toString()
                    mViewModel.Lng = location.longitude.toString()

                }
            }
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            finish()
        }
    }

    fun attachDocument(view: View) {
        when (view.id) {

            R.id.tvNationalIdDoc -> {

                if (!flagNationalId) {
                    if (tvNationalIdDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvNationalIdDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvNationalIdDocUri)
                    }
                } else {
                    flag = FLAG_NATIONAL_ID_DOC
                    openDialog()
                }
            }

            R.id.tvDriverLicenceDoc -> {

                if (!flagDriverLicenceDoc) {
                    if (tvDriverLicenceDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvDriverLicenceDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvDriverLicenceDocUri)
                    }
                } else {
                    flag = FLAG_DRIVER_LICENCE_DOC
                    openDialog()
                }
            }

            R.id.tvNTSABudgeCertificateDoc -> {

                if (!flagNTSABudgeCertificateDoc) {
                    if (tvNTSABudgeCertificateDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvNTSABudgeCertificateDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvNTSABudgeCertificateDocUri)
                    }
                } else {
                    flag = FLAG_NTSA_BUDGE_DOC
                    openDialog()
                }
            }

            R.id.tvPoliceReceiptDoc -> {
                if (!flagPoliceReceiptDoc) {
                    if (tvPoliceReceiptDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvPoliceReceiptDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvPoliceReceiptDocUri)
                    }
                } else {
                    flag = FLAG_POLICE_CARTI_DOC
                    openDialog()
                }
            }

            R.id.tvVehicleLogBookDoc -> {
                if (!flagVehicleLogBookDoc) {
                    if (tvVehicleLogBookDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvVehicleLogBookDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvVehicleLogBookDocUri)
                    }
                } else {
                    flag = FLAG_VEHICLE_LOGBOOK_DOC
                    openDialog()
                }
            }

            R.id.tvNTSAInspectionDoc -> {
                if (!flagNTSAInspectionDoc) {
                    if (tvNTSAInspectionDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvNTSAInspectionDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvNTSAInspectionDocUri)
                    }
                } else {
                    flag = FLAG_NTSA_INSPECTION_DOC
                    openDialog()
                }
            }

            R.id.tvPSVComprehensiveInsuranceDoc -> {
                if (!flagPSVComprehensiveInsuranceDoc) {
                    if (tvPSVComprehensiveInsuranceDocUri.contains(".pdf")) {
                        openPdf(ApiConstant.Image_URL + tvPSVComprehensiveInsuranceDocUri)
                    } else {
                        showDocImage(ApiConstant.Image_URL + tvPSVComprehensiveInsuranceDocUri)
                    }
                } else {
                    flag = FLAG_PSV_COMPRIHENSIVE_DOC
                    openDialog()
                }
            }
        }
    }

    private fun showDocImage(imageUrl: Any) {
        Log.w(ContentValues.TAG, "showDocImage() Image Load - $imageUrl")
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_image_view)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val close = dialog.findViewById<ImageView>(R.id.iv_CloseImageView)
        val doc = dialog.findViewById<ImageView>(R.id.iv_DocView)

        close.setOnClickListener { dialog.dismiss() }

        Glide.with(this)
            .load(imageUrl)
            .error(R.mipmap.ic_launcher) // Set fallback image resource in case of error
            .into(doc)

        dialog.show()
    }

    private fun openPdf(pdfUrl: Any) {

        try {
            Log.e("PDfOpen", pdfUrl.toString())
            val webPage = Uri.parse(pdfUrl.toString())
            val intentUrl = Intent(Intent.ACTION_VIEW)
            intentUrl.setDataAndType(webPage, "application/pdf")
            intentUrl.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intentUrl)
        } catch (e: java.lang.Exception) {
            Log.e("PDfOpen", e.message!!)
        }
    }

    private fun openDialog() {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.TransparentDialogTrans)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(R.layout.dialog_photo_choose)
        val llCamera: LinearLayout? = bottomSheetDialog.findViewById(R.id.llCamera)
        val llGallery: LinearLayout? = bottomSheetDialog.findViewById(R.id.llGallery)
        val llDoc: LinearLayout? = bottomSheetDialog.findViewById(R.id.llDoc)
        llDoc!!.visibility = View.VISIBLE
        val ivCloseImageChooseDialog: ImageView? =
            bottomSheetDialog.findViewById(R.id.ivCloseImageChooseDialog)
        llGallery!!.setOnClickListener {
            flagInner = FLAG_GALLARY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissionGallary13()
            } else {
                if (ContextCompat.checkSelfPermission(
                        this@EditVehicleDocumentActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@EditVehicleDocumentActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        STORAGE_PERMISSION_REQUEST_CODE
                    )
                } else {
                    checkPermissionGallary()
                }
            }

            bottomSheetDialog.dismiss()

        }
        llCamera!!.setOnClickListener {
            flagInner = FLAG_CAMERA

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissionCamera13()
            } else {
                if (ContextCompat.checkSelfPermission(
                        this@EditVehicleDocumentActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@EditVehicleDocumentActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        STORAGE_PERMISSION_REQUEST_CODE
                    )
                } else {
                    checkPermissionCamera()
                }
            }
            bottomSheetDialog.dismiss()

        }
        llDoc.setOnClickListener {
            flagInner = FLAG_DOC
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                openDocumentPicker()
            } else {

                checkPermissionForDoc()
            }
            bottomSheetDialog.dismiss()/* Handler().postDelayed({ takePermission(false, true) }, 800)*/
        }
        ivCloseImageChooseDialog!!.setOnClickListener { view: View? -> bottomSheetDialog.dismiss() }
        bottomSheetDialog.show()
    }

    private fun checkPermissionGallary13() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            openGallery()
        } else {
            // Request camera permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), READ_MEDIA_CODE
                )
            }
        }
    }

    private fun checkPermissionCamera13() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            openCamera()
        } else {
            // Request camera permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_MEDIA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                openCamera()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    // User denied permission with "Don't ask again" option
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.please_turn_on_camera_permission)
                    )
                } else {
                    // User denied permission
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.please_turn_on_camera_permission)
                    )
                }
            }
        }
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                openCamera()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    // User denied permission with "Don't ask again" option
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.please_turn_on_gallary_permission)
                    )
                } else {
                    // User denied permission
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.please_turn_on_gallary_permission)
                    )
                }
            }
        }

        if (requestCode == LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                getCurrentLocation()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    // User denied permission with "Don't ask again" option
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.showfa_require_to_location_permission)
                    )
                } else {
                    // User denied permission
                    CommonFun.showSetting(
                        this@EditVehicleDocumentActivity,
                        getString(R.string.showfa_require_to_location_permission)

                    )
                }
            }
        }
    }

    private fun checkPermissionForDoc() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            openDocumentPicker()
        } else {
            // Request document permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                DOCUMENT_PERMISSION_CODE
            )
        }
    }

    private fun openDocumentPicker() {
        val documentIntent = Intent(Intent.ACTION_GET_CONTENT)
        documentIntent.type = "application/pdf"
        /*  startActivityForResult(documentIntent, PICK_DOCUMENT_REQUEST_CODE)*/

        requestCode = PICK_DOCUMENT_REQUEST_CODE
        documentIntent.putExtra("reqCode", PICK_DOCUMENT_REQUEST_CODE)
        cameraGallaryDocumentLauncher.launch(documentIntent)
    }

    private fun checkPermissionGallary() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            openGallery()
        } else {
            // Request gallery permission
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_PERMISSION_CODE
            )
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        /*  startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)*/
        requestCode = PICK_IMAGE_REQUEST_CODE
        galleryIntent.putExtra("reqCode", PICK_IMAGE_REQUEST_CODE)
        cameraGallaryDocumentLauncher.launch(galleryIntent)
    }

    private fun checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            openCamera()
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "take picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "take a discription")
        imageUriCamera =
            activity.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriCamera)
        /*   startActivityForResult(intent, CAMERA_PERMISSION_CODE)*/
        requestCode = CAMERA_PERMISSION_CODE
        intent.putExtra("reqCode", CAMERA_PERMISSION_CODE)
        cameraGallaryDocumentLauncher.launch(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setExpiryDate(view: View) {

        when (view.id) {

            R.id.tvDriverLicenceExpDate -> {
                showDatePicker(binding.tvDriverLicenceExpDate, binding.llDriverLicenceDocument, 0)
                flagDriverLicenceExpDate = false
            }

            R.id.tvPSVComprehensiveInsuranceExpDate -> {
                showDatePicker(
                    binding.tvPSVComprehensiveInsuranceExpDate,
                    binding.llPsvComprehensiveInsuranceDocument, 1
                )
                flagPSVComprehensiveInsuranceExpDate = false
            }

            R.id.tvNSTAInspectionExpDate -> {
                showDatePicker(binding.tvNSTAInspectionExpDate, binding.llNtsaInspectionDocument, 2)
                flagNSTAInspectionExpDate = false
            }

            R.id.tvNTSABudgeCertificateExpDate -> {
                showDatePicker(
                    binding.tvNTSABudgeCertificateExpDate, binding.llNtsaBudgeCertificateDocument, 3
                )
                flagNTSABudgeCertificateExpDate = false
            }

        }
    }


    private fun showDatePicker(textView: TextView?, minlayout: LinearLayout, i: Int) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val currentDatee = Date()
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            Log.e(TAG, "showDatePicker: version less")
        }
        val datePickerDialog = DatePickerDialog(
            this,
            {
                    _: DatePicker,
                    selectedYear: Int, selectedMonth: Int, selectedDay: Int,
                ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)



                if (isValidDate(current.toString(), formattedDate)) {
                    minlayout.setBackgroundResource(R.drawable.bg_vehicle_details)
                } else {
                    minlayout.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                }



                when (i) {
                    0 -> {

                        if (isValidDate(current.toString(), formattedDate)) {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details)
                            textView!!.text = formattedDate
                            mViewModel.driverLicenceExp = formattedDate
                            flagDriverLicenceExpDate = false

                        } else {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                            textView!!.text = formattedDate
                            flagDriverLicenceExpDate = true
                        }

                    }

                    1 -> {
                        if (isValidDate(current.toString(), formattedDate)) {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details)
                            textView!!.text = formattedDate
                            lifecycleScope.launch {
                                dataStore.setPsvComprehensiveExpiry(formattedDate)
                            }
                            mViewModel.PsvComprehensiveInsuranceExp = formattedDate
                            flagPSVComprehensiveInsuranceExpDate = false

                        } else {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                            textView!!.text = formattedDate
                            flagPSVComprehensiveInsuranceExpDate = true
                        }
                    }

                    2 -> {
                        if (isValidDate(current.toString(), formattedDate)) {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details)
                            textView!!.text = formattedDate
                            mViewModel.NTSAInspectionExp = formattedDate
                            flagNSTAInspectionExpDate = false

                        } else {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                            textView!!.text = formattedDate
                            flagNSTAInspectionExpDate = true
                        }
                    }


                    3 -> {
                        if (isValidDate(current.toString(), formattedDate)) {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details)
                            textView!!.text = formattedDate
                            lifecycleScope.launch {
                                dataStore.setNTSABudgeExpiry(formattedDate)
                            }
                            mViewModel.NTSABudgeCetrificateExp = formattedDate
                            flagNTSABudgeCertificateExpDate = false

                        } else {
                            minlayout.setBackgroundResource(R.drawable.bg_vehicle_details_expired)
                            textView!!.text = formattedDate
                            flagNTSABudgeCertificateExpDate = true
                        }
                    }
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = currentDatee.time
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", datePickerDialog)
        datePickerDialog.setCancelable(false)
        datePickerDialog.show()
    }

    private fun isValidDate(currentDate: String, selectedDate: String?): Boolean {
        val selectedDate = selectedDate?.split("-")
        val oneS = selectedDate?.get(0)
        val twoS = selectedDate?.get(1)
        val threeS = selectedDate?.get(2)

        oneS?.toInt()
        twoS?.toInt()
        threeS?.toInt()


        val currentDate = currentDate.split("-")
        val onec = currentDate[0]
        val twoc = currentDate[1]
        val threec = currentDate[2]

        onec.toInt()
        twoc.toInt()
        threec.toInt()

        try {
            if (oneS == onec && twoS == twoc && threeS == threec) {
                return false
            } else if (oneS != null) {
                if (twoS != null) {
                    if (threeS != null) {
                        if (oneS <= onec || twoS <= twoc || threeS <= threec) {
                            return true
                        } else if (oneS >= onec && twoS >= twoc && threeS >= threec) {
                            return true
                        } else if (oneS < onec || twoS >= twoc || threeS <= threec) {
                            return true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("error", "isValidDate:$e ")
        }
        return false
    }

    fun updateUploadImage(view: View) {
        when (view.id) {
            R.id.ivEditNationalId -> {
                flag = FLAG_NATIONAL_ID_DOC
                openDialog()
            }

            R.id.ivEditDriverLicence -> {
                flag = FLAG_DRIVER_LICENCE_DOC
                openDialog()
            }

            R.id.ivEditNTSABudgeCertificate -> {
                flag = FLAG_NTSA_BUDGE_DOC
                openDialog()
            }

            R.id.ivEditPoliceReceipt -> {
                flag = FLAG_POLICE_CARTI_DOC
                openDialog()
            }

            R.id.ivEditVehicleLogBook -> {
                flag = FLAG_VEHICLE_LOGBOOK_DOC
                openDialog()
            }

            R.id.ivEditNTSAInspection -> {
                flag = FLAG_NTSA_INSPECTION_DOC
                openDialog()
            }

            R.id.ivEditPSVComprehensiveInsuranceDoc -> {
                flag = FLAG_PSV_COMPRIHENSIVE_DOC
                openDialog()
            }
        }
    }

    override fun onResume() {
        getCurrentLocation()
        super.onResume()
    }
}
