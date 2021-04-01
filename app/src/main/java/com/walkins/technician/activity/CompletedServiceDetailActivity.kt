package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.walkins.technician.R
import com.walkins.technician.adapter.PendingTyreSuggestionAdpater
import com.walkins.technician.common.onClickAdapter

class CompletedServiceDetailActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var pendingSuggestionsRecycView: RecyclerView? = null
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var tyreSuggestionAdapter: PendingTyreSuggestionAdpater? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var tvCurrentDateTime: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_service_detail)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)

        tvCurrentDateTime = findViewById(R.id.tvCurrentDateTime)
        pendingSuggestionsRecycView = findViewById(R.id.pendingSuggestionsRecycView)
        tyreSuggestionAdapter = PendingTyreSuggestionAdpater(suggestionArr, this, this)
        tyreSuggestionAdapter?.onclick = this
        pendingSuggestionsRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        pendingSuggestionsRecycView?.adapter = tyreSuggestionAdapter

        tvTitle?.text = "Add Service Details"
        ivBack?.setOnClickListener(this)

        tvCurrentDateTime?.text = Common.getCurrentDateTime()
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