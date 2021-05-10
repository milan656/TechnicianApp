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
import kotlin.collections.ArrayList

class DialogueAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<DialogueAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogueAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.dialogue_common_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: DialogueAdpater.Viewholder, position: Int) {

        holder.tvContent.text = array.get(position).capitalize(Locale.getDefault())

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                if (array.get(position).equals("Gallery") || array.get(position).equals("Camera")) {
                    onclick?.onPositionClick(position, 10)
                } else {
                    onclick?.onPositionClick(position, 11)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }

}