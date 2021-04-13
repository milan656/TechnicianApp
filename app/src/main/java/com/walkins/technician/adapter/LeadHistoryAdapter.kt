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
import com.walkins.technician.model.login.LeadHistoryData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LeadHistoryAdapter(private val mActivity: Context, dataset: ArrayList<LeadHistoryData>) :
    RecyclerView.Adapter<LeadHistoryAdapter.ViewHolder>(),
    StickyHeaderAdapter<LeadHistoryAdapter.HeaderHolder?> {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private var mDataset: ArrayList<LeadHistoryData>? = ArrayList()
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""
    private val builder: AlertDialog.Builder? = null
    private val cur_val: TextView? = null

    //    var mSettings: Settings
    private var onBottomReachedListener: OnBottomReachedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.leadhistory_raw, parent, false)
        return ViewHolder(view)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener?) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item: LeadHistoryData = mDataset?.get(position)!!
        if (item.getName().equals("") || item.getName() == null) {
            holder.name.visibility = View.GONE
        } else {
            holder.name.visibility = View.VISIBLE
        }
        holder.name.setText(item.getName())
        holder.number.setText(item.getPhoneNumber())
        holder.createAt.text = mDateFormatTime.format(Date(item.getCreatedAt()))
    }

    override fun getItemCount(): Int {
        return mDataset?.size!!
    }

    override fun getHeaderId(position: Int): Long {
        val item: LeadHistoryData = mDataset?.get(position)!!
        var headerId = 0L
        try {
            headerId = mDateFormat.parse(mDateFormat.format(Date(item.getCreatedAt()))).time
            Log.e("getlogs", "" + headerId)
        } catch (ex: Exception) {
            Log.e("getlogs", "" + ex.message + " " + ex.cause)
            ex.printStackTrace()
        }
        return headerId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder {
        val view: View = mInflater.inflate(R.layout.header_lead, parent, false)
        return HeaderHolder(view)
    }


    class ViewHolder(var mRoot: View) : RecyclerView.ViewHolder(
        mRoot
    ) {
        public val createAt: TextView
        public val name: TextView
        public val number: TextView
        var order_type: TextView? = null

        init {
            name = itemView.findViewById<View>(R.id.name) as TextView
            number = itemView.findViewById<View>(R.id.number) as TextView
            createAt = itemView.findViewById<View>(R.id.date) as TextView
        }
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp: TextView

        init {
            timestamp = itemView.findViewById<View>(R.id.timestamp) as TextView
        }
    }


    init {
        //mContext = context;
        mContext = mActivity
//        mSettings = Settings(mContext)
        mInflater = LayoutInflater.from(mContext)
        mDataset = dataset
        mDateFormat = SimpleDateFormat("dd-MM-yyyy")
        mDateFormatTime = SimpleDateFormat("hh:mm a")
        mToday = mDateFormat.format(Date())
    }


    override fun onBindHeaderViewHolder(viewholder: HeaderHolder?, position: Int) {
        val item = mDataset!![position]
//        viewholder?.timestamp?.setText(mDateFormat.format(Date(item.createdAt)))
        Log.e("getlogs", "call")
        viewholder?.timestamp?.text = "Today"
        if (mToday == viewholder?.timestamp?.getText()) {
            viewholder.timestamp.text = "Today"
        }

    }


}
