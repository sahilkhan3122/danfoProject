package com.example.showfadriverletest.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.databinding.RowNotificationItemBinding
import com.example.showfadriverletest.response.notificationlist.NotificationResponse

class NotificationAdapter(
    var context: Context,
    var notificationList: ArrayList<NotificationResponse.DataItem>,
    var callback: (Position: Int) -> Unit,
) :
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =
            RowNotificationItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {
        var data = notificationList[position]
        holder.binding.tvDescription.text = data.description
        holder.binding.tvDate.text = data.createdDate
    }

    override fun getItemCount(): Int = notificationList.size

    inner class MyViewHolder(var binding: RowNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
