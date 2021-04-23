package com.walkins.technician.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.IssueResolveModel

class TyreSuggestionAdpater(
    var array: ArrayList<IssueResolveModel>,
    var context: Context,
    onPositionClick: onClickAdapter,
    var isFromDialog: Boolean
) :
    RecyclerView.Adapter<TyreSuggestionAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null
    var arrayChecked: ArrayList<IssueResolveModel>? = ArrayList()

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var chkTyreSuggestion: CheckBox = itemView.findViewById(R.id.chkTyreSuggestion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.tyre_suggestions_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.chkTyreSuggestion.text = array.get(position).issueName.capitalize()

        if (array.get(position).isSelected) {
            holder.chkTyreSuggestion.isChecked = true
            holder.chkTyreSuggestion?.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_blue_corner))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                holder.chkTyreSuggestion.setButtonTintList(
                    getColorStateList(
                        context,
                        R.color.colorPrimary
                    )
                )
            }
        } else {
            holder.chkTyreSuggestion.isChecked = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                holder.chkTyreSuggestion.setButtonTintList(
                    getColorStateList(
                        context,
                        R.color.header_title
                    )
                )
            }
            holder.chkTyreSuggestion.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_red_corner))

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

                    Log.e("clickcall", "000")
                    if (!isFromDialog) {
                        if (onclick != null) {
                            Log.e("clickcall", "0")
                            onclick?.onPositionClick(position, 5)
                        }
                    } else {
                        if (onclick != null) {
                            Log.e("clickcall", "1")
                            onclick?.onPositionClick(position, 6)
                        }
                    }

                    array.get(position).isSelected = true
                    arrayChecked?.add(array.get(position))
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.chkTyreSuggestion.setButtonTintList(
                            getColorStateList(
                                context,
                                R.color.header_title
                            )
                        )
                    }
                    holder.chkTyreSuggestion.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_red_corner))

                    if (!isFromDialog) {
                        if (onclick != null) {
                            Log.e("clickcall", "0")
                            onclick?.onPositionClick(position, 5)
                        }
                    } else {
                        if (onclick != null) {
                            Log.e("clickcall", "1")
                            onclick?.onPositionClick(position, 6)
                        }
                    }

                    array.get(position).isSelected = false
                    arrayChecked?.remove(array.get(position))

                }

            }

        })
    }

    override fun getItemCount(): Int {
        return array.size

    }

    fun getList(): ArrayList<IssueResolveModel>? {
        Log.e("clickcall", "" + arrayChecked?.size!!)
        return arrayChecked
    }
}