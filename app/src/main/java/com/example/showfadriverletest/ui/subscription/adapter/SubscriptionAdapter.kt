package com.example.showfadriverletest.ui.subscription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.R
import com.example.showfadriverletest.databinding.RowSubscriptionLayoutBinding
import com.example.showfadriverletest.response.subscription.SubscriptionResponse

class SubscriptionAdapter(
    var context: Context,
    var subscriptionList: ArrayList<SubscriptionResponse.SubscriptionItem>,
    var callback: (Position: Int) -> Unit,
) :
    RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =
            RowSubscriptionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = subscriptionList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = subscriptionList[position]

        holder.binding.tvTitle.text = data.name
        holder.binding.tvAmount.text = context.getString(R.string.kes) + " " + "${data.amount}"
        holder.binding.tvDesc.text = data.description
        holder.binding.tvSubscriptionType.text = data.type

        holder.binding.tvSubscribeNow.setOnClickListener {
            callback.invoke(position)
        }
    }

    inner class MyViewHolder(var binding: RowSubscriptionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
