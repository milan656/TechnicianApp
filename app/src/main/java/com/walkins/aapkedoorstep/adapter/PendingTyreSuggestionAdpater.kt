package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import java.util.*

class PendingTyreSuggestionAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<PendingTyreSuggestionAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var chkTyreSuggestion: TextView = itemView.findViewById(R.id.chkTyreSuggestion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.pending_tyre_suggestions_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.chkTyreSuggestion.text = array.get(position).capitalize(Locale.getDefault())

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