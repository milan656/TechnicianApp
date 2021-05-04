package com.walkins.technician.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.walkins.technician.R
import com.walkins.technician.activity.CompletedServiceDetailActivity
import com.walkins.technician.activity.ReportFilterActivity
import com.walkins.technician.activity.SkippedServiceDetailActivity
import com.walkins.technician.adapter.*
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.DashboardModel
import com.walkins.technician.model.login.IssueResolveModel
import com.walkins.technician.model.login.ReportHistoryModel
import com.walkins.technician.model.login.ReportServiceData
import com.walkins.technician.model.login.building.BuildingListData
import com.walkins.technician.model.login.makemodel.VehicleMakeData
import com.walkins.technician.model.login.makemodel.VehicleModelData
import com.walkins.technician.model.login.service.ServiceModel
import com.walkins.technician.model.login.servicelistmodel.ServiceListData
import com.walkins.technician.model.login.servicelistmodel.ServiceListData_2
import com.walkins.technician.viewmodel.CommonViewModel
import com.walkins.technician.viewmodel.MakeModelViewModel
import com.walkins.technician.viewmodel.ServiceViewModel
import java.text.SimpleDateFormat

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : Fragment(), onClickAdapter, View.OnClickListener {
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

    var historyDataList: ArrayList<ReportHistoryModel> = ArrayList<ReportHistoryModel>()

    private lateinit var prefManager: PrefManager
    private lateinit var makeModelViewModel: MakeModelViewModel

    private var llCompleted: LinearLayout? = null
    private var tvSkipped: TextView? = null
    private var tvCompleted: TextView? = null
    private var llSkipped: LinearLayout? = null
    private var skipSelected = false

    private var actvehicleMake: AutoCompleteTextView? = null
    private var actvehicleModel: AutoCompleteTextView? = null
    private var actvehicleSociety: AutoCompleteTextView? = null
    var mAdapter: ReportHistoryAdapter? = null

    private var makeSearchdata: ArrayList<BuildingListData>? = ArrayList()
    private var modelSearchdata: ArrayList<VehicleModelData>? = ArrayList()
    private var selectedMakeId: Int = -1

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

        reportRecycView = view?.findViewById(R.id.reportRecycView)
        tvTitle = view?.findViewById(R.id.tvTitle)
        ivBack = view?.findViewById(R.id.ivBack)
        ivFilterImg = view?.findViewById(R.id.ivFilterImg)
        ivFilterImg?.setOnClickListener(this)

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


        getServiceList()
        val jsonObject = JsonObject()
        jsonObject.addProperty("building_id", "")
        jsonObject.add("service", JsonArray())
        getDashboardService(jsonObject)
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

                        }
                    }
                } else {
                }
            })

        }
    }

    private fun getDashboardService(jsonObject_: JsonObject) {


        activity?.let {
            Common.showLoader(it)

            serviceViewModel?.callApiReportList(jsonObject_, prefManager.getAccessToken()!!, it)
            serviceViewModel?.getReportservice()?.observe(it, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {

                        Log.e("getdataa", "" + it.data)
                        historyDataList.clear()

                        for (i in it.data.serviceData.indices) {
                            Log.e("getdataa", "" + i)
                            var dashboardModel: ReportHistoryModel? = null
                            var list: ArrayList<ServiceListData>? = ArrayList()
                            list?.addAll(it.data.serviceData.get(i).service)
                            when (i) {

                                0, 1 -> {
                                    dashboardModel = ReportHistoryModel(
                                        it.data.serviceData.get(i).regNumber.toInt(), it.data.serviceData.get(i).make + " " + it.data.serviceData.get(i).model, it.data.serviceData.get(i).color, "",
                                        it.data.serviceData.get(i).modelImage, 30, 4, list!!, 40, System.currentTimeMillis(),
                                        System.currentTimeMillis()
                                    )
                                }
                                2, 3 -> {

                                    val dateString = "25/08/2021"
                                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                                    val date_ = sdf.parse(dateString)

                                    val startDate = date_.time

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
                            historyDataList.add(dashboardModel!!)
                        }
                        Log.e("getdataa", "" + historyDataList.size)
                        mAdapter?.notifyDataSetChanged()

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
//                            relNoData?.visibility = View.GONE
                            reportRecycView?.visibility = View.VISIBLE
                        } else {
//                            relNoData?.visibility = View.VISIBLE

                            reportRecycView?.visibility = View.GONE
                        }
//                        mAdapter?.notifyDataSetChanged()

                    } else {
//                        relNoData?.visibility = View.VISIBLE
                        reportRecycView?.visibility = View.GONE
                    }
                } else {
//                    relNoData?.visibility = View.VISIBLE
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
            /* reportRecycView?.addItemDecoration(
                 DividerItemDecoration(
                     this,
                     DividerItemDecoration.VERTICAL
                 )
             )*/
            reportRecycView?.adapter = arrayAdapter
            arrayAdapter?.onclick = this
        } else {
            val arrayAdapter =
                context?.let { ReportHistorySkippedAdapter(it, historyDataList, this) }
            reportRecycView?.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            /* reportRecycView?.addItemDecoration(
                 DividerItemDecoration(
                     this,
                     DividerItemDecoration.VERTICAL
                 )
             )*/
            reportRecycView?.adapter = arrayAdapter
            arrayAdapter?.onclick = this
        }
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

        if (skipSelected) {
            var intent = Intent(context, SkippedServiceDetailActivity::class.java)
            startActivity(intent)

        } else {
            var intent = Intent(context, CompletedServiceDetailActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
//                onBackPressed()
            }
            R.id.ivFilterImg -> {
                openReportFilterDialogue("Choose Filter")
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

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
//        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val serviceRecycView = view.findViewById<RecyclerView>(R.id.serviceRecycView)
        var suggestionArray: ArrayList<IssueResolveModel>? = ArrayList()
        var jsonArray: JsonArray = JsonArray()

        for (i in serviceModel?.data?.indices!!) {
            suggestionArray?.add(
                IssueResolveModel(
                    serviceModel?.data?.get(i)?.name!!, serviceModel?.data?.get(i)?.id!!,
                    false
                )
            )
        }
        var tyreSuggestionAdapter: TyreSuggestionAdpater? = null
        tyreSuggestionAdapter = context?.let { TyreSuggestionAdpater(suggestionArray!!, it, this, false) }
        tyreSuggestionAdapter?.onclick = this
        serviceRecycView?.layoutManager = GridLayoutManager(
            context, 2,
            RecyclerView.VERTICAL,
            false
        )
        serviceRecycView?.adapter = tyreSuggestionAdapter

        tvTitleText?.text = titleStr

        actvehicleMake = view?.findViewById(R.id.actvehicleMake)
        actvehicleModel = view?.findViewById(R.id.actvehicleModel)
        actvehicleSociety = view?.findViewById(R.id.actvehicleSociety)

        actvehicleMake!!.addTextChangedListener(object : TextWatcher {
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

                    searchMake(actvehicleMake?.text.toString())
                } else {

                }
            }

        })

        actvehicleMake!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    selectedMakeId = makeSearchdata?.get(p2)?.id!!
                    selectedSociety = makeSearchdata?.get(p2)?.uuid

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
                } else {

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
            dialog?.dismiss()

            for (i in suggestionArray?.indices!!) {

                if (suggestionArray.get(i).isSelected) {
                    jsonArray.add(suggestionArray.get(i).issueName)
                }
            }
            Log.e("selectedarr", "" + jsonArray)
            val jsonObject = JsonObject()
            jsonObject.addProperty("building_id", "" + selectedSociety)
            jsonObject.add("service", jsonArray)
            getDashboardService(jsonObject)
        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()

            for (i in suggestionArray?.indices!!) {

                suggestionArray.get(i).isSelected = false
            }

            tyreSuggestionAdapter?.notifyDataSetChanged()
            selectedSociety = ""
            actvehicleMake?.setText("")
            val jsonObject = JsonObject()
            jsonObject.addProperty("building_id", "")
            jsonObject.add("service", JsonArray())
            getDashboardService(jsonObject)
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

    private fun searchMake(toString: String) {
        context?.let { makeModelViewModel.callBuildingListApi(it, prefManager.getAccessToken()!!) }

        makeModelViewModel.getBuildingModelList()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {

                    makeSearchdata?.addAll(it.data)
                    try {
                        makeDataForSearchApi(makeSearchdata!!)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {

                }
            } else {

            }
        })
    }

    private fun searchSociety(toString: String) {

    }

    private fun makeDataForSearchApi(makeSearchdata: ArrayList<BuildingListData>) {

        listClicked.clear()
        try {
            for ((index, value) in makeSearchdata.withIndex()) {
                val string =
                    makeSearchdata.get(index).name/* + " --> " + makeSearchdata.get(index).id*/
                listClicked.add(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("listClicked", "" + listClicked)
        if (listClicked.size > 0) {
            adapter =
                context?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        listClicked
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

}