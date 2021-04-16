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
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramotion.fluidslider.FluidSlider
import com.trading212.demo.item.SimpleStickyTextRecyclerItem
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.layoutmanager.DiverseLinearLayoutManager
import com.trading212.stickyheader.StickyHeaderDecoration
import com.walkins.technician.R
import com.walkins.technician.activity.MainActivity
import com.walkins.technician.activity.ServiceListActivity
import com.walkins.technician.common.item.SimpleTextRecyclerItem
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.datepicker.dialog.SingleDateAndTimePickerDialog
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

    var gamesRecyclerItems = listOf<SimpleTextRecyclerItem>()

    var simpleDateFormat: SimpleDateFormat? = null
    var singleBuilder: SingleDateAndTimePickerDialog.Builder? = null
    var sectionModelArrayList: ArrayList<SectionModel> = ArrayList()

    private var arrayList = arrayListOf("Gallery", "Camera")


    private var tvUsername: TextView? = null
    private var homeRecycView: RecyclerView? = null
    private var relmainContent: RelativeLayout? = null

    var currentYear: Int = 0
    var currentMonth: Int = 0
    var currentDay: Int = 0
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

    private lateinit var stickyHeaderDecoration: StickyHeaderDecoration

    private var stickyIdsCounter = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH)
        currentDate = calendar.get(Calendar.DATE)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        activity = getActivity() as MainActivity?

        Log.e("getvalues", "" + currentYear + " " + currentMonth + " " + currentDate)


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

//
        homeRecycView = view.findViewById(R.id.recyclerView)

        homeRecycView?.layoutManager = context?.let { DiverseLinearLayoutManager(it) }

        fillRecyclerView()

        return view

    }

    fun fillRecyclerView() {
        val adapter = DiverseRecyclerAdapter()
        gamesRecyclerItems = generateGamesList().map { SimpleTextRecyclerItem(it, this) }
        for (i in 1..3) {

            if (i == 1) {
                adapter.addItem(
                    SimpleStickyTextRecyclerItem(
                        SimpleStickyTextRecyclerItem.StickyData(
                            "Today",
                            ++stickyIdsCounter
                        )
                    ), false
                )
                adapter.addItems(gamesRecyclerItems, false)

            } else if (i == 2) {
                adapter.addItem(
                    SimpleStickyTextRecyclerItem(
                        SimpleStickyTextRecyclerItem.StickyData(
                            "29 April",
                            ++stickyIdsCounter
                        )
                    ), false
                )
                adapter.addItems(gamesRecyclerItems, false)

            } else if (i == 3) {
                adapter.addItem(
                    SimpleStickyTextRecyclerItem(
                        SimpleStickyTextRecyclerItem.StickyData(
                            "15 May",
                            ++stickyIdsCounter
                        )
                    ), false
                )
                adapter.addItems(gamesRecyclerItems, false)

            }
        }



        stickyHeaderDecoration = StickyHeaderDecoration()
        homeRecycView?.addItemDecoration(stickyHeaderDecoration)

        homeRecycView?.adapter = adapter

        adapter.onItemActionListener = object : DiverseRecyclerAdapter.OnItemActionListener() {
            override fun onItemClicked(v: View, position: Int) {

                adapter.insertItem(
                    0,
                    SimpleStickyTextRecyclerItem(
                        SimpleStickyTextRecyclerItem.StickyData(
                            "Item${System.currentTimeMillis()}",
                            ++stickyIdsCounter
                        )
                    )
                )
                adapter.insertItem(
                    0,
                    SimpleTextRecyclerItem("Item${System.currentTimeMillis()}", this@HomeFragment)
                )
            }

            override fun onItemLongClicked(v: View, position: Int): Boolean {

                adapter.removeItem(1)

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

            Log.e("getsection", "" + sectionModelArrayList?.get(variable)?.sectionLabel)

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
        calendar[Calendar.DAY_OF_MONTH] = currentDay // 4. Feb. 2018
        calendar[Calendar.MONTH] = currentMonth
        calendar[Calendar.YEAR] = currentYear
//        calendar[Calendar.HOUR_OF_DAY] = 11
//        calendar[Calendar.MINUTE] = 13
        val defaultDate = calendar.time
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
            .defaultDate(defaultDate)
            .displayMonthNumbers(true)
            .minDateRange(future) //.mustBeOnFuture()
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