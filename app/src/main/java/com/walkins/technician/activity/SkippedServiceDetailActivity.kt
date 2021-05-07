package com.walkins.technician.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.common.*
import com.walkins.technician.viewmodel.CommonViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SkippedServiceDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var tvChange: TextView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var commonViewModel: CommonViewModel? = null
    private lateinit var prefManager: PrefManager
    private var color: String = ""
    private var colorCode: String = ""
    private var makeModel: String = ""
    private var regNumber: String = ""
    private var carImage: String = ""
    private var uuid: String = ""
    private var address: String = ""
    private var ischange: String = ""
    private var formatedDate: String = ""
    private var comment_id: String = ""
    private var reasonId: String = ""

    private var tvCurrentDateTime: TextView? = null

    private var tvRegNumber: TextView? = null
    private var tvMakeModel: TextView? = null
    private var tvReason: TextView? = null
    private var tvcolor: TextView? = null
    private var llbg: LinearLayout? = null
    private var ivCarImage: ImageView? = null
    private var ivInfoAddService: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        prefManager = PrefManager(this)
        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        tvChange = findViewById(R.id.tvChange)
        tvReason = findViewById(R.id.tvReason)
        ivBack = findViewById(R.id.ivBack)
        ivInfoAddService = findViewById(R.id.ivInfoAddService)

        tvcolor = findViewById(R.id.tvcolor)
        tvCurrentDateTime = findViewById(R.id.tvCurrentDateTime)
        tvMakeModel = findViewById(R.id.tvMakeModel)
        llbg = findViewById(R.id.llbg)
        tvRegNumber = findViewById(R.id.tvRegNumber)
        ivCarImage = findViewById(R.id.ivCarImage)

        ivBack?.setOnClickListener(this)
        ivInfoAddService?.setOnClickListener(this)
        tvTitle?.text = "Service Details"

        if (intent != null) {
            if (intent.getStringExtra("color") != null) {
                color = intent.getStringExtra("color")!!
            }
            if (intent.getStringExtra("makeModel") != null) {
                makeModel = intent.getStringExtra("makeModel")!!
            }
            if (intent.getStringExtra("regNumber") != null) {
                regNumber = intent.getStringExtra("regNumber")!!
            }
            if (intent.getStringExtra("carImage") != null) {
                carImage = intent.getStringExtra("carImage")!!
            }
            if (intent.getStringExtra("uuid") != null) {
                uuid = intent.getStringExtra("uuid")!!
            }
            if (intent.getStringExtra("colorcode") != null) {
                colorCode = intent.getStringExtra("colorcode")!!
            }
            if (intent.getStringExtra("reason") != null) {
//                colorCode = intent.getStringExtra("reason")!!
                tvReason?.text = intent.getStringExtra("reason")!!
            }
            if (intent.getStringExtra("reasonId") != null) {
                reasonId = intent.getStringExtra("reasonId")!!
            }
            if (intent.getStringExtra("address") != null) {
//                colorCode = intent.getStringExtra("reason")!!
                address = intent.getStringExtra("address")!!
            }
            if (intent.getStringExtra("ischange") != null) {
                ischange = intent.getStringExtra("ischange")!!
            }
            if (intent.getStringExtra("formatedDate") != null) {
                formatedDate = intent.getStringExtra("formatedDate")!!
            }
            if (intent.getStringExtra("comment_id") != null) {
                comment_id = intent.getStringExtra("comment_id")!!
            }
        }

        Log.e("getregno", "" + regNumber)
        tvcolor?.text = color
        tvMakeModel?.text = makeModel
        tvRegNumber?.text = regNumber

        llbg?.setBackgroundColor(Color.parseColor(colorCode))

        try {
            Glide.with(this)
                .load(carImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_no_car_image)
                .into(ivCarImage!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tvChange?.setOnClickListener(this)

        if (ischange.equals("false")) {
            tvChange?.visibility = View.GONE
            getCommentList()
        }

        if (formatedDate != null && !formatedDate.equals("")) {
            Log.e("getdated0", "" + formatedDate)
            Log.e("getdated0", "" + Common.datefrom(formatedDate))
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val output = SimpleDateFormat("HH:mm a, dd MMMM yyyy")

            var d: Date? = null
            try {
                d = input.parse(formatedDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val formatted: String = output.format(d)
            Log.e("DATE", "" + formatted)
            try {
                tvCurrentDateTime?.text = formatted
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            tvCurrentDateTime?.text = Common.getCurrentDateTime()
        }


    }

    private fun getCommentList() {
        Common.showLoader(this)
        commonViewModel?.callApiGetComments(prefManager.getAccessToken()!!, this)
        commonViewModel?.getCommentList()?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success) {

                    if (it.data != null && it.data.size > 0) {
                        for (i in it.data.indices) {
                            if (!comment_id.equals("")) {
                                Log.e("getcomment", "" + comment_id + " " + it.data.get(i).id)
                                if (comment_id.toInt() == it.data.get(i).id) {
                                    tvReason?.text = it.data.get(i).name
                                }
                            }
                        }
                    }
                    Common.hideLoader()

                } else {
                    Common.hideLoader()
                    if (it.error != null && it.error.get(0).message != null) {
                        showShortToast(it.error.get(0).message, this)
                    }
                }
            } else {
                Common.hideLoader()
            }
        })
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                var intent = Intent()
                intent.putExtra("back", "" + true)
                setResult(RESULT_OK, intent)

                onBackPressed()
            }
            R.id.tvChange -> {
                var intent = Intent()
                intent.putExtra("reason", "" + tvReason?.text?.toString())
                intent.putExtra("reasonId", "" + reasonId)
                setResult(RESULT_OK, intent)
                finish()
            }
            R.id.ivInfoAddService -> {
                showBottomSheetdialogNormal(
                    Common.commonPhotoChooseArr,
                    "Address Details",
                    this,
                    Common.btn_filled,
                    false, Common.getStringBuilder(address)
                )

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
            this?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

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