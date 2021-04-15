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
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramotion.fluidslider.FluidSlider
import com.walkins.technician.R
import com.walkins.technician.activity.MainActivity
import com.walkins.technician.activity.ServiceListActivity
import com.walkins.technician.adapter.*
import com.walkins.technician.common.RecyclerViewType
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.datepicker.dialog.SingleDateAndTimePickerDialog
import com.walkins.technician.model.login.SectionModel
import com.walkins.technician.model.login.date.DateModel
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

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    var arrayDate: ArrayList<DateModel>? = ArrayList()
    var arrayDateMonth: ArrayList<DateModel>? = ArrayList()
    var arrayDateYear: ArrayList<DateModel>? = ArrayList()

    private var recyclerViewDay: RecyclerView? = null
    private var recyclerViewMonth: RecyclerView? = null
    private var recyclerViewYear: RecyclerView? = null

    private var adapterDay: DialogueDateAdpater? = null
    private var adapterMonth: DialogueDateAdpaterMonth? = null
    private var adapterYear: DialogueDateAdpaterYear? = null

    var simpleDateFormat: SimpleDateFormat? = null
    var simpleTimeFormat: SimpleDateFormat? = null
    var simpleDateOnlyFormat: SimpleDateFormat? = null
    var simpleDateLocaleFormat: SimpleDateFormat? = null
    var singleBuilder: SingleDateAndTimePickerDialog.Builder? = null
    var sectionModelArrayList: ArrayList<SectionModel> = ArrayList()

    private var arrayList = arrayListOf("Gallery", "Camera")

    private var vehicleMakeList = arrayListOf("")
    private var dummyvaluestart: String? = "0"
    private var dummyvalueend: String? = "100"

    private var tvUsername: TextView? = null
    private var homeRecycView: RecyclerView? = null
    private var adapter: HomeListAdpater? = null
    private var relmainContent: RelativeLayout? = null

    var currentYear: Int = 0
    var currentMonth: Int = 0
    var currentMonth_: String = ""
    var currentDate: Int = 0
    var activity: MainActivity? = null

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

        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH)
        currentDate = calendar.get(Calendar.DATE)

        activity = getActivity() as MainActivity?

        Log.e("getvalues", "" + currentYear + " " + currentMonth + " " + currentDate)


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


        relmainContent = view?.findViewById(R.id.relmainContent)
        tvUsername = view?.findViewById(R.id.tvUsername)
        tvUsername?.text = "Hello, " + "Arun"
        ivFilter?.setOnClickListener(this)

//        homeRecycView = view.findViewById(R.id.homeRecycView)
//
//        homeRecycView?.layoutManager = LinearLayoutManager(
//            context,
//            RecyclerView.VERTICAL,
//            false
//        )
//        adapter = context?.let { HomeListAdpater(arrayList, it, this) }
//        homeRecycView?.adapter = adapter
//        adapter?.onclick = this

        homeRecycView = view.findViewById(R.id.sectioned_recycler_view)

        homeRecycView?.setHasFixedSize(true)
        homeRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )


        populateRecyclerView()
// Java


        return view

    }

    private fun populateRecyclerView() {

        //for loop for sections
        for (i in 1..2) {
            val itemArrayList: ArrayList<String> = ArrayList()
            //for loop for items
            for (j in 1..2) {
                itemArrayList.add("Item $j")
            }

            //add the section and items to array list
            if (i == 1) {
                sectionModelArrayList.add(SectionModel("Today", itemArrayList))
            } else if (i == 2) {
                sectionModelArrayList.add(SectionModel("29 April", itemArrayList))
            }
        }
        val adapter = context?.let {
            SectionRecyclerViewAdapter(
                it,
                RecyclerViewType.LINEAR_VERTICAL,
                sectionModelArrayList, this
            )
        }
        homeRecycView?.setAdapter(adapter)
        adapter?.onclick = this


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
                simpleClicked()

            }
        }
    }

    fun simpleClicked() {

        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = currentMonth // 4. Feb. 2018
        calendar[Calendar.MONTH] = 1
        calendar[Calendar.YEAR] = currentYear
        calendar[Calendar.HOUR_OF_DAY] = 11
        calendar[Calendar.MINUTE] = 13
        val defaultDate = calendar.time
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
            .defaultDate(defaultDate)
            .displayMonthNumbers(true).maxDateRange(Date()) //.mustBeOnFuture()
            //.minutesStep(15)
            //.mustBeOnFuture()
            //.defaultDate(defaultDate)
            // .minDateRange(minDate)
            // .maxDateRange(maxDate)
            /*.displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                fun onDisplayed(picker: SingleDateAndTimePicker?) {
                    TODO("Not yet implemented")
                }

                fun onClosed(picker: SingleDateAndTimePicker?) {
                    TODO("Not yet implemented")
                }
            })*/
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