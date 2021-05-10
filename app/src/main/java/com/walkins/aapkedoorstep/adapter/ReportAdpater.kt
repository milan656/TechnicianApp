package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter

class ReportAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ReportAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvReportVehicleName: TextView = itemView.findViewById(R.id.tvReportVehicleName)
        var tvReportVehicleNumber: TextView = itemView.findViewById(R.id.tvReportVehicleNumber)

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

        if (position == 0) {
            holder.tvReportVehicleNumber?.text = "Maruti"
            holder.tvReportVehicleName?.text = "GJ01RY2563"
        }
        if (position == 1) {
            holder.tvReportVehicleNumber?.text = "Toyota Innova"
            holder.tvReportVehicleName?.text = "GJ01RY9856"
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }

}