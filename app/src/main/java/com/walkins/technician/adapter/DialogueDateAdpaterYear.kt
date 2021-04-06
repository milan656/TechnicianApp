package com.walkins.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.date.DateModel

class DialogueDateAdpaterYear(
    var array: ArrayList<DateModel>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<DialogueDateAdpaterYear.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvContent: TextView = itemView.findViewById(R.id.tvContent)
        var llmaincontent: LinearLayout = itemView.findViewById(R.id.llmaincontent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DialogueDateAdpaterYear.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.dialogue_common_design_date_year, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: DialogueDateAdpaterYear.Viewholder, position: Int) {

        holder.tvContent.text = array.get(position).name.capitalize()

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                if (array.get(position).isSelected) {
                    // name!!.get(position).isSelected = false;
                    if (array != null && array?.size!! > 0) {
                        for (i in array?.indices!!) {
//                        arr?.get(i)?.setTypeface(Typeface.DEFAULT)
                            array?.get(i)?.isSelected=false
                        }
                    }
                    holder.tvContent.setBackgroundDrawable(context.resources.getDrawable(R.drawable.tyre_config))
                } else {
                    for (date in array) {
                        if (date.isSelected) {
                            date.isSelected = false


                        }
                    }

                    array.get(position).isSelected = true;

                }
                notifyDataSetChanged()
                onclick?.onPositionClick(position, 5)
            }
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }

}