package com.example.showfadriverletest.ui.register.registerprofile.viewmodel

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
import com.example.showfadriverletest.response.uploaddoc.UploadDocsResponse
import com.example.showfadriverletest.response.vehicledetail.VehicleManufacturerResponse
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import com.example.showfadriverletest.util.file.PathUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.net.ConnectException
import javax.inject.Inject

class VehicleDetailViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {

    var docFile: Uri? = null

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
    /** Upload doc api*/

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
                        response = networkService.uploadDoc(image)
                        Log.d("response--------------------------->", "${response.message()}")
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
