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
    private var tvSelectedPattern: TextView? = null
    private var tvSelectedSize: TextView? = null
    private var tvManufacturingDate: TextView? = null
    private var tvPsiIn: TextView? = null
    private var tvPsiOut: TextView? = null
    private var tvWeight: TextView? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_visual_detail)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
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
        try {
            Glide.with(this@CompletedVisualDetailActivity)
                .load(TyreDetailCommonClass.visualDetailPhotoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(ivVisualDetailPhoto!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.e("getvaluess_all", TyreDetailCommonClass.tyreType!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMake!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMakeId!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMakeURL!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehiclePattern!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehiclePatternId!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleSize!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleSizeId!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.manufaturingDate!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.psiInTyreService!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.psiOutTyreService!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.weightTyreService!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.sidewell!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.shoulder!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.treadDepth!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.treadWear!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.rimDamage!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.bubble!!)
        Log.e("getvaluess_all", "" + TyreDetailCommonClass.issueResolvedArr!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.visualDetailPhotoUrl!!)

        suggestionArr.clear()

        for (i in TyreDetailCommonClass.issueResolvedArr?.indices!!) {
            suggestionArr.add(TyreDetailCommonClass.issueResolvedArr?.get(i)!!)
        }

        tyreSuggestionAdapter?.notifyDataSetChanged()
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