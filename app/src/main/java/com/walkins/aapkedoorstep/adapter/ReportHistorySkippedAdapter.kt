package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

class ReportHistorySkippedAdapter(
    private val mActivity: Context, dataset: List<ReportHistoryModel>,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ReportHistorySkippedAdapter.ViewHolder>(),
    StickyHeaderAdapter<ReportHistorySkippedAdapter.HeaderHolder?> {

    private val mContext: Context
    private val mInflater: LayoutInflater
    private val mDataset: List<ReportHistoryModel>
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""
    var onclick: onClickAdapter? = null
    private var onBottomReachedListener: OnBottomReachedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.design_report_skiped_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("getdatee00", "" + mDataset.get(position))
        Log.e("getdatee00", "" + mDataset.size)
        val item: ReportHistoryModel = mDataset[position]

        if (position == mDataset.size - 1) {
            Log.e("getdatee00", "call")
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

        holder.ivInfoAddService.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 10)
            }
        }

        holder.itemView.setOnClickListener {
            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }

        }

        holder.tvColor?.text = mDataset.get(position).carColor
        holder.tvMakemodel?.text = mDataset.get(position).makeModel
        holder.tvRegNumber?.text = mDataset.get(position).regNumber.toString()

        try {
            Glide.with(mContext).load(mDataset.get(position).carImageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_no_car_image).into(holder.ivCarImage)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
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

        var tvRegNumber = itemView.findViewById<TextView>(R.id.tvRegNumber)
        var tvMakemodel = itemView.findViewById<TextView>(R.id.tvMakemodel)
        var ivCarImage = itemView.findViewById<ImageView>(R.id.ivCarImage)
        var tvColor = itemView.findViewById<TextView>(R.id.tvColor)
        var ivInfoAddService = itemView.findViewById<ImageView>(R.id.ivInfoAddService)

    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp = itemView.findViewById<TextView>(R.id.timestamp)
    }


    init {
        //mContext = context;
        mContext = mActivity
        mInflater = LayoutInflater.from(mContext)
        mDataset = dataset
        mDateFormat = SimpleDateFormat("dd-MM-yyyy")
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

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener
    }
}
