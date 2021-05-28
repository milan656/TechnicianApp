package com.walkins.aapkedoorstep.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walkins.aapkedoorstep.DB.VehiclePatternModelClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternData
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
class VehiclePatternAdapter internal constructor(
    private val mContext: Context,
    private var name: ArrayList<VehiclePatternModelClass>?,
    onPositionClick: onClickAdapter,
    private var selectedId: Int

) : RecyclerView.Adapter<VehiclePatternAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById(R.id.ivVehicleImage) as TextView

        //        val ivVehicleImage = itemView.findViewById(R.id.ivVehicleImage) as ImageView
        val rlItemView = itemView.findViewById(R.id.rl_item_view) as RelativeLayout
        val ivselectedVehicleModel = itemView.findViewById(R.id.ivselectedVehicleModel) as ImageView
    }

    private val positionClick: onClickAdapter = onPositionClick
    private var size: Int? = null
    private var isClick: Boolean? = false

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiclePatternAdapter.ViewHolder {
        var grid: View
        val inflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        grid = View(mContext)
        grid = inflater.inflate(R.layout.item_vehicle_model, null)

        return ViewHolder(grid)
    }

    override fun onBindViewHolder(holder: VehiclePatternAdapter.ViewHolder, position: Int) {
        holder.textView.setText(name?.get(position)?.name)
//        holder.textView.text = "185/65 R15"

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
    }

    override fun getItemCount(): Int {
        return name?.size!!
    }

}