package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import java.text.SimpleDateFormat
import java.util.*

class HomeListAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<HomeListAdpater.Viewholder>(){

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivInfo: ImageView = itemView.findViewById(R.id.ivInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.design_home_adapter_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: HomeListAdpater.Viewholder, position: Int) {

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }

        holder.ivInfo?.setOnClickListener {
            if (onclick != null) {
                onclick?.onPositionClick(position, 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }



}