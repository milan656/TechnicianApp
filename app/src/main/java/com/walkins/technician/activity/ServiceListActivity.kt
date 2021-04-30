package com.walkins.technician.activity

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
import com.walkins.technician.R
import com.walkins.technician.adapter.HomeListAdpater
import com.walkins.technician.adapter.ServicesListAdpater
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.showShortToast
import com.walkins.technician.model.login.servicelistmodel.ServiceListByDateData
import com.walkins.technician.viewmodel.ServiceViewModel
import java.lang.StringBuilder

class ServiceListActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var serviceViewModel: ServiceViewModel? = null
    private var prefManager: PrefManager? = null
    private var llSkipped: LinearLayout? = null
    private var llCompleted: LinearLayout? = null
    private var llUpcoming: LinearLayout? = null

    private var tvSkipped: TextView? = null
    private var tvUpcoming: TextView? = null
    private var tvCompleted: TextView? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null

    private var serviceRecycView: RecyclerView? = null
    private var ivInfoService: ImageView? = null

    //    private var arrayList: muta<ServiceListByDateData> = ArrayList()
    private val arrayList = mutableListOf<ServiceListByDateData>()
    private var adapter: ServicesListAdpater? = null
    private var tvAddress: TextView? = null
    private var tvDate: TextView? = null
    private var tvNoServiceData: TextView? = null
    private var serviceStatus = ""
    private var selectedDate = ""
    private var selectedDateFormated = ""
    private var addressTitle = ""
    private var fullAddress = ""

    companion object {
        var upcomming = "pending"
        var completed = "completed"
        var skipped = "skipped"
    }

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

        llUpcoming?.performClick()

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
        }
        tvDate?.text = selectedDateFormated
        tvAddress?.text = addressTitle

        getServiceListByDate()
    }

    private fun getServiceListByDate() {

        Common.showLoader(this)
        serviceViewModel?.callApiServiceByDate(selectedDate, prefManager?.getAccessToken()!!, this)
        serviceViewModel?.getServiceByDate()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {

                    if (it.data != null) {

                        arrayList.clear()
                        arrayList.addAll(it.data)

                        if (serviceStatus.equals(upcomming)) {
                            arrayList.filter { it.status.equals(upcomming) }
                            Log.e("getservicedata", "" + arrayList.size)
                        } else if (serviceStatus.equals(completed)) {
                            arrayList.filter { it.status.equals(completed) }
                            Log.e("getservicedata0", "" + arrayList.size)
                        } else if (serviceStatus.equals(skipped)) {
                            arrayList.filter { it.status.equals(skipped) }
                            Log.e("getservicedata1", "" + arrayList.size)
                        }
                        tvNoServiceData?.visibility = View.GONE
                        if (arrayList.size == 0) {
                            tvNoServiceData?.text = "There is no any Upcomming service to display"
                            tvNoServiceData?.visibility = View.VISIBLE
                        }

                        adapter?.notifyDataSetChanged()

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
                adapter = this.let { ServicesListAdpater(arrayList.filter { it.status.equals(upcomming) } as MutableList<ServiceListByDateData>, it, this) }
                if (arrayList.filter { it.status.equals(upcomming) }.size == 0) {
                    tvNoServiceData?.text = "There is no any Upcomming service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                }

                serviceRecycView?.adapter = adapter

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
                adapter = this.let { ServicesListAdpater(arrayList.filter { it.status.equals(completed) } as MutableList<ServiceListByDateData>, it, this) }
                if (arrayList.filter { it.status.equals(completed) }.size == 0) {
                    tvNoServiceData?.text = "There is no any Completed service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                }

                serviceRecycView?.adapter = adapter


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
                adapter = this.let { ServicesListAdpater(arrayList.filter { it.status.equals(skipped) } as MutableList<ServiceListByDateData>, it, this) }

                if (arrayList.filter { it.status.equals(skipped) }.size == 0) {
                    tvNoServiceData?.text = "There is no any Skipped service to display"
                    tvNoServiceData?.visibility = View.VISIBLE
                }
                serviceRecycView?.adapter = adapter

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

            if (serviceStatus.equals(upcomming)) {
                var intent = Intent(this, AddServiceDetailsActivity::class.java)
                startActivity(intent)

            } else if (serviceStatus.equals(completed)) {
                var intent = Intent(this, CompletedServiceDetailActivity::class.java)
                intent.putExtra("title", "Service Detail")
                startActivity(intent)
            } else if (serviceStatus.equals(skipped)) {
                var intent = Intent(this, SkippedServiceDetailActivity::class.java)
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