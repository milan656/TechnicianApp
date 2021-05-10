package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter

class ReportSkippAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ReportSkippAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportSkippAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.design_report_skiped_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ReportSkippAdpater.Viewholder, position: Int) {


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