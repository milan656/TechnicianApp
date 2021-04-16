package com.walkins.technician.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter
import com.walkins.technician.R
import com.walkins.technician.common.OnBottomReachedListener
import com.walkins.technician.model.login.DashboardModel
import com.walkins.technician.model.login.LeadHistoryData
import java.text.SimpleDateFormat
import java.util.*

class LeadHistoryAdapter(private val mActivity: Context, dataset: List<DashboardModel>) :
    RecyclerView.Adapter<LeadHistoryAdapter.ViewHolder>(),
    StickyHeaderAdapter<LeadHistoryAdapter.HeaderHolder?> {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private val mDataset: List<DashboardModel>
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""
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
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getHeaderId(position: Int): Long {
        val item: DashboardModel = mDataset[position]
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

//        var name = mRoot.findViewById<TextView>(R.id.name)
//        var number = mRoot.findViewById<TextView>(R.id.number)
//        var createAt = mRoot.findViewById<TextView>(R.id.date)

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
        val item: DashboardModel = mDataset[p1]

        Log.e("getdatee", "" + mDateFormat.format(Date(item.createdAt)))
        p0?.timestamp?.text = mDateFormat.format(Date(item.createdAt)).toString()
//        p0?.timestamp?.text = "date format"
        if (mToday == p0?.timestamp?.text) {
            p0?.timestamp?.text = "Today"
        }

        Log.e("gettext",""+p0?.timestamp?.text?.toString())
    }
}
