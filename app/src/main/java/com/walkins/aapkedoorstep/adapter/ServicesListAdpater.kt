package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.TyreKey
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateData

class ServicesListAdpater(
    var array: MutableList<ServiceListByDateData>,
    var context: Context,
    onPositionClick: onClickAdapter,
    var servicestatus: String
) :
    RecyclerView.Adapter<ServicesListAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardAddService: LinearLayout = itemView.findViewById(R.id.cardAddService)
        var ivCarimg: ImageView = itemView.findViewById(R.id.ivCarimg)
        var ivServiceTyre: ImageView = itemView.findViewById(R.id.ivServiceTyre)
        var lllineView: LinearLayout = itemView.findViewById(R.id.lllineView)
        var tvVehicleNumber: TextView = itemView.findViewById(R.id.tvVehicleNumber)
        var tvVehicleName: TextView = itemView.findViewById(R.id.tvVehicleName)
        var tvColorName: TextView = itemView.findViewById(R.id.tvColorName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        var view =
            LayoutInflater.from(context)
                .inflate(R.layout.design_service_adapter_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        if (array.get(position).model_image != null && !array.get(position).model_image.equals("")) {

            try {
                Glide.with(context)
                    .load(array.get(position).model_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_no_car_image)
                    .into(holder.ivCarimg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            holder.ivCarimg.setImageDrawable(context.resources.getDrawable(R.drawable.ic_no_car_image))
        }

        holder.tvVehicleName.text = "" + array.get(position).make + "," + array.get(position).model
        holder.tvVehicleNumber.text = array.get(position).regNumber
        holder.tvColorName.text = array.get(position).color

        holder.lllineView.setBackgroundColor(Color.parseColor(array.get(position).color_code))
        holder.itemView.setOnClickListener {
            Log.e("getposs00", "" + position)
            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }

        holder.cardAddService.setOnClickListener {
            Log.e("getposs", "" + position)
            if (onclick != null) {
                onclick?.onPositionClick(position, 1)
            }
        }

        if (array.get(position).status.equals("completed",ignoreCase = true)) {
            holder.ivServiceTyre.setImageResource(R.mipmap.ic_completed_service)
        } else {
            holder.ivServiceTyre.setImageResource(R.mipmap.ic_service_icon)
        }

    }

    override fun getItemCount(): Int {
        return array.size

    }

}