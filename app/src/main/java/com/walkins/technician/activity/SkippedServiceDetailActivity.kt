package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.walkins.technician.R

class SkippedServiceDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var tvChange: TextView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Service Details"
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