package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.technician.common.Common
import com.walkins.technician.R

class SkippedServiceDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var tvChange: TextView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    private var color: String = ""
    private var makeModel: String = ""
    private var regNumber: String = ""
    private var carImage: String = ""
    private var uuid: String = ""

    private var tvCurrentDateTime: TextView? = null

    private var tvRegNumber: TextView? = null
    private var tvMakeModel: TextView? = null
    private var tvcolor: TextView? = null
    private var llbg: LinearLayout? = null
    private var ivCarImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)

        tvcolor = findViewById(R.id.tvcolor)
        tvCurrentDateTime = findViewById(R.id.tvCurrentDateTime)
        tvMakeModel = findViewById(R.id.tvMakeModel)
        llbg = findViewById(R.id.llbg)
        tvRegNumber = findViewById(R.id.tvRegNumber)
        ivCarImage = findViewById(R.id.ivCarImage)

        ivBack?.setOnClickListener(this)
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
        }

        tvcolor?.text = color
        tvMakeModel?.text = makeModel
        tvRegNumber?.text = regNumber

        if (color.equals("white", ignoreCase = true)) {
            llbg?.setBackgroundColor(this.resources?.getColor(R.color.white)!!)
        } else if (color.equals("blue", ignoreCase = true)) {
            llbg?.setBackgroundColor(this.resources?.getColor(R.color.blue_color)!!)
        } else if (color.equals("red", ignoreCase = true)) {
            llbg?.setBackgroundColor(this.resources?.getColor(R.color.red_color)!!)
        }

        try {
//            Glide.with(this).load(carImage).into(ivCarImage!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        tvCurrentDateTime?.text = Common.getCurrentDateTime()
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                setResult(0)
                onBackPressed()
            }

        }
    }
}