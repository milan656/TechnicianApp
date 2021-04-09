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
    private var ivOkSideWell: ImageView? = null
    private var ivSugSideWell: ImageView? = null
    private var ivReqSideWell: ImageView? = null


    private var ivOkShoulder: ImageView? = null
    private var ivSugShoulder: ImageView? = null
    private var ivReqShoulder: ImageView? = null

    private var ivOkTreadDepth: ImageView? = null
    private var ivSugTreadDepth: ImageView? = null
    private var ivReqTreadDepth: ImageView? = null

    private var ivOkTreadWear: ImageView? = null
    private var ivSugTreadWear: ImageView? = null
    private var ivReqTreadWear: ImageView? = null

    private var ivOkRimDamage: ImageView? = null
    private var ivSugRimDamage: ImageView? = null
    private var ivReqRimDamage: ImageView? = null

    private var ivOkbubble: ImageView? = null
    private var ivSugbubble: ImageView? = null
    private var ivReqbubble: ImageView? = null

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
    private var weightFrame: FrameLayout? = null
    private var psiInFrame: FrameLayout? = null
    private var psiOutFrame: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_details)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        btnDone = findViewById(R.id.btnDone)
        ivBack = findViewById(R.id.ivBack)

        weightFrame = findViewById(R.id.weightFrame)
        psiOutFrame = findViewById(R.id.psiOutFrame)
        psiInFrame = findViewById(R.id.psiInFrame)

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

        if (!TyreConfigClass.nitrogenRefillChecked) {
            weightFrame?.visibility = View.GONE
        } else {
            weightFrame?.visibility = View.VISIBLE
        }

        if (!TyreConfigClass.nitrogenRefillChecked && !TyreConfigClass.nitrogenTopupChecked &&
            !TyreConfigClass.nitrogenTyreRotationChecked && TyreConfigClass.nitrogenWheelBalancingChecked
        ) {
            weightFrame?.visibility = View.VISIBLE
            psiOutFrame?.visibility = View.GONE
            psiInFrame?.visibility = View.GONE
        }

        ivOkSideWell = findViewById(R.id.ivOkSideWell)
        ivSugSideWell = findViewById(R.id.ivSugSideWell)
        ivReqSideWell = findViewById(R.id.ivReqSideWell)

        ivOkShoulder = findViewById(R.id.ivOkShoulder)
        ivSugShoulder = findViewById(R.id.ivSugShoulder)
        ivReqShoulder = findViewById(R.id.ivReqShoulder)

        ivOkTreadDepth = findViewById(R.id.ivOkTreadDepth)
        ivSugTreadDepth = findViewById(R.id.ivSugTreadDepth)
        ivReqTreadDepth = findViewById(R.id.ivReqTreadDepth)

        ivOkTreadWear = findViewById(R.id.ivOkTreadWear)
        ivSugTreadWear = findViewById(R.id.ivSugTreadWear)
        ivReqTreadWear = findViewById(R.id.ivReqTreadWear)

        ivOkRimDamage = findViewById(R.id.ivOkRimDamage)
        ivSugRimDamage = findViewById(R.id.ivSugRimDamage)
        ivReqRimDamage = findViewById(R.id.ivReqRimDamage)

        ivOkbubble = findViewById(R.id.ivOkbubble)
        ivSugbubble = findViewById(R.id.ivSugbubble)
        ivReqbubble = findViewById(R.id.ivReqbubble)

        ivOkSideWell?.setOnClickListener(this)
        ivSugSideWell?.setOnClickListener(this)
        ivReqSideWell?.setOnClickListener(this)

        ivOkShoulder?.setOnClickListener(this)
        ivSugShoulder?.setOnClickListener(this)
        ivReqShoulder?.setOnClickListener(this)

        ivOkTreadDepth?.setOnClickListener(this)
        ivSugTreadDepth?.setOnClickListener(this)
        ivReqTreadDepth?.setOnClickListener(this)

        ivOkTreadWear?.setOnClickListener(this)
        ivSugTreadWear?.setOnClickListener(this)
        ivReqTreadWear?.setOnClickListener(this)

        ivOkRimDamage?.setOnClickListener(this)
        ivSugRimDamage?.setOnClickListener(this)
        ivReqRimDamage?.setOnClickListener(this)

        ivOkbubble?.setOnClickListener(this)
        ivSugbubble?.setOnClickListener(this)
        ivReqbubble?.setOnClickListener(this)

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
            R.id.ivOkSideWell -> {

                ivOkSideWell?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivSugSideWell -> {

                ivSugSideWell?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivReqSideWell -> {

                ivReqSideWell?.setImageResource(R.mipmap.ic_condition_down)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkSideWell?.setImageResource(R.mipmap.ic_blank_condition)
            }

            R.id.ivOkShoulder -> {
                ivOkShoulder?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqShoulder?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivSugShoulder -> {
                ivSugShoulder?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqShoulder?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivReqShoulder -> {
                ivReqShoulder?.setImageResource(R.mipmap.ic_condition_down)
                ivSugShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkShoulder?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivOkTreadDepth -> {
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivSugTreadDepth -> {
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivReqTreadDepth -> {
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_condition_down)
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivOkTreadWear -> {
                ivOkTreadWear?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivSugTreadWear -> {
                ivSugTreadWear?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivReqTreadWear -> {
                ivReqTreadWear?.setImageResource(R.mipmap.ic_condition_down)
                ivSugTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivOkRimDamage -> {
                ivOkRimDamage?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivSugRimDamage -> {
                ivSugRimDamage?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivReqRimDamage -> {
                ivReqRimDamage?.setImageResource(R.mipmap.ic_condition_down)
                ivSugRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivOkbubble -> {
                ivOkbubble?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivSugbubble -> {
                ivSugbubble?.setImageResource(R.mipmap.ic_condition_degrade)
                ivSugbubble?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqbubble?.setImageResource(R.mipmap.ic_blank_condition)
            }
            R.id.ivReqbubble -> {
                ivReqbubble?.setImageResource(R.mipmap.ic_condition_down)
                ivSugbubble?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkbubble?.setImageResource(R.mipmap.ic_blank_condition)
            }


        }
    }
}

