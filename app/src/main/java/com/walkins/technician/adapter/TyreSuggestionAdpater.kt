package com.walkins.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter

class TyreSuggestionAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter,
    var isFromDialog: Boolean
) :
    RecyclerView.Adapter<TyreSuggestionAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var chkTyreSuggestion: CheckBox = itemView.findViewById(R.id.chkTyreSuggestion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TyreSuggestionAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.tyre_suggestions_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: TyreSuggestionAdpater.Viewholder, position: Int) {

        holder.chkTyreSuggestion.text = array.get(position).capitalize()

        holder.itemView.setOnClickListener {

            if (!isFromDialog) {
                if (onclick != null) {
                    onclick?.onPositionClick(position, 0)
                }
            } else {
                if (onclick != null) {
                    onclick?.onPositionClick(position, 1)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }

}