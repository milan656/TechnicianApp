package com.walkins.technician.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramotion.fluidslider.FluidSlider
import com.walkins.technician.R
import com.walkins.technician.activity.ServiceListActivity
import com.walkins.technician.adapter.*
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.date.DateModel
import io.apptik.widget.MultiSlider
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null

    private var prefManager: PrefManager? = null
    private var ivFilter: ImageView? = null
    private var selectedDate: String? = null

    private var arrayList = arrayListOf("Gallery", "Camera")
    private var arrayListMonth = arrayListOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "Aug",
        "September",
        "October",
        "November",
        "December"
    )
    private var arrayListYear = arrayListOf(
        "2000",
        "2001",
        "2002",
        "2003",
        "2004",
        "2005",
        "2006",
        "2007",
        "2008",
        "2009",
        "2010",
        "2011",
        "2012",
        "2013",
        "2014",
        "2015",
        "2016",
        "2017",
        "2018",
        "2019",
        "2020",
        "2021"
    )
    private var arrayListdaysFeb = arrayListOf(
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28"
    )
    private var arrayListdays = arrayListOf(
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31"
    )
    private var vehicleMakeList = arrayListOf("")
    private var dummyvaluestart: String? = "0"
    private var dummyvalueend: String? = "100"

    private var tvUsername: TextView? = null
    private var homeRecycView: RecyclerView? = null
    private var adapter: HomeListAdpater? = null
    private var relNoDataView: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        val multiSlider1 = view.findViewById<MultiSlider>(R.id.multiSlider1)
//        val starting_point = view.findViewById<TextView>(R.id.starting_point)
//        val last_end = view.findViewById<TextView>(R.id.last_end)
//        multiSlider1?.min = dummyvaluestart?.toInt()!!
//        multiSlider1?.max = dummyvalueend?.toInt()!!
//
//        multiSlider1.setNumberOfThumbs(2)
//        multiSlider1.setOnThumbValueChangeListener(object : MultiSlider.SimpleChangeListener() {
//            override fun onValueChanged(
//                multiSlider: MultiSlider,
//                thumb: MultiSlider.Thumb,
//                thumbIndex: Int,
//                value: Int
//            ) {
//                if (thumbIndex == 0) {
//                    starting_point!!.text = value.toString()
//                    dummyvaluestart = "" + value
//                } else {
//
//                    dummyvalueend = "" + value
//
//                    if (value == 100) {
//                        last_end!!.text = "100"
//                    } else {
//                        last_end!!.text = value.toString()
//                    }
//                }
//            }
//        })
//
        // Kotlin
        val max = 45
        val min = 10
        val total = max - min

        val slider = view.findViewById<FluidSlider>(R.id.multiSlider1)
        slider.positionListener = { pos ->
            slider.bubbleText = "${min + (total * pos).toInt()}"
            Log.e("getvaluess", "" + pos)
        }
        slider.position = 0.3f
        slider.startText = "$min"
        slider.endText = "$max"
        slider.animation?.cancel()

        ivFilter = view?.findViewById(R.id.ivFilter)

        tvUsername = view?.findViewById(R.id.tvUsername)
        tvUsername?.text = "Hello, " + "Arun"
        homeRecycView = view.findViewById(R.id.homeRecycView)
        relNoDataView = view.findViewById(R.id.relNoDataView)
        relNoDataView?.visibility = View.GONE
        ivFilter?.setOnClickListener(this)

        homeRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        adapter = context?.let { HomeListAdpater(arrayList, it, this) }
        homeRecycView?.adapter = adapter
        adapter?.onclick = this

// Java


        return view

    }

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        val dialog =
            getContext()?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btn_send)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val dialogueRecycView = view.findViewById<RecyclerView>(R.id.dialogueRecycView)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        var arrayAdapter = context?.let { DialogueAdpater(array, it, this) }
        dialogueRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        dialogueRecycView.addItemDecoration(
            DividerItemDecoration(
                getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        dialogueRecycView.adapter = arrayAdapter
        arrayAdapter?.onclick = this

        ivClose?.setOnClickListener {
            dialog?.dismiss()
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
        val tv_message1 = view.findViewById<TextView>(R.id.tv_message1)
        val tv_message2 = view.findViewById<TextView>(R.id.tv_message2)
        val tv_message3 = view.findViewById<TextView>(R.id.tv_message3)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr

        tv_message?.text = msg
        tv_message1?.text = msg1
        tv_message2?.text = msg2
        tv_message3?.text = msg3

        if (msg.isNotEmpty()) {
            tv_message.visibility = View.VISIBLE
        }
        if (msg1.isNotEmpty()) {
            tv_message1.visibility = View.VISIBLE
        }
        if (msg2.isNotEmpty()) {
            tv_message2.visibility = View.VISIBLE
        }
        if (msg3.isNotEmpty()) {
            tv_message3.visibility = View.VISIBLE
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
        }
//        Log.e("getclickpos", arrayList.get(variable))
    }

    override fun onClick(v: View?) {
        val i = v?.id
        when (i) {
            R.id.ivFilter -> {
                showBottomSheetdialogDate(
                    arrayListdays,
                    arrayListdaysFeb,
                    arrayListMonth,
                    arrayListYear,
                    "Choose Date",
                    context
                )
            }
        }
    }

    private fun openDateSelection() {
        val pickerPopWin = DatePickerPopWin.Builder(
            context
        ) { year, month, day, dateDesc ->
            Toast.makeText(context, dateDesc, Toast.LENGTH_SHORT).show()
            Log.e("getdatee", "" + dateDesc + " " + year + " " + month + " " + day)

            selectedDate = dateDesc
        }.textConfirm("CONFIRM") //text of confirm button
            .textCancel("CANCEL") //text of cancel button
            .btnTextSize(16) // button text size
            .viewTextSize(25) // pick view text size
            .colorCancel(Color.parseColor("#999999")) //color of cancel button
            .colorConfirm(Color.parseColor("#009900")) //color of confirm button
            .minYear(1990) //min year in loop
            .maxYear(2550) // max year in loop
            .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
            .dateChose("2013-11-11") // date chose when init popwindow
            .build()

        pickerPopWin?.cancelBtn?.setOnClickListener {
            pickerPopWin.dismissPopWin()
            if (!selectedDate.equals("")) {
                ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_applied_calender))
            } else {
                ivFilter?.setImageResource(R.mipmap.ic_calender_icon)
            }
        }
        pickerPopWin?.confirmBtn?.setOnClickListener {

            pickerPopWin.dismissPopWin()
            ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_applied_calender))
        }

        pickerPopWin?.showPopWin(activity)
    }

    private fun showBottomSheetdialogDate(
        array: ArrayList<String>,
        arrayfeb: ArrayList<String>,
        arrayMonth: ArrayList<String>,
        arrayYear: ArrayList<String>,
        titleStr: String,
        context: Context?,

        ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req_date, null)
        val dialog =
            context?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btn_confirm)
        val btn_reset = view.findViewById<Button>(R.id.btn_reset)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val dialogueRecycView = view.findViewById<RecyclerView>(R.id.dialogueRecycView)
        val dialogueRecycViewMonth = view.findViewById<RecyclerView>(R.id.dialogueRecycView1)
        val dialogueRecycViewYear = view.findViewById<RecyclerView>(R.id.dialogueRecycView2)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        var arrayDate: ArrayList<DateModel>? = ArrayList()
        var arrayDateMonth: ArrayList<DateModel>? = ArrayList()
        var arrayDateYear: ArrayList<DateModel>? = ArrayList()
        for (i in array?.indices) {

            var model = DateModel()
            model.name = array?.get(i)
            model.isSelected = false
            arrayDate?.add(model)
        }

        for (i in arrayMonth?.indices) {

            var model = DateModel()
            model.name = arrayMonth?.get(i)
            model.isSelected = false
            arrayDateMonth?.add(model)
        }

        for (i in arrayYear?.indices) {

            var model = DateModel()
            model.name = arrayYear?.get(i)
            model.isSelected = false
            arrayDateYear?.add(model)
        }

        var arrayAdapter = context?.let { DialogueDateAdpater(arrayDate!!, it, this) }
        var arrayAdaptermonth =
            context?.let { DialogueDateAdpaterMonth(arrayDateMonth!!, it, this) }
        var arrayAdapteryear = context?.let { DialogueDateAdpaterYear(arrayDateYear!!, it, this) }
        dialogueRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        dialogueRecycViewMonth?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        dialogueRecycViewYear?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        dialogueRecycView.adapter = arrayAdapter
        dialogueRecycViewMonth.adapter = arrayAdaptermonth
        dialogueRecycViewYear.adapter = arrayAdapteryear
        arrayAdapter?.onclick = this

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }


        btnSend.setOnClickListener {

            selectedDate = "str"
            ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_applied_calender))
            dialog?.dismiss()

        }

        btn_reset.setOnClickListener {
            selectedDate = "str"
            ivFilter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_calender_icon))
            dialog?.dismiss()

        }

        dialog?.show()

    }
}