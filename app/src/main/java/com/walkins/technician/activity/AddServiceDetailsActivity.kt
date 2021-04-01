package com.walkins.technician.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.onClickAdapter

class AddServiceDetailsActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter,
    View.OnTouchListener {
    private var arrayList = arrayListOf("Gallery", "Camera")

    private var ivInfoAddService: ImageView? = null
    private var ivAddServices: ImageView? = null
    private var ivAddTyreConfig: ImageView? = null
    private var ivAddTechnicalSuggestion: ImageView? = null

    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var llServiceExpanded: LinearLayout? = null
    private var llTyreConfigExpanded: LinearLayout? = null
    private var llTechnicalSuggestionExpanded: LinearLayout? = null

    private var serviceExpanded = false
    private var techicalSuggestionExpanded = false
    private var tyreConfigExpanded = false
    private var tyreConfig = false
    private var technicalSuggestion = false
    private var llUpdatedPlacement: LinearLayout? = null

    private var chkNitrogenTopup: CheckBox? = null
    private var chkNitrogenRefill: CheckBox? = null
    private var chkWheelBalacing: CheckBox? = null
    private var chkTyreRotation: CheckBox? = null

    private var suggestionsRecycView: RecyclerView? = null
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var tyreSuggestionAdapter: TyreSuggestionAdpater? = null

    private var relCarPhotoAdd2: RelativeLayout? = null
    private var relCarPhotoAdd1: RelativeLayout? = null

    private var ivTyre1: ImageView? = null
    private var ivTyre2: ImageView? = null
    private var ivTyre3: ImageView? = null
    private var ivTyre4: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_details)

        init()
    }

    private fun init() {

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivInfoAddService = findViewById(R.id.ivInfoAddService)
        ivAddServices = findViewById(R.id.ivAddServices)
        ivAddTechnicalSuggestion = findViewById(R.id.ivAddTechnicalSuggestion)
        ivAddTyreConfig = findViewById(R.id.ivAddTyreConfig)
        llServiceExpanded = findViewById(R.id.llServiceExpanded)
        llTyreConfigExpanded = findViewById(R.id.llTyreConfigExpanded)
        llTechnicalSuggestionExpanded = findViewById(R.id.llTechnicalSuggestionExpanded)
        llUpdatedPlacement = findViewById(R.id.llUpdatedPlacement)
        suggestionsRecycView = findViewById(R.id.suggestionsRecycView)

        ivTyre1 = findViewById(R.id.ivTyre1)
        ivTyre2 = findViewById(R.id.ivTyre2)
        ivTyre3 = findViewById(R.id.ivTyre3)
        ivTyre4 = findViewById(R.id.ivTyre4)

        chkNitrogenRefill = findViewById(R.id.chkNitrogenRefill)
        chkNitrogenTopup = findViewById(R.id.chkNitrogenTopup)
        chkTyreRotation = findViewById(R.id.chkTyreRotation)
        chkWheelBalacing = findViewById(R.id.chkWheelBalacing)

        relCarPhotoAdd2 = findViewById(R.id.relCarPhotoAdd2)
        relCarPhotoAdd1 = findViewById(R.id.relCarPhotoAdd1)

        tvTitle?.text = "Add Service Details"
        ivInfoAddService?.setOnClickListener(this)
        ivAddTechnicalSuggestion?.setOnClickListener(this)
        ivAddTyreConfig?.setOnClickListener(this)
        relCarPhotoAdd1?.setOnClickListener(this)
        relCarPhotoAdd2?.setOnClickListener(this)

        ivTyre1?.setOnTouchListener(this)
        ivTyre2?.setOnTouchListener(this)
        ivTyre3?.setOnTouchListener(this)
        ivTyre4?.setOnTouchListener(this)

        tyreSuggestionAdapter = TyreSuggestionAdpater(suggestionArr, this, this)
        tyreSuggestionAdapter?.onclick = this
        suggestionsRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        suggestionsRecycView?.adapter = tyreSuggestionAdapter


        ivAddServices?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                /*if (!serviceExpanded) {
                    ivAddServices?.setImageResource(R.drawable.ic_minus_icon)
                    Common.expand(llServiceExpanded!!)

                    Common.collapse(llTyreConfigExpanded!!)
                    Common.collapse(llTechnicalSuggestionExpanded!!)

                    techicalSuggestionExpanded = false
                    tyreConfigExpanded = false
                    serviceExpanded = true

                } else {
                    ivAddServices?.setImageResource(R.drawable.ic_add_icon)
                    Common.collapse(llServiceExpanded!!)
                    serviceExpanded = false

                }*/

                return false
            }

        })
        ivAddTechnicalSuggestion?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                /* if (techicalSuggestionExpanded) {
                     ivAddTechnicalSuggestion?.setImageResource(R.drawable.ic_add_icon)
                     llTechnicalSuggestionExpanded?.visibility = View.GONE
                     techicalSuggestionExpanded = false

                 } else {
                     ivAddTechnicalSuggestion?.setImageResource(R.drawable.ic_minus_icon)
                     llTechnicalSuggestionExpanded?.visibility = View.VISIBLE
                     techicalSuggestionExpanded = true

                 }*/

                return false
            }

        })
        ivAddTyreConfig?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                /*if (tyreConfigExpanded) {
                    ivAddTyreConfig?.setImageResource(R.drawable.ic_add_icon)
                    llTyreConfigExpanded?.visibility = View.GONE
                    tyreConfigExpanded = false

                } else {
                    ivAddTyreConfig?.setImageResource(R.drawable.ic_minus_icon)
                    llTyreConfigExpanded?.visibility = View.VISIBLE
                    tyreConfigExpanded = true

                }*/

                return false
            }

        })

        ivBack?.setOnClickListener(this)

        checkChangeListener()
    }

    private fun checkChangeListener() {
        chkWheelBalacing?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                showHideUpdatedPlacement()

            }

        })
        chkTyreRotation?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                showHideUpdatedPlacement()
            }

        })
        chkNitrogenTopup?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                showHideUpdatedPlacement()
            }

        })
        chkNitrogenRefill?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                showHideUpdatedPlacement()
            }

        })
    }

    fun showHideUpdatedPlacement() {
        if (chkWheelBalacing?.isChecked!! && chkNitrogenRefill?.isChecked!! &&
            chkNitrogenTopup?.isChecked!! && chkTyreRotation?.isChecked!!
        ) {
            Common.expand(llUpdatedPlacement!!)
        } else {
            if (llUpdatedPlacement?.visibility == View.VISIBLE) {
                Common.collapse(llUpdatedPlacement!!)
            }
        }
    }

    override fun onClick(v: View?) {

        val id = v?.id
        when (id) {

            R.id.ivInfoAddService -> {

            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.relCarPhotoAdd1, R.id.relCarPhotoAdd2 -> {

                showBottomSheetdialog(arrayList, "Choose From", this, Common.btn_filled)
            }
        }
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

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val id = v?.id
        val intent = Intent(this, VehicleMakeActivity::class.java)
        when (id) {

            R.id.ivTyre1, R.id.ivTyre2, R.id.ivTyre3, R.id.ivTyre4 -> {

                startActivity(intent)
            }
        }
        return false
    }
}