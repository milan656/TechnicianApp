package com.walkins.technician.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.walkins.technician.DB.VehicleMakeModelClass
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import java.util.logging.Handler


class VehicleMakeAdapterNew internal constructor(
    private val mContext: Context,
    private var name: ArrayList<VehicleMakeModelClass>?,
    onPositionClick: onClickAdapter,
    private var selectedName: String

) : RecyclerView.Adapter<VehicleMakeAdapterNew.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val textView = itemView.findViewById(R.id.tv_company_name) as TextView
        val ivVehicleImage = itemView.findViewById(R.id.ivVehicleImage) as ImageView
        val rlItemView = itemView.findViewById(R.id.rl_item_view) as RelativeLayout
        val ivselectedVehicle = itemView.findViewById(R.id.ivselectedVehicle) as ImageView
    }

    private val positionClick: onClickAdapter = onPositionClick
    private var size: Int? = null
    private var isClick: Boolean? = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var grid: View
        val inflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        grid = View(mContext)
        grid = inflater.inflate(R.layout.item_vehicle_make, null)

        return ViewHolder(grid)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.textView.setText(name?.get(position)?.name)

        Log.e("getimages", "" + name?.get(position)?.concat)
        Log.e("getimages", "" + name?.get(position)?.name)

        Log.e("getimages", "::::" + selectedName)

        try {
            Glide.with(mContext)
                .load(name?.get(position)?.concat)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.ivVehicleImage)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (name!!.get(position).isSelected) {
            holder.rlItemView.setBackgroundResource(R.drawable.selected)
        } else {
            holder.rlItemView.setBackgroundResource(R.drawable.unselected)
        }


        holder.rlItemView.setOnClickListener {
            if (name!!.get(position).isSelected) {
                // name!!.get(position).isSelected = false;
                holder.ivselectedVehicle.visibility = View.VISIBLE
            } else {
                for (date in name!!) {
                    if (date.isSelected) {
                        date.isSelected = false
                    }
                }

                name!!.get(position).isSelected = true;
                holder.ivselectedVehicle.visibility = View.VISIBLE
            }
//            notifyDataSetChanged()
            positionClick.onPositionClick(position, 0)
        }

        if (name?.get(position)?.isSelected!!) {
            holder.ivselectedVehicle.visibility = View.VISIBLE
        } else {
            holder.ivselectedVehicle.visibility = View.GONE
        }

        var handler = android.os.Handler()
        handler.postDelayed(Runnable {
            if (selectedName != null && !selectedName.equals("")) {
                /*if (selectedName.equals(name?.get(position)?.name, ignoreCase = true)) {
                    Log.e("selectedmake", "::::" + selectedName + " " + name?.get(position)?.name)
                    if (name!!.get(position).isSelected) {
                        // name!!.get(position).isSelected = false;
                        holder.ivselectedVehicle.visibility = View.VISIBLE
                    } else {
                        for (date in name!!) {
                            if (date.isSelected) {
                                date.isSelected = false
                            }
                        }

                        name!!.get(position).isSelected = true;
                        holder.ivselectedVehicle.visibility = View.VISIBLE
                    }
                    notifyDataSetChanged()
                    positionClick.onPositionClick(position, 0)
                }*/

                holder.rlItemView?.performClick()
            }
        }, 2000)


    }

    override fun getItemCount(): Int {
        Log.e("getimages", "" + name?.size)
        return name?.size!!
    }

}