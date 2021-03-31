package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter

class ReportActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var arrayList = arrayListOf("Gallery", "Camera")
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        init()
    }

    private fun init() {

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Your Report"

    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }


}