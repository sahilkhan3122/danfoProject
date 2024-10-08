package com.example.showfadriverletest.network

import com.example.showfadriverletest.response.BaseResponse
import com.example.showfadriverletest.util.PrintLog
import retrofit2.Response


open class BaseDataSource {
    open suspend fun <T> getResult(
        isInit: Boolean = false,
        call: suspend () -> Response<T>,
    ): com.example.showfadriverletest.network.Resource<T & Any> {
        try {
            val response = call()
            PrintLog.e("responseCode", "isSuccessful => ${response.code()}")
            if (response.code() == 403) {
                return com.example.showfadriverletest.network.Resource.error("Something went wrong, please try after sometime", code = response.code())
            } else if (response.code() == 502) {
                return com.example.showfadriverletest.network.Resource.error("Something went wrong, please try after sometime", code = response.code())
            } else if (response.code() != 200) {
                return com.example.showfadriverletest.network.Resource.error(
                    "Something went wrong, please try after sometime",
                    code = response.code()
                )
            } else if (response.body() != null) {
                val baseResponse = (response.body() as BaseResponse)
                PrintLog.e(
                    "responseCode",
                    "is If condition => ${(response.isSuccessful && baseResponse.status!!)}"
                )

                if (response.isSuccessful && baseResponse.status!!) {
                    response.body()?.let {
                        return com.example.showfadriverletest.network.Resource.success(it, baseResponse.message)
                    }
                } else {
                    if (isInit) {
                        response.body()?.let {
                            return com.example.showfadriverletest.network.Resource.success(it, baseResponse.message)
                        }
                    } else {
                        val body = response.body()
                        PrintLog.e("response.body", "response.body => ${response.body()}")

                        if (body != null) {
                            PrintLog.e("response.body", "response.bodyyyyy => ${response.body()}")
                            PrintLog.e(
                                "response.body",
                                "response.bodyyyyy11111 => ${baseResponse.message}"
                            )

                            return com.example.showfadriverletest.network.Resource.error(baseResponse.message!!)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return com.example.showfadriverletest.network.Resource.error("Error => ${e.message} ?: ${e.toString()}")
        }
        return com.example.showfadriverletest.network.Resource.error("Internet Connection Issue")
    }

    /* private fun <T> error(message: String): Resource<T> {
         return .error(message)
     }*/
}