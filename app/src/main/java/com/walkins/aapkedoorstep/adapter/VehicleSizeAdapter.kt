package com.walkins.aapkedoorstep.adapter

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
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.aapkedoorstep.DB.VehicleSizeModelClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeData
import java.util.logging.Handler


class VehicleSizeAdapter internal constructor(
    private val mContext: Context,
    private var name: ArrayList<VehicleSizeModelClass>?,
    onPositionClick: onClickAdapter,
    private var selectedId: Int

) : RecyclerView.Adapter<VehicleSizeAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById(R.id.ivVehicleImage) as TextView

        //        val ivVehicleImage = itemView.findViewById(R.id.ivVehicleImage) as ImageView
        val rlItemView = itemView.findViewById(R.id.rl_item_view) as RelativeLayout
        val ivselectedVehicleModel = itemView.findViewById(R.id.ivselectedVehicleModel) as ImageView
    }

    private val positionClick: onClickAdapter = onPositionClick
    private var size: Int? = null
    private var isClick: Boolean? = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleSizeAdapter.ViewHolder {
        var grid: View
        val inflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        grid = View(mContext)
        grid = inflater.inflate(R.layout.item_vehicle_model, null)

        return ViewHolder(grid)
    }

    override fun onBindViewHolder(holder: VehicleSizeAdapter.ViewHolder, position: Int) {
        holder.textView.setText(name?.get(position)?.name)
        Log.e("getsizdata", "" + name?.get(position)?.sizeId + " " + name?.get(position)?.name)
//        holder.textView.text = "185/65 R15"

        /*try {
            Glide.with(mContext)
                .load(name?.get(position)?.image_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.ivVehicleImage)

        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        if (name!!.get(position).isSelected) {
            holder.rlItemView.setBackgroundResource(R.drawable.selected)
            holder.ivselectedVehicleModel.visibility = View.VISIBLE
        } else {
            holder.rlItemView.setBackgroundResource(R.drawable.unselected)
            holder.ivselectedVehicleModel.visibility = View.GONE
        }

        holder.rlItemView.setOnClickListener {
            if (name!!.get(position).isSelected) {
                // name!!.get(position).isSelected = false;
            } else {
                for (date in name!!) {
                    if (date.isSelected) {
                        date.isSelected = false
                    }
                }

                name!!.get(position).isSelected = true;
                holder.ivselectedVehicleModel.visibility = View.VISIBLE
            }
            notifyDataSetChanged()
            positionClick.onPositionClick(position, 0)
        }

//        var handler=android.os.Handler()
//        handler.postDelayed(Runnable {
//            if (selectedId != -1) {
//                if (selectedId == name?.get(position)?.sizeId) {
//                    if (name?.get(position)?.isSelected!!) {
//                        // name!!.get(position).isSelected = false;
//                    } else {
//                        for (date in name!!) {
//                            if (date.isSelected) {
//                                date.isSelected = false
//                            }
//                        }
//
//                        name?.get(position)?.isSelected = true
//                        holder.ivselectedVehicleModel.visibility = View.VISIBLE
//                    }
//                    positionClick.onPositionClick(position, 0)
//                }
//            }
//
//        },700)

    }

    override fun getItemCount(): Int {
        return name?.size!!
    }

}