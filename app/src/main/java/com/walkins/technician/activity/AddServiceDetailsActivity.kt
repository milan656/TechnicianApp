package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.technician.common.Common
import com.walkins.technician.R

class AddServiceDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private var ivInfoAddService: ImageView? = null
    private var ivAddServices: ImageView? = null
    private var ivAddTyreConfig: ImageView? = null
    private var ivAddTechnicalSuggestion: ImageView? = null

    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var llServiceExpanded: LinearLayout? = null

    private var serviceExpanded = false
    private var tyreConfig = false
    private var technicalSuggestion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_details)

        init()
    }

    private fun init() {

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivInfoAddService = findViewById(R.id.ivInfoAddService)
        ivAddServices = findViewById(R.id.ivAddServices)
        ivAddTechnicalSuggestion = findViewById(R.id.ivAddTechnicalSuggestion)
        ivAddTyreConfig = findViewById(R.id.ivAddTyreConfig)
        llServiceExpanded = findViewById(R.id.llServiceExpanded)

        tvTitle?.text = "Add Service Details"
        ivInfoAddService?.setOnClickListener(this)
        ivAddTechnicalSuggestion?.setOnClickListener(this)
        ivAddTyreConfig?.setOnClickListener(this)

        ivAddServices?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!serviceExpanded) {
                    ivAddServices?.setImageResource(R.drawable.ic_minus_icon)
                    Common.expand(llServiceExpanded!!)
                    serviceExpanded = true

                } else {
                    ivAddServices?.setImageResource(R.drawable.ic_add_icon)
                    Common.collapse(llServiceExpanded!!)
                    serviceExpanded = false

                }

                return false
            }

        })

        ivBack?.setOnClickListener(this)
        llServiceExpanded?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        val id = v?.id
        when (id) {
            R.id.ivAddTyreConfig -> {
                ivAddTyreConfig?.setImageResource(R.drawable.ic_minus_icon)
            }
            R.id.ivAddTechnicalSuggestion -> {
                ivAddTyreConfig?.setImageResource(R.drawable.ic_minus_icon)
            }
            R.id.ivAddServices -> {


            }
            R.id.ivInfoAddService -> {

            }
            R.id.ivBack -> {
                onBackPressed()
            }

        }
    }
}