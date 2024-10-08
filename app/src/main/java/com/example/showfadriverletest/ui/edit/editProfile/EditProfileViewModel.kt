package com.example.showfadriverletest.ui.edit.editProfile

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.updatedata.EditProfileResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import com.example.showfadriverletest.util.file.RealPathUtil
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

class EditProfileViewModel
@Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {

    var userProfile: Uri? = null
    var fname = ""
    var lName = ""
    var email = ""
    var number = ""
    var dob = ""
    var carType = ""
    var gender = ""
    var address = ""
    var ownname = ""
    var ownMail = ""
    var ownMobile = ""
    var id = ""

    private val profileInfoObserver: MutableLiveData<Resource<EditProfileResponse>> =
        MutableLiveData()

    fun getProfileInfoObservable(): LiveData<Resource<EditProfileResponse>> {
        return profileInfoObserver
    }

    private fun getMultipartImage(imagePath: Uri?, context: Context): MultipartBody.Part {
        var documentImage: File? = null
        var requestImageFile: RequestBody? = null
        if (RealPathUtil.getRealPath(context, imagePath!!) != null) {

            documentImage = File(RealPathUtil.getRealPath(context, imagePath)!!)
            requestImageFile =
                documentImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        return MultipartBody.Part.createFormData(
            ApiConstant.WEB_PARAM_PROFILE_IMAGE,
            documentImage?.name,
            requestImageFile!!
        )
    }

    private fun getRequestBody(name: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
    }

    @SuppressLint("SuspiciousIndentation")
    fun profileInfo() {
        if (NetworkUtils.isNetworkConnected(context)) {
            profileInfoObserver.value = Resource.loading()
            lateinit var response: Response<EditProfileResponse>
            val profileInfoRequestMap = HashMap<String, RequestBody>()
            var image: MultipartBody.Part? = null
            if (userProfile != null) {

                image =
                    getMultipartImage(
                        userProfile!!,
                        (context as ContextWrapper).baseContext
                    )
            }
            profileInfoRequestMap[ApiConstant.DRIVER_ID] = getRequestBody(id)
            profileInfoRequestMap[ApiConstant.CARTYPE] = getRequestBody(carType)
            profileInfoRequestMap[ApiConstant.FIRSTNAME] = getRequestBody(fname)
            profileInfoRequestMap[ApiConstant.LASTNAME] = getRequestBody(lName)
            profileInfoRequestMap[ApiConstant.GENDERR] = getRequestBody(gender)
            profileInfoRequestMap[ApiConstant.PAYMENTMETHOD] = getRequestBody("card")
            profileInfoRequestMap[ApiConstant.ADDRESS] = getRequestBody(address)
            profileInfoRequestMap[ApiConstant.DOB] = getRequestBody(dob)
            profileInfoRequestMap[ApiConstant.MOBILENO] = getRequestBody(number)
            profileInfoRequestMap[ApiConstant.EMAIL] = getRequestBody(email)
            profileInfoRequestMap[ApiConstant.OWNERNAME] = getRequestBody(ownname)
            profileInfoRequestMap[ApiConstant.OWNEREMAIL] = getRequestBody(ownMail)
            profileInfoRequestMap[ApiConstant.OWNERNO] = getRequestBody(ownMobile)
            Log.e(PrintLog.TAG, "profileInfoRequestMap => $profileInfoRequestMap")

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        response =
                            networkService.editProfile(profileInfoRequestMap, image)
                    }
                    PrintLog.e(PrintLog.TAG, "$response")


                    withContext(Dispatchers.Main) {
                        response.run {
                            profileInfoObserver.value = baseDataSource.getResult(true) { this }
                            PrintLog.e(
                                PrintLog.TAG,
                                "${baseDataSource.getResult(true) { this }}"
                            )
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            profileInfoObserver.value = Resource(
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
                            profileInfoObserver.value = Resource(
                                Resource.Status.UNKNOWN,
                                null,
                                e.localizedMessage?.toString().plus("- " + e.cause),
                                500
                            )
                        }
                    }
                }
            }
        }
    }
}
