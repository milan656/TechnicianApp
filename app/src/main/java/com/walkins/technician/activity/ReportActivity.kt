package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.NotificationAdpater
import com.walkins.technician.adapter.ReportAdpater
import com.walkins.technician.common.onClickAdapter

class ReportActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var ivBack: ImageView? = null

    private var tvTitle: TextView? = null
    private var tvSkipped: TextView? = null
    private var tvCompleted: TextView? = null

    private var reportRecycView: RecyclerView? = null
    private var llCompleted: LinearLayout? = null
    private var llSkipped: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        init()
    }

    private fun init() {

        llSkipped = findViewById(R.id.llSkipped)
        llCompleted = findViewById(R.id.llCompleted)
        reportRecycView = findViewById(R.id.reportRecycView)
        tvTitle = findViewById(R.id.tvTitle)
        tvSkipped = findViewById(R.id.tvSkipped)
        tvCompleted = findViewById(R.id.tvCompleted)
        ivBack = findViewById(R.id.ivBack)

        ivBack?.setOnClickListener(this)
        llCompleted?.setOnClickListener(this)
        llSkipped?.setOnClickListener(this)

        tvTitle?.text = "Your Report"

        var arrayAdapter = this.let { ReportAdpater(Common.commonPhotoChooseArr, it, this) }
        reportRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        /* reportRecycView?.addItemDecoration(
             DividerItemDecoration(
                 this,
                 DividerItemDecoration.VERTICAL
             )
         )*/
        reportRecycView?.adapter = arrayAdapter
        arrayAdapter.onclick = this
    }

    override fun onPositionClick(variable: Int, check: Int) {

        var intent = Intent(this, CompletedServiceDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.llCompleted -> {
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvCompleted?.setTextColor(this.resources.getColor(R.color.white))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))
            }
            R.id.llSkipped -> {
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))

                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.white))

            }

        }
    }


}