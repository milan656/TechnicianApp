package com.walkins.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.servicelistmodel.ServiceListByDateData

class ServicesListAdpater(
    var array: ArrayList<ServiceListByDateData>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ServicesListAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardAddService: LinearLayout = itemView.findViewById(R.id.cardAddService)
        var ivCarimg: ImageView = itemView.findViewById(R.id.ivCarimg)
        var lllineView: LinearLayout = itemView.findViewById(R.id.lllineView)
        var tvVehicleNumber: TextView = itemView.findViewById(R.id.tvVehicleNumber)
        var tvVehicleName: TextView = itemView.findViewById(R.id.tvVehicleName)
        var tvColorName: TextView = itemView.findViewById(R.id.tvColorName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServicesListAdpater.Viewholder {
        var view =
            LayoutInflater.from(context)
                .inflate(R.layout.design_service_adapter_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ServicesListAdpater.Viewholder, position: Int) {

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }

        holder.cardAddService.setOnClickListener {
            if (onclick != null) {
                onclick?.onPositionClick(position, 1)
            }
        }

        holder.ivCarimg.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_car_image1))
        holder.tvVehicleName.text = "" + array.get(position).make + "," + array.get(position).model
        holder.tvVehicleNumber.text = array.get(position).regNumber
        holder.tvColorName.text = array.get(position).color
        if (array.get(position).color.equals("white", ignoreCase = true)) {
            holder.lllineView.setBackgroundColor(context.resources.getColor(R.color.white))
        }
        if (array.get(position).color.equals("blue", ignoreCase = true)) {
            holder.lllineView.setBackgroundColor(context.resources.getColor(R.color.blue_color))
        }
        if (array.get(position).color.equals("red", ignoreCase = true)) {
            holder.lllineView.setBackgroundColor(context.resources.getColor(R.color.red_color))
        }


    }

    override fun getItemCount(): Int {
        return array.size

    }

}