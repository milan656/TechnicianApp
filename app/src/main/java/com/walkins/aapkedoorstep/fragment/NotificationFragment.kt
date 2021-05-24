package com.walkins.aapkedoorstep.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.activity.MainActivity
import com.walkins.aapkedoorstep.adapter.LeadHistoryAdapter
import com.walkins.aapkedoorstep.adapter.NotificationAdpater
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.DashboardModel
import com.walkins.aapkedoorstep.model.login.NotificationModel
import com.walkins.aapkedoorstep.model.login.notification.Notification
import com.walkins.aapkedoorstep.model.login.notification.NotificationData
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility", "SimpleDateFormat","InflateParams",
    "SetTextI18n")
class NotificationFragment : Fragment(), onClickAdapter, View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var prefManager: PrefManager? = null
    private var commonViewModel: CommonViewModel? = null
    private var notificationArr: ArrayList<NotificationModel>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var notiRecycView: RecyclerView? = null
    private var notificationRecycView: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvNoNotiData: TextView? = null
    var activity: MainActivity? = null
    private var notificationAdpater: NotificationAdpater? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        activity = getActivity() as MainActivity?
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)

        prefManager = context?.let { PrefManager(it) }
        init(view)
        return view
    }

    private fun init(view: View?) {
        notificationRecycView = view?.findViewById(R.id.notificationRecycView)
        tvTitle = view?.findViewById(R.id.tvTitle)
        tvNoNotiData = view?.findViewById(R.id.tvNoNotiData)
        ivBack = view?.findViewById(R.id.ivBack)
        ivBack?.visibility = View.GONE
        notificationRecycView = view?.findViewById(R.id.notificationRecycView)
        notificationAdpater =
            context?.let { NotificationAdpater(notificationArr!!, it, this) }
        val decor = StickyHeaderDecoration(notificationAdpater)
        notificationRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        notificationRecycView?.addItemDecoration(decor)
        notificationRecycView?.adapter = notificationAdpater
        notificationAdpater?.onclick = this

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Notification"

        getNotificationList()
    }

    private fun getNotificationList() {

        activity?.let {
            Common.showLoader(it)
            commonViewModel?.callApiGetNotificationList(prefManager?.getAccessToken()!!, it)
            commonViewModel?.getNotiList()?.observe(it, Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {

//                        notificationArr?.clear()
//                        notificationArr?.addAll(it.data.notifications)

//                        notificationAdpater?.notifyDataSetChanged()

                        if (it.data.notifications != null && it.data.notifications.size > 0) {
                            for (i in it.data.notifications.indices) {

                                var dashboardModel: NotificationModel? = null
                                val dateString = Common.addHour(it.data.notifications.get(i).createdAt, 5, 30)
                                Log.e("getdatefrom", "" + dateString)
                                val sdf = SimpleDateFormat("yyyy-MM-dd")
                                val date = sdf.parse(dateString)

                                val startDate = date.time
                                Log.e("getdatefromstart", "" + startDate)
                                dashboardModel = NotificationModel(
                                    it.data.notifications.get(i).title, it.data.notifications.get(i).message, it.data.notifications.get(i).createdAt, "", "",
                                    0, 0, 0, 0, startDate,
                                    startDate
                                )

                                notificationArr?.add(dashboardModel)
                            }
                        }

                        if (notificationArr?.size!! > 0) {
                            tvNoNotiData?.visibility = View.GONE
                        } else {
                            tvNoNotiData?.visibility = View.VISIBLE
                        }

                        notificationAdpater?.notifyDataSetChanged()
                    } else {
                        if (it.error != null) {
                            if (it.error.get(0).message != null) {

                            }
                        }
                    }
                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {

            }
        }
    }

}