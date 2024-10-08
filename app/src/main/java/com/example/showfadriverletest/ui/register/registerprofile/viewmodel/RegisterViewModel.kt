package com.example.showfadriverletest.ui.register.registerprofile.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.register.RegisterResponse
import com.example.showfadriverletest.response.uploaddoc.UploadDocsResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import com.example.showfadriverletest.util.file.PathUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.net.ConnectException
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {

    var docFile: Uri? = null

    private val uploadDocObserver: MutableLiveData<Resource<UploadDocsResponse>> =
        MutableLiveData()

    fun getUploadDocsObservableDoc(): LiveData<Resource<UploadDocsResponse>> {
        return uploadDocObserver
    }

    var fname = ""
    var lname = ""
    var addresses = ""
    var dob = ""
    var token = ""
    var gender = ""
    var cartype = ""
    var nationIdNo = ""
    var number = ""
    var email = ""
    var plateNo = ""
    var ownerName = ""
    var ownerEmail = ""
    var ownerMobileNo = ""
    var imageInside = ""
    var imageBack = ""
    var imageFront = ""
    var imageLeft = ""
    var imageRight = ""
    var vehicleYear = ""
    lateinit var profileImage: Uri
    var color = ""

    var nationalIdIImage = ""
    var tvDriverLicenceDoc = ""
    var tvNTSABudgeCertificateDoc = ""
    var tvPoliceReceiptDoc = ""
    var tvVehicleLogBookDoc = ""
    var tvNTSAInspectionDoc = ""
    var tvPSVComprehensiveInsuranceDoc = ""

    var driverLicenceExp = ""
    var PsvComprehensiveInsuranceExp = ""
    var NTSAInspectionExp = ""
    var NTSABudgeCetrificateExp = ""

    var Lat = ""
    var Lng = ""

    var vehicleModelName = ""
    var vehicleModelId = ""
    var manufactureName = ""
    var manufactureId = ""
    var vehicleType = ""

    private val registerObserver: MutableLiveData<Resource<RegisterResponse>> =
        MutableLiveData()

    fun getRegisterObservable(): LiveData<Resource<RegisterResponse>> {
        return registerObserver
    }

    fun uploadDocsApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            uploadDocObserver.value = Resource.loading()
            lateinit var response: Response<UploadDocsResponse>
            val image: MultipartBody.Part? = getMultipartDocImages(
                docFile,
                context
            )

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        response = networkService.uploadDoc(image)
                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            PrintLog.e(PrintLog.TAG, "${baseDataSource.getResult(true) { this }}")
                            uploadDocObserver.value =
                                baseDataSource.getResult(true) { this }
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            uploadDocObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                502
                            )
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            uploadDocObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                500
                            )
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    uploadDocObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }

    fun registerApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            registerObserver.value = Resource.loading()
            lateinit var response: Response<RegisterResponse>
            var image: MultipartBody.Part? = null
            if (profileImage != null) {
                image = getMultipartImages(profileImage, (context as ContextWrapper).baseContext)
            }

            val registerRequestMap = HashMap<String, RequestBody>()
            registerRequestMap[ApiConstant.DRIVER_ROLE] = getRequestBody("captain")
            registerRequestMap[ApiConstant.CAR_TYPE] = getRequestBody(cartype)
            registerRequestMap[ApiConstant.OWNER_NAME] = getRequestBody(ownerName)
            registerRequestMap[ApiConstant.OWNER_EMAIL] = getRequestBody(ownerEmail)
            registerRequestMap[ApiConstant.OWNER_MOBILE_NUMBER] = getRequestBody(ownerMobileNo)
            registerRequestMap[ApiConstant.MOBILE_NO] = getRequestBody(number)
            registerRequestMap[ApiConstant.FIRST_NAME] = getRequestBody(fname)
            registerRequestMap[ApiConstant.LAST_NAME] = getRequestBody(lname)
            registerRequestMap[ApiConstant.EMAIL] = getRequestBody(email)
            /*  registerRequestMap[ApiConstant.REGISTER_MOBILE_NO] = getRequestBody("12398007890")*/
            registerRequestMap[ApiConstant.DOB] = getRequestBody(dob)
            registerRequestMap[ApiConstant.GENDER] = getRequestBody(gender)
            registerRequestMap[ApiConstant.ACCOUNT_HOLDER_NAME] = getRequestBody("sahil")
            registerRequestMap[ApiConstant.BANK_NAME] = getRequestBody("Bob")
            registerRequestMap[ApiConstant.BANK_BRANCH] = getRequestBody("PATAN")
            registerRequestMap[ApiConstant.ACCOUNT_NUMBER] = getRequestBody("08418100000882")
            registerRequestMap[ApiConstant.LAT] = getRequestBody(Lat)
            registerRequestMap[ApiConstant.LNG] = getRequestBody(Lng)
            registerRequestMap[ApiConstant.DEVICE_TYPE] =
                getRequestBody(context.getString(R.string.androidd))
            registerRequestMap[ApiConstant.DEVICE_TOKEN] = getRequestBody(token)
            registerRequestMap[ApiConstant.ADDRESS] = getRequestBody(addresses)
            registerRequestMap[ApiConstant.PLATE_NUMBER] = getRequestBody(plateNo)
            registerRequestMap[ApiConstant.YEAR_OF_MANUFACTURE] = getRequestBody(vehicleYear)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MODEL_NAME] =
                getRequestBody(vehicleModelName)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MODEL_ID] = getRequestBody(vehicleModelId)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MANUFACTURER_NAME] =
                getRequestBody(manufactureName)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MANUFACTURER_ID] =
                getRequestBody(manufactureId)
            registerRequestMap[ApiConstant.NO_OF_PASSANGER] = getRequestBody("3")
            registerRequestMap[ApiConstant.VEHICLE_TYPE] = getRequestBody(vehicleType)
            registerRequestMap[ApiConstant.INVITE_CODE] = getRequestBody("154516156")
            registerRequestMap[ApiConstant.WORK_WITH_OTHER_COMPANY] = getRequestBody("0")
            registerRequestMap[ApiConstant.OTHER_COMPANY_NAME] = getRequestBody(" test LTD")
            registerRequestMap[ApiConstant.COMPANY_ID] = getRequestBody("1")
            registerRequestMap[ApiConstant.CAR_LEFT] = getRequestBody(imageLeft)
            registerRequestMap[ApiConstant.CAR_RIGHT] = getRequestBody(imageRight)
            registerRequestMap[ApiConstant.CAR_FRONT] = getRequestBody(imageFront)
            registerRequestMap[ApiConstant.CAR_BACK] = getRequestBody(imageBack)
            registerRequestMap[ApiConstant.NTSA_INSPECTION_IMAGE] =
                getRequestBody(tvNTSAInspectionDoc)
            registerRequestMap[ApiConstant.NTSA_EXP_DATE] = getRequestBody(NTSAInspectionExp)
            registerRequestMap[ApiConstant.VEHICLE_LOG_BOOK] = getRequestBody(tvVehicleLogBookDoc)
            registerRequestMap[ApiConstant.DRIVER_LICENCE_IMAGE] =
                getRequestBody(tvDriverLicenceDoc)
            registerRequestMap[ApiConstant.DRIVER_LICENCE_EXP_DATE] =
                getRequestBody(driverLicenceExp)
            registerRequestMap[ApiConstant.NATIONAL_ID_IMAGE] = getRequestBody(nationalIdIImage)
            registerRequestMap[ApiConstant.POLICE_CLEARNCE_CERTI] =
                getRequestBody(tvPoliceReceiptDoc)
            registerRequestMap[ApiConstant.NATIONAL_ID_NUMBER] = getRequestBody(nationIdNo)
            registerRequestMap[ApiConstant.VEHICLE_COLOR] = getRequestBody(color)
            registerRequestMap[ApiConstant.COMREHENSIVE_IMAGE] =
                getRequestBody(tvPSVComprehensiveInsuranceDoc)
            registerRequestMap[ApiConstant.NTSA_BUDGE] = getRequestBody(tvNTSABudgeCertificateDoc)
            registerRequestMap[ApiConstant.BUDGE_EXP] = getRequestBody(NTSABudgeCetrificateExp)
            registerRequestMap[ApiConstant.COMREHENSIVE_EXP] =
                getRequestBody(PsvComprehensiveInsuranceExp)
            registerRequestMap[ApiConstant.POLICE_CLEARNCE_EXP] =
                getRequestBody(NTSABudgeCetrificateExp)
            registerRequestMap[ApiConstant.CAR_INSIDE] = getRequestBody(imageInside)
            Log.e("TAG", "registerApi: $profileImage")

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        response = networkService.register(registerRequestMap, image)
                        PrintLog.e(PrintLog.TAG, "$response")
                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            PrintLog.e(
                                PrintLog.TAG,
                                "${baseDataSource.getResult(true) { this }}"
                            )
                            registerObserver.value =
                                baseDataSource.getResult(true) { this }
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            registerObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                502
                            )
                        }
                    }
                } catch (e: Exception) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            registerObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                500
                            )
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    registerObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }

    private fun getRequestBody(name: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
    }

    private fun getMultipartImages(imagePath: Uri?, context: Context): MultipartBody.Part? {
        val documentImage = File(PathUtil.getUriRealPath(context, imagePath!!)!!)

        if (docFile != null) {

            val requestImageFile =
                documentImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            return MultipartBody.Part.createFormData(
                ApiConstant.WEB_PARAM_DOC_IMAGE_REGISTER,
                documentImage.name,
                requestImageFile
            )
        }
        return null
    }

    private fun getMultipartDocImages(imagePath: Uri?, context: Context): MultipartBody.Part? {
        var documentImage: File = File(PathUtil.getUriRealPath(context, imagePath!!)!!)

        if (docFile != null) {

            val requestImageFile =
                documentImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            return MultipartBody.Part.createFormData(
                ApiConstant.WEB_PARAM_DOC_IMAGE,
                documentImage.name,
                requestImageFile
            )
        }
        return null
    }
}