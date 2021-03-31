package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
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
    private var llTyreConfigExpanded: LinearLayout? = null

    private var serviceExpanded = false
    private var tyreConfigExpanded = false
    private var tyreConfig = false
    private var technicalSuggestion = false
    private var llUpdatedPlacement: LinearLayout? = null

    private var chkNitrogenTopup: CheckBox? = null
    private var chkNitrogenRefill: CheckBox? = null
    private var chkWheelBalacing: CheckBox? = null
    private var chkTyreRotation: CheckBox? = null

    private var suggestionsRecycView:RecyclerView?=null

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
        llTyreConfigExpanded = findViewById(R.id.llTyreConfigExpanded)
        llUpdatedPlacement = findViewById(R.id.llUpdatedPlacement)

        chkNitrogenRefill = findViewById(R.id.chkNitrogenRefill)
        chkNitrogenTopup = findViewById(R.id.chkNitrogenTopup)
        chkTyreRotation = findViewById(R.id.chkTyreRotation)
        chkWheelBalacing = findViewById(R.id.chkWheelBalacing)

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
        ivAddTyreConfig?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!tyreConfigExpanded) {
                    ivAddTyreConfig?.setImageResource(R.drawable.ic_minus_icon)
                    Common.expand(llTyreConfigExpanded!!)
                    tyreConfigExpanded = true

                } else {
                    ivAddTyreConfig?.setImageResource(R.drawable.ic_add_icon)
                    Common.collapse(llTyreConfigExpanded!!)
                    tyreConfigExpanded = false

                }

                return false
            }

        })

        ivBack?.setOnClickListener(this)
        llServiceExpanded?.setOnClickListener(this)

        checkChangeListener()
    }

    private fun checkChangeListener() {
        chkWheelBalacing?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                showHideUpdatedPlacement()

            }

        })
        chkTyreRotation?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                showHideUpdatedPlacement()
            }

        })
        chkNitrogenTopup?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                showHideUpdatedPlacement()
            }

        })
        chkNitrogenRefill?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                showHideUpdatedPlacement()
            }

        })
    }

    fun showHideUpdatedPlacement() {
        if (chkWheelBalacing?.isChecked!! && chkNitrogenRefill?.isChecked!! &&
            chkNitrogenTopup?.isChecked!! && chkTyreRotation?.isChecked!!
        ) {
            Common.expand(llUpdatedPlacement!!)
        } else {
            if (llUpdatedPlacement?.visibility == View.VISIBLE) {
                Common.collapse(llUpdatedPlacement!!)
            }
        }
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