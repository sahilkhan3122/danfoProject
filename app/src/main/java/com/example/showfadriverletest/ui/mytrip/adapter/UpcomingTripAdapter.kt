package com.example.showfadriverletest.ui.mytrip.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.R
import com.example.showfadriverletest.databinding.ItemUpcomingTripBinding
import com.example.showfadriverletest.response.featuretrip.FeatureTripResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpcomingTripAdapter(
    var context: Context,
    var upcomingList: ArrayList<FeatureTripResponse.DataItem>,
    var callback: (Position: Int,timeStamp:String) -> Unit,
) :
    RecyclerView.Adapter<UpcomingTripAdapter.MyViewHolder>() {

    var pickup = ""
    var dropoff = ""
    var referenceId = ""
    var pickupDateTime = ""
    var tripDuration = ""
    var arrivedTime = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemUpcomingTripBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = upcomingList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = upcomingList[position]
        var timeStamp = convertTimestampToTime(data.bookingTime!!.toLong())
        pickup = data.pickupLocation.toString()
        dropoff = data.dropoffLocation.toString()
        referenceId = data.id.toString()
        pickupDateTime = data.pickupDateTime.toString()
        tripDuration = data.bookingTime.toString()
        arrivedTime = data.arrivedTime.toString()

        if (data.status.toString() == context.getString(R.string.completed)) {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.colorGreen))
        } else if (data.status.toString() == context.getString(com.example.showfadriverletest.R.string.pending)) {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.appPickColor))
        } else {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.red_dark))
        }

        holder.binding.tvStatus.text = data.status
        holder.binding.pickUpDropOff.tvPickupLocation.text = pickup
        holder.binding.pickUpDropOff.tvDropOffLocation.text = dropoff
        holder.binding.tvTripId.text = "ID: $referenceId"
        holder.binding.tvDateTime.text = pickupDateTime
        holder.binding.layoutTrip.tvTripDuration.text = timeStamp +" ago"
        holder.binding.layoutTrip.tvGrandTotal.text = arrivedTime
        holder.itemView.setOnClickListener {
            callback.invoke(position,timeStamp)

        }
    }

    inner class MyViewHolder(var binding: ItemUpcomingTripBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        return dateFormat.format(date)
    }
}
