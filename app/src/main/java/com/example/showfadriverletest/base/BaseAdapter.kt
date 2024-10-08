package com.example.showfadriverletest.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<T : ViewDataBinding, N>(val context: Context?) :
    RecyclerView.Adapter<BaseAdapter<T, N>.MyViewHolder>() {

    abstract val layoutId: Int
    abstract val myList: MutableList<N>
    lateinit var mBinding: T


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )

        return MyViewHolder(mBinding)
    }

    abstract fun onBind(position: Int, item: N)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        onBind(position, myList[position])
    }

    override fun getItemCount(): Int {
        return if (myList.isNotEmpty())
            myList.size
        else
            0
    }

    inner class MyViewHolder(var binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}
