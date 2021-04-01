package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.NotificationAdpater
import com.walkins.technician.common.onClickAdapter

class NotificationActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var notificationRecycView: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        init()
    }

    private fun init() {
        notificationRecycView = findViewById(R.id.notificationRecycView)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        notificationRecycView = findViewById(R.id.notificationRecycView)
        var arrayAdapter = this?.let { NotificationAdpater(Common.commonPhotoChooseArr, it, this) }
        notificationRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        notificationRecycView?.adapter = arrayAdapter
        arrayAdapter?.onclick = this

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Notification"
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