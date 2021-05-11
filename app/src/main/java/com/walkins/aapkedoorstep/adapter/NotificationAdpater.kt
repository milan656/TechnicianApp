package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.findDifference
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.DashboardModel
import com.walkins.aapkedoorstep.model.login.NotificationModel
import com.walkins.aapkedoorstep.model.login.notification.Notification
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdpater(
    var array: ArrayList<NotificationModel>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<NotificationAdpater.Viewholder>(),
    StickyHeaderAdapter<NotificationAdpater.HeaderHolder?> {

    private val mContext: Context
    private val mInflater: LayoutInflater
    private val mDataset: ArrayList<NotificationModel>
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvNotiTitle = itemView.findViewById<TextView>(R.id.tvNotiTitle)
        var tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
        var tvTime = itemView.findViewById<TextView>(R.id.tvTime)

    }

    override fun getHeaderId(position: Int): Long {
        val item: NotificationModel = mDataset[position]
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdpater.Viewholder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.design_notification_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdpater.Viewholder, position: Int) {

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }

        holder.tvNotiTitle?.text = array.get(position).addressTitle
        holder.tvMessage?.text = array.get(position).fullAddress

        val formatedDate = Common.addHour(array.get(position).date, 5, 30)!!
        try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val output = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")

            var d: Date? = null
            d = input.parse(formatedDate)
            val formatted: String = output.format(d)

            val startDate: String = formatted
            val enddate: String = Common.getCurrentDateTimeSimpleFormat()

            val dif = findDifference(startDate, enddate)
            val temp: Array<String> = dif.split(",").toTypedArray()

            var time = "0"
            if (temp.get(1) != null && !temp.get(1).equals("") && temp.get(1).toInt() > 0) {
                time = temp.get(1) + " day"
            }
            if (temp.get(2) != null && !temp.get(2).equals("") && temp.get(2).toInt() > 0) {
                if (!time.equals("") && !time.equals("0")) {
                    time = time + "," + temp.get(2) + " hours"
                } else {
                    time = temp.get(2) + " hours"
                }
            }
            if (temp.get(3) != null && !temp.get(3).equals("") && temp.get(3).toInt() > 0) {
                if (!time.equals("") && !time.equals("0")) {
                    time = time + "," + temp.get(3) + " mins"
                } else {
                    time = temp.get(3) + " mins"
                }
            }
            Log.e("getmin00", "" + time)
            holder.tvTime?.text = time
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return array.size

    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp = itemView.findViewById<TextView>(R.id.timestamp)
        var ivRedDot = itemView.findViewById<ImageView>(R.id.ivRedDot)
    }

    init {
        //mContext = context;
        mContext = context
        mInflater = LayoutInflater.from(mContext)
        mDataset = array
        mDateFormat = SimpleDateFormat("dd MMMM yy")
        mDateFormatTime = SimpleDateFormat("hh:mm a")
        mToday = mDateFormat.format(Date())
    }


    override fun onBindHeaderViewHolder(p0: HeaderHolder?, p1: Int) {
        val item: NotificationModel = mDataset[p1]

        p0?.ivRedDot?.visibility = View.GONE
        p0?.timestamp?.text = mDateFormat.format(Date(item.createdAt)).toString()
        Log.e("gettimedate", "" + mDataset.get(p1))
        if (mToday == p0?.timestamp?.text) {
            p0.timestamp?.text = "Today"
            p0.ivRedDot?.visibility = View.VISIBLE
        }
    }
}

