package com.walkins.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter

class HomeListAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<HomeListAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        var tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.design_home_adapter_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: HomeListAdpater.Viewholder, position: Int) {

//        holder.tvContent.text = array.get(position).capitalize()

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }

}