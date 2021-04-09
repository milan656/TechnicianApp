package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.adapter.PendingTyreSuggestionAdpater
import com.walkins.technician.common.onClickAdapter

class CompletedVisualDetailActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var issueResolvedRecycView: RecyclerView? = null
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var tyreSuggestionAdapter: PendingTyreSuggestionAdpater? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_visual_detail)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)

        issueResolvedRecycView = findViewById(R.id.issueResolvedRecycView)
        tyreSuggestionAdapter = PendingTyreSuggestionAdpater(suggestionArr, this, this)
        tyreSuggestionAdapter?.onclick = this
        issueResolvedRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        issueResolvedRecycView?.adapter = tyreSuggestionAdapter

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
        }
        ivBack?.setOnClickListener(this)

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