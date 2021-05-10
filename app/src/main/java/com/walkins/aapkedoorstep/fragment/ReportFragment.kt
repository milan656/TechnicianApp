package com.walkins.aapkedoorstep.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.activity.CompletedServiceDetailActivity
import com.walkins.aapkedoorstep.activity.SkippedServiceDetailActivity
import com.walkins.aapkedoorstep.adapter.*
import com.walkins.aapkedoorstep.common.OnBottomReachedListener
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.model.login.IssueResolveModel
import com.walkins.aapkedoorstep.model.login.ReportHistoryModel
import com.walkins.aapkedoorstep.model.login.building.BuildingListData
import com.walkins.aapkedoorstep.model.login.makemodel.VehicleModelData
import com.walkins.aapkedoorstep.model.login.service.ServiceModel
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListData
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.MakeModelViewModel
import com.walkins.aapkedoorstep.viewmodel.ServiceViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var timer: Timer? = null
    private var param1: String? = null
    private var param2: String? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivFilterImg: ImageView? = null
    private var reportRecycView: RecyclerView? = null
    private val listClicked = ArrayList<String>()
    private val listClickedModel = ArrayList<String>()
    private var adapter: AutoSuggestProductAdapter? = null
    private var serviceViewModel: ServiceViewModel? = null
    private var commonViewModel: CommonViewModel? = null
    private var serviceModel: ServiceModel? = null
    private var selectedSociety: String? = ""
    private var selectedSocietyName: String? = ""
    private var arrayService: ArrayList<Int>? = ArrayList()

    var historyDataList: ArrayList<ReportHistoryModel> = ArrayList<ReportHistoryModel>()

    private lateinit var prefManager: PrefManager
    private lateinit var makeModelViewModel: MakeModelViewModel

    private var llCompleted: LinearLayout? = null
    private var tvSkipped: TextView? = null
    private var tvNoData: TextView? = null
    private var tvCompleted: TextView? = null
    private var edtSearch: EditText? = null
    private var llSkipped: LinearLayout? = null
    private var skipSelected = false

    private var actvehicleMake: AutoCompleteTextView? = null
    private var actvehicleModel: AutoCompleteTextView? = null
    private var actvehicleSociety: AutoCompleteTextView? = null
    var societyList = java.util.ArrayList<String>()
    private var autoSuggestProductAdapter: ArrayAdapter<String>? = null

    var mAdapter: ReportHistoryAdapter? = null
    var mAdapterSkipped: ReportHistorySkippedAdapter? = null

    private var makeSearchdata: ArrayList<BuildingListData>? = ArrayList()
    private var modelSearchdata: ArrayList<VehicleModelData>? = ArrayList()
    private var selectedMakeId: Int = -1
    private var pagesize = 10
    private var page = 1
    private var isDataFinish: Boolean? = false
    private var selectedTab = "complete"
    private var selectedServiceJson: JsonArray? = JsonArray()

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
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        prefManager = context?.let { PrefManager(it) }!!
        makeModelViewModel = ViewModelProviders.of(this).get(MakeModelViewModel::class.java)
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        init(view)
        return view
    }

    private fun init(view: View?) {
        llSkipped = view?.findViewById(R.id.llSkippedReport)
        llCompleted = view?.findViewById(R.id.llCompletedReport)
        tvSkipped = view?.findViewById(R.id.tvSkipped)
        tvCompleted = view?.findViewById(R.id.tvCompleted)
        tvNoData = view?.findViewById(R.id.tvNoData)
        edtSearch = view?.findViewById(R.id.edtSearch)
        tvNoData?.visibility = View.GONE

        reportRecycView = view?.findViewById(R.id.reportRecycView)
        tvTitle = view?.findViewById(R.id.tvTitle)
        ivBack = view?.findViewById(R.id.ivBack)
        ivFilterImg = view?.findViewById(R.id.ivFilterImg)
        ivFilterImg?.setOnClickListener(this)

        try {
            ivFilterImg?.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.ic_report_icon))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        ivBack?.setOnClickListener(this)
        llCompleted?.setOnClickListener(this)
        llSkipped?.setOnClickListener(this)

        tvTitle?.text = "Your Report"

        ivBack?.visibility = View.GONE

//        setadapter(skipSelected)

        /* for (i in 0..5) {

             var dashboardModel: ReportHistoryModel? = null
             when (i) {
                 0, 1 -> {
                     dashboardModel = ReportHistoryModel(
                         "Titanium City Center,Anandnagar","","","",
                         24, 20, 21, 45, System.currentTimeMillis(),
                         System.currentTimeMillis()
                     )
                 }
                 2, 3, 4, 5 -> {
                     val dateString = "30/09/2021"
                     val sdf = SimpleDateFormat("dd/MM/yyyy")
                     val date = sdf.parse(dateString)

                     val startDate = date.time
                     dashboardModel = ReportHistoryModel(
                         "Prahladnagar garden","","","",
                         34, 30, 4, 40, startDate,
                         startDate
                     )
                 }
             }

             historyDataList.add(dashboardModel!!)
         }*/

//        homeRecycView?.setHasFixedSize(true)
        mAdapter = context?.let { ReportHistoryAdapter(it, historyDataList, this) }
        var decor = StickyHeaderDecoration(mAdapter)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        reportRecycView?.setLayoutManager(layoutManager)
        reportRecycView?.setAdapter(mAdapter)
        reportRecycView?.addItemDecoration(decor)
        mAdapter?.onclick = this



        mAdapter?.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                if (position >= 9) {

                    if (!isDataFinish!!) {

                        page = page + 1

                        // fetchBlockList()
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("building_id", selectedSocietyName)
                        jsonObject.add("service", selectedServiceJson)
                        jsonObject.addProperty("status", selectedTab)
                        jsonObject.addProperty("pagesize", pagesize)
                        jsonObject.addProperty("page", page)
                        jsonObject.addProperty("q", edtSearch?.text?.toString())
                        getDashboardService(jsonObject, false)

//                        callApiToGetAllCustomer("", false, 10, page!!, 1, groupId)

                    }
                }
            }

        })

        mAdapterSkipped?.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                if (position >= 9) {

                    if (!isDataFinish!!) {

                        page = page + 1

                        // fetchBlockList()
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("building_id", selectedSocietyName)
                        jsonObject.add("service", selectedServiceJson)
                        jsonObject.addProperty("status", selectedTab)
                        jsonObject.addProperty("pagesize", pagesize)
                        jsonObject.addProperty("page", page)
                        jsonObject.addProperty("q", edtSearch?.text?.toString())
                        getDashboardService(jsonObject, false)

//                        callApiToGetAllCustomer("", false, 10, page!!, 1, groupId)

                    }
                }
            }

        })


        /* mAdapterSkipped = context?.let { ReportHistorySkippedAdapter(it, historyDataList, this) }
         var decorSkip = StickyHeaderDecoration(mAdapterSkipped)

         // use a linear layout manager
         val layoutManagerSkip = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
         reportRecycView?.setLayoutManager(layoutManagerSkip)
         reportRecycView?.setAdapter(mAdapterSkipped)
         reportRecycView?.addItemDecoration(decorSkip)
         mAdapterSkipped?.onclick = this

         mAdapterSkipped?.setOnBottomReachedListener(object : OnBottomReachedListener {
             override fun onBottomReached(position: Int) {
                 if (position >= 9) {

                     if (!isDataFinish!!) {

                         page = page + 1

                         // fetchBlockList()
                         val jsonObject = JsonObject()
                         jsonObject.addProperty("building_id", selectedSocietyName)
                         jsonObject.add("service", selectedServiceJson)
                         jsonObject.addProperty("status", selectedTab)
                         jsonObject.addProperty("pagesize", pagesize)
                         jsonObject.addProperty("page", page)
                         jsonObject.addProperty("q", edtSearch?.text?.toString())
                         getDashboardService(jsonObject, false)

 //                        callApiToGetAllCustomer("", false, 10, page!!, 1, groupId)

                     }
                 }
             }

         })*/
        getServiceList()

        llCompleted?.performClick()

        edtSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (timer != null) {
                    timer?.cancel()
                }

            }

            override fun afterTextChanged(s: Editable?) {


                timer = Timer()
                timer?.schedule(
                    object : TimerTask() {
                        override fun run() {
                            activity?.runOnUiThread {
                                val jsonObject = JsonObject()
                                jsonObject.addProperty("building_id", selectedSocietyName)
                                jsonObject.add("service", selectedServiceJson)
                                jsonObject.addProperty("status", selectedTab)
                                jsonObject.addProperty("pagesize", pagesize)
                                jsonObject.addProperty("page", page)
                                if (s != null && s.toString().length > 1) {
                                    jsonObject.addProperty("q", edtSearch?.text?.toString())
                                } else {

                                    jsonObject.addProperty("q", "")
                                }
                                getDashboardService(jsonObject, true)

                            }
                        }
                    }, 600
                )
            }

        })

        searchMake("", false)

    }

    private fun getServiceList() {

        activity?.let {
            commonViewModel?.callApiGetService(prefManager.getAccessToken()!!, it)
            commonViewModel?.getService()?.observe(it, androidx.lifecycle.Observer {
                if (it != null) {
                    if (it.success) {

                        serviceModel = it

                    } else {
                        if (it.error != null && it.error?.get(0)?.message != null) {
                            Toast.makeText(context, "" + it.error.get(0).message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                }
            })

        }
    }

    private fun getDashboardService(jsonObject_: JsonObject, b: Boolean) {

        activity?.let {
            Common.showLoader(it)

            serviceViewModel?.callApiReportList(jsonObject_, prefManager.getAccessToken()!!, it)
            serviceViewModel?.getReportservice()?.observe(it, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {

                        Log.e("getdataa", "" + it.data)
                        if (b) {
                            historyDataList.clear()
                        }
                        for (i in it.data.serviceData.indices) {
                            Log.e("getdataa", "" + i)
                            var dashboardModel: ReportHistoryModel? = null
                            var list: ArrayList<ServiceListData>? = ArrayList()
                            list?.addAll(it.data.serviceData.get(i).service)

                            val dateString = it.data.serviceData.get(i).service_scheduled_date
                            Log.e("getdatefrom", "" + dateString)
                            val sdf = SimpleDateFormat("yyyy-MM-dd")
                            val date = sdf.parse(dateString)

                            val startDate = date.time

                            var commentid = -1

                            try {
                                if (it.data.serviceData.get(i).comment_id.get(0) != null
                                ) {
                                    Log.e("getcomment00", "" + it.data.serviceData.get(i).comment_id.get(0))
                                    commentid = it.data.serviceData.get(i).comment_id.get(0)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            dashboardModel = ReportHistoryModel(
                                it.data.serviceData.get(i).uuid, it.data.serviceData.get(i).address,
                                it.data.serviceData.get(i).regNumber,
                                it.data.serviceData.get(i).make + " " + it.data.serviceData.get(i).model,
                                it.data.serviceData.get(i).color, it.data.serviceData.get(i).color_code,
                                it.data.serviceData.get(i).service_scheduled_date, commentid,
                                it.data.serviceData.get(i).modelImage,
                                30,
                                4,
                                list!!,
                                40,
                                startDate,
                                startDate
                            )
                            historyDataList.add(dashboardModel)
                            /*when (i) {

                                0, 1 -> {
                                    dashboardModel = ReportHistoryModel(
                                        it.data.serviceData.get(i).regNumber.toInt(), it.data.serviceData.get(i).make + " " + it.data.serviceData.get(i).model, it.data.serviceData.get(i).color, "",
                                        it.data.serviceData.get(i).modelImage, 30, 4, list!!, 40, System.currentTimeMillis(),
                                        System.currentTimeMillis()
                                    )
                                }
                                2, 3 -> {

                                    val dateString = it.data.serviceData.get(i).service_scheduled_date
                                    Log.e("getdatefrom", "" + dateString)
                                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                                    val date = sdf.parse(dateString)

                                    val startDate = date.time

                                    dashboardModel = ReportHistoryModel(
                                        it.data.serviceData.get(i).regNumber.toInt(), it.data.serviceData.get(i).make + " " + it.data.serviceData.get(i).model, it.data.serviceData.get(i).color, "",
                                        it.data.serviceData.get(i).modelImage, 30, 4, list!!, 40, startDate,
                                        startDate
                                    )
                                }
                                4, 5 -> {

                                    val dateString = "24/10/2021"
                                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                                    val date_ = sdf.parse(dateString)

                                    val startDate = date_.time
                                    dashboardModel = ReportHistoryModel(
                                        it.data.serviceData.get(i).regNumber.toInt(), it.data.serviceData.get(i).make + " " + it.data.serviceData.get(i).model, it.data.serviceData.get(i).color, "",
                                        it.data.serviceData.get(i).modelImage, 30, 4, list!!, 40, startDate,
                                        startDate
                                    )
                                }
                            }
                            Log.e("getdataa", "" + dashboardModel)
                            historyDataList.add(dashboardModel!!)*/
                        }
                        Log.e("getdataa", "" + historyDataList.size)

                        if (historyDataList.size > 0) {
                            tvNoData?.visibility = View.GONE
                            if (selectedTab.equals("complete")) {
                                tvCompleted?.text = "Completed - ${historyDataList.size}"
                            } else {
                                tvSkipped?.text = "Skipped - ${historyDataList.size}"
                            }
                        } else {
                            tvNoData?.visibility = View.VISIBLE
                            if (selectedTab.equals("complete")) {
                                tvNoData?.text = "There is no any completed report"
                                tvCompleted?.text = "Completed - ${historyDataList.size}"
                            } else {
                                tvNoData?.text = "There is no any skip report"
                                tvSkipped?.text = "Skipped - ${historyDataList.size}"
                            }
                        }

                        if (selectedTab.equals("complete")) {
                            mAdapter?.notifyDataSetChanged()
                        } else {
                            mAdapterSkipped?.notifyDataSetChanged()
                        }

                        if (it.data != null && it.data.serviceData.size > 0) {


                            /*for (i in it.data.serviceData.indices) {

                                var dashboardModel: ReportHistoryModel? = null
                                when (i) {
                                    0, 1 -> {
                                        val dateString = "25/06/2021"
                                        Log.e("getdatefrom", "" + dateString)
                                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                                        val date = sdf.parse(dateString)
                                        val startDate = date.time
                                        dashboardModel = ReportHistoryModel(
                                            "", "", "", "",
                                            1, 2, 3, 45, startDate,
                                            startDate
                                        )
                                    }
                                    2, 3, 4, 5 -> {
                                        val dateString = "30/09/2021"
                                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                                        val date = sdf.parse(dateString)

                                        val startDate = date.time
                                        dashboardModel = ReportHistoryModel(
                                            "", "", "", "",
                                            1, 2, 3, 45, startDate,
                                            startDate
                                        )
                                    }
                                }

                                historyDataList.add(dashboardModel!!)
                            }*/
                            reportRecycView?.visibility = View.VISIBLE
                        } else {
                            reportRecycView?.visibility = View.GONE
                        }
                    } else {
                        reportRecycView?.visibility = View.GONE
                    }
                } else {
                    reportRecycView?.visibility = View.GONE
                }
            })
        }
    }

    private fun setadapter(skipSelected: Boolean) {
        if (!skipSelected) {
            val arrayAdapter = context?.let { ReportHistoryAdapter(it, historyDataList, this) }
            reportRecycView?.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            mAdapter = arrayAdapter
            reportRecycView?.adapter = arrayAdapter
            arrayAdapter?.onclick = this
            selectedTab = "complete"
            page = 1
            pagesize = 10
        } else {
            val arrayAdapter =
                context?.let { ReportHistorySkippedAdapter(it, historyDataList, this) }
            reportRecycView?.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            mAdapterSkipped = arrayAdapter
            reportRecycView?.adapter = arrayAdapter
            arrayAdapter?.onclick = this
            selectedTab = "skip"
            page = 1
            pagesize = 10

        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("building_id", selectedSocietyName)
        jsonObject.add("service", selectedServiceJson)
        jsonObject.addProperty("status", selectedTab)
        jsonObject.addProperty("pagesize", pagesize)
        jsonObject.addProperty("page", page)
        jsonObject.addProperty("q", edtSearch?.text?.toString())
        getDashboardService(jsonObject, true)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 10) {

            showBottomSheetdialogNormal(
                Common.commonPhotoChooseArr,
                "Address Details",
                context,
                Common.btn_filled,
                false, Common.getStringBuilder(historyDataList.get(variable).fullAddress)
            )
        } else {
            if (selectedTab.equals("skip")) {
                Log.e("getregno", "" + historyDataList.get(variable).comment_id)
                var intent = Intent(context, SkippedServiceDetailActivity::class.java)
                intent.putExtra("color", historyDataList.get(variable).carColor)
                intent.putExtra("makeModel", historyDataList.get(variable).makeModel)
                intent.putExtra("regNumber", "" + historyDataList.get(variable).regNumber)
                intent.putExtra("carImage", historyDataList.get(variable).carImageURL)
                intent.putExtra("uuid", historyDataList.get(variable).uuid)
                intent.putExtra("address", historyDataList.get(variable).fullAddress)
                intent.putExtra("colorcode", historyDataList.get(variable).color_code)
                intent.putExtra("ischange", "false")
                intent.putExtra("formatedDate", historyDataList.get(variable).dateFormated)
                intent.putExtra("comment_id", "" + historyDataList.get(variable).comment_id)
                startActivity(intent)

            } else {
                var intent = Intent(context, CompletedServiceDetailActivity::class.java)
                intent.putExtra("color", historyDataList.get(variable).carColor)
                intent.putExtra("makeModel", historyDataList.get(variable).makeModel)
                intent.putExtra("regNumber", "" + historyDataList.get(variable).regNumber)
                intent.putExtra("carImage", historyDataList.get(variable).carImageURL)
                intent.putExtra("uuid", historyDataList.get(variable).uuid)
                intent.putExtra("address", historyDataList.get(variable).fullAddress)
                intent.putExtra("colorcode", historyDataList.get(variable).color_code)
                intent.putExtra("formatedDate", historyDataList.get(variable).dateFormated)
                startActivity(intent)

            }
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
//                onBackPressed()
            }
            R.id.ivFilterImg -> {

                searchMake("Choose Filter", true)
//                var intent = Intent(context, ReportFilterActivity::class.java)
//                startActivityForResult(intent, 100)
            }
            R.id.llCompletedReport -> {
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvCompleted?.setTextColor(this.resources.getColor(R.color.white))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))
                skipSelected = false

                setadapter(skipSelected)
            }
            R.id.llSkippedReport -> {
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))

                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.white))
                skipSelected = true
                setadapter(skipSelected)

            }

        }
    }

    private fun openReportFilterDialogue(titleStr: String) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_report_filter, null)
        val dialog =
            context.let {
                it?.let { it1 ->
                    BottomSheetDialog(
                        it1,
                        R.style.CustomBottomSheetDialogTheme
                    )
                }
            }
//        val dialog = AlertDialog.Builder(context).create()
        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
//        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)
//        dialog?.setView(view)

        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val serviceRecycView = view.findViewById<RecyclerView>(R.id.serviceRecycView)
        val suggestionArray: ArrayList<IssueResolveModel>? = ArrayList()
        val jsonArray: JsonArray = JsonArray()

        actvehicleMake = view.findViewById(R.id.actvehicleMake)
        actvehicleMake?.setText("" + selectedSocietyName)

        activity?.let {
            autoSuggestProductAdapter =
                object : ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, societyList) {

                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val v = super.getView(position, convertView, parent)

//                        (v as TextView).textSize = 14f
//                        v.gravity = Gravity.LEFT

                        return v
                    }
                }

        }

        actvehicleMake?.threshold = 1
        actvehicleMake?.setAdapter(autoSuggestProductAdapter)

        suggestionArray?.clear()
        for (i in serviceModel?.data?.indices!!) {

            suggestionArray?.add(
                IssueResolveModel(
                    serviceModel?.data?.get(i)?.name!!, serviceModel?.data?.get(i)?.id!!,
                    false
                )
            )

        }
        Log.e("selectedarr", "" + arrayService)
        for (i in suggestionArray?.indices!!) {
            if (arrayService?.size!! > 0) {
                for (j in arrayService?.indices!!) {
                    if (suggestionArray.get(i).id == arrayService?.get(j)) {

                        suggestionArray.set(
                            i,
                            IssueResolveModel(
                                suggestionArray?.get(i)?.issueName!!, suggestionArray?.get(i)?.id!!,
                                true
                            )
                        )
                    }
                }
            }
        }

        Log.e("selectedarr00", "" + suggestionArray)
        var tyreSuggestionAdapter: TyreSuggestionAdpater? = null
        tyreSuggestionAdapter = context?.let { TyreSuggestionAdpater(suggestionArray!!, it, this, false, true) }
        serviceRecycView?.layoutManager = GridLayoutManager(
            context, 2,
            RecyclerView.VERTICAL,
            false
        )
        serviceRecycView?.adapter = tyreSuggestionAdapter

        tvTitleText?.text = titleStr


        actvehicleModel = view?.findViewById(R.id.actvehicleModel)
        actvehicleSociety = view?.findViewById(R.id.actvehicleSociety)


        /* actvehicleMake!!.addTextChangedListener(object : TextWatcher {
             override fun afterTextChanged(s: Editable?) {

             }

             override fun beforeTextChanged(
                 s: CharSequence?,
                 start: Int,
                 count: Int,
                 after: Int
             ) {

             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                 if (s?.toString()?.length!! > 1) {

                     searchMake(actvehicleMake?.text.toString())
                 } else {

                     selectedSociety = ""
                     selectedSocietyName = ""
                 }
             }
         })

         actvehicleMake!!.onItemClickListener =
             object : AdapterView.OnItemClickListener {
                 override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                     selectedMakeId = makeSearchdata?.get(p2)?.id!!
                     selectedSociety = makeSearchdata?.get(p2)?.uuid
                     selectedSocietyName = makeSearchdata?.get(p2)?.name

                 }
             }*/

        actvehicleMake!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    selectedMakeId = makeSearchdata?.get(p2)?.id!!
                    selectedSociety = makeSearchdata?.get(p2)?.uuid
                    selectedSocietyName = makeSearchdata?.get(p2)?.name

                }
            }
        actvehicleModel!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.toString()?.length!! > 0) {

                    searchModel(actvehicleModel?.text.toString())
                }
            }

        })

        actvehicleModel!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }
            }
        actvehicleSociety!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.toString()?.length!! > 0) {

                    searchSociety(actvehicleSociety?.text.toString())
                } else {

                }
            }

        })

        actvehicleSociety!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }
            }
        tvTitleText?.text = titleStr

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }

        btnConfirm.setOnClickListener {

            arrayService?.clear()

            for (i in suggestionArray.indices) {

                if (suggestionArray.get(i).isSelected) {
                    jsonArray.add(suggestionArray.get(i).id)
                    arrayService?.add(suggestionArray.get(i).id)
                    Log.e("selectedarr", "" + suggestionArray.get(i).id + " " + suggestionArray.get(i).issueName)
                }
            }

            selectedServiceJson = jsonArray

            if (!selectedSociety.equals("") || selectedServiceJson?.size()!! > 0) {
                dialog?.dismiss()
                ivFilterImg?.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.ic_report_filtered_icon))
                val jsonObject = JsonObject()
                jsonObject.addProperty("building_id", "" + selectedSociety)
                jsonObject.add("service", selectedServiceJson)
                jsonObject.addProperty("status", selectedTab)
                jsonObject.addProperty("pagesize", pagesize)
                jsonObject.addProperty("page", page)
                jsonObject.addProperty("q", edtSearch?.text?.toString())
                getDashboardService(jsonObject, true)

            } else {
                Toast.makeText(context, "Please select service or society", Toast.LENGTH_SHORT).show()
            }

        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()

            for (i in suggestionArray.indices) {

                suggestionArray.get(i).isSelected = false
            }

            tyreSuggestionAdapter?.notifyDataSetChanged()
            selectedSociety = ""
            selectedSocietyName = ""
            actvehicleMake?.setText("")
            selectedServiceJson = JsonArray()
            arrayService?.clear()
            ivFilterImg?.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.ic_report_icon))
            val jsonObject = JsonObject()
            jsonObject.addProperty("building_id", "")
            jsonObject.add("service", selectedServiceJson)
            jsonObject.addProperty("status", selectedTab)
            jsonObject.addProperty("pagesize", pagesize)
            jsonObject.addProperty("page", page)
            jsonObject.addProperty("q", edtSearch?.text?.toString())
            getDashboardService(jsonObject, true)

        }

        dialog?.show()
    }

    private fun searchModel(toString: String) {
        context?.let { makeModelViewModel?.getVehicleModel(it, selectedMakeId, prefManager.getAccessToken()!!) }

        makeModelViewModel?.getVehicleModelList()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {

                    modelSearchdata?.addAll(it.data)
                    try {
                        modelDataForSearchApi(modelSearchdata!!)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {

                }
            } else {

            }
        })
    }

    private fun searchMake(toString: String, isDialoguOpen: Boolean) {
        if (makeSearchdata != null && makeSearchdata?.size!! > 0 && societyList.size > 0) {
            openReportFilterDialogue(toString)
        } else {
            activity?.let {
                Common.showLoader(it)
                makeModelViewModel.callBuildingListApi(it, prefManager.getAccessToken()!!)

                makeModelViewModel.getBuildingModelList()?.observe(it, Observer {

                    Common.hideLoader()
                    if (it != null) {
                        if (it.success) {

                            makeSearchdata?.clear()
                            makeSearchdata?.addAll(it.data)

                            try {
                                makeDataForSearchApi(makeSearchdata!!, isDialoguOpen)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun searchSociety(toString: String) {

    }

    private fun makeDataForSearchApi(makeSearchdata: ArrayList<BuildingListData>, isDialoguOpen: Boolean) {
        societyList.clear()
        try {
            for ((index, value) in makeSearchdata.withIndex()) {
                val string =
                    makeSearchdata.get(index).name/* + " --> " + makeSearchdata.get(index).id*/
                societyList.add(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (isDialoguOpen) {
            openReportFilterDialogue("Choose Filter")
        }
//        Log.e("listClicked", "" + listClicked)
        /*if (listClicked.size > 0) {
            val adapter: ArrayAdapter<String>? = context?.let { ArrayAdapter<String>(it, android.R.layout.select_dialog_item, listClicked) }
//            adapter =
//                context?.let {
//                    AutoSuggestProductAdapter(
//                        it,
//                        android.R.layout.simple_list_item_1,
//                        listClicked
//                    )
//                }
            actvehicleMake?.threshold = 1
            actvehicleMake?.setAdapter<ArrayAdapter<String>>(adapter)
        } else {
            val noValueList: ArrayList<String> = ArrayList()
            noValueList.add("No any building found")
            val adapter: ArrayAdapter<String>? = context?.let { ArrayAdapter<String>(it, android.R.layout.select_dialog_item, noValueList) }
//            adapter =
//                context?.let {
//                    AutoSuggestProductAdapter(
//                        it,
//                        android.R.layout.simple_list_item_1,
//                        noValueList
//                    )
//                }
            actvehicleMake?.threshold = 1
            actvehicleMake?.setAdapter<ArrayAdapter<String>>(adapter)
        }*/


    }

    private fun modelDataForSearchApi(modelSearchData: ArrayList<VehicleModelData>) {

        listClickedModel.clear()
        try {
            for ((index, value) in modelSearchData.withIndex()) {
                val string =
                    modelSearchData.get(index).name + " --> " + modelSearchData.get(index).id
                listClickedModel.add(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("listClicked", "" + listClicked)
        if (listClickedModel.size > 0) {
            adapter =
                context?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        listClickedModel
                    )
                }
            actvehicleMake?.threshold = 1
            actvehicleMake?.setAdapter<ArrayAdapter<String>>(adapter)
        } else {
            var noValueList: ArrayList<String> = ArrayList()
            noValueList.add("No any dealer found")
            adapter =
                context?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        noValueList
                    )
                }
            actvehicleMake?.threshold = 1
            actvehicleMake?.setAdapter<ArrayAdapter<String>>(adapter)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            100 -> {
                Log.e("getresults", "" + data?.getStringExtra("action"))

                if (data?.getStringExtra("action").equals("confirm")) {

                } else if (data?.getStringExtra("action").equals("reset")) {

                }
            }
        }
    }

    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        stringBuilder: StringBuilder
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog =
            context?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

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
        val str = stringBuilder.toString().replace(",", "," + "\n")
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
}