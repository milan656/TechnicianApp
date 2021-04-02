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
    private var ivFilterImg: ImageView? = null
    private var tvTitle: TextView? = null

    private var reportRecycView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        init()
    }

    private fun init() {

        reportRecycView = findViewById(R.id.reportRecycView)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivFilterImg = findViewById(R.id.ivFilterImg)
        ivBack?.setOnClickListener(this)
        ivFilterImg?.setOnClickListener(this)
        tvTitle?.text = "Your Report"

        var arrayAdapter = this?.let { ReportAdpater(Common.commonPhotoChooseArr, it, this) }
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
            R.id.ivFilterImg -> {
                openReportFilterDialogue("Choose Filter")
            }
        }
    }

    private fun openReportFilterDialogue(titleStr: String) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialogue_report_filter, null)
        val dialog =
            this.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        tvTitleText?.text = titleStr

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }


        btnConfirm.setOnClickListener {

            dialog?.dismiss()

        }
        btnCancel.setOnClickListener {

            dialog?.dismiss()

        }

        dialog?.show()
    }


}