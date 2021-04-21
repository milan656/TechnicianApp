package com.walkins.technician.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trading212.demo.item.SimpleStickyTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.walkins.technician.R
import com.walkins.technician.activity.MainActivity
import com.walkins.technician.activity.ServiceListActivity
import com.walkins.technician.adapter.LeadHistoryAdapter
import com.walkins.technician.common.item.SimpleTextRecyclerItem
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.datepicker.dialog.SingleDateAndTimePickerDialog
import com.walkins.technician.model.login.DashboardModel
import com.walkins.technician.model.login.LeadHistoryData
import com.walkins.technician.model.login.SectionModel
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
    private var selectedDate: String? = null

    var gamesRecyclerItems = ArrayList<SimpleTextRecyclerItem>()
    var historyDataList: ArrayList<DashboardModel> = ArrayList<DashboardModel>()

    var simpleDateFormat: SimpleDateFormat? = null
    var singleBuilder: SingleDateAndTimePickerDialog.Builder? = null
    var sectionModelArrayList: ArrayList<SectionModel> = ArrayList()

    private var arrayList = arrayListOf("Gallery", "Camera")


    private var tvUsername: TextView? = null
    private var homeRecycView: RecyclerView? = null
    private var relmainContent: RelativeLayout? = null

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

        activity = getActivity() as MainActivity?

        ivFilter = view?.findViewById(R.id.ivFilter)

        relmainContent = view?.findViewById(R.id.relmainContent)
        tvUsername = view?.findViewById(R.id.tvUsername)
        tvUsername?.text = "Hello, " + "Arun"
        ivFilter?.setOnClickListener(this)

        homeRecycView = view.findViewById(R.id.recyclerView)

        for (i in 0..5) {

            var dashboardModel: DashboardModel? = null
            when (i) {
                0, 1 -> {
                    dashboardModel = DashboardModel(
                        "Titanium City Center,Anandnagar",
                        24, 20, 21, 45, System.currentTimeMillis(),
                        System.currentTimeMillis()
                    )

                }
                2, 3, 4, 5 -> {
                    val dateString = "30/09/2021"
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val date = sdf.parse(dateString)

                    val startDate = date.time
                    dashboardModel = DashboardModel(
                        "Prahladnagar garden",
                        34, 30, 4, 40, startDate,
                        startDate
                    )
                }
            }

            historyDataList.add(dashboardModel!!)
        }

//        homeRecycView?.setHasFixedSize(true)
        mAdapter = context?.let { LeadHistoryAdapter(it, historyDataList, this) }
        var decor = StickyHeaderDecoration(mAdapter)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        homeRecycView?.setLayoutManager(layoutManager)
        homeRecycView?.setAdapter(mAdapter)
        homeRecycView?.addItemDecoration(decor)
        mAdapter?.onclick = this

        return view

    }

    fun fillRecyclerView() {
        val adapter = DiverseRecyclerAdapter()
//        gamesRecyclerItems = generateGamesList().map { SimpleTextRecyclerItem(it, this) }
        var model = DashboardModel("Titanium City Center,Anandnagar", 34, 50, 15, 40)
        gamesRecyclerItems.add(SimpleTextRecyclerItem("", model, this))

        var modelsecond = DashboardModel("Titanium City Center,Anandnagar", 34, 50, 15, 40)
        gamesRecyclerItems.add(SimpleTextRecyclerItem("", modelsecond, this))

        var modelThird = DashboardModel("Titanium City Center,Anandnagar", 34, 50, 15, 40)
        gamesRecyclerItems.add(SimpleTextRecyclerItem("", modelThird, this))

        adapter.addItem(
            SimpleStickyTextRecyclerItem(
                SimpleStickyTextRecyclerItem.StickyData(
                    "Today",
                    ++stickyIdsCounter
                )
            ), false
        )
        adapter.addItems(gamesRecyclerItems, false)

        adapter.addItem(
            SimpleStickyTextRecyclerItem(
                SimpleStickyTextRecyclerItem.StickyData(
                    "29 April",
                    ++stickyIdsCounter
                )
            ), false
        )
        adapter.addItems(gamesRecyclerItems, false)

        adapter.addItem(
            SimpleStickyTextRecyclerItem(
                SimpleStickyTextRecyclerItem.StickyData(
                    "15 May",
                    ++stickyIdsCounter
                )
            ), false
        )
        adapter.addItems(gamesRecyclerItems, false)


//        stickyHeaderDecoration = StickyHeaderDecoration(mAdapter)
//        homeRecycView?.addItemDecoration(stickyHeaderDecoration)

        homeRecycView?.adapter = adapter

        adapter.onItemActionListener = object : DiverseRecyclerAdapter.OnItemActionListener() {
            override fun onItemClicked(v: View, position: Int) {

                try {
                    Log.e(
                        "getclick",
                        "" + gamesRecyclerItems.get(position)
                    )
                    Log.e(
                        "getclick",
                        "" + gamesRecyclerItems.get(position)
                    )
                    Log.e("getclick", "" + gamesRecyclerItems.get(position).type)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("getclick", "" + e.cause + " " + e.message)
                }
            }

            override fun onItemLongClicked(v: View, position: Int): Boolean {

                return super.onItemLongClicked(v, position)
            }
        }

        adapter.notifyDataSetChanged()
    }


    fun generateGamesList() = listOf(
        "Titanium City Center,Anandnagar",
        "Titanium City Center,Anandnagar",
        "Titanium City Center,Anandnagar"
    )

    fun generateList(): ArrayList<DashboardModel> {

        var list = ArrayList<DashboardModel>()


//        list.add(model)
        return list
    }

    fun generateProgrammingLanguagesList() = listOf(
        "Titanium City Center,Anandnagar",
        "Titanium City Center,Anandnagar",
        "Titanium City Center,Anandnagar"
    )

    fun generateSongsList() = listOf(
        "Titanium City Center,Anandnagar",
        "Titanium City Center,Anandnagar",
        "Titanium City Center,Anandnagar"
    )


    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        msg: String,
        msg1: String,
        msg2: String,
        msg3: String,
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

        tv_message?.text = msg + "\n" + msg1 + "\n" + msg2 + "\n" + msg3

        if (msg.isNotEmpty()) {
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
                false, "Palm Spring,", "Vastrapur Road,", "Opposite Siddhivinayak mandir,",
                "Ahmedabad - 123456"
            )

        } else if (check == 0) {

            var intent = Intent(context, ServiceListActivity::class.java)
            startActivity(intent)
        } else {
            Log.e("getsection", "" + sectionModelArrayList?.get(variable)?.sectionLabel)
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
                }
            })

        activity?.lltransparent?.visibility = View.VISIBLE
        singleBuilder?.display()


    }


}