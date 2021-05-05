package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.walkins.technician.R
import com.walkins.technician.adapter.PendingTyreSuggestionAdpater
import com.walkins.technician.common.TyreDetailCommonClass
import com.walkins.technician.common.TyreKey
import com.walkins.technician.common.onClickAdapter
import java.lang.Exception

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
    private var ivVisualDetailPhoto: ImageView? = null
    private var ivSelectedMake: ImageView? = null

    private var ivSideWell: ImageView? = null
    private var ivTreadDepth: ImageView? = null
    private var ivShoulder: ImageView? = null
    private var ivTreadWear: ImageView? = null
    private var ivRimDamage: ImageView? = null
    private var ivBulgeBubble: ImageView? = null

    private var tvSelectedPattern: TextView? = null
    private var tvSelectedSize: TextView? = null
    private var tvManufacturingDate: TextView? = null
    private var tvPsiIn: TextView? = null
    private var tvPsiOut: TextView? = null
    private var tvWeight: TextView? = null
    private var tvBubble: TextView? = null
    private var tvsideWell: TextView? = null
    private var tvShoulder: TextView? = null
    private var tvTreadDepth: TextView? = null
    private var tvTreadWear: TextView? = null
    private var tvRimDamage: TextView? = null
    private var title: String? = null

    companion object {
        var okCondition_message = "Condition is acceptable"
        var sugCondition_message = "Possible performance degredation"
        var reqCondition_message = "Needs immediate attention"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_visual_detail)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivBulgeBubble = findViewById(R.id.ivBulgeBubble)
        ivTreadDepth = findViewById(R.id.ivTreadDepth)
        ivTreadWear = findViewById(R.id.ivTreadWear)
        ivSideWell = findViewById(R.id.ivSideWell)
        ivShoulder = findViewById(R.id.ivShoulder)
        ivRimDamage = findViewById(R.id.ivRimDamage)

        tvRimDamage = findViewById(R.id.tvRimDamage)
        tvBubble = findViewById(R.id.tvBubble)
        tvsideWell = findViewById(R.id.tvsideWell)
        tvShoulder = findViewById(R.id.tvShoulder)
        tvTreadDepth = findViewById(R.id.tvTreadDepth)
        tvTreadWear = findViewById(R.id.tvTreadWear)

        ivVisualDetailPhoto = findViewById(R.id.ivVisualDetailPhoto)

        issueResolvedRecycView = findViewById(R.id.issueResolvedRecycView)
        tyreSuggestionAdapter = PendingTyreSuggestionAdpater(suggestionArr, this, this)
        tyreSuggestionAdapter?.onclick = this
        issueResolvedRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        issueResolvedRecycView?.adapter = tyreSuggestionAdapter

        tvManufacturingDate = findViewById(R.id.tvManufacturingDate)
        tvPsiIn = findViewById(R.id.tvPsiIn)
        tvPsiOut = findViewById(R.id.tvPsiOut)
        tvWeight = findViewById(R.id.tvWeight)
        tvSelectedPattern = findViewById(R.id.tvSelectedPattern)
        tvSelectedSize = findViewById(R.id.tvSelectedSize)
        ivSelectedMake = findViewById(R.id.ivSelectedMake)

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
        }
        ivBack?.setOnClickListener(this)

        tvManufacturingDate?.isClickable = false
        tvManufacturingDate?.isEnabled = false

        tvManufacturingDate?.text = TyreDetailCommonClass.manufaturingDate
        tvSelectedSize?.text = TyreDetailCommonClass.vehicleSize
        tvSelectedPattern?.text = TyreDetailCommonClass.vehiclePattern

        try {
            Glide.with(this@CompletedVisualDetailActivity)
                .load(TyreDetailCommonClass.vehicleMakeURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(ivSelectedMake!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.e("geturl", "" + TyreDetailCommonClass.visualDetailPhotoUrl)
        Log.e("geturl0", "" + TyreDetailCommonClass.vehicleMakeURL)
        try {
            Glide.with(this@CompletedVisualDetailActivity)
                .load(TyreDetailCommonClass.visualDetailPhotoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(ivVisualDetailPhoto!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        suggestionArr.clear()

        if (TyreDetailCommonClass.issueResolvedArr?.size!!>0) {
            for (i in TyreDetailCommonClass.issueResolvedArr?.indices!!) {
                suggestionArr.add(TyreDetailCommonClass.issueResolvedArr?.get(i)!!)
            }
        }

        tyreSuggestionAdapter?.notifyDataSetChanged()

        if (TyreDetailCommonClass.sidewell.equals("OK", ignoreCase = true)) {
            ivSideWell?.setImageResource(R.mipmap.ic_condition_ok)
            tvsideWell?.text = TyreDetailCommonClass.sidewell + " - " + okCondition_message
        } else if (TyreDetailCommonClass.sidewell.equals("SUG")) {
            ivSideWell?.setImageResource(R.mipmap.ic_condition_degrade)
            tvsideWell?.text = TyreDetailCommonClass.sidewell + " - " + sugCondition_message
        } else if (TyreDetailCommonClass.sidewell.equals("REQ")) {
            ivSideWell?.setImageResource(R.mipmap.ic_condition_down)
            tvsideWell?.text = TyreDetailCommonClass.sidewell + " - " + reqCondition_message
        }


        if (TyreDetailCommonClass.shoulder.equals("OK", ignoreCase = true)) {
            ivShoulder?.setImageResource(R.mipmap.ic_condition_ok)
            tvShoulder?.text = TyreDetailCommonClass.shoulder + " - " + okCondition_message
        } else if (TyreDetailCommonClass.shoulder.equals("SUG")) {
            ivShoulder?.setImageResource(R.mipmap.ic_condition_degrade)
            tvShoulder?.text = TyreDetailCommonClass.shoulder + " - " + sugCondition_message
        } else if (TyreDetailCommonClass.shoulder.equals("REQ")) {
            ivShoulder?.setImageResource(R.mipmap.ic_condition_down)
            tvShoulder?.text = TyreDetailCommonClass.shoulder + " - " + reqCondition_message
        }


        if (TyreDetailCommonClass.treadDepth.equals("OK", ignoreCase = true)) {
            ivTreadDepth?.setImageResource(R.mipmap.ic_condition_ok)
            tvTreadDepth?.text = TyreDetailCommonClass.treadDepth + " - " + okCondition_message
        } else if (TyreDetailCommonClass.treadDepth.equals("SUG")) {
            ivTreadDepth?.setImageResource(R.mipmap.ic_condition_degrade)
            tvTreadDepth?.text = TyreDetailCommonClass.treadDepth + " - " + sugCondition_message
        } else if (TyreDetailCommonClass.treadDepth.equals("REQ")) {
            ivTreadDepth?.setImageResource(R.mipmap.ic_condition_down)
            tvTreadDepth?.text = TyreDetailCommonClass.treadDepth + " - " + reqCondition_message
        }


        if (TyreDetailCommonClass.treadWear.equals("OK", ignoreCase = true)) {
            ivTreadWear?.setImageResource(R.mipmap.ic_condition_ok)
            tvTreadWear?.text = TyreDetailCommonClass.treadWear + " - " + okCondition_message
        } else if (TyreDetailCommonClass.treadWear.equals("SUG")) {
            ivTreadWear?.setImageResource(R.mipmap.ic_condition_degrade)
            tvTreadWear?.text = TyreDetailCommonClass.treadWear + " - " + sugCondition_message
        } else if (TyreDetailCommonClass.treadWear.equals("REQ")) {
            ivTreadWear?.setImageResource(R.mipmap.ic_condition_down)
            tvTreadWear?.text = TyreDetailCommonClass.treadWear + " - " + reqCondition_message
        }


        if (TyreDetailCommonClass.rimDamage.equals("OK", ignoreCase = true)) {
            ivRimDamage?.setImageResource(R.mipmap.ic_condition_ok)
            tvRimDamage?.text = TyreDetailCommonClass.rimDamage + " - " + okCondition_message
        } else if (TyreDetailCommonClass.rimDamage.equals("SUG")) {
            ivRimDamage?.setImageResource(R.mipmap.ic_condition_degrade)
            tvRimDamage?.text = TyreDetailCommonClass.rimDamage + " - " + sugCondition_message
        } else if (TyreDetailCommonClass.rimDamage.equals("REQ")) {
            ivRimDamage?.setImageResource(R.mipmap.ic_condition_down)
            tvRimDamage?.text = TyreDetailCommonClass.rimDamage + " - " + reqCondition_message
        }


        if (TyreDetailCommonClass.bubble.equals("OK", ignoreCase = true)) {
            ivBulgeBubble?.setImageResource(R.mipmap.ic_condition_ok)
            tvBubble?.text = TyreDetailCommonClass.bubble + " - " + okCondition_message
        } else if (TyreDetailCommonClass.bubble.equals("SUG")) {
            ivBulgeBubble?.setImageResource(R.mipmap.ic_condition_degrade)
            tvBubble?.text = TyreDetailCommonClass.bubble + " - " + sugCondition_message
        } else if (TyreDetailCommonClass.bubble.equals("REQ")) {
            ivBulgeBubble?.setImageResource(R.mipmap.ic_condition_down)
            tvBubble?.text = TyreDetailCommonClass.bubble + " - " + reqCondition_message
        }

        tvPsiOut?.text = TyreDetailCommonClass.psiOutTyreService
        tvPsiIn?.text = TyreDetailCommonClass.psiInTyreService
        tvWeight?.text = TyreDetailCommonClass.weightTyreService

        Log.e("getweight", "" + TyreDetailCommonClass.weightTyreService)
        Log.e("getweight1", "" + TyreDetailCommonClass.psiOutTyreService)
        Log.e("getweight0", "" + TyreDetailCommonClass.psiInTyreService)


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