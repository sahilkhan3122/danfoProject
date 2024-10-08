package com.example.showfadriverletest.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.databinding.ItemCancelResonBinding
import com.example.showfadriverletest.response.canceltrip.CancelModel

class CancelTripAdapter( var context: Context,
var cancellationList: ArrayList<CancelModel>,
var callback: (position: Int,cancelModel: CancelModel) -> Unit
) : RecyclerView.Adapter<CancelTripAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCancelResonBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = cancellationList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPosition = cancellationList[position]
        holder.binding.txtDriverDelay.text = currentPosition.textCancellationName


        holder.binding.checkbox.setOnClickListener {
            cancellationList[position].isCheck = true
            cancellationList[position].textCancellationName
            Log.d("data", "data==>${holder.binding.txtDriverDelay.text}")
            Log.d("data", "isCheck==>${cancellationList[position].isCheck}")
            callback.invoke(position,cancellationList[position])
        }
    }

    inner class MyViewHolder(var binding: ItemCancelResonBinding) :
        RecyclerView.ViewHolder(binding.root)
}

