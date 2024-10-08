package com.example.showfadriverletest.ui.edit.editVehicledetaile

import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.Constants.EditVehicleDetail
import com.example.showfadriverletest.common.Constants.UploadDoc
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.editvehicleinfo.EditVehicleInfoResponse
import com.example.showfadriverletest.response.uploaddoc.UploadDocsResponse
import com.example.showfadriverletest.response.vehicledetail.VehicleManufacturerResponse
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
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.net.ConnectException
import javax.inject.Inject

class EditVehicleDetailViewModel
@Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {
    var docFile: Uri? = null

    var imageInside = ""
    var imageBack = ""
    var imageFront = ""
    var imageLeft = ""
    var imageRight = ""
    var id = ""

    var vehicleModelName = ""
    var vehicleModelId = ""
    var manufactureName = ""
    var manufactureId = ""
    var vehicleType = ""
    var color = ""
    var vehicleYear = ""
    var plateNo = ""

    private val manufactureVehicleResponseObserver: MutableLiveData<Resource<VehicleManufacturerResponse>> =
        MutableLiveData()

    fun getManufactureVehicleObservable(): LiveData<Resource<VehicleManufacturerResponse>> {
        return manufactureVehicleResponseObserver
    }

    fun vehicleManufactureDetailApi() {
        if (NetworkUtils.isNetworkConnected(context)) {

            lateinit var response: Response<VehicleManufacturerResponse>
            viewModelScope.launch {
                try {
                    manufactureVehicleResponseObserver.value = Resource.loading()
                    withContext(Dispatchers.IO) {
                        EditVehicleDetail = true
                        response =
                            networkService.vehicleManufacturer(ApiConstant.END_POINT_VEHICLE_LIST)
                        PrintLog.e(
                            "VehicleManufacturerResponse==========================>",
                            "$response"
                        )
                    }

                    withContext(Dispatchers.Main) {
                        response.run {
                            manufactureVehicleResponseObserver.value =
                                baseDataSource.getResult(true) { this }
                            Log.e(ContentValues.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            manufactureVehicleResponseObserver.value = Resource(
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
                            manufactureVehicleResponseObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage.let { "- " + e.cause },
                                500
                            )
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    manufactureVehicleResponseObserver.value = Resource.noInternetConnection(null)
                }
            }
        }

    }

    private val uploadDocObserver: MutableLiveData<Resource<UploadDocsResponse>> =
        MutableLiveData()

    fun getUploadDocsObservable(): LiveData<Resource<UploadDocsResponse>> {
        return uploadDocObserver
    }


    fun uploadDocsApi() {
        if (NetworkUtils.isNetworkConnected(context)) {

            uploadDocObserver.value = Resource.loading()
            lateinit var response: Response<UploadDocsResponse>
            var image: MultipartBody.Part? = null
            if (docFile != null) {

                image = getMultipartImages(
                    docFile,
                    (context as ContextWrapper).baseContext
                )
            }
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        UploadDoc
                        response = networkService.uploadDoc(image)
                        Log.d("response--------------------------->", response.message())
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


    private val editVehicleInfoObserver: MutableLiveData<Resource<EditVehicleInfoResponse>> =
        MutableLiveData()

    fun geteditVEhicleInfoObservable(): LiveData<Resource<EditVehicleInfoResponse>> {
        return editVehicleInfoObserver
    }

    fun editVehicleInfoApi() {
        if (NetworkUtils.isNetworkConnected(context)) {

            editVehicleInfoObserver.value = Resource.loading()
            lateinit var response: Response<EditVehicleInfoResponse>

            val registerRequestMap = HashMap<String, RequestBody>()
            registerRequestMap[ApiConstant.DRIVER_ID] = getRequestBody(id)
            registerRequestMap[ApiConstant.ACCOUNT_HOLDER_NAME] = getRequestBody("sahil")
            registerRequestMap[ApiConstant.BANK_NAME] = getRequestBody("Bob")
            registerRequestMap[ApiConstant.BANK_BRANCH] = getRequestBody("PATAN")
            registerRequestMap[ApiConstant.ACCOUNT_NUMBER] = getRequestBody("08418100000882")
            registerRequestMap[ApiConstant.VEHICLE_TYPE] = getRequestBody(vehicleType)
            registerRequestMap[ApiConstant.PLATE_NUMBER] = getRequestBody(plateNo)
            registerRequestMap[ApiConstant.YEAR_OF_MANUFACTURE] = getRequestBody(vehicleYear)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MODEL_NAME] = getRequestBody(vehicleModelName)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MODEL_ID] = getRequestBody(vehicleModelId)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MANUFACTURER_NAME] = getRequestBody(manufactureName)
            registerRequestMap[ApiConstant.VEHICLE_TYPE_MANUFACTURER_ID] = getRequestBody(manufactureId)
            registerRequestMap[ApiConstant.NO_OF_PASSANGER] = getRequestBody("1")
            registerRequestMap[ApiConstant.CAR_LEFT] = getRequestBody(imageLeft)
            registerRequestMap[ApiConstant.CAR_RIGHT] = getRequestBody(imageRight)
            registerRequestMap[ApiConstant.CAR_FRONT] = getRequestBody(imageFront)
            registerRequestMap[ApiConstant.CAR_BACK] = getRequestBody(imageBack)
            registerRequestMap[ApiConstant.VEHICLE_COLOR] = getRequestBody(color)
            registerRequestMap[ApiConstant.CAR_INSIDE] = getRequestBody(imageInside)

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        EditVehicleDetail = false
                        UploadDoc = false
                        response = networkService.editVehicleDetail(registerRequestMap)
                        PrintLog.e(PrintLog.TAG, "$response")
                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            PrintLog.e(
                                PrintLog.TAG,
                                "${baseDataSource.getResult(true) { this }}"
                            )
                            editVehicleInfoObserver.value =
                                baseDataSource.getResult(true) { this }
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            editVehicleInfoObserver.value = Resource(
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
                            editVehicleInfoObserver.value = Resource(
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
                    editVehicleInfoObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}
private fun getRequestBody(name: String): RequestBody {
    return name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

private fun getMultipartImages(imagePath: Uri?, context: Context): MultipartBody.Part {

    // val documentImage = File(RealPathUtil.getRealPath(context, imagePath!!)!!)
    val documentImage = File(PathUtil.getUriRealPath(context, imagePath!!)!!)

    val requestImageFile =
        documentImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        ApiConstant.WEB_PARAM_DOC_IMAGE,
        documentImage.name,
        requestImageFile
    )
}