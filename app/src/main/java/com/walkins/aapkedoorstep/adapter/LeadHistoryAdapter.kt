package com.walkins.aapkedoorstep.adapter

import android.annotation.SuppressLint
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
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.OnBottomReachedListener
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.DashboardModel
import com.walkins.aapkedoorstep.model.login.LeadHistoryData
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class LeadHistoryAdapter(
    private val mActivity: Context, dataset: List<DashboardModel>,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<LeadHistoryAdapter.ViewHolder>(),
    StickyHeaderAdapter<LeadHistoryAdapter.HeaderHolder?> {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private val mDataset: List<DashboardModel>
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""
    var onclick: onClickAdapter? = null
    private var onBottomReachedListener: OnBottomReachedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.design_home_adapter_item, parent, false)
        return ViewHolder(view)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener?) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("getdatee00", "" + mDataset.get(position))
        val item: DashboardModel = mDataset[position]
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

        holder.ivInfo?.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 1)
            }
        }

        holder.tvCompleted?.text = mDataset.get(position).completedCount.toString()
        holder.tvUpcoming?.text = mDataset.get(position).upcomingCount.toString()
        holder.tvSkipped?.text = mDataset.get(position).skippedCount.toString()
        holder.tvCarCount?.text = mDataset.get(position).carCount.toString()
        holder.tvaddress?.text = mDataset.get(position).addressTitle
        holder.tvCarCount?.text = mDataset.get(position).carCount.toString()
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getHeaderId(position: Int): Long {
        var headerId = 0L
        try {
            if (mDataset!=null && mDataset.size>0) {
                val item: DashboardModel = mDataset[position]
                headerId = mDateFormat.parse(mDateFormat.format(Date(item.createdAt))).time
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
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

        var ivInfo = mRoot.findViewById<ImageView>(R.id.ivInfo)
        var tvUpcoming = mRoot.findViewById<TextView>(R.id.tvUpcoming)
        var tvCompleted = mRoot.findViewById<TextView>(R.id.tvCompleted)
        var tvSkipped = mRoot.findViewById<TextView>(R.id.tvSkipped)
        var tvCarCount = mRoot.findViewById<TextView>(R.id.tvCarCount)
        var tvaddress = mRoot.findViewById<TextView>(R.id.tvaddress)

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
        val item: DashboardModel = mDataset[p1]

        p0?.timestamp?.text = mDateFormat.format(Date(item.createdAt)).toString()
        Log.e("gettimedate", "" + mDataset.get(p1))
        if (mToday == p0?.timestamp?.text) {
            p0.timestamp?.text = "Today"
        }
    }
}
