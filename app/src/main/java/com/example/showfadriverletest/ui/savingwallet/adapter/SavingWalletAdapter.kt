package com.example.showfadriverletest.ui.savingwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.databinding.ItemWalletHistoryBinding
import com.example.showfadriverletest.response.savingwallet.SavingWalletHistoryResponse

class SavingWalletAdapter (
    var context: Context,
    var historyModel: ArrayList<SavingWalletHistoryResponse.DataItem>,
    var callback: (Position: Int) -> Unit,
) :
    RecyclerView.Adapter<SavingWalletAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemWalletHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = historyModel.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = historyModel[position]
        holder.binding.tvCustomerName.text = data.description
        if(data.status == "failed"){
            holder.binding.tvStatus.setTextColor(context.getColor(com.example.showfadriverletest.R.color.red_dark))
            holder.binding.tvPaymentAmount.setTextColor(context.getColor(com.example.showfadriverletest.R.color.red_dark))
        }else if(data.type == "-"){
            holder.binding.tvPaymentAmount.setTextColor(context.getColor(com.example.showfadriverletest.R.color.red_dark))
            holder.binding.tvStatus.setTextColor(context.getColor(com.example.showfadriverletest.R.color.colorGreen))
        }else{
            holder.binding.tvStatus.setTextColor(context.getColor(com.example.showfadriverletest.R.color.colorGreen))
            holder.binding.tvPaymentAmount.setTextColor(context.getColor(com.example.showfadriverletest.R.color.colorGreen))
        }
        holder.binding.tvStatus.text = data.status
        holder.binding.tvPaymentAmount.text ="${data.type}"+data.amount
        holder.binding.tvRideDateTime.text = data.createdAt

        holder.itemView.setOnClickListener {
            callback.invoke(position)
        }
    }

    inner class MyViewHolder(var binding: ItemWalletHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}