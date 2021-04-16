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

class ServicesListAdpater(
    var array: ArrayList<String>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ServicesListAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardAddService: CardView = itemView.findViewById(R.id.cardAddService)
        var ivCarimg: ImageView = itemView.findViewById(R.id.ivCarimg)
        var lllineView: LinearLayout = itemView.findViewById(R.id.lllineView)
        var tvVehicleNumber: TextView = itemView.findViewById(R.id.tvVehicleNumber)
        var tvVehicleName: TextView = itemView.findViewById(R.id.tvVehicleName)
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

        if (position == 0) {
            holder.ivCarimg?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_car_image))
            holder.lllineView?.setBackgroundColor(context.resources.getColor(R.color.red))
            holder.tvVehicleName?.text = "Toyota Innova"
            holder.tvVehicleNumber.text = "GJ01HV4521"
        } else if (position == 1) {
            holder.ivCarimg?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_car_image1))
            holder.lllineView?.setBackgroundColor(context.resources.getColor(R.color.green))
            holder.tvVehicleName?.text = "Maruti"
            holder.tvVehicleNumber.text = "GJ01HV0015"
        } else if (position == 2) {
            holder.ivCarimg?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_no_car_image))
            holder.lllineView?.setBackgroundColor(context.resources.getColor(R.color.blue_color))
            holder.tvVehicleName?.text = "Maruti Suzu..Dzire"
            holder.tvVehicleNumber.text = "GJ01HV3578"
        }


    }

    override fun getItemCount(): Int {
        return array.size

    }

}