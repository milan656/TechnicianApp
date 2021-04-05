package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.walkins.technician.R

class VehicleMakeApplyTyreActivty : AppCompatActivity(), View.OnClickListener {
    private var ivBack: ImageView? = null
    private var ivEditVehicleMake: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvSelectTyre: TextView? = null
    private var btnNext: Button? = null
    private var isFromVehicleMake = false
    private var isFromVehiclePattern = false
    private var isFromVehicleSize = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_make_apply_tyre_activty)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        tvSelectTyre = findViewById(R.id.tvSelectTyre)
        ivBack = findViewById(R.id.ivBack)
        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)
        btnNext = findViewById(R.id.btnNext)

        ivBack?.setOnClickListener(this)
        ivEditVehicleMake?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)

        if (intent != null) {
            if (intent?.getStringExtra("which") != null) {

                if (intent?.getStringExtra("which").equals("vehiclemake")) {
                    tvTitle?.text = "Select Tyre Make - LF"
                    tvSelectTyre?.text = "Select tyres to apply tyre make"
                    isFromVehicleMake = true
                } else if (intent?.getStringExtra("which").equals("vehiclepattern")) {
                    tvTitle?.text = "Select Tyre Pattern - LF"
                    tvSelectTyre?.text = "Select tyres to apply tyre pattern"
                    isFromVehiclePattern = true
                } else if (intent?.getStringExtra("which").equals("vehiclesize")) {
                    tvTitle?.text = "Select Tyre Size - LF"
                    tvSelectTyre?.text = "Select tyres to apply tyre size"
                    isFromVehicleSize = true
                }
            }
        }

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack, R.id.ivEditVehicleMake -> {
                onBackPressed()
            }
            R.id.btnNext -> {
                if (isFromVehicleMake) {
                    var intent = Intent(this, VehiclePatternActivity::class.java)
                    startActivityForResult(intent, 1002)
                } else if (isFromVehiclePattern) {
                    var intent = Intent(this, VehicleSizeActivity::class.java)
                    startActivityForResult(intent, 1003)
                } else if (isFromVehicleSize) {
                    var intent = Intent(this, VisualDetailsActivity::class.java)
                    startActivityForResult(intent, 1004)
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getresult00", "" + resultCode)
        when (resultCode) {
            1002 -> {
                Log.e("getresult", "" + resultCode)
                setResult(1001)
                finish()
            }
            1003 -> {
                Log.e("getresult0", "" + resultCode)
                setResult(1001)
                finish()
            }
            1004 -> {
                Log.e("getresult1", "" + resultCode)
                setResult(1001)
                finish()
            }
            1005 -> {
                Log.e("getresult1", "" + resultCode)
                setResult(1003)
                finish()
            }
            1006 -> {
                Log.e("getresult2", "" + resultCode)
                setResult(1003)
                finish()
            }
        }
    }
}