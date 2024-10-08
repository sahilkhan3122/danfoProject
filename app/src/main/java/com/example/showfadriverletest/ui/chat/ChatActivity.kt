package com.example.showfadriverletest.ui.chat

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivityChatBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.chat.ChatResponse
import com.example.showfadriverletest.service.TAG
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.view.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding, ChatViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_chat
    override val bindingVariable: Int
        get() = BR.viewModel
    var chatAdapter: ChatAdapter? = null
    var chatApiModel: ArrayList<ChatResponse.DataItem> = ArrayList()

    var customerId = ""
    var driverId = ""
    var bookingId = ""
    var driverName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFuc()
    }

    private fun initFuc() {

        if (intent != null) {
            customerId = intent.getStringExtra("customerId").toString()
            driverId = intent.getStringExtra("driverId").toString()
            bookingId = intent.getStringExtra("bookingId").toString()
            driverName = intent.getStringExtra("driverName").toString()
        }
        binding.chatToolbar.txtTittle.text = driverName
        binding.chatToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        mViewModel.callChatHistoryApi(bookingId)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setUpObserver() {
        mViewModel.getChatObserver().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data!!.status) {
                        val layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        Log.d(TAG, "SUCCESS====>${it.data.data}")
                        for (message in it.data.data!!) {
                            if (message.senderType == "customer") {
                                chatApiModel.add(
                                    ChatResponse.DataItem(
                                        id = message.id,
                                        viewType = 1,
                                        bookingId = message.bookingId,
                                        senderId = message.senderId,
                                        message = message.message,
                                        senderType = message.senderType,
                                        createdAt = message.createdAt.toString(),
                                        receiverId = message.receiverId,
                                        receiverType = message.receiverType
                                    )
                                )
                            } else if (message.senderType == "driver") {
                                chatApiModel.add(
                                    ChatResponse.DataItem(
                                        id = message.id,
                                        viewType = 2,
                                        bookingId = message.bookingId,
                                        senderType = message.senderType,
                                        senderId = message.senderId,
                                        message = message.message,
                                        createdAt = message.createdAt.toString(),
                                        receiverId = message.receiverId,
                                        receiverType = message.receiverType
                                    )
                                )
                            }
                        }
                        binding.chatHistoryRecyclerView.layoutManager = layoutManager
                        chatAdapter = ChatAdapter(this, chatApiModel)
                        binding.chatHistoryRecyclerView.adapter = chatAdapter
                        binding.chatHistoryRecyclerView.adapter?.notifyDataSetChanged()
                        if (chatApiModel.size != 0) {
                            binding.chatHistoryRecyclerView.scrollToPosition(chatApiModel.size - 1)
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                            it.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(it.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                    showFailAlert(it.message!!)
                }

                Resource.Status.LOADING -> {
                    Log.d(ContentValues.TAG, "loading=>${it.message}")
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    Log.d(ContentValues.TAG, "no internet=>${it.message}")
                    binding.root.showSnackBar(getString(R.string.no_internet_connection))
                }

                else -> {}
            }
        }

           mViewModel.getChatHistoryObserver().observe(this) {
               when (it.status) {
                   Resource.Status.SUCCESS -> {
                       myDialog.hide()
                       if (it.data!!.status) {
                           val layoutManager =
                               LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                           for (message in it.data.data!!) {
                               if (message.senderType == "customer") {
                                   chatApiModel.add(
                                       ChatResponse.DataItem(
                                           id = message.id,
                                           viewType = 1,
                                           bookingId = message.bookingId,
                                           senderId = message.senderId,
                                           message = message.message,
                                           senderType = message.senderType,
                                           createdAt = message.createdAt,
                                           receiverId = message.receiverId,
                                           receiverType = message.receiverType
                                       )
                                   )
                               } else if (message.senderType == "driver") {
                                   chatApiModel.add(
                                       ChatResponse.DataItem(
                                           id = message.id,
                                           viewType = 2,
                                           bookingId = message.bookingId,
                                           senderType = message.senderType,
                                           senderId = message.senderId,
                                           message = message.message,
                                           createdAt = message.createdAt,
                                           receiverId = message.receiverId,
                                           receiverType = message.receiverType
                                       )
                                   )
                               }
                           }
                           binding.chatHistoryRecyclerView.layoutManager = layoutManager
                           chatAdapter = ChatAdapter(this, chatApiModel)
                           binding.chatHistoryRecyclerView.adapter = chatAdapter
                           binding.chatHistoryRecyclerView.adapter?.notifyDataSetChanged()
                           if (chatApiModel.size != 0) {
                               binding.chatHistoryRecyclerView.scrollToPosition(chatApiModel.size - 1)
                           }

                       } else {
                           showFailAlert(it.data.message)
                       }
                   }

                   Resource.Status.ERROR -> {
                       myDialog.hide()
                       lifecycleScope.launch {
                           if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                               it.message?.let { message ->
                                   if (CommonFun.checkIsConnectionReset(it.code)) {
                                       getString(R.string.no_internet)
                                   } else {
                                       message
                                   }
                               }
                           }
                       }
                       Log.d(ContentValues.TAG, "on error----------------=>${it.message}")
                   }

                   Resource.Status.LOADING -> {
                       myDialog.show()
                       Log.d(ContentValues.TAG, "loading=>${it.message}")
                   }

                   Resource.Status.NO_INTERNET_CONNECTION -> {
                       myDialog.hide()
                       Log.d(ContentValues.TAG, "no internet=>${it.message}")
                       isInternetConnected = false
                   }

                   else -> {}
               }
           }
    }

    fun chatWithCustomer(view: View) {
        val message = binding.txtMessage.text.toString()
        mViewModel.chatApiCall(bookingId, driverId, customerId, message)
      /*  hideSoftKeyboard(this)*/
        binding.txtMessage.text?.clear()
    }
}