package com.walkins.aapkedoorstep.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.HomeListAdpater
import com.walkins.aapkedoorstep.adapter.ServicesListAdpater
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.common.showShortToast
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateData
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.aapkedoorstep.viewmodel.ServiceViewModel
import java.lang.StringBuilder

class ServiceListActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

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

    private var selectedDate = ""
    private var selectedDateFormated = ""
    private var addressTitle = ""
    private var fullAddress = ""
    private var building_uuid = ""

    companion object {
        var upcomming = "open"
        var completed = "completed"
        var skipped = "skipped"
    }

    private var serviceStatus = upcomming

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        prefManager = PrefManager(this)
        init()
    }

    private fun init() {
        serviceRecycView = findViewById(R.id.serviceRecycView)

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

        llUpcoming?.setOnClickListener(this)
        llCompleted?.setOnClickListener(this)
        llSkipped?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        ivInfoService?.setOnClickListener(this)

        tvAddress?.text = "Titanium City Centre,\nAnand Nagar"
        serviceRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        adapter = this.let { ServicesListAdpater(arrayList, it, this) }
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
        tvDate?.text = selectedDateFormated
        tvAddress?.text = addressTitle

    }

    override fun onResume() {
        super.onResume()
        getServiceListByDate()
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

                        if (serviceStatus.equals(upcomming)) {
//                            arrayList.filter { it.status.equals(upcomming) }
                            arrayList = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>
                            adapter = this.let { ServicesListAdpater(arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>, it, this) }

                            tvUpcoming?.text = "Upcomming - ${arrayList.size}"
                            Log.e("getservicedata", "" + arrayList.size)
                        } else if (serviceStatus.equals(completed)) {
//                            arrayList.filter { it.status.equals(completed) }
                            Log.e("getservicedata0", "" + arrayList.size)
                            arrayList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>
                            adapter = this.let { ServicesListAdpater(arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>, it, this) }
                            tvCompleted?.text = "Completed - ${arrayList.size}"
                        } else if (serviceStatus.equals(skipped)) {
//                            arrayList.filter { it.status.equals(skipped) }
                            Log.e("getservicedata1", "" + arrayList.size)
                            arrayList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>
                            adapter = this.let { ServicesListAdpater(arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>, it, this) }
                            tvSkipped?.text = "Skipped - ${arrayList.size}"
                        }



                        tvNoServiceData?.visibility = View.GONE
                        if (arrayList.size == 0) {
                            tvNoServiceData?.text = "There is no any Upcomming service to display"
                            tvNoServiceData?.visibility = View.VISIBLE
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
                arrayList.addAll(serviceListDataModel?.data!!)

                arrayList = arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>
                adapter = ServicesListAdpater(arrayList, this, this)
                if (arrayList.size == 0) {
                    tvNoServiceData?.text = "There is no any Upcomming service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                }
                tvUpcoming?.text = "Upcomming - ${arrayList.size}"
                serviceRecycView?.adapter = adapter
                adapter?.onclick = this

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
                arrayList.addAll(serviceListDataModel?.data!!)

                arrayList = arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>
                adapter = ServicesListAdpater(arrayList, this, this)

                if (arrayList.size == 0) {
                    tvNoServiceData?.text = "There is no any Completed service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                }
                tvCompleted?.text = "Completed - ${arrayList.size}"
                serviceRecycView?.adapter = adapter
                adapter?.onclick = this


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
                arrayList.addAll(serviceListDataModel?.data!!)

                arrayList = arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>
                adapter = ServicesListAdpater(arrayList, this, this)

                if (arrayList.size == 0) {
                    tvNoServiceData?.text = "There is no any Skipped service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                }
                tvSkipped?.text = "Skipped - ${arrayList.size}"
                serviceRecycView?.adapter = adapter
                adapter?.onclick = this

            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivInfoService -> {
                showBottomSheetdialogNormal(
                    arrayList,
                    "Address Details",
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
                var intent = Intent(this, AddServiceDetailsActivity::class.java)
                intent.putExtra("color", arrayList.get(variable).color)
                intent.putExtra("makeModel", arrayList.get(variable).make + " " + arrayList.get(variable).model)
                intent.putExtra("regNumber", arrayList.get(variable).regNumber)
                intent.putExtra("carImage", arrayList.get(variable).model_image)
                intent.putExtra("uuid", arrayList.get(variable).uuid)
                intent.putExtra("colorcode", arrayList.get(variable).color_code)
                intent.putExtra("address", "" + fullAddress)
                intent.putExtra("make_id", "" + arrayList.get(variable).make_id)
                intent.putExtra("model_id", "" + arrayList.get(variable).model_id)

                startActivity(intent)

            } else if (serviceStatus.equals(completed)) {
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
            } else if (serviceStatus.equals(skipped)) {
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

//                intent.putExtra("comment_id", arrayList.get(variable).comment_id.get(0))

//                intent.putExtra("formatedDate", arrayList.get(variable).)
                startActivity(intent)
            }
        }
    }

    private fun showBottomSheetdialogNormal(
        array: MutableList<ServiceListByDateData>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        stringBuilder: StringBuilder
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
        val str = stringBuilder.toString().replace(",", "," + "\n")
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
            dialog?.dismiss()
        }
        dialog?.show()
    }

}