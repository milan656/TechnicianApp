package com.walkins.aapkedoorstep.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.example.technician.common.RetrofitCommonClass
import com.facebook.internal.Utility
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import com.walkins.aapkedoorstep.DB.DBClass
import com.walkins.aapkedoorstep.DB.ServiceDashboardModelClass
import com.walkins.aapkedoorstep.DB.ServiceListModelClass
import com.walkins.aapkedoorstep.DB.VehicleMakeModelClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.activity.MainActivity
import com.walkins.aapkedoorstep.activity.ServiceListActivity
import com.walkins.aapkedoorstep.adapter.LeadHistoryAdapter
import com.walkins.aapkedoorstep.common.dateForWebservice_2
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.datepicker.dialog.SingleDateAndTimePickerDialog
import com.walkins.aapkedoorstep.model.login.DashboardModel
import com.walkins.aapkedoorstep.model.login.SectionModel
import com.walkins.aapkedoorstep.model.login.dashboard_model.DashboardServiceListModel
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.aapkedoorstep.networkApi.ServiceApi
import com.walkins.aapkedoorstep.repository.CommonRepo
import com.walkins.aapkedoorstep.repository.ServiceRepo
import com.walkins.aapkedoorstep.viewmodel.common.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.common.CommonViewModelFactory
import com.walkins.aapkedoorstep.viewmodel.service.ServiceViewModel
import com.walkins.aapkedoorstep.viewmodel.service.ServiceViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@SuppressLint(
    "UseCompatLoadingForDrawables", "ClickableViewAccessibility", "SimpleDateFormat", "InflateParams",
    "SetTextI18n"
)
class HomeFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private lateinit var mDb: DBClass
    private var param1: String? = null
    private var param2: String? = null
    var calendar = Calendar.getInstance()
    private var ivFilter: ImageView? = null
    var dashboardModel: DashboardServiceListModel? = null
    var dashboardServiceListModel: DashboardServiceListModel? = null
    private var selectedDate: String? = ""
    private var serviceViewModel: ServiceViewModel? = null
    private lateinit var serviceRepo: ServiceRepo
    private lateinit var serviceViewModelFactory: ServiceViewModelFactory

    private var prefManager: PrefManager? = null
    private lateinit var commonRepo: CommonRepo
    private lateinit var commonViewModelFactory: CommonViewModelFactory
    private var commonViewModel: CommonViewModel? = null

    var historyDataList: ArrayList<DashboardModel> = ArrayList<DashboardModel>()

    var simpleDateFormat: SimpleDateFormat? = null
    var singleBuilder: SingleDateAndTimePickerDialog.Builder? = null
    var sectionModelArrayList: ArrayList<SectionModel> = ArrayList()

    private var arrayList = arrayListOf("Gallery", "Camera")

    private var tvUsername: TextView? = null
    private var homeRecycView: RecyclerView? = null
    private var relmainContent: RelativeLayout? = null
    private var relNoData: LinearLayout? = null
    private var tvNoData: TextView? = null
    private var homeSwipeRefresh: SwipeRefreshLayout? = null

    var activity: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private var mAdapter: LeadHistoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        activity = getActivity() as MainActivity?
        commonRepo = CommonRepo()
        commonViewModelFactory = CommonViewModelFactory(commonRepo)
        commonViewModel = ViewModelProvider(this, commonViewModelFactory).get(CommonViewModel::class.java)

        serviceRepo = ServiceRepo()
        serviceViewModelFactory = ServiceViewModelFactory(serviceRepo)
        serviceViewModel = ViewModelProvider(this, serviceViewModelFactory).get(ServiceViewModel::class.java)

        mDb = context?.let { DBClass.getInstance(it) }!!

        prefManager = context?.let { PrefManager(it) }
        init(view)

        return view

    }

    private fun getUserInfo() {
        activity?.let {
            commonViewModel?.callApiGetUserInfo(prefManager?.getAccessToken()!!, it)
            commonViewModel?.userInfo?.observe(it, {
                if (it != null) {
                    if (it.success) {

                        Log.e("TAG", "getUserInfo: " + it.data)
                        var firstName: String? = ""
                        var lastName: String? = ""
                        if (it.data.firstName != null) {
                            firstName = it.data.firstName
                        }
                        if (it.data.lastName != null) {
                            lastName = it.data.lastName
                        }
                        tvUsername?.text = "Hello, " + firstName + " " + lastName
                    } else {
                        Log.e("TAG", "getUserInfo: " + it.error)
                        if (it.error != null) {
                            if (it.error?.get(0).message != null) {
                                Toast.makeText(context, "" + it.error.get(0).message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun init(view: View?) {
        ivFilter = view?.findViewById(R.id.ivFilter)

        homeSwipeRefresh = view?.findViewById(R.id.homeSwipeRefresh)
        relmainContent = view?.findViewById(R.id.relmainContent)
        relNoData = view?.findViewById(R.id.relNoData)
        tvNoData = view?.findViewById(R.id.tvNoData)
        tvUsername = view?.findViewById(R.id.tvUsername)
        tvUsername?.text = "Hello, " + ""
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            homeSwipeRefresh?.setColorSchemeColors(
                resources.getColor(android.R.color.holo_green_dark, null),
                resources.getColor(android.R.color.holo_red_dark, null),
                resources.getColor(android.R.color.holo_blue_dark, null),
                resources.getColor(android.R.color.holo_orange_dark, null)
            )
        }


        homeSwipeRefresh?.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                this@HomeFragment.onRefresh()
            }

        })

    }

    fun onRefresh() {
        context?.let {

            if (!Common.isConnectedToInternet(it)) {
                getServiceFromDB()
            } else {
                getDashboardService(selectedDate!!)
                getUserInfo()
            }
        }


        homeSwipeRefresh?.post { homeSwipeRefresh?.isRefreshing = false }
    }

    private fun getServiceFromDB() {

        CoroutineScope(Dispatchers.IO).launch {
            if (mDb.ServiceListDashbaordDaoClass().getAll() != null &&
                mDb.ServiceListDashbaordDaoClass().getAll().size > 0
            ) {
                historyDataList.clear()
                for (i in mDb.ServiceListDashbaordDaoClass().getAll()) {

                    var dashboardModel: DashboardModel? = null
                    val dateString = i.date
                    Log.e("getdatefrom", "" + dateString)
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val date = sdf.parse(dateString)

                    val startDate = date.time
                    Log.e("getdatefromstart", "" + startDate)
                    dashboardModel = DashboardModel(
                        i.buildingnamearea!!, i.address!!, i.date!!, i.buildinguuid!!, i.dateformated!!,
                        i.openjobs!!, i.completedjobs!!, i.skippedjobs, i.totaljobs, startDate,
                        startDate
                    )

                    historyDataList.add(dashboardModel)
                }

                getActivity()?.runOnUiThread {
                    relNoData?.visibility = View.GONE
                    homeRecycView?.visibility = View.VISIBLE
                    mAdapter?.notifyDataSetChanged()
                }

            } else {
                getActivity()?.runOnUiThread {
                    relNoData?.visibility = View.VISIBLE
                    homeRecycView?.visibility = View.GONE
                    mAdapter?.notifyDataSetChanged()
                }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        context?.let {
            if (!Common.isConnectedToInternet(it)) {
                getServiceFromDB()
            } else {
                getDashboardService(selectedDate!!)
                getUserInfo()
            }
        }

    }

    private fun getDashboardService(displayDate: String) {

        activity?.let {
            Common.showLoader(it)
            serviceViewModel?.callApiDashboardService(displayDate, prefManager?.getAccessToken()!!, it)
            serviceViewModel?.dashboardServiceListModel?.observe(it, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {

                        Log.e("TAG", "getDashboardService: " + it.data)

                        Log.e("getdataa", "" + it.data)
                        dashboardModel = it
                        historyDataList.clear()

                        if (it.data != null && it.data.size > 0) {

                            dashboardServiceListModel = it
                            for (i in it.data.indices) {

                                var dashboardModel: DashboardModel? = null
                                val dateString = it.data.get(i).date
                                Log.e("getdatefrom", "" + dateString)
                                val sdf = SimpleDateFormat("yyyy-MM-dd")
                                val date = sdf.parse(dateString)

                                val startDate = date.time
                                Log.e("getdatefromstart", "" + startDate)
                                dashboardModel = DashboardModel(
                                    it.data.get(i).building_name + ", " + it.data.get(i).area, it.data.get(i).address, it.data.get(i).date, it.data.get(i).building_uuid, it.data.get(i).date_formated,
                                    it.data.get(i).open_jobs.toInt(), it.data.get(i).complete_jobs.toInt(), it.data.get(i).skip_jobs.toInt(), it.data.get(i).total_jobs.toInt(), startDate,
                                    startDate
                                )

                                historyDataList.add(dashboardModel)
                            }
                            relNoData?.visibility = View.GONE
                            homeRecycView?.visibility = View.VISIBLE

                            saveDashboardService(dashboardServiceListModel!!)

                            if (dashboardServiceListModel?.data != null && dashboardServiceListModel?.data?.size!! > 0) {
                                for (i in dashboardServiceListModel?.data!!) {
                                    Log.e("getpassdata", "" + i.building_uuid)
                                    val mDateFormat = SimpleDateFormat("dd MMMM yy")
                                    val mToday = mDateFormat.format(Date())
                                    val date_ = mDateFormat.format(Date(i.date_formated)).toString()
                                    Log.e("getdatefor", "" + mToday + " " + date_)

                                    if (mToday.equals(date_)) {
                                        getServiceList(i.building_uuid, i.date)
                                    }
                                }
                            }

                        } else {
                            relNoData?.visibility = View.VISIBLE

                            homeRecycView?.visibility = View.GONE
                            if (selectedDate.equals("")) {
                                tvNoData?.text = "Currently you do not have any services"
                            } else {
                                tvNoData?.text = "There are no service on " + selectedDate
                            }
                        }
                        mAdapter?.notifyDataSetChanged()

                    } else {
                        Log.e("TAG", "getDashboardService: " + it.success)
                        relNoData?.visibility = View.VISIBLE
                        homeRecycView?.visibility = View.GONE
                        if (selectedDate.equals("")) {
                            tvNoData?.text = "Currently you do not have any services"
                        } else {
                            tvNoData?.text = "There are no service on " + selectedDate
                        }
                    }
                } else {
                    relNoData?.visibility = View.VISIBLE
                    homeRecycView?.visibility = View.GONE
                    if (selectedDate.equals("")) {
                        tvNoData?.text = "Currently you do not have any services"
                    } else {
                        tvNoData?.text = "There are no service on " + selectedDate
                    }
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
        strBuilder: StringBuilder,
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
//        "address": "Pehel Lake View, Beside Auda Garden, Behind Shaligram Lake View",
        val str = strBuilder.toString().replace(", ", "" + "\n").replace(",", "" + "\n")
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
                "Address Detail",
                context,
                Common.btn_filled,
                false, Common.getStringBuilder(historyDataList.get(variable).fullAddress)
            )

        } else if (check == 0) {

            val intent = Intent(context, ServiceListActivity::class.java)
            Log.e("getdateformat", "" + activity?.dateForWebservice_2(historyDataList.get(variable).date))
            intent.putExtra("selectedDate", "" + activity?.dateForWebservice_2(historyDataList.get(variable).date))
            intent.putExtra("selectedDateFormated", historyDataList.get(variable).dateFormated)
            intent.putExtra("addressTitle", historyDataList.get(variable).addressTitle)
            intent.putExtra("fullAddress", historyDataList.get(variable).fullAddress)
            intent.putExtra("building_uuid", historyDataList.get(variable).building_uuid)
//            intent.putExtra("",""+historyDataList.get(variable).)
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
        var future: Date? = null
        var setdefault: Date? = null

        setdefault = calendar.getTime()
        if (selectedDate.equals("")) {
            future = calendar.getTime()
        } else {
//            Wed May 19 12:55:46 GMT+05:30 2021
            var date_: Date? = null
            val formatter = SimpleDateFormat("dd MMM yyyy")
            try {
                date_ = formatter.parse(selectedDate)
                Log.e("formated_date ", date_.toString() + "")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            future = date_
        }
        Log.e("getfuturedate", "" + future + " " + selectedDate)
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
            .minDateRange(setdefault) //.mustBeOnFuture()
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
                        context?.let {
                            if (!Common.isConnectedToInternet(it)) {
                                getServiceFromDB()
                            } else {
                                getDashboardService(displayDate)
                            }
                        }
//                        getDashboardService(displayDate)

                    } else {
                        if (!str.equals("Close")) {
                            context?.let {
                                if (!Common.isConnectedToInternet(it)) {
                                    getServiceFromDB()
                                } else {
                                    getDashboardService(selectedDate!!)
                                }
                            }
//                            getDashboardService(selectedDate!!)
                        }
                    }
                }
            })

        activity?.lltransparent?.visibility = View.VISIBLE
        singleBuilder?.display()


    }

    private fun saveDashboardService(dashboardServiceListModel: DashboardServiceListModel) {

        var thread: Thread = Thread {
            if (mDb.ServiceListDashbaordDaoClass().getAll().size > 0) {
                mDb.ServiceListDashbaordDaoClass().deleteAll()
            }
            if (mDb.serviceListDaoClass().getAll().size > 0) {
                mDb.serviceListDaoClass().deleteAll()
            }

            if (dashboardServiceListModel.data != null && dashboardServiceListModel.data.size > 0) {
                for (i in dashboardServiceListModel.data) {

                    var entity = ServiceDashboardModelClass()

                    val dateString = i.date
                    Log.e("getdatefrom", "" + dateString)
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val date = sdf.parse(dateString)

                    val startDate = date.time

                    entity.buildingnamearea = i.building_name + ", " + i.area
                    entity.address = i.address
                    entity.date = i.date
                    entity.buildinguuid = i.building_uuid
                    entity.dateformated = i.date_formated
                    entity.openjobs = i.open_jobs.toInt()
                    entity.completedjobs = i.complete_jobs.toInt()
                    entity.skippedjobs = i.skip_jobs.toInt()
                    entity.totaljobs = i.total_jobs.toInt()
                    entity.startdate = startDate
                    entity.updatedate = startDate

                    val mDateFormat = SimpleDateFormat("dd MMMM yy")
                    val mToday = mDateFormat.format(Date())
                    val date_ = mDateFormat.format(Date(entity.dateformated)).toString()
                    Log.e("getdatefor", "" + mToday + " " + date_)

                    if (mToday.equals(date_)) {
                        mDb.ServiceListDashbaordDaoClass().save(entity)
                    }
                }

            }
            Log.e("response+++", "++++" + mDb.ServiceListDashbaordDaoClass().getAll().size)
        }

        thread.start()

    }

    private fun getServiceList(buildingUuid: String, date: String) {

        getActivity()?.let {
            serviceViewModel?.callApiServiceByDate_(activity?.dateForWebservice_2(date)!!, buildingUuid,
                prefManager?.getAccessToken()!!, it
            )
        }

        serviceViewModel?.serviceListByDateModel?.observe(this, {
            if (it.success) {
                saveServiceList(it, buildingUuid)
            }
        })
        /*val serviceApi = RetrofitCommonClass.createService(ServiceApi::class.java)

        var call: Call<ResponseBody>? = null
        call = serviceApi.getServiceByDate(activity?.dateForWebservice_2(date)!!, buildingUuid,
            prefManager?.getAccessToken()!!
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val gson = GsonBuilder().create()
                        var serviceListModel: ServiceListByDateModel = gson.fromJson(
                            response.body()?.string(),
                            ServiceListByDateModel::class.java
                        )
                        Log.e("getservicelistmodel::", "" + serviceListModel)

                        saveServiceList(serviceListModel, buildingUuid)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })*/
    }

    private fun saveServiceList(serviceListModel: ServiceListByDateModel, buildingUuid: String) {
        var thread: Thread = Thread {
            /* if (mDb.serviceListDaoClass().getAll().size > 0) {
                 mDb.serviceListDaoClass().deleteAll()
             }*/

            if (serviceListModel.data != null && serviceListModel.data.size > 0) {
                for (i in serviceListModel.data) {

                    val entity = ServiceListModelClass()
                    Log.e("getservicelistmodel0::", "" + i.id)
                    entity.serviceId = i.id.toString()
                    entity.uuid = i.uuid
                    entity.color = i.color
                    entity.color_code = i.color_code
                    entity.status = i.status
                    entity.regNumber = i.regNumber
                    entity.service_user_name = i.service_user_name
                    entity.service_user_mobile = i.service_user_mobile
                    entity.make = i.make
                    entity.model = i.model
                    entity.make_id = i.make_id
                    entity.model_id = i.model_id
                    entity.model_image = i.model_image
                    entity.building_uuid = buildingUuid
                    if (i.service != null && i.service.size > 0) {
                        entity.service = i.service
                    }
                    entity.image = i.image
                    entity.buildingName = i.buildingName
                    entity.address = i.address

                    if (i.comment_id != null && i.comment_id.size > 0) {
                        entity.comment_id = i.comment_id
                    }

                    mDb.serviceListDaoClass().save(entity)
                }

            }
            Log.e("response+++", "++++" + mDb.serviceListDaoClass().getAll().size)
        }

        thread.start()

    }


}