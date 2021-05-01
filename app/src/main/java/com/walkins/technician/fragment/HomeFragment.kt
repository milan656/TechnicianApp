package com.walkins.technician.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.walkins.technician.common.dateForWebservice_2
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.activity.MainActivity
import com.walkins.technician.activity.ServiceListActivity
import com.walkins.technician.adapter.LeadHistoryAdapter
import com.walkins.technician.common.item.SimpleTextRecyclerItem
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.datepicker.dialog.SingleDateAndTimePickerDialog
import com.walkins.technician.model.login.DashboardModel
import com.walkins.technician.model.login.SectionModel
import com.walkins.technician.viewmodel.ServiceViewModel
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    var calendar = Calendar.getInstance()
    private var prefManager: PrefManager? = null
    private var ivFilter: ImageView? = null
    private var selectedDate: String? = ""
    private var serviceViewModel: ServiceViewModel? = null

    var gamesRecyclerItems = ArrayList<SimpleTextRecyclerItem>()
    var historyDataList: ArrayList<DashboardModel> = ArrayList<DashboardModel>()

    var simpleDateFormat: SimpleDateFormat? = null
    var singleBuilder: SingleDateAndTimePickerDialog.Builder? = null
    var sectionModelArrayList: ArrayList<SectionModel> = ArrayList()

    private var arrayList = arrayListOf("Gallery", "Camera")

    private var tvUsername: TextView? = null
    private var homeRecycView: RecyclerView? = null
    private var relmainContent: RelativeLayout? = null
    private var relNoData: LinearLayout? = null

    var activity: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private var stickyIdsCounter = 0
    private var mAdapter: LeadHistoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        activity = getActivity() as MainActivity?

        prefManager = context?.let { PrefManager(it) }
        init(view)

        return view

    }

    private fun init(view: View?) {
        ivFilter = view?.findViewById(R.id.ivFilter)

        relmainContent = view?.findViewById(R.id.relmainContent)
        relNoData = view?.findViewById(R.id.relNoData)
        tvUsername = view?.findViewById(R.id.tvUsername)
        tvUsername?.text = "Hello, " + "Arun"
        ivFilter?.setOnClickListener(this)

        homeRecycView = view?.findViewById(R.id.recyclerView)


//        homeRecycView?.setHasFixedSize(true)
        mAdapter = context?.let { LeadHistoryAdapter(it, historyDataList, this) }
        var decor = StickyHeaderDecoration(mAdapter)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        homeRecycView?.setLayoutManager(layoutManager)
        homeRecycView?.setAdapter(mAdapter)
        homeRecycView?.addItemDecoration(decor)
        mAdapter?.onclick = this

        getDashboardService(selectedDate!!)
    }

    private fun getDashboardService(displayDate: String) {


        activity?.let {
            Common.showLoader(it)
            serviceViewModel?.callApiDashboardService(displayDate, prefManager?.getAccessToken()!!, it)
            serviceViewModel?.getDashboardService()?.observe(it, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {

                        Log.e("getdataa", "" + it.data)
                        historyDataList.clear()

                        if (it.data != null && it.data.size > 0) {
                            for (i in it.data.indices) {

                                var dashboardModel: DashboardModel? = null
                                val dateString = it.data.get(i).date
                                Log.e("getdatefrom", "" + dateString)
                                val sdf = SimpleDateFormat("yyyy-MM-dd")
                                val date = sdf.parse(dateString)

                                val startDate = date.time
                                Log.e("getdatefromstart", "" + startDate)
                                dashboardModel = DashboardModel(
                                    it.data.get(i).building_name, it.data.get(i).address, it.data.get(i).date, it.data.get(i).date_formated,
                                    it.data.get(i).open_jobs.toInt(), it.data.get(i).complete_jobs.toInt(), it.data.get(i).skip_jobs.toInt(), 45, startDate,
                                    startDate
                                )

                                historyDataList.add(dashboardModel)
                            }
                            relNoData?.visibility = View.GONE
                            homeRecycView?.visibility = View.VISIBLE
                        } else {
                            relNoData?.visibility = View.VISIBLE

                            homeRecycView?.visibility = View.GONE
                        }
                        mAdapter?.notifyDataSetChanged()

                    } else {
                        relNoData?.visibility = View.VISIBLE
                        homeRecycView?.visibility = View.GONE
                    }
                } else {
                    relNoData?.visibility = View.VISIBLE
                    homeRecycView?.visibility = View.GONE
                }
            })
        }


    }


    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        strBuilder: StringBuilder
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog =
            getContext()?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btnOk)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr

        val str = strBuilder.toString().replace(",", "," + "\n")
        tv_message?.text = str

        if (str.isNotEmpty()) {
            tv_message.visibility = View.VISIBLE
        }

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }
        if (isBtnVisible) {
            btnSend.visibility = View.VISIBLE
        } else {
            btnSend.visibility = View.GONE
        }
        if (btnBg.equals(Common.btn_filled, ignoreCase = true)) {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_yellow))
            btnSend.setTextColor(context?.resources?.getColor(R.color.white)!!)
            btnSend?.text = "Submit"
        } else {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_white))
            btnSend.setTextColor(context?.resources?.getColor(R.color.header_title)!!)
            btnSend?.text = "Cancel"
        }

        btnSend.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.show()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 1) {

            showBottomSheetdialogNormal(
                arrayList,
                "Address Details",
                context,
                Common.btn_filled,
                false, Common.getStringBuilder(historyDataList.get(variable).fullAddress)
            )

        } else if (check == 0) {

            var intent = Intent(context, ServiceListActivity::class.java)
            intent.putExtra("selectedDate", "" + activity?.dateForWebservice_2(historyDataList.get(variable).date))
            intent.putExtra("selectedDateFormated", historyDataList.get(variable).dateFormated)
            intent.putExtra("addressTitle", historyDataList.get(variable).addressTitle)
            intent.putExtra("fullAddress", historyDataList.get(variable).fullAddress)
            startActivity(intent)
        } else {
            Log.e("getsection", "" + sectionModelArrayList.get(variable).sectionLabel)
            Log.e("getsection", "" + check)
        }
//        Log.e("getclickpos", arrayList.get(variable))
    }

    override fun onClick(v: View?) {
        val i = v?.id
        when (i) {
            R.id.ivFilter -> {
                openDatePicker()

            }
        }
    }

    fun openDatePicker() {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MONTH, 0)
        calendar.add(Calendar.YEAR, 0)
        val future: Date = calendar.getTime()
        Log.e("getfuturedate", "" + future)
        singleBuilder = SingleDateAndTimePickerDialog.Builder(context)
            .setTimeZone(TimeZone.getDefault())
            .bottomSheet()
            .curved() //.backgroundColor(Color.BLACK)
            //.mainColor(Color.GREEN)
            .displayHours(false)
            .displayMinutes(false)
            .displayDays(false)
            .displayMonth(true)
            .displayDaysOfMonth(true)
            .displayYears(true)
            .defaultDate(future)
            .displayMonthNumbers(true)
            .minDateRange(future) //.mustBeOnFuture()
            //.minutesStep(15)
            //.mustBeOnFuture()
            //.defaultDate(defaultDate)
            // .minDateRange(minDate)
            // .maxDateRange(maxDate)

            .title("Simple")
            .listener(object : SingleDateAndTimePickerDialog.Listener {
                override fun onDateSelected(date: Date?, str: String) {
                    activity?.lltransparent?.visibility = View.GONE
                    if (str.equals("")) {
                        simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
                        Log.e("getdatee", "" + simpleDateFormat?.format(date))
                        selectedDate = simpleDateFormat?.format(date)
                        Log.e("getdatee00", "" + selectedDate)
                        ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_applied_calender))
                    } else if (str.equals("Reset")) {
                        Log.e("getdatee2", "" + selectedDate)
                        ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_calender_icon))
                        selectedDate = ""
                    } else if (str.equals("Close")) {
                        Log.e("getdatee1", "" + selectedDate)
                        if (selectedDate == null || selectedDate.equals("null") || selectedDate.equals(
                                ""
                            )
                        ) {
                            ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_calender_icon))
                        } else {
                            ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_applied_calender))
                        }
                    }
                    if (!selectedDate.equals("") && !str.equals("Close")) {
                        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        val formatterDisplay = SimpleDateFormat("dd MMMM yyyy")
                        val dateInString = formatterDisplay.parse(selectedDate)
                        val displayDate = formatter.format(dateInString)
                        getDashboardService(displayDate)

                    } else {
                        if (!str.equals("Close")) {
                            getDashboardService(selectedDate!!)
                        }
                    }
                }
            })

        activity?.lltransparent?.visibility = View.VISIBLE
        singleBuilder?.display()


    }


}