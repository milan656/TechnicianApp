package com.walkins.technician.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.adapter.HomeListAdpater
import com.walkins.technician.adapter.ServicesListAdpater
import com.walkins.technician.common.onClickAdapter

class ServiceListActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

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


    private var arrayList = arrayListOf("one", "two", "three")
    private var adapter: ServicesListAdpater? = null
    private var tvAddress: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

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

            }
            R.id.llCompleted -> {
                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.white))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))

            }
            R.id.llSkipped -> {
                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.white))

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
                    "Block No-2, Palm Spring,",
                    "Vastrapur Road,",
                    "Opposite Siddhivinayak mandir,",
                    "Ahmedabad - 123456"
                )
            }
        }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 1) {
            var intent = Intent(this, AddServiceDetailsActivity::class.java)
            startActivity(intent)
        }

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

}