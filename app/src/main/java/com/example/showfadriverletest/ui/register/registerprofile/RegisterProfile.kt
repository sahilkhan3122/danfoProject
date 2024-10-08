package com.example.showfadriverletest.ui.register.registerprofile

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.databinding.ActivityRegisterProfileBinding
import com.example.showfadriverletest.ui.register.registerprofile.viewmodel.RegisterProfileModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class RegisterProfile : BaseActivity<ActivityRegisterProfileBinding, RegisterProfileModel>() {
    override val layoutId: Int
        get() = R.layout.activity_register_profile
    override val bindingVariable: Int
        get() = BR.viewModel

    var tag = ""
    private var fName = ""
    private var lName = ""
    private var rdCarType = ""
    private var rdGender = ""
    var dob = ""
    var spinnerData = ""
    var image: Bitmap? = null
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private var imageUriCamera: Uri? = null
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private var CAMERA_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            defaultSelection()
            activityHeader.tvTitle.setText(getString(R.string.profile_detail))
            activityHeader.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
            setSpinnerData()
            tvRegisterProfileNext.setOnClickListener {
                checkValidation()
            }

            /*camera result*/
            cameraResultLauncher =
                registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { it ->
                    if (it.resultCode == RESULT_OK) {
                            val pathCol = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor: Cursor? = imageUriCamera.let {
                            activity.contentResolver.query(
                                it!!,
                                pathCol,
                                null,
                                null,
                                null
                            )
                        }
                        cursor?.moveToFirst()
                        val colIdx: Int = cursor?.getColumnIndex(pathCol[0])!!
                        val img: String = cursor.getString(colIdx)
                        Log.e("imgTest", "" + img)
                        cursor.close()
                        binding.ivProfileRegister.setImageURI(Uri.fromFile(File(img)))
                        myDialog.hide()
                        //  binding.ivProfileRegister.setImageURI(imageUriCamera)
                        lifecycleScope.launch {
                            dataStore.setProfileImage(imageUriCamera.toString())
                            Log.e("imgTest", "" + img)
                            Log.e("imgTest", "" + img)
                            Log.e("imgTest", "" + img)

                        }
                    }
                }

            llRegisterUserProfile.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermissionCamera()
                } else {
                    if (ContextCompat.checkSelfPermission(
                            this@RegisterProfile,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@RegisterProfile,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            STORAGE_PERMISSION_REQUEST_CODE
                        )

                    } else {
                        ActivityCompat.requestPermissions(
                            this@RegisterProfile,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            STORAGE_PERMISSION_REQUEST_CODE
                        )
                    }
                }
            }
        }
    }

    private fun defaultSelection() {
        binding.carOwner.isChecked = true
        binding.radioMale.isChecked = true
        rdGender = "male"
        rdCarType = "own"
    }

    override fun setUpObserver() {
    }

    private fun checkPermissionCamera() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, open camera
            openCamera()
        } else {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                openCamera()
            } else {
                // Permission denied
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    // User denied permission with "Don't ask again" option
                    showPermissionRationaleDialog()
                } else {
                    // User denied permission
                    showPermissionDeniedDialog()
                }
            }
        }

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                checkPermissionCamera()
            } else {
                // Permission denied
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ).toString()
                    )
                ) {
                    // User denied permission with "Don't ask again" option
                    CommonFun.showSetting(
                        this@RegisterProfile,
                        getString(R.string.storage_permission_require_to_take_image)
                    )
                } else {
                    // User denied permission
                    CommonFun.showSetting(
                        this@RegisterProfile,
                        getString(R.string.storage_permission_require_to_take_image)
                    )
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Camera permission was denied. Please grant the permission in the app settings to use the camera feature.")
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                navigateToAppSettings()
            }
            .show()
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Camera permission is required to use the camera feature. Please grant the permission in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                navigateToAppSettings()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    private fun setSpinnerData() {
        val languages = resources.getStringArray(R.array.City)
        val spinner = binding.spArea
        val adapter = ArrayAdapter(
            this, R.layout.spinner_item, languages
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long,
            ) {
                spinnerData = languages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun checkValidation() {
        if (imageUriCamera == null) {
            binding.root.showSnackBar(getString(R.string.plz_select_profile_image))
        } else if (binding.etRegisterFirstname.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.plz_enter_first_name))
        } else if (binding.etRegisterLastname.text.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.plz_enter_last_name))
        } else if (!binding.ownerByPartner.isChecked && !binding.carOwner.isChecked) {
            binding.root.showSnackBar(getString(R.string.plz_select_radio))
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
            myDialog.show()
            fName = binding.etRegisterFirstname.text.toString()
            lName = binding.etRegisterLastname.text.toString()

            lifecycleScope.launch {
                dataStore.setFirstName(fName)
                dataStore.setLastName(lName)
                dataStore.setDob(dob)
                dataStore.setCarType(rdCarType)
                dataStore.setGender(rdGender)
                dataStore.setAdress(spinnerData)
                dataStore.setProfileImage(imageUriCamera.toString())
            }
            myDialog.hide()
            startActivity(Intent(this, VehicleDetail::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }

    fun radioOwnerClick(view: View) {
        if (view.id == R.id.carOwner) {
            binding.llOwnerDetial.visibility = View.GONE
            rdCarType = "own"
            tag = "cancel"
        }

        if (view.id == R.id.ownerByPartner) {
            binding.llOwnerDetial.visibility = View.VISIBLE
            rdCarType = "rent"
            tag = "rent"
            lifecycleScope.launch {
                dataStore.setOwnerName(binding.etOwnerName.text.toString())
                dataStore.setOwnerEmail(binding.etOwnerEmail.text.toString())
                dataStore.setOwnerMobileNo(binding.etOwnerMobile.text.toString())
            }
        }
    }

    fun radioGender(view: View) {
        if (view.id == R.id.radio_male) {
            rdGender = "male"
        }
        if (view.id == R.id.radio_female) {
            rdGender = "female"
        }
    }


    fun dateOfBirth(view: View) {
        CommonFun.hideKeyboard(this)
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