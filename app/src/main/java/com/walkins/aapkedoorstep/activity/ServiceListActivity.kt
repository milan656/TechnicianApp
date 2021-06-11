package com.walkins.aapkedoorstep.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.walkins.aapkedoorstep.DB.DBClass
import com.walkins.aapkedoorstep.DB.ServiceListModelClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.ServicesListAdpater
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.common.showShortToast
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateData
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel
import com.walkins.aapkedoorstep.viewmodel.ServiceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext


@SuppressLint("SetTextI18n")
class ServiceListActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {
    private lateinit var mDb: DBClass
    private var loginViewModel: LoginActivityViewModel? = null
    private var serviceViewModel: ServiceViewModel? = null
    private var prefManager: PrefManager? = null
    private var llSkipped: LinearLayout? = null
    private var llCompleted: LinearLayout? = null
    private var llUpcoming: LinearLayout? = null

    private var serviceListDataModel: ServiceListByDateModel? = null

    private var tvSkipped: TextView? = null
    private var tvUpcoming: TextView? = null
    private var tvCompleted: TextView? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null

    private var serviceRecycView: RecyclerView? = null
    private var ivInfoService: ImageView? = null

    //    private var arrayList: muta<ServiceListByDateData> = ArrayList()
    private var arrayList = mutableListOf<ServiceListByDateData>()
    private var adapter: ServicesListAdpater? = null
    private var tvAddress: TextView? = null
    private var tvDate: TextView? = null
    private var tvNoServiceData: TextView? = null
    private var llAddressView: LinearLayout? = null
    private var relNoData: LinearLayout? = null
    private var serviceListSwipe: SwipeRefreshLayout? = null

    private var selectedDate = ""
    private var selectedDateFormated = ""
    private var addressTitle = ""
    private var fullAddress = ""
    private var building_uuid = ""
    private var isAddServiceEnable = false

    companion object {
        var upcomming = "open"
        var completed = "completed"
        var skipped = "skipped"
    }

    private var serviceStatus = upcomming
    private var llTabs: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        prefManager = PrefManager(this)
        mDb = DBClass.getInstance(this)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        init()

        val diff = Common.dateDifference(prefManager?.getAccessTokenExpireDate()!!)
        if (diff <= 1) {
            refreshToken()
        }


    }

    private fun init() {
        serviceRecycView = findViewById(R.id.serviceRecycView)
        serviceListSwipe = findViewById(R.id.serviceListSwipe)
        llTabs = findViewById(R.id.llTabs)
        relNoData = findViewById(R.id.relNoData)
        llAddressView = findViewById(R.id.llAddressView)

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        llSkipped = findViewById(R.id.llSkipped)
        llUpcoming = findViewById(R.id.llUpcoming)
        llCompleted = findViewById(R.id.llCompleted)
        ivInfoService = findViewById(R.id.ivInfoService)

        tvCompleted = findViewById(R.id.tvCompleted)
        tvUpcoming = findViewById(R.id.tvUpcoming)
        tvSkipped = findViewById(R.id.tvSkipped)
        tvAddress = findViewById(R.id.tvAddress)
        tvDate = findViewById(R.id.tvDate)
        tvNoServiceData = findViewById(R.id.tvNoServiceData)

        tvUpcoming?.text = "Upcoming - 0"
        tvCompleted?.text = "Completed - 0"
        tvSkipped?.text = "Skipped - 0"

        llUpcoming?.setOnClickListener(this)
        llCompleted?.setOnClickListener(this)
        llSkipped?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        ivInfoService?.setOnClickListener(this)

        serviceRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        adapter = this.let { ServicesListAdpater(arrayList, it, this, serviceStatus, isAddServiceEnable) }
        serviceRecycView?.adapter = adapter
        adapter?.onclick = this

        tvTitle?.text = "Service List"

        if (intent != null) {
            if (intent.hasExtra("selectedDate")) {
                selectedDate = intent.getStringExtra("selectedDate")!!
            }
            if (intent.hasExtra("selectedDateFormated")) {
                selectedDateFormated = intent.getStringExtra("selectedDateFormated")!!
            }
            if (intent.hasExtra("addressTitle")) {
                addressTitle = intent.getStringExtra("addressTitle")!!
            }
            if (intent.hasExtra("fullAddress")) {
                fullAddress = intent.getStringExtra("fullAddress")!!
            }
            if (intent.hasExtra("building_uuid")) {
                building_uuid = intent.getStringExtra("building_uuid")!!
            }
        }
        val mDateFormat = SimpleDateFormat("dd MMMM yy")
        val mToday = mDateFormat.format(Date())
        val date = mDateFormat.format(Date(selectedDateFormated)).toString()
        Log.e("getdatefor", "" + mToday + " " + date)

        isAddServiceEnable = mToday.equals(date)

        if (mToday.equals(date)) {
            llTabs?.visibility = View.VISIBLE
        } else {
            llTabs?.visibility = View.GONE
        }

        tvDate?.text = selectedDateFormated
        tvAddress?.text = addressTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            serviceListSwipe?.setColorSchemeColors(
                resources.getColor(android.R.color.holo_green_dark, null),
                resources.getColor(android.R.color.holo_red_dark, null),
                resources.getColor(android.R.color.holo_blue_dark, null),
                resources.getColor(android.R.color.holo_orange_dark, null)
            )
        }

        serviceListSwipe?.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                this@ServiceListActivity.onRefresh()
            }
        })

    }

    fun onRefresh() {
        if (Common.isConnectedToInternet(this)) {
            getServiceListByDate()
            serviceListSwipe?.post { serviceListSwipe?.isRefreshing = false }
        } else {
            getDataFromDatabase()
            serviceListSwipe?.post { serviceListSwipe?.isRefreshing = false }
        }

    }

    override fun onResume() {
        super.onResume()
        if (Common.isConnectedToInternet(this)) {
            getServiceListByDate()
            serviceListSwipe?.post { serviceListSwipe?.isRefreshing = false }
        } else {
            getDataFromDatabase()

        }
    }

    fun getDataFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("getservicedataa", "" + mDb.serviceListDaoClass().getAll().size)
            Log.e("getservicedataa", "" + mDb.serviceListDaoClass().getAll())

            val arrayListfinal = mDb.serviceListDaoClass().getAll().filter { it.building_uuid.equals(building_uuid) } as MutableList<ServiceListModelClass>
            Log.e("getservicedataa", "" + building_uuid + " -- " + arrayListfinal.size)

            arrayList.clear()

            for (i in arrayListfinal) {
                arrayList.add(ServiceListByDateData(
                    i.serviceId.toInt(),
                    i.uuid!!,
                    if (i.color != null) i.color!! else "",
                    if (i.color_code != null) i.color_code!! else "",
                    if (i.status != null) i.status!! else "",
                    if (i.regNumber != null) i.regNumber!! else "",
                    if (i.service_user_name != null) i.service_user_name!! else "",
                    if (i.service_user_mobile != null) i.service_user_mobile!! else "",
                    if (i.make != null) i.make!! else "",
                    if (i.model != null) i.model!! else "",
                    i.make_id!!,
                    i.model_id!!,
                    if (i.model_image != null) i.model_image!! else "",
                    if (i.service != null) i.service!! else ArrayList(),
                    if (i.image != null) i.image!! else "",
                    if (i.buildingName != null) i.buildingName!! else "",
                    if (i.address != null) i.address!! else "",
                    if (i.comment_id != null && i.comment_id?.size!! > 0) i.comment_id!! else ArrayList()
                ))
                Log.e("savearr", "" + arrayList.size)
            }

            runOnUiThread {
                Log.e("savearr1", "" + arrayList.size)
                if (arrayList.size > 0) {

                    val arrayListUpcoming = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>

                    tvUpcoming?.text = "Upcoming - " + arrayListUpcoming.size
                    val arrayskipList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>

                    tvSkipped?.text = "Skipped - " + arrayskipList.size
                    val arrayCompleteList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>

                    tvCompleted?.text = "Completed - " + arrayCompleteList.size

                    Log.e("getservicestatuslist", "" + arrayList)
                    Log.e("getservicestatuslist", "" + arrayskipList)
                    Log.e("getservicestatuslist", "" + arrayCompleteList)
                    Log.e("getservicestatuslist", "" + serviceStatus)
                }

                if (serviceStatus.equals(upcomming)) {
//                            arrayList.filter { it.status.equals(upcomming) }
                    arrayList = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>
                    adapter =
                        this.let {
                            ServicesListAdpater(arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>,
                                this@ServiceListActivity,
                                this@ServiceListActivity,
                                serviceStatus,
                                isAddServiceEnable)
                        }

                    tvUpcoming?.text = "Upcoming - ${arrayList.size}"
                    Log.e("getservicedata", "" + arrayList.size)
                } else if (serviceStatus.equals(completed)) {
//                            arrayList.filter { it.status.equals(completed) }
                    Log.e("getservicedata0", "" + arrayList.size)
                    arrayList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>
                    adapter =
                        this.let {
                            ServicesListAdpater(arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>,
                                this@ServiceListActivity,
                                this@ServiceListActivity,
                                serviceStatus,
                                isAddServiceEnable)
                        }
                    tvCompleted?.text = "Completed - ${arrayList.size}"
                } else if (serviceStatus.equals(skipped)) {
//                            arrayList.filter { it.status.equals(skipped) }
                    Log.e("getservicedata1", "" + arrayList.size)
                    arrayList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>
                    adapter =
                        this@ServiceListActivity.let {
                            ServicesListAdpater(arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>,
                                it,
                                this@ServiceListActivity,
                                serviceStatus,
                                isAddServiceEnable)
                        }
                    tvSkipped?.text = "Skipped - ${arrayList.size}"
                }

                tvNoServiceData?.visibility = View.GONE
                Log.e("getservicesize", "" + arrayList.size + " " + serviceStatus)
                if (arrayList.size == 0) {
                    tvNoServiceData?.text = "There is no any Upcomming service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                    relNoData?.visibility = View.VISIBLE
                    llAddressView?.visibility = View.GONE
                    tvNoServiceData?.visibility = View.GONE
                } else {
                    llAddressView?.visibility = View.VISIBLE
                    relNoData?.visibility = View.GONE
                }

                serviceRecycView?.adapter = adapter
                adapter?.onclick = this@ServiceListActivity
            }

        }
    }

    private fun getServiceListByDate() {

        Common.showLoader(this)
        serviceViewModel?.callApiServiceByDate(selectedDate, building_uuid, prefManager?.getAccessToken()!!, this)
        serviceViewModel?.getServiceByDate()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {

                    serviceListDataModel = it

                    if (it.data != null) {

                        arrayList.clear()
                        arrayList.addAll(it.data)

                        if (it.data.size > 0) {

                            val arrayList = it.data.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>

                            tvUpcoming?.text = "Upcoming - " + arrayList.size
                            val arrayskipList = it.data.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>

                            tvSkipped?.text = "Skipped - " + arrayskipList.size
                            val arrayCompleteList = it.data.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>

                            tvCompleted?.text = "Completed - " + arrayCompleteList.size
                        }

                        if (serviceStatus.equals(upcomming)) {
//                            arrayList.filter { it.status.equals(upcomming) }
                            arrayList = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>
                            adapter =
                                this.let { ServicesListAdpater(arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>, it, this, serviceStatus, isAddServiceEnable) }

                            tvUpcoming?.text = "Upcoming - ${arrayList.size}"
                            Log.e("getservicedata", "" + arrayList.size)
                        } else if (serviceStatus.equals(completed)) {
//                            arrayList.filter { it.status.equals(completed) }
                            Log.e("getservicedata0", "" + arrayList.size)
                            arrayList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>
                            adapter =
                                this.let { ServicesListAdpater(arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>, it, this, serviceStatus, isAddServiceEnable) }
                            tvCompleted?.text = "Completed - ${arrayList.size}"
                        } else if (serviceStatus.equals(skipped)) {
//                            arrayList.filter { it.status.equals(skipped) }
                            Log.e("getservicedata1", "" + arrayList.size)
                            arrayList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>
                            adapter =
                                this.let { ServicesListAdpater(arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>, it, this, serviceStatus, isAddServiceEnable) }
                            tvSkipped?.text = "Skipped - ${arrayList.size}"
                        }



                        tvNoServiceData?.visibility = View.GONE
                        if (arrayList.size == 0) {
                            tvNoServiceData?.text = "There is no any Upcomming service to display"
                            tvNoServiceData?.visibility = View.VISIBLE
                            relNoData?.visibility = View.VISIBLE
                            llAddressView?.visibility = View.GONE
                            tvNoServiceData?.visibility = View.GONE
                        } else {
                            llAddressView?.visibility = View.VISIBLE
                            relNoData?.visibility = View.GONE
                        }

                        serviceRecycView?.adapter = adapter
                        adapter?.onclick = this

//                        llUpcoming?.performClick()
                    }
                } else {
                    if (it.error != null && it.error?.get(0).message != null) {
                        showShortToast(it.error?.get(0).message, this)
                    }
                }
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(v: View?) {

        val id = v?.id
        when (id) {
            R.id.llUpcoming -> {

                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.white))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))

                serviceStatus = upcomming
                tvNoServiceData?.visibility = View.GONE

                arrayList.clear()
                if (Common.isConnectedToInternet(this)) {
                    arrayList.addAll(serviceListDataModel?.data!!)

                    arrayList = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>

                    adapter = ServicesListAdpater(arrayList, this, this, serviceStatus, isAddServiceEnable)
                    if (arrayList.size == 0) {
                        tvNoServiceData?.text = "There is no any Upcoming service to display"
                        tvNoServiceData?.visibility = View.VISIBLE
                        llAddressView?.visibility = View.GONE
                        tvNoServiceData?.visibility = View.GONE
                        relNoData?.visibility = View.VISIBLE
                    } else {
                        llAddressView?.visibility = View.VISIBLE
                        relNoData?.visibility = View.GONE
                    }
                    tvUpcoming?.text = "Upcoming - ${arrayList.size}"
                    serviceRecycView?.adapter = adapter
                    adapter?.onclick = this@ServiceListActivity


                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        val arrayListfinal = mDb.serviceListDaoClass().getAll().filter { it.building_uuid.equals(building_uuid) } as MutableList<ServiceListModelClass>
                        for (i in arrayListfinal) {
                            arrayList.add(ServiceListByDateData(
                                i.serviceId.toInt(),
                                i.uuid!!,
                                if (i.color != null) i.color!! else "",
                                if (i.color_code != null) i.color_code!! else "",
                                if (i.status != null) i.status!! else "",
                                if (i.regNumber != null) i.regNumber!! else "",
                                if (i.service_user_name != null) i.service_user_name!! else "",
                                if (i.service_user_mobile != null) i.service_user_mobile!! else "",
                                if (i.make != null) i.make!! else "",
                                if (i.model != null) i.model!! else "",
                                i.make_id!!,
                                i.model_id!!,
                                if (i.model_image != null) i.model_image!! else "",
                                if (i.service != null) i.service!! else ArrayList(),
                                if (i.image != null) i.image!! else "",
                                if (i.buildingName != null) i.buildingName!! else "",
                                if (i.address != null) i.address!! else "",
                                if (i.comment_id != null && i.comment_id?.size!! > 0) i.comment_id!! else ArrayList()
                            ))
                            Log.e("savearr66", "" + arrayList.size)
                        }
                        arrayList = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>

                        runOnUiThread {
                            adapter = ServicesListAdpater(arrayList, this@ServiceListActivity, this@ServiceListActivity, serviceStatus, isAddServiceEnable)
                            if (arrayList.size == 0) {
                                tvNoServiceData?.text = "There is no any Upcoming service to display"
                                tvNoServiceData?.visibility = View.VISIBLE
                                llAddressView?.visibility = View.GONE
                                tvNoServiceData?.visibility = View.GONE
                                relNoData?.visibility = View.VISIBLE
                            } else {
                                llAddressView?.visibility = View.VISIBLE
                                relNoData?.visibility = View.GONE
                            }
                            tvUpcoming?.text = "Upcoming - ${arrayList.size}"
                            serviceRecycView?.adapter = adapter
                            adapter?.onclick = this@ServiceListActivity

                        }
                    }
                }

                Log.e("savearr1", "" + arrayList.size)

            }
            R.id.llCompleted -> {
                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.white))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))
                serviceStatus = completed
                tvNoServiceData?.visibility = View.GONE

                arrayList.clear()
                if (Common.isConnectedToInternet(this)) {
                    arrayList.addAll(serviceListDataModel?.data!!)

                    arrayList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>

                    Log.e("savearr44", "" + arrayList.size)
                    adapter = ServicesListAdpater(arrayList, this, this, serviceStatus, isAddServiceEnable)

                    if (arrayList.size == 0) {
                        tvNoServiceData?.text = "There is no any Completed service to display"
                        tvNoServiceData?.visibility = View.VISIBLE
                        llAddressView?.visibility = View.GONE
                        relNoData?.visibility = View.VISIBLE
                        tvNoServiceData?.visibility = View.GONE
                    } else {
                        llAddressView?.visibility = View.VISIBLE
                        relNoData?.visibility = View.GONE
                    }
                    tvCompleted?.text = "Completed - ${arrayList.size}"
                    serviceRecycView?.adapter = adapter
                    adapter?.onclick = this


                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val arrayListfinal = mDb.serviceListDaoClass().getAll().filter { it.building_uuid.equals(building_uuid) } as MutableList<ServiceListModelClass>
                        for (i in arrayListfinal) {
                            arrayList.add(ServiceListByDateData(
                                i.serviceId.toInt(),
                                i.uuid!!,
                                if (i.color != null) i.color!! else "",
                                if (i.color_code != null) i.color_code!! else "",
                                if (i.status != null) i.status!! else "",
                                if (i.regNumber != null) i.regNumber!! else "",
                                if (i.service_user_name != null) i.service_user_name!! else "",
                                if (i.service_user_mobile != null) i.service_user_mobile!! else "",
                                if (i.make != null) i.make!! else "",
                                if (i.model != null) i.model!! else "",
                                i.make_id!!,
                                i.model_id!!,
                                if (i.model_image != null) i.model_image!! else "",
                                if (i.service != null) i.service!! else ArrayList(),
                                if (i.image != null) i.image!! else "",
                                if (i.buildingName != null) i.buildingName!! else "",
                                if (i.address != null) i.address!! else "",
                                if (i.comment_id != null && i.comment_id?.size!! > 0) i.comment_id!! else ArrayList()
                            ))
                            Log.e("savearr45", "" + arrayList.size)
                        }
                        arrayList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>

                        runOnUiThread {
                            Log.e("savearr44", "" + arrayList.size)
                            adapter = ServicesListAdpater(arrayList, this@ServiceListActivity, this@ServiceListActivity, serviceStatus, isAddServiceEnable)

                            if (arrayList.size == 0) {
                                tvNoServiceData?.text = "There is no any Completed service to display"
                                tvNoServiceData?.visibility = View.VISIBLE
                                llAddressView?.visibility = View.GONE
                                relNoData?.visibility = View.VISIBLE
                                tvNoServiceData?.visibility = View.GONE
                            } else {
                                llAddressView?.visibility = View.VISIBLE
                                relNoData?.visibility = View.GONE
                            }
                            tvCompleted?.text = "Completed - ${arrayList.size}"
                            serviceRecycView?.adapter = adapter
                            adapter?.onclick = this@ServiceListActivity

                        }
                    }
                }

            }
            R.id.llSkipped -> {
                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.white))
                serviceStatus = skipped
                tvNoServiceData?.visibility = View.GONE


                arrayList.clear()
                if (Common.isConnectedToInternet(this)) {
                    arrayList.addAll(serviceListDataModel?.data!!)

                    arrayList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>

                    adapter = ServicesListAdpater(arrayList, this@ServiceListActivity, this@ServiceListActivity, serviceStatus, isAddServiceEnable)

                    if (arrayList.size == 0) {
                        tvNoServiceData?.text = "There is no any Skipped service to display"
                        tvNoServiceData?.visibility = View.VISIBLE
                        llAddressView?.visibility = View.GONE
                        relNoData?.visibility = View.VISIBLE
                        tvNoServiceData?.visibility = View.GONE
                    } else {
                        llAddressView?.visibility = View.VISIBLE
                        relNoData?.visibility = View.GONE
                    }
                    tvSkipped?.text = "Skipped - ${arrayList.size}"
                    serviceRecycView?.adapter = adapter
                    adapter?.onclick = this@ServiceListActivity

                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val arrayListfinal = mDb.serviceListDaoClass().getAll().filter { it.building_uuid.equals(building_uuid) } as MutableList<ServiceListModelClass>
                        for (i in arrayListfinal) {
                            arrayList.add(ServiceListByDateData(
                                i.serviceId.toInt(),
                                i.uuid!!,
                                if (i.color != null) i.color!! else "",
                                if (i.color_code != null) i.color_code!! else "",
                                if (i.status != null) i.status!! else "",
                                if (i.regNumber != null) i.regNumber!! else "",
                                if (i.service_user_name != null) i.service_user_name!! else "",
                                if (i.service_user_mobile != null) i.service_user_mobile!! else "",
                                if (i.make != null) i.make!! else "",
                                if (i.model != null) i.model!! else "",
                                i.make_id!!,
                                i.model_id!!,
                                if (i.model_image != null) i.model_image!! else "",
                                if (i.service != null) i.service!! else ArrayList(),
                                if (i.image != null) i.image!! else "",
                                if (i.buildingName != null) i.buildingName!! else "",
                                if (i.address != null) i.address!! else "",
                                if (i.comment_id != null && i.comment_id?.size!! > 0) i.comment_id!! else ArrayList()
                            ))
                            Log.e("savearr00", "" + arrayList.size)
                        }
                        arrayList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>

                        runOnUiThread {

                            adapter = ServicesListAdpater(arrayList, this@ServiceListActivity, this@ServiceListActivity, serviceStatus, isAddServiceEnable)

                            if (arrayList.size == 0) {
                                tvNoServiceData?.text = "There is no any Skipped service to display"
                                tvNoServiceData?.visibility = View.VISIBLE
                                llAddressView?.visibility = View.GONE
                                relNoData?.visibility = View.VISIBLE
                                tvNoServiceData?.visibility = View.GONE
                            } else {
                                llAddressView?.visibility = View.VISIBLE
                                relNoData?.visibility = View.GONE
                            }
                            tvSkipped?.text = "Skipped - ${arrayList.size}"
                            serviceRecycView?.adapter = adapter
                            adapter?.onclick = this@ServiceListActivity
                        }
                    }
                }

                Log.e("savearr33", "" + arrayList.size)


            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivInfoService -> {
                showBottomSheetdialogNormal(
                    arrayList,
                    "Address Detail",
                    this,
                    Common.btn_filled,
                    false,
                    Common.getStringBuilder(fullAddress)
                )
            }
        }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 1) {
            Log.e("checkva", "" + arrayList.get(variable).status + " " + arrayList.get(variable).regNumber)
            Log.e("checkva", "" + " " + arrayList.get(variable).uuid)

            if (serviceStatus.equals(upcomming)) {

                val handler = Handler()
                handler.postDelayed(Runnable {
                    super.onResume()
                    Common.setClearAllValues()
                    Common.setFalseAllTyreStatus()
                    val intent = Intent(this, AddServiceDetailsActivity::class.java)
                    intent.putExtra("color", arrayList.get(variable).color)
                    intent.putExtra("makeModel", arrayList.get(variable).make + " " + arrayList.get(variable).model)
                    intent.putExtra("regNumber", arrayList.get(variable).regNumber)
                    intent.putExtra("carImage", arrayList.get(variable).model_image)
                    intent.putExtra("uuid", arrayList.get(variable).uuid)
                    intent.putExtra("colorcode", arrayList.get(variable).color_code)
                    intent.putExtra("address", "" + fullAddress)
                    intent.putExtra("make_id", "" + arrayList.get(variable).make_id)
                    intent.putExtra("model_id", "" + arrayList.get(variable).model_id)
                    intent.putExtra("service_name", "" + arrayList.get(variable).service_user_name)
                    if (arrayList.get(variable).service_user_mobile != null) {
                        intent.putExtra("service_number", "" + arrayList.get(variable).service_user_mobile)
                    }
                    intent.putExtra("id", "" + arrayList.get(variable).id)
                    val gson = Gson()
                    val serviceList = gson.toJson(arrayList.get(variable))

                    intent.putExtra("serviceList", serviceList)
                    startActivity(intent)

                }, 1000)

            } else if (serviceStatus.equals(completed)) {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    Log.e("checkva", "" + check)
                    var intent = Intent(this, CompletedServiceDetailActivity::class.java)
                    intent.putExtra("title", "Service Detail")
                    intent.putExtra("color", arrayList.get(variable).color)
                    intent.putExtra("makeModel", arrayList.get(variable).make + " " + arrayList.get(variable).model)
                    intent.putExtra("regNumber", arrayList.get(variable).regNumber)
                    intent.putExtra("carImage", arrayList.get(variable).model_image)
                    intent.putExtra("uuid", arrayList.get(variable).uuid)
                    intent.putExtra("address", "" + fullAddress)
                    intent.putExtra("colorcode", arrayList.get(variable).color_code)
                    startActivity(intent)
                }, 1000)
            } else if (serviceStatus.equals(skipped)) {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    Log.e("checkva", "" + check)
                    var intent = Intent(this, SkippedServiceDetailActivity::class.java)
                    intent.putExtra("color", arrayList.get(variable).color)
                    intent.putExtra("makeModel", arrayList.get(variable).make + " " + arrayList.get(variable).model)
                    intent.putExtra("regNumber", arrayList.get(variable).regNumber)
                    intent.putExtra("carImage", arrayList.get(variable).model_image)
                    intent.putExtra("uuid", arrayList.get(variable).uuid)
                    intent.putExtra("address", "" + fullAddress)
                    intent.putExtra("colorcode", arrayList.get(variable).color_code)
                    intent.putExtra("ischange", "false")
                    intent.putExtra("servicelist", "true")
                    intent.putExtra("which", "skip_screen")

//                intent.putExtra("comment_id", arrayList.get(variable).comment_id.get(0))

//                intent.putExtra("formatedDate", arrayList.get(variable).)
                    startActivity(intent)
                }, 1000)
            }
        }
    }

    private fun showBottomSheetdialogNormal(
        array: MutableList<ServiceListByDateData>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        stringBuilder: StringBuilder,
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog =
            this.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btnOk)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        val str = stringBuilder.toString().replace(", ", "" + "\n").replace(",", "" + "\n")
        tv_message?.text = str

        if (str.isNotEmpty()) {
            tv_message.visibility = View.VISIBLE
        }

        ivClose?.setOnClickListener {
            dialog.dismiss()
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
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun refreshToken() {
        loginViewModel?.refreshToken(
            "Basic ZG9vcnN0ZXA6MTIz", "refresh_token", prefManager?.getRefreshToken()
        )

        loginViewModel?.getLoginData()?.observe(this, Observer {
            if (it != null) {
                if (it.success) {
                    if (it.accessToken != null && !it.accessToken.equals("")) {
                        prefManager?.setAccessToken("Bearer " + it.accessToken)
                        prefManager?.setRefreshToken(it.refreshToken)
                        prefManager?.setToken(it.token)
                        prefManager?.setUuid(it.userDetailModel!!.uuid)

//                    firebaseAnalytics?.setUserId(it.userDetailModel?.sap_id!!);

                        prefManager?.setAccessTokenExpireDate(it.accessTokenExpiresAt)
                        if (it.userDetailModel?.owner_name != null) {
                            prefManager?.setOwnerName(it.userDetailModel?.owner_name)
                        }
                    }
                }
            }
        })

    }
}