package com.example.showfadriverletest.ui.earning.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.databinding.ItemEarningHistoryBinding
import com.example.showfadriverletest.response.earning.EarningResponse

class EarningHistoryAdapter(
    var context: Context,
    var historyModel: ArrayList<EarningResponse.EarningsItem>,
    var callback: (Position: Int) -> Unit,
) :
    RecyclerView.Adapter<EarningHistoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemEarningHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = historyModel.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = historyModel[position]
        holder.binding.tvCustomerName.text = data.id
        holder.binding.tvPaymentAmount.text = data.driverAmount
        holder.binding.tvRideDateTime.text = data.dropoffTime

        holder.itemView.setOnClickListener {
            callback.invoke(position)
        }
    }

    inner class MyViewHolder(var binding: ItemEarningHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}