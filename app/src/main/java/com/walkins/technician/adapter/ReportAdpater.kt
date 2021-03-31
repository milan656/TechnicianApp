package com.walkins.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter

class ReportAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ReportAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.design_report_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ReportAdpater.Viewholder, position: Int) {


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