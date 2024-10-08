package com.example.showfadriverletest.ui.chat

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showfadriverletest.base.BaseViewModel
import com.example.showfadriverletest.common.ApiConstant
import com.example.showfadriverletest.common.ApiConstant.CHAT_HISTORY
import com.example.showfadriverletest.network.ApiService
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.BaseResponse
import com.example.showfadriverletest.response.chat.ChatHistoryResponse
import com.example.showfadriverletest.response.chat.ChatResponse
import com.example.showfadriverletest.util.NetworkUtils
import com.example.showfadriverletest.util.PrintLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    var context: Context,
    var networkService: ApiService,
) : BaseViewModel<Any>() {


    /**Chat Api*/
    private val chatObserver: MutableLiveData<Resource<ChatResponse>> =
        MutableLiveData()

    fun getChatObserver(): LiveData<Resource<ChatResponse>> {
        return chatObserver
    }

    fun chatApiCall(bookingId: String, driverId: String, customerId: String, message: String) {
        if (NetworkUtils.isNetworkConnected(context)) {
            var response: Response<ChatResponse>

            val chatRequest: MutableMap<String, String> = HashMap()
            chatRequest[ApiConstant.BOOKING_ID] = bookingId
            chatRequest[ApiConstant.SENDER_TYPE] = ApiConstant.DRIVER
            chatRequest[ApiConstant.SENDER_ID] = customerId
            chatRequest[ApiConstant.RECIEVER_ID] = driverId
            chatRequest[ApiConstant.RECIEVER_TYPE] = ApiConstant.CUSTOMER
            chatRequest[ApiConstant.MESSAGE] = message

            viewModelScope.launch {
                chatObserver.value = Resource.loading(null)

                withContext(Dispatchers.IO) {
                    response = networkService.chatApi(chatRequest)
                    Log.e("chatRequestResponse------------------>", "$response")
                }
                withContext(Dispatchers.Main) {
                    response.run {
                        chatObserver.value = baseDataSource.getResult { this!! }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    chatObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }


    private val chatHistoryObserver: MutableLiveData<Resource<ChatResponse>> =
        MutableLiveData()

    fun getChatHistoryObserver(): LiveData<Resource<ChatResponse>> {
        return chatHistoryObserver
    }

    fun callChatHistoryApi(id: String) {
        if (NetworkUtils.isNetworkConnected(context)) {

            chatHistoryObserver.value = Resource.loading()
            lateinit var response: Response<ChatResponse>
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        response = networkService.chatHistory(CHAT_HISTORY.plus(id))
                        PrintLog.e(PrintLog.TAG, "$response")

                    }
                    withContext(Dispatchers.Main) {
                        response.run {
                            chatHistoryObserver.value = baseDataSource.getResult(true) { this }
                            PrintLog.e(PrintLog.TAG, "${baseDataSource.getResult(true) { this }}")
                        }
                    }
                } catch (e: ConnectException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            chatHistoryObserver.value = Resource(
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
                            chatHistoryObserver.value = Resource(
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
                    chatHistoryObserver.value = Resource.noInternetConnection(null)
                }
            }
        }
    }
}