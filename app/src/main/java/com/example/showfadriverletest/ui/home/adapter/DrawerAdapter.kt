package com.example.showfadriverletest.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showfadriverletest.databinding.ItemDrawerLayoutBinding
import com.example.showfadriverletest.response.drawermodel.DrawerModel

class DrawerAdapter(var context: Context, var drawerModel: ArrayList<DrawerModel>, var callback:(Position:Int)->Unit) :
    RecyclerView.Adapter<DrawerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemDrawerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = drawerModel.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(drawerModel[position].image).into(holder.binding.imgDrawer)
        holder.binding.txtDrawerData.text = drawerModel[position].text
        holder.binding.txtDrawerData.setOnClickListener {
            callback.invoke(position)
        }
    }

    inner class MyViewHolder(var binding: ItemDrawerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
