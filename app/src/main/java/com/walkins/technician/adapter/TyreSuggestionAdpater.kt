package com.walkins.technician.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.IssueResolveModel
import java.util.*
import kotlin.collections.ArrayList


class TyreSuggestionAdpater(
    var array: ArrayList<IssueResolveModel>,
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
    ): Viewholder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.tyre_suggestions_design, parent, false)


        return Viewholder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.chkTyreSuggestion.text = array.get(position).issueName.capitalize(Locale.getDefault())

        holder.chkTyreSuggestion.setTag(position)

        if (array.get(position).isSelected) {
            holder.chkTyreSuggestion.isChecked = true
            holder.chkTyreSuggestion.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_blue_corner))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.chkTyreSuggestion.setButtonTintList(
                    getColorStateList(
                        context,
                        R.color.colorPrimary
                    )
                )
            }
        } else {
            holder.chkTyreSuggestion.isChecked = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.chkTyreSuggestion.setButtonTintList(
                    getColorStateList(
                        context,
                        R.color.header_title
                    )
                )
            }
            holder.chkTyreSuggestion.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_red_corner))

        }

        holder.chkTyreSuggestion.setOnClickListener {
            val pos: Int = holder.chkTyreSuggestion.getTag() as Int
            try {
                array.get(pos).isSelected = !array.get(pos).isSelected
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }

            if (!isFromDialog) {
                if (onclick != null) {
                    onclick?.onPositionClick(position, 5)
                }
            } else {
                if (onclick != null) {
                    onclick?.onPositionClick(position, 6)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return array.size

    }


}