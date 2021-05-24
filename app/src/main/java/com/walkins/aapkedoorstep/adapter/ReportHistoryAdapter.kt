package com.walkins.aapkedoorstep.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.OnBottomReachedListener
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.DashboardModel
import com.walkins.aapkedoorstep.model.login.LeadHistoryData
import com.walkins.aapkedoorstep.model.login.ReportHistoryModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class ReportHistoryAdapter(
    private val mActivity: Context, dataset: List<ReportHistoryModel>,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ReportHistoryAdapter.ViewHolder>(),
    StickyHeaderAdapter<ReportHistoryAdapter.HeaderHolder?> {

    private val mContext: Context
    private val mInflater: LayoutInflater
    private val mDataset: List<ReportHistoryModel>
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""
    var onclick: onClickAdapter? = null
    private var onBottomReachedListener: OnBottomReachedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.design_report_item, parent, false)
        return ViewHolder(view)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener?) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("getdatee00", "" + mDataset.get(position))
        val item: ReportHistoryModel = mDataset[position]

        if (position == mDataset.size - 1) {
            onBottomReachedListener?.onBottomReached(position)

        }
//        if (item.getName().equals("") || item.getName() == null) {
//            holder.name.visibility = View.GONE
//        } else {
//            holder.name.visibility = View.VISIBLE
//        }
//        holder.name.setText(item.getName())
//        holder.number.setText(item.getPhoneNumber())
//        holder.createAt.text = mDateFormatTime.format(Date(item.getCreatedAt()))

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }

        holder.tvReportVehicleNumber.setText("" + mDataset.get(position).regNumber)
        holder.tvReportVehicleName.setText(mDataset.get(position).makeModel)

        holder.tvCarColor.setText(mDataset.get(position).carColor)
        try {
            Glide.with(mContext).load(mDataset.get(position).carImageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_no_car_image).into(holder.ivCarImage)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        holder.ivNitrogenRifil.visibility = View.GONE
        holder.ivNitrogenTopup.visibility = View.GONE
        holder.ivWheelBalancing.visibility = View.GONE
        holder.ivTyreRotation.visibility = View.GONE
        if (mDataset.get(position).serviceList != null && mDataset.get(position).serviceList.size > 0) {
            for (i in mDataset.get(position).serviceList.indices) {
                if (mDataset.get(position).serviceList.get(i).name.equals("Type Rotation")) {

                    holder.ivTyreRotation.visibility = View.VISIBLE
                    try {
                        Glide.with(mContext).load(mDataset.get(position).serviceList.get(i).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder).into(holder.ivTyreRotation)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else if (mDataset.get(position).serviceList.get(i).name.equals("Wheel Balancing")) {
                    holder.ivWheelBalancing.visibility = View.VISIBLE
                    try {
                        Glide.with(mContext).load(mDataset.get(position).serviceList.get(i).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder).into(holder.ivWheelBalancing)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else if (mDataset.get(position).serviceList.get(i).name.equals("Nitrogen Top Up")) {
                    holder.ivNitrogenTopup.visibility = View.VISIBLE
                    try {
                        Glide.with(mContext).load(mDataset.get(position).serviceList.get(i).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder).into(holder.ivNitrogenTopup)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else if (mDataset.get(position).serviceList.get(i).name.equals("Nitrogen Refill")) {
                    holder.ivNitrogenRifil.visibility = View.VISIBLE
                    try {
                        Glide.with(mContext).load(mDataset.get(position).serviceList.get(i).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder).into(holder.ivNitrogenRifil)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        holder.llInfoReport.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 10)
            }
        }

    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getHeaderId(position: Int): Long {
        val item: ReportHistoryModel = mDataset[position]
        var headerId = 0L
        try {
            headerId = mDateFormat.parse(mDateFormat.format(Date(item.createdAt))).time
        } catch (ex: Exception) {
        }
        return headerId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder {
        val view: View = mInflater.inflate(R.layout.header_lead, parent, false)
        return HeaderHolder(view)
    }


    class ViewHolder(mRoot: View) : RecyclerView.ViewHolder(
        mRoot
    ) {

        var tvReportVehicleName: TextView = itemView.findViewById(R.id.tvReportVehicleName)
        var tvReportVehicleNumber: TextView = itemView.findViewById(R.id.tvReportVehicleNumber)
        var ivCarImage: ImageView = itemView.findViewById(R.id.ivCarImage)
        var ivTyreRotation: ImageView = itemView.findViewById(R.id.ivTyreRotation)
        var ivWheelBalancing: ImageView = itemView.findViewById(R.id.ivWheelBalancing)
        var ivNitrogenRifil: ImageView = itemView.findViewById(R.id.ivNitrogenRifil)
        var ivNitrogenTopup: ImageView = itemView.findViewById(R.id.ivNitrogenTopup)
        var tvCarColor: TextView = itemView.findViewById(R.id.tvCarColor)
        var llInfoReport: LinearLayout = itemView.findViewById(R.id.llInfoReport)

    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp = itemView.findViewById<TextView>(R.id.timestamp)
    }


    init {
        //mContext = context;
        mContext = mActivity
        mInflater = LayoutInflater.from(mContext)
        mDataset = dataset
        mDateFormat = SimpleDateFormat("dd MMMM yy")
        mDateFormatTime = SimpleDateFormat("hh:mm a")
        mToday = mDateFormat.format(Date())
    }

    override fun onBindHeaderViewHolder(p0: HeaderHolder?, p1: Int) {
        val item: ReportHistoryModel = mDataset[p1]

        p0?.timestamp?.text = mDateFormat.format(Date(item.createdAt)).toString()
        if (mToday == p0?.timestamp?.text) {
            p0.timestamp?.text = "Today"
        }
    }
}
