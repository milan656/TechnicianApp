package com.walkins.aapkedoorstep.adapter

import android.annotation.SuppressLint
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
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.NotificationModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat", "SetTextI18n")
class NotificationAdpater(
    var array: ArrayList<NotificationModel>,
    var context: Context,
    onPositionClick: onClickAdapter,
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
        var ivRedDot = itemView.findViewById<ImageView>(R.id.ivRedDot)

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
        viewType: Int,
    ): Viewholder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.design_notification_item, parent, false)
        return Viewholder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.itemView.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }
        }
        if (!array.get(position).isRead) {
            holder.ivRedDot?.visibility = View.VISIBLE
        } else {
            holder.ivRedDot?.visibility = View.GONE
        }

        holder.tvNotiTitle?.text = array.get(position).addressTitle
        holder.tvMessage?.text = array.get(position).fullAddress

        val formatedDate = Common.addHour(array.get(position).date, 5, 30)!!
        try {

            val dateAccessToken = Common.dateFotmatInDate(formatedDate)
            val diff: Long = Date().time - dateAccessToken.getTime()
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            Log.e("gettimees", "" + days + " " + hours + " " + minutes + " " + seconds)

            if (hours > 0L) {
                if (hours > 1L) {
                    holder.tvTime?.text = "" + hours + " " + "hours ago"
                } else {
                    holder.tvTime?.text = "" + hours + " " + "hour ago"
                }
            }
            if (hours <= 0L) {
                if (minutes > 0L) {
                    if (minutes > 1L) {
                        holder.tvTime?.text = "" + minutes + " " + "minutes ago"
                    } else {
                        holder.tvTime?.text = "" + minutes + " " + "minute ago"
                    }
                }
            }
            if (minutes <= 0L) {
                if (seconds > 0L) {
                    if (seconds > 1L) {
                        holder.tvTime?.text = "" + seconds + " " + "seconds ago"
                    } else {
                        holder.tvTime?.text = "" + seconds + " " + "second ago"
                    }
                }
            }

            if (mToday != mDateFormat.format(Date(array.get(position).createdAt)).toString()) {
                holder.tvTime?.visibility = View.GONE
            } else {
                holder.tvTime?.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return array.size

    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp = itemView.findViewById<TextView>(R.id.timestamp)

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

        p0?.timestamp?.text = mDateFormat.format(Date(item.createdAt)).toString()
        Log.e("gettimedate", "" + mDataset.get(p1))
        if (mToday == p0?.timestamp?.text) {
            p0.timestamp?.text = "Today"
//            p0.ivRedDot?.visibility = View.VISIBLE
        }
    }
}

