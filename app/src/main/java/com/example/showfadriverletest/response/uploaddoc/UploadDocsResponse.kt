package com.example.showfadriverletest.response.uploaddoc

import com.example.showfadriverletest.response.BaseResponse
import com.google.gson.annotations.SerializedName

data class UploadDocsResponse(
    @field:SerializedName("url")
    val url: String? = null,
) : BaseResponse()
