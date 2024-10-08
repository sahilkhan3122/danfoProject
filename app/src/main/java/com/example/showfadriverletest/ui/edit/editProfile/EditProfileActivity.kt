package com.example.showfadriverletest.ui.edit.editProfile

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivityEditProfileBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.util.CommonFun.checkIsConnectionReset
import com.example.showfadriverletest.util.PopupDialog
import com.example.showfadriverletest.util.SnackbarUtil
import com.example.showfadriverletest.view.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_edit_profile
    override val bindingVariable: Int
        get() = BR.viewModel

    var tag = ""
    private var fName = ""
    private var lName = ""
    private var rdCarType = ""
    private var rdGender = ""
    var dob = ""
    var email = ""
    var number = ""
    private var selectOwnByPartner = ""
    var spinnerData = ""
    var ownerName = ""
    var ownerEmail = ""
    private var imageEdit = ""
    private var imageUriCamera: Uri? = null
    private var ownerMobile = ""
    var image: Bitmap? = null
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private var CAMERA_PERMISSION_CODE = 101
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPrefillData()
        binding.apply {
            activityHeader.tvTitle.text = getString(R.string.profile_detail)
            activityHeader.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
            cameraResultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) {
                    val pathCol = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = imageUriCamera.let {
                        it?.let { it1 ->
                            activity.contentResolver.query(
                                it1, pathCol, null, null, null
                            )
                        }
                    }
                    cursor?.moveToFirst()
                    val colIdx: Int = cursor?.getColumnIndex(pathCol[0])!!
                    val img: String = cursor.getString(colIdx)
                    Log.e("imgTest", "" + img)
                    cursor.close()
                    binding.ivProfileRegister.setImageURI(Uri.fromFile(File(img)))
                    //  binding.ivProfileRegister.setImageURI(imageUriCamera)
                    mViewModel.userProfile = imageUriCamera

                    lifecycleScope.launch {
                        dataStore.setProfileImage(imageUriCamera.toString())
                    }
                }
            }

            setSpinnerData()
            tvRegisterProfileNext.setOnClickListener {
                /*startActivity(Intent(this@RegisterProfile ,VehicleDetail::class.java))*//*   overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)*/
                checkValidation()
            }

            ivProfileRegister.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermissionCamera()
                } else {
                    requestPermission()
                }
            }

        }
    }

    override fun setUpObserver() {
        mViewModel.getProfileInfoObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data?.status == true) {
                        Log.e(TAG, "on Profile Info success=>${it.message}")
                        lifecycleScope.launch {
                            dataStore.setFirstName(it.data!!.data!!.firstName.toString())
                            dataStore.setLastName(it.data.data!!.lastName.toString())
                            dataStore.setMobileNo(it.data.data.mobileNo.toString())
                            dataStore.setEmail(it.data.data.email.toString())
                            dataStore.setProfileImage(it.data.data.profileImage.toString())
                            dataStore.setGender(it.data.data.gender.toString())
                            dataStore.setCarType(it.data.data.carType.toString())
                            dataStore.setDob(it.data.data.dob.toString())
                            dataStore.setOwnerName(ownerName)
                            dataStore.setOwnerEmail(ownerEmail)
                            dataStore.setOwnerMobileNo(ownerMobile)
                            dataStore.setAdress(spinnerData)
                        }
                        PopupDialog.editVehicleDetailPopup(
                            this@EditProfileActivity, it.message!!
                        ) {
                            Handler(Looper.myLooper()!!).postDelayed({
                                finish()
                            }, 100)
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
                    Log.e(TAG, "on Profile Info error=>${it.message}")
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.please_check_internet_connection))
                }

                Resource.Status.UNKNOWN -> {
                    myDialog.hide()
                    window.clearFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    if (it.code == 502) {
                        SnackbarUtil.show(
                            this, "no internet", Snackbar.LENGTH_LONG
                        )
                    } else {
                        SnackbarUtil.show(
                            this, "${it.message}", Snackbar.LENGTH_LONG
                        )
                    }
                }

                else -> {}
            }
        }
    }

    private fun checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, open camera
            openCamera()
        } else {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            checkPermissionCamera()
        }
    }

    private fun openCamera() {
        val values = ContentValues(1)
        imageUriCamera =
            activity.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriCamera)
        intent.putExtra("reqCode", CAMERA_PERMISSION_CODE)
        cameraResultLauncher.launch(intent)
    }

    private fun setPrefillData() {
        binding.apply {
            dataStore.getFirstName.asLiveData().observe(this@EditProfileActivity) {
                etRegisterFirstname.setText(it.toString())
            }
            dataStore.getLastName.asLiveData().observe(this@EditProfileActivity) {
                etRegisterLastname.setText(it.toString())
            }
            dataStore.getMobileNo.asLiveData().observe(this@EditProfileActivity) {
                edtMobileNumber.setText(it.toString())
            }
            dataStore.getEmail.asLiveData().observe(this@EditProfileActivity) {
                edtEmail.setText(it.toString())
            }
            dataStore.getDob.asLiveData().observe(this@EditProfileActivity) {
                etRegisterDob.setText(it.toString())
            }

            dataStore.getProfileImage.asLiveData().observe(this@EditProfileActivity) {
                imageEdit = it.toString()
                Glide.with(this@EditProfileActivity).load(ApiConstant.Image_URL.plus(imageEdit))
                    .into(binding.ivProfileRegister)
            }
            dataStore.getGender.asLiveData().observe(this@EditProfileActivity) {
                if (it.toString() == "male") {
                    binding.radioMale.isChecked = true
                    rdGender = "male"
                    mViewModel.gender = it.toString()

                } else {
                    binding.radioFemale.isChecked = true
                    rdGender = "female"
                    mViewModel.gender = it.toString()
                }
            }
            dataStore.getCarType.asLiveData().observe(this@EditProfileActivity) { it ->
                if (it.toString() == "own") {
                    binding.carOwner.isChecked = true
                    binding.llOwnerDetial.visibility = View.GONE
                    rdCarType = "own"
                    mViewModel.carType = rdCarType
                } else {
                    binding.ownerByPartner.isChecked = true
                    binding.llOwnerDetial.visibility = View.VISIBLE
                    rdCarType = "rent"
                    mViewModel.carType = rdCarType
                    selectOwnByPartner = "partner"

                    dataStore.getOwnerName.asLiveData().observe(this@EditProfileActivity) {
                        etOwnerName.setText(it.toString())
                    }
                    dataStore.getOwnerEmail.asLiveData().observe(this@EditProfileActivity) {
                        etOwnerEmail.setText(it.toString())
                    }
                    dataStore.getOwnerMobileNo.asLiveData().observe(this@EditProfileActivity) {
                        etOwnerMobile.setText(it.toString())
                    }
                    dataStore.getAddress.asLiveData().observe(this@EditProfileActivity) {
                        setSpinnerData()
                    }
                }
            }
        }
    }

    private fun setSpinnerData() {
        val languages = resources.getStringArray(R.array.City)
        val spinner = binding.spArea
        val adapter = ArrayAdapter(this, R.layout.spinner_item, languages)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long,
            ) {
                spinnerData = languages[position]
                mViewModel.address = spinnerData
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun checkValidation() {
        if (imageEdit == null) {
            binding.root.showSnackBar(getString(R.string.plz_select_profile_image))
        } else if (binding.etRegisterFirstname.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.plz_enter_first_name))
        } else if (binding.etRegisterLastname.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.plz_enter_last_name))
        } else if (!binding.ownerByPartner.isChecked && !binding.carOwner.isChecked) {
            binding.root.showSnackBar(getString(R.string.plz_select_radio))
        } else if (binding.edtEmail.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.please_enter_emaill))
        } else if (binding.edtMobileNumber.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.please_enter_mobile_number))
        } else if (tag == "rent") {
            if (binding.etOwnerName.text.isEmpty() || binding.etOwnerMobile.text.isEmpty() || binding.etOwnerEmail.text.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.plz_enter_owner_details))
            } else {
                tag = "cancel"
            }
        } else if (binding.etRegisterDob.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.plz_select_birthdate))
        } else if (spinnerData == "SelectArea") {
            binding.root.showSnackBar(getString(R.string.plz_select_address_spinner))
        } else if (!binding.radioMale.isChecked && !binding.radioFemale.isChecked) {
            binding.root.showSnackBar(getString(R.string.plz_select_gender))
        } else {

            dataStore.getUserId.asLiveData().observe(this@EditProfileActivity) {
                mViewModel.id = it.toString()
            }
            fName = binding.etRegisterFirstname.text.toString()
            lName = binding.etRegisterLastname.text.toString()
            email = binding.edtEmail.text.toString()
            number = binding.edtMobileNumber.text.toString()
            dob = binding.etRegisterDob.text.toString()
            ownerName = binding.etOwnerName.text.toString()
            ownerMobile = binding.etOwnerMobile.text.toString()
            ownerEmail = binding.etOwnerEmail.text.toString()

            mViewModel.fname = fName
            mViewModel.lName = lName
            mViewModel.email = email
            mViewModel.number = number
            mViewModel.dob = dob
            mViewModel.ownname = ownerName
            mViewModel.ownMail = ownerEmail
            mViewModel.ownMobile = ownerMobile
            mViewModel.profileInfo()
        }
    }

    fun radioOwnerClick(view: View) {
        if (view.id == R.id.carOwner) {
            binding.llOwnerDetial.visibility = View.GONE
            rdCarType = "own"
            mViewModel.carType = rdCarType
        }

        if (view.id == R.id.ownerByPartner) {
            binding.llOwnerDetial.visibility = View.VISIBLE
            rdCarType = "rent"
            mViewModel.carType = rdCarType
            tag = "rent"

        }
    }

    fun radio_male_female_check(view: View) {
        if (view.id == R.id.radio_male) {
            rdGender = "male"
            mViewModel.gender = rdGender
        }

        if (view.id == R.id.radio_female) {
            rdGender = "female"
            mViewModel.gender = rdGender
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateOfBirth(view: View) {

        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            {
                    _: DatePicker,
                    selectedYear: Int, selectedMonth: Int, selectedDay: Int,
                ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dob = dateFormat.format(selectedDate.time)
                binding.etRegisterDob.setText(dob)
            },
            year,
            month,
            day
        )
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", datePickerDialog)
        datePickerDialog.setCancelable(false)
        datePickerDialog.show()
    }
}