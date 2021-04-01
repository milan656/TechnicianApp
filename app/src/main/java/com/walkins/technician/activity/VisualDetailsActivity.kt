package com.walkins.technician.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramotion.fluidslider.FluidSlider
import com.walkins.technician.R
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.onClickAdapter

class VisualDetailsActivity : AppCompatActivity(), onClickAdapter {
    private var sliderIn: FluidSlider? = null
    private var multiSliderPsiOut: FluidSlider? = null
    private var multiSliderWeight: FluidSlider? = null
    var max = 50
    var min = 0
    var total = max - min

    private var issueResolvedRecycView: RecyclerView? = null
    private var issueResolveArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var issueResolveAdapter: TyreSuggestionAdpater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_details)

        init()
    }

    private fun init() {
        issueResolvedRecycView = findViewById(R.id.issueResolvedRecycView)
        issueResolveAdapter = TyreSuggestionAdpater(issueResolveArr, this, this)
        issueResolveAdapter?.onclick = this
        issueResolvedRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        issueResolvedRecycView?.adapter = issueResolveAdapter

        psiInSlider()
        psiOutSlider()
        weightSlider()
    }

    fun psiInSlider() {
        sliderIn = findViewById<FluidSlider>(R.id.multiSlider1)
        sliderIn?.positionListener = { pos ->
            sliderIn?.bubbleText = "${min + (total * pos).toInt()}"
            Log.e("getvaluess", "" + pos)
        }
        sliderIn?.position = 0.3f
        sliderIn?.startText = "$min"
        sliderIn?.endText = "$max"
        sliderIn?.animation?.cancel()
    }

    fun psiOutSlider() {
        multiSliderPsiOut = findViewById<FluidSlider>(R.id.multiSliderPsiOut)
        multiSliderPsiOut?.positionListener = { pos ->
            multiSliderPsiOut?.bubbleText = "${min + (total * pos).toInt()}"
            Log.e("getvaluess", "" + pos)
        }
        multiSliderPsiOut?.position = 0.3f
        multiSliderPsiOut?.startText = "$min"
        multiSliderPsiOut?.endText = "$max"
        multiSliderPsiOut?.animation?.cancel()
    }

    fun weightSlider() {
        multiSliderWeight = findViewById<FluidSlider>(R.id.multiSliderWeight)
        multiSliderWeight?.positionListener = { pos ->
            multiSliderWeight?.bubbleText = "${min + (total * pos).toInt()}"
            Log.e("getvaluess", "" + pos)
        }
        multiSliderWeight?.position = 0.3f
        multiSliderWeight?.startText = "$min"
        multiSliderWeight?.endText = "$max"
        multiSliderWeight?.animation?.cancel()
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }
}