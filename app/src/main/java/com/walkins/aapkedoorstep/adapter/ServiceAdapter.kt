package com.walkins.aapkedoorstep.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.service.ServiceModelData
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat", "SetTextI18n")
class ServiceAdapter(
    var array: ArrayList<ServiceModelData>,
    var context: Context,
    onPositionClick: onClickAdapter
) : RecyclerView.Adapter<ServiceAdapter.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var chkNitrogenTopup: CheckBox = itemView.findViewById(R.id.chkNitrogenTopup)
        var ivServiceImage: ImageView = itemView.findViewById(R.id.ivServiceImage)
        var tvServiceName: TextView = itemView.findViewById(R.id.tvServiceName)
        var llSpacing: LinearLayout = itemView.findViewById(R.id.llSpacing)
        var llServiceLayout: LinearLayout = itemView.findViewById(R.id.llServiceLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceAdapter.Viewholder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.design_service_adapter, parent, false)
        return Viewholder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ServiceAdapter.Viewholder, position: Int) {
        holder.chkNitrogenTopup.setTag(position)
        holder.tvServiceName.text = array.get(position).name.capitalize(Locale.getDefault())

        if (array.get(position).isSelected) {
            holder.chkNitrogenTopup.isChecked = true
        } else {
            holder.chkNitrogenTopup.isChecked = false
        }

        Log.e("getdataa", "" + array.get(position).name)
        Log.e("getdataa", "" + array.get(position).image)

        if (position != 0 && position != 1) {
            holder.llSpacing.visibility = View.VISIBLE
        } else {
            holder.llSpacing.visibility = View.GONE
        }

        try {
            Glide.with(context).load(array.get(position).image).into(holder.ivServiceImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.llServiceLayout.setOnClickListener {

            /* val pos: Int = holder.chkNitrogenTopup.getTag() as Int
             try {
                 array.get(pos).isSelected = !array.get(pos).isSelected
             } catch (e: IndexOutOfBoundsException) {
                 e.printStackTrace()
             }

             if (onclick != null) {
                 onclick?.onPositionClick(position, 11)
             }*/
            if (holder.chkNitrogenTopup.isChecked) {
                holder.chkNitrogenTopup.isChecked = false
            } else {
                holder.chkNitrogenTopup.isChecked = true
            }
            clickOperation(holder, position)

        }

        holder.chkNitrogenTopup.setOnClickListener {
            /* val pos: Int = holder.chkNitrogenTopup.getTag() as Int
             try {
                 array.get(pos).isSelected = !array.get(pos).isSelected
             } catch (e: IndexOutOfBoundsException) {
                 e.printStackTrace()
             }

             holder.llServiceLayout.performClick()*/

            clickOperation(holder, position)


        }

        if (array.get(position).isDisable) {
            holder.llServiceLayout.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_border_light_gray))
            holder.llServiceLayout.isClickable = false
            holder.llServiceLayout.isEnabled = false
            holder.chkNitrogenTopup.visibility = View.INVISIBLE
            holder.tvServiceName.setTextColor(context.resources.getColor(R.color.light_gray_text_color))
        } else {
            holder.llServiceLayout.setBackgroundDrawable(context.resources?.getDrawable(R.drawable.layout_bg_secondary_))
            holder.llServiceLayout.isClickable = true
            holder.llServiceLayout.isEnabled = true
            holder.chkNitrogenTopup.visibility = View.VISIBLE
            holder.tvServiceName.setTextColor(context.resources.getColor(R.color.text_color1))
        }

    }

    override fun getItemCount(): Int {
        return array.size

    }

    fun clickOperation(holder: Viewholder, position: Int) {
        val pos: Int = holder.chkNitrogenTopup.getTag() as Int
        try {
            array.get(pos).isSelected = !array.get(pos).isSelected
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }


        if (onclick != null) {
            onclick?.onPositionClick(position, 11)
        }
    }
}