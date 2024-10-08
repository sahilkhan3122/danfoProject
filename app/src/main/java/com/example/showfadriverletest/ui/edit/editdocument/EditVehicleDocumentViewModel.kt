package com.example.showfadriverletest.ui.edit.editdocument

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.register.RegisterResponse
import com.example.showfadriverletest.response.updatedata.EditVehicleDocResponse
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

class EditVehicleDocumentViewModel
@Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {
    var docFile: Uri? = null


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
    var nationalId = ""
    lateinit var profileImage: Uri
    var color = ""
    var id = ""

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

    private val uploadDocObserver: MutableLiveData<Resource<UploadDocsResponse>> =
        MutableLiveData()

    fun getUploadDocsObservableDoc(): LiveData<Resource<UploadDocsResponse>> {
        return uploadDocObserver
    }


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

    private val editVehicleDocObserver: MutableLiveData<Resource<EditVehicleDocResponse>> =
        MutableLiveData()

    fun getEditVehicleDocObservable(): LiveData<Resource<EditVehicleDocResponse>> {
        return editVehicleDocObserver
    }


    fun editVehicleDocApi() {
        if (NetworkUtils.isNetworkConnected(context)) {

            editVehicleDocObserver.value = Resource.loading()
            lateinit var response: Response<EditVehicleDocResponse>
            var image: MultipartBody.Part? = null

            val registerRequestMap = HashMap<String, RequestBody>()
            registerRequestMap[ApiConstant.DRIVER_ID] = getRequestBody(id)
            registerRequestMap[ApiConstant.NATIONAL_ID_NUMBER] = getRequestBody(nationalId)
            registerRequestMap[ApiConstant.NATIONAL_ID_IMAGE] = getRequestBody(nationalIdIImage)
            registerRequestMap[ApiConstant.DRIVER_LICENCE_IMAGE] = getRequestBody(tvDriverLicenceDoc)
            registerRequestMap[ApiConstant.DRIVER_LICENCE_EXP_DATE] = getRequestBody(driverLicenceExp)
            registerRequestMap[ApiConstant.NTSA_BUDGE] = getRequestBody(tvNTSABudgeCertificateDoc)
            registerRequestMap[ApiConstant.BUDGE_EXP] = getRequestBody(NTSABudgeCetrificateExp)
            registerRequestMap[ApiConstant.POLICE_CLEARNCE_CERTI] = getRequestBody(tvPoliceReceiptDoc)
            registerRequestMap[ApiConstant.COMREHENSIVE_EXP] = getRequestBody(PsvComprehensiveInsuranceExp)
            registerRequestMap[ApiConstant.VEHICLE_LOG_BOOK] = getRequestBody(tvVehicleLogBookDoc)
            registerRequestMap[ApiConstant.NTSA_INSPECTION_IMAGE] = getRequestBody(tvNTSAInspectionDoc)
            registerRequestMap[ApiConstant.NTSA_EXP_DATE] = getRequestBody(NTSAInspectionExp)
            registerRequestMap[ApiConstant.COMREHENSIVE_IMAGE] = getRequestBody(tvPSVComprehensiveInsuranceDoc)
            registerRequestMap[ApiConstant.VEHICLE_INSURANCE] = getRequestBody(PsvComprehensiveInsuranceExp)


            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        Constants.EditVehicleDetail = false
                        Constants.UploadDoc = false
                        response = networkService.editVehicleDocument(registerRequestMap)
                        PrintLog.e(PrintLog.TAG, "$response")
                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            PrintLog.e(
                                PrintLog.TAG,
                                "${baseDataSource.getResult(true) { this }}"
                            )
                            editVehicleDocObserver.value =
                                baseDataSource.getResult(true) { this }
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            editVehicleDocObserver.value = Resource(
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
                            editVehicleDocObserver.value = Resource(
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
                    editVehicleDocObserver.value = Resource.noInternetConnection(null)
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