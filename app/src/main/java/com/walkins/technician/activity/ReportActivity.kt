package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.adapter.NotificationAdpater
import com.walkins.technician.adapter.ReportAdpater
import com.walkins.technician.common.onClickAdapter

class ReportActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var arrayList = arrayListOf("Gallery", "Camera")
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    private var reportRecycView:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        init()
    }

    private fun init() {

        reportRecycView = findViewById(R.id.reportRecycView)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Your Report"

        var arrayAdapter = this?.let { ReportAdpater(arrayList, it, this) }
        reportRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        reportRecycView?.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        reportRecycView?.adapter = arrayAdapter
        arrayAdapter?.onclick = this
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