package com.walkins.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
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

        holder.chkTyreSuggestion.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                if (isChecked) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.chkTyreSuggestion.setButtonTintList(
                            getColorStateList(
                                context,
                                R.color.colorPrimary
                            )
                        )
                    }
                    holder.chkTyreSuggestion?.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_blue_corner))
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.chkTyreSuggestion.setButtonTintList(
                            getColorStateList(
                                context,
                                R.color.header_title
                            )
                        )
                    }
                    holder.chkTyreSuggestion?.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_red_corner))

                }
            }

        })
    }

    override fun getItemCount(): Int {
        return array.size

    }

}