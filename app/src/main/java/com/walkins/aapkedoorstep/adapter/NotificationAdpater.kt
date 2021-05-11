package com.walkins.aapkedoorstep.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.findDifference
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.notification.Notification
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdpater(
    var array: ArrayList<Notification>,
    var context: Context,
    onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<NotificationAdpater.Viewholder>() {

    var onclick: onClickAdapter? = null

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvUpcomingServive = itemView.findViewById<TextView>(R.id.tvUpcomingServive)
        var tvAddress = itemView.findViewById<TextView>(R.id.tvAddress)
        var tvTime = itemView.findViewById<TextView>(R.id.tvTime)

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

        holder.tvUpcomingServive?.text = array.get(position).message

        val formatedDate = Common.addHour(array.get(position).createdAt, 5, 30)!!
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

}

