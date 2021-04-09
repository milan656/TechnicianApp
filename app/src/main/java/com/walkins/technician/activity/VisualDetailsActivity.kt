package com.walkins.technician.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramotion.fluidslider.FluidSlider
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.onClickAdapter

class VisualDetailsActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var sliderIn: FluidSlider? = null
    private var multiSliderPsiOut: FluidSlider? = null
    private var multiSliderWeight: FluidSlider? = null
    var max = 50
    var min = 0
    var total = max - min

    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null


    private var issueResolvedRecycView: RecyclerView? = null
    private var issueResolveArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var issueResolveAdapter: TyreSuggestionAdpater? = null
    private var relTyrePhotoAdd: RelativeLayout? = null
    private var btnDone: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_details)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        btnDone = findViewById(R.id.btnDone)
        ivBack = findViewById(R.id.ivBack)

        relTyrePhotoAdd = findViewById(R.id.relTyrePhotoAdd)
        issueResolvedRecycView = findViewById(R.id.issueResolvedRecycView)
        issueResolveAdapter = TyreSuggestionAdpater(issueResolveArr, this, this, false)
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

        relTyrePhotoAdd?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Visual Detail"

        btnDone?.setOnClickListener(this)

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

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        val dialog =
            this.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btn_send)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val dialogueRecycView = view.findViewById<RecyclerView>(R.id.dialogueRecycView)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        var arrayAdapter = context?.let { DialogueAdpater(array, it, this) }
        dialogueRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        dialogueRecycView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        dialogueRecycView.adapter = arrayAdapter
        arrayAdapter?.onclick = this

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }
        if (btnBg.equals(Common.btn_filled, ignoreCase = true)) {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_yellow))
            btnSend.setTextColor(context?.resources?.getColor(R.color.white)!!)
            btnSend?.text = "Submit"
        } else {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_white))
            btnSend.setTextColor(context?.resources?.getColor(R.color.header_title)!!)
            btnSend?.text = "Cancel"
        }


        btnSend.setOnClickListener {

            dialog?.dismiss()

        }

        dialog?.show()

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.relTyrePhotoAdd -> {
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnDone -> {
                if (TyreConfigClass.selectedTyreConfigType.equals("LFpending")) {
                    TyreConfigClass.selectedTyreConfigType = "LF"
                }
                if (TyreConfigClass.selectedTyreConfigType.equals("RFpending")) {
                    TyreConfigClass.selectedTyreConfigType = "RF"
                }
                if (TyreConfigClass.selectedTyreConfigType.equals("LRpending")) {
                    TyreConfigClass.selectedTyreConfigType = "LR"
                }
                if (TyreConfigClass.selectedTyreConfigType.equals("RRpending")) {
                    TyreConfigClass.selectedTyreConfigType = "RR"
                }
                setResult(1004)
                finish()
            }


        }
    }
}

