package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.adapter.HomeListAdpater
import com.walkins.technician.adapter.ServicesListAdpater
import com.walkins.technician.common.onClickAdapter

class ServiceListActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var llSkipped: LinearLayout? = null
    private var llCompleted: LinearLayout? = null
    private var llUpcoming: LinearLayout? = null

    private var tvSkipped: TextView? = null
    private var tvUpcoming: TextView? = null
    private var tvCompleted: TextView? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null

    private var serviceRecycView: RecyclerView? = null


    private var arrayList = arrayListOf("one", "two")
    private var adapter: ServicesListAdpater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        init()
    }

    private fun init() {
        serviceRecycView = findViewById(R.id.serviceRecycView)

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        llSkipped = findViewById(R.id.llSkipped)
        llUpcoming = findViewById(R.id.llUpcoming)
        llCompleted = findViewById(R.id.llCompleted)


        tvCompleted = findViewById(R.id.tvCompleted)
        tvUpcoming = findViewById(R.id.tvUpcoming)
        tvSkipped = findViewById(R.id.tvSkipped)

        llUpcoming?.setOnClickListener(this)
        llCompleted?.setOnClickListener(this)
        llSkipped?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)

        llUpcoming?.performClick()

        serviceRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        adapter = this.let { ServicesListAdpater(arrayList, it, this) }
        serviceRecycView?.adapter = adapter
        adapter?.onclick = this

        tvTitle?.text = "Service List"
    }

    override fun onClick(v: View?) {

        val id = v?.id
        when (id) {
            R.id.llUpcoming -> {

                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.white))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))

            }
            R.id.llCompleted -> {
                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.white))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))

            }
            R.id.llSkipped -> {
                llUpcoming?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))

                tvUpcoming?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.white))

            }
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 1) {
            var intent = Intent(this, AddServiceDetailsActivity::class.java)
            startActivity(intent)
        }

    }
}