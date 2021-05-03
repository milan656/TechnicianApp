package com.walkins.technician.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.service.ServiceModelData
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CompletedServiceAdapter(
    var array: ArrayList<ServiceModelData>,
    var context: Context,
    onPositionClick: onClickAdapter
) : RecyclerView.Adapter<CompletedServiceAdapter.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivServiceImage: ImageView = itemView.findViewById(R.id.ivServiceImage)
        var tvServiceName: TextView = itemView.findViewById(R.id.tvservice)
        var llSpacing: LinearLayout = itemView.findViewById(R.id.llSpacing)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedServiceAdapter.Viewholder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.completed_design_service_adapter, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: CompletedServiceAdapter.Viewholder, position: Int) {
        holder.tvServiceName.text = array.get(position).name.capitalize()

        Log.e("getdataa", "" + array.get(position).name)
        Log.e("getdataa", "" + array.get(position).image)

        if (position != 0 && position != 1) {
            holder.llSpacing.visibility = View.VISIBLE
        } else {
            holder.llSpacing.visibility = View.GONE
        }

        try {
            Glide.with(context).load(array.get(position).image).into(holder.ivServiceImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return array.size

    }
}