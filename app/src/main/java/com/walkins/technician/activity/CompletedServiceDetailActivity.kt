package com.walkins.technician.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.PendingTyreSuggestionAdpater
import com.walkins.technician.common.*
import com.walkins.technician.model.login.servicemodel.servicedata.ServiceDataByIdData
import com.walkins.technician.viewmodel.CommonViewModel


class CompletedServiceDetailActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener,
    View.OnTouchListener {

    private var commonViewModel: CommonViewModel? = null
    private lateinit var prefManager: PrefManager
    private var pendingSuggestionsRecycView: RecyclerView? = null
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var pendingArr = arrayListOf("Tyre Pattern", "Visual Detail - LF")
    private var tyreSuggestionAdapter: PendingTyreSuggestionAdpater? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var tvCurrentDateTime: TextView? = null
    private var tvtyreServiceInfo: TextView? = null
    private var selectedPending = ""

    private var tvTechnicalSuggetion: TextView? = null
    private var tvTyreConfig: TextView? = null
    private var tvServices: TextView? = null

    private var ivTyre4: ImageView? = null
    private var ivTyre1: ImageView? = null
    private var ivTyre2: ImageView? = null
    private var ivTyre3: ImageView? = null

    private var ivPhoneCall: ImageView? = null

    private var ivInfoImg: ImageView? = null
    private var title: String = ""
    private var llservicecollapse: LinearLayout? = null
    private var lltyreconfig: LinearLayout? = null
    private var lltechnical: LinearLayout? = null
    private var ivAddServices: ImageView? = null
    private var ivAddTyreConfig: ImageView? = null
    private var ivAddTechnicalSuggestion: ImageView? = null

    private var llServiceExpanded: LinearLayout? = null
    private var llTyreConfigExpanded: LinearLayout? = null
    private var llTechnicalSuggestionExpanded: LinearLayout? = null
    private var llUpdatedPlacement: LinearLayout? = null
    private var lltechnicalbg: LinearLayout? = null
    private var lltyreconfigbg: LinearLayout? = null
    private var llservicebg: LinearLayout? = null
    private var serviceRecycView: RecyclerView? = null
//    private var serviceAdapter:

    private var uuid: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_service_detail)
        prefManager = PrefManager(this)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        init()
    }

    private fun init() {

        lltyreconfigbg = findViewById(R.id.lltyreconfigbg)
        serviceRecycView = findViewById(R.id.serviceRecycView)
        lltechnicalbg = findViewById(R.id.lltechnicalbg)
        llservicebg = findViewById(R.id.llservicebg)
        tvTechnicalSuggetion = findViewById(R.id.tvTechnicalSuggetion)
        tvServices = findViewById(R.id.tvServices)
        tvTyreConfig = findViewById(R.id.tvTyreConfig)

        llServiceExpanded = findViewById(R.id.llServiceExpanded)
        llTechnicalSuggestionExpanded = findViewById(R.id.llTechnicalSuggestionExpanded)
        llTyreConfigExpanded = findViewById(R.id.llTyreConfigExpanded)
        llUpdatedPlacement = findViewById(R.id.llUpdatedPlacement)
        ivPhoneCall = findViewById(R.id.ivPhoneCall)

        ivAddServices = findViewById(R.id.ivAddServices)
        ivAddTechnicalSuggestion = findViewById(R.id.ivAddTechnicalSuggestion)
        ivAddTyreConfig = findViewById(R.id.ivAddTyreConfig)
        lltyreconfig = findViewById(R.id.lltyreconfig)
        llservicecollapse = findViewById(R.id.llservicecollapse)
        lltechnical = findViewById(R.id.lltechnical)
        tvtyreServiceInfo = findViewById(R.id.tvtyreServiceInfo)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivTyre4 = findViewById(R.id.ivTyre4)
        ivTyre3 = findViewById(R.id.ivTyre3)
        ivTyre2 = findViewById(R.id.ivTyre2)
        ivTyre1 = findViewById(R.id.ivTyre1)

//        ivInfoImg = findViewById(R.id.ivInfoImg)

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

        tvTitle?.text = "Report Details"

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
            if (intent.getStringExtra("uuid") != null) {
                uuid = intent.getStringExtra("uuid")
            }
        }
        ivBack?.setOnClickListener(this)
        tvtyreServiceInfo?.setOnClickListener(this)

        ivAddServices?.setOnClickListener(this)
        ivAddTyreConfig?.setOnClickListener(this)
        ivAddTechnicalSuggestion?.setOnClickListener(this)
        llservicecollapse?.setOnClickListener(this)
        lltyreconfig?.setOnClickListener(this)
        lltechnical?.setOnClickListener(this)
//        ivInfoImg?.setOnClickListener(this)


        ivTyre1?.setOnTouchListener(this)
        ivTyre2?.setOnTouchListener(this)
        ivTyre3?.setOnTouchListener(this)
        ivTyre4?.setOnTouchListener(this)
        ivPhoneCall?.setOnTouchListener(this)

        tvCurrentDateTime?.text = Common.getCurrentDateTime()

//        getServiceDataById()
    }

    private fun getServiceDataById() {

        val jsonObject = JsonObject()
        jsonObject.addProperty("id", uuid)
        commonViewModel?.callApiGetServiceById(jsonObject, prefManager.getAccessToken()!!, this)
        commonViewModel?.getServiceById()?.observe(this, Observer {
            if (it != null) {
                if (it.success) {

                    setTyreServiceData(it.data?.get(0))

                } else {
                    if (it.error != null) {
                        if (it.error?.get(0).message != null) {
                            showShortToast(it.error?.get(0).message, this)
                        }
                    }
                }
            }
        })
    }

    private fun setTyreServiceData(data: ServiceDataByIdData) {

        /*"id": 4,
        "uuid": "862247ba-77ad-4429-a39d-1e87e239eabe",
        "vehicle_id": 1,
        "dealer_id": 2,
        "service_user_id": null,
        "service_activity_history_id": null,
        "date_of_service": "2021-05-03 06:22:47.467945+00",
        "technician_name": "Dipak Bhoot",
        "walkins_team_member": null,
        "front_left_tyre_make": "jk tyre",
        "front_right_tyre_make": "jk tyre",
        "back_left_tyre_make": "jk tyre",
        "back_right_tyre_make": "jk tyre",
        "front_left_tyre_size": "165/80 R14",
        "front_right_tyre_size": "165/80 R14",
        "back_left_tyre_size": "165/80 R14",
        "back_right_tyre_size": "165/80 R14",
        "front_left_tyre_pattern": "Estate N6",
        "front_right_tyre_pattern": "Estate N6",
        "back_left_tyre_pattern": "Estate N6",
        "back_right_tyre_pattern": "Estate N6",
        "front_left_tyre_side_wall": "SUG",
        "front_right_tyre_side_wall": "OK",
        "back_left_tyre_side_wall": "REQ",
        "back_right_tyre_side_wall": "OK",
        "front_left_tyre_shoulder": "SUG",
        "front_right_tyre_shoulder": "OK",
        "back_left_tyre_shoulder": "REQ",
        "back_right_tyre_shoulder": "OK",
        "front_left_tyre_tread_wear": "REQ",
        "front_right_tyre_tread_wear": "OK",
        "back_left_tyre_tread_wear": "REQ",
        "back_right_tyre_tread_wear": "OK",
        "front_left_tyre_tread_depth": "OK",
        "front_right_tyre_tread_depth": "OK",
        "back_left_tyre_tread_depth": "REQ",
        "back_right_tyre_tread_depth": "OK",
        "front_left_tyre_rim_demage": "OK",
        "front_right_tyre_rim_demage": "OK",
        "back_left_tyre_rim_demage": "REQ",
        "back_right_tyre_rim_demage": "OK",
        "front_left_tyre_buldge_bubble": "OK",
        "front_right_tyre_buldge_bubble": "OK",
        "back_left_tyre_buldge_bubble": "REQ",
        "back_right_tyre_buldge_bubble": "OK",
        "front_left_tyre_psi_in": "35",
        "front_right_tyre_psi_in": "25",
        "back_left_tyre_psi_in": "33",
        "back_right_tyre_psi_in": "30",
        "front_left_tyre_psi_out": "30",
        "front_right_tyre_psi_out": "28",
        "back_left_tyre_psi_out": "32",
        "back_right_tyre_psi_out": "30",
        "front_left_tyre_weight": "33",
        "back_left_tyre_weight": "30",
        "back_right_tyre_weight": "36",
        "front_left_tyre_wheel_rotation": null,
        "back_left_tyre_wheel_rotation": null,
        "back_right_tyre_wheel_rotation": null,
        "front_left_tyre_wheel_image": "front_left_tyre_wheel_image",
        "back_left_tyre_wheel_image": "back_left_tyre_wheel_image",
        "back_right_tyre_wheel_image": "back_right_tyre_wheel_image",
        "wheel_rotation_text": null,
        "service_narration": null,
        "technician_suggestion": null,
        "front_left_manufacturing_date": "0620",
        "front_right_manufacturing_date": "0620",
        "back_left_manufacturing_date": "0620",
        "back_right_manufacturing_date": "0620",
        "front_left_issues_to_be_resolved": [
        "issue"
        ],
        "front_right_issues_to_be_resolved": [
        "issue"
        ],
        "back_left_issues_to_be_resolved": [
        "issue"
        ],
        "back_right_issues_to_be_resolved": [
        "issue"
        ],
        "additional_comments": null,
        "summary_from_expert": null,
        "service_suggestions": "more_suggestions",
        "next_service_due": "2021-06-03T18:29:00.000Z",
        "last_service_history": null,
        "actual_service_date": "2021-05-03T06:22:47.467Z",
        "future_service_date": null,
        "services_to_do": [
        12,
        13
        ],
        "car_photo_1": "",
        "car_photo_2": "",
        "created_by": null,
        "updated_by": null,
        "created_at": "2021-04-27T11:48:12.508Z",
        "updated_at": "2021-04-27T11:48:12.508Z",
        "technician_suggestions": [
        "suggestion"
        ],
        "front_right_tyre_wheel_image": "f",
        "service": [
        {
            "id": 12,
            "name": "Type Rotation",
            "image": "https://tyreservice-images.s3.amazonaws.com/service/TyreRotation.png"
        },
        {
            "id": 13,
            "name": "Wheel Balancing",
            "image": "https://tyreservice-images.s3.amazonaws.com/service/WheelBalancing.png"
        }
        ],
        "reg_number": "123",
        "make": "Fiat",
        "make_image": "https://tyreservice-images.aapkedoorstep.com/Vehicle-Make/fiat.jpeg",
        "model": "Palio",
        "model_image": "https://tyreservice-images.aapkedoorstep.com/VehicleModelImages/palio.png",
        "technician_image": "https://tyreservice-images.s3.amazonaws.com/profile/file-7345-1619699524279.png"
        */
        var json = JsonObject()
        var jsonArr = JsonArray()
        json.addProperty(TyreKey.tyreType, "LF")
        json.addProperty(TyreKey.vehicleMake, data.frontLeftTyreMake)
        json.addProperty(TyreKey.vehicleMakeId, data.frontLeftTyreMake)
        json.addProperty(
            TyreKey.vehicleMakeURL,
            ""
        )
        json.addProperty(
            TyreKey.vehiclePattern,
            data.frontLeftTyrePattern
        )
        json.addProperty(
            TyreKey.vehiclePatternId,
            data.frontLeftTyrePattern
        )
        json.addProperty(TyreKey.vehicleSize, data.frontLeftTyreSize)
        json.addProperty(TyreKey.vehicleSizeId, data.frontLeftTyreSize)
        json.addProperty(
            TyreKey.manufaturingDate,
            data.frontLeftManufacturingDate
        )
        json.addProperty(
            TyreKey.psiInTyreService,
            data.frontLeftTyrePsiIn
        )
        json.addProperty(
            TyreKey.psiOutTyreService,
            data.frontLeftTyrePsiOut
        )
        json.addProperty(
            TyreKey.weightTyreService,
            data.frontLeftTyreWeight
        )
        json.addProperty(TyreKey.sidewell, data.frontLeftTyreSideWall)
        json.addProperty(TyreKey.shoulder, data.frontLeftTyreShoulder)
        json.addProperty(TyreKey.treadDepth, data.frontLeftTyreTreadDepth)
        json.addProperty(TyreKey.treadWear, data.frontLeftTyreTreadWear)
        json.addProperty(TyreKey.rimDamage, data.frontLeftTyreRimDemage)
        json.addProperty(TyreKey.bubble, data.frontLeftTyreBuldgeBubble)
        json.addProperty(TyreKey.visualDetailPhotoUrl, data.frontLeftTyreWheelImage)

        json.addProperty(TyreKey.isCompleted, "true")

        if (data.frontLeftIssuesToBeResolved.size>0) {
            for (i in data.frontLeftIssuesToBeResolved.indices) {

                jsonArr.add(data.frontLeftIssuesToBeResolved.get(i))
            }
        }
        json.add(TyreKey.issueResolvedArr, jsonArr)

        prefManager.setValue(TyreConfigClass.TyreLFObject, json.toString())


    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 0) {

            if (pendingArr?.get(variable)?.equals("Tyre Pattern")) {
                selectedPending = "pattern"
//                val intent = Intent(this, VehiclePatternActivity::class.java)
//                startActivity(intent)
            } else if (pendingArr?.get(variable)?.equals("Visual Detail - LF", ignoreCase = true)) {
                selectedPending = "visual"
//                val intent = Intent(this, VisualDetailsActivity::class.java)
//                startActivity(intent)
            }
        }

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivAddServices -> {
                if (llServiceExpanded?.visibility == View.VISIBLE) {
                    Common.collapse(llServiceExpanded!!)
//                    Common.collapse(llUpdatedPlacement!!)
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = false
                    ivAddServices?.setImageResource(R.mipmap.ic_add_icon)
//                    llservicebg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                } else {
//                    llservicebg?.setBackgroundDrawable(null)
                    ivAddServices?.setImageResource(R.mipmap.ic_minus_icon)
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = false
                    Common.expand(llServiceExpanded!!)

                    if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTyreConfigExpanded!!)
                        ivAddTyreConfig?.setImageResource(R.mipmap.ic_add_icon)
//                        lltyreconfigbg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }
                    if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTechnicalSuggestionExpanded!!)
                        ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_add_icon)
//                        lltechnicalbg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }

                    tvTyreConfig?.isAllCaps = false
                    tvTechnicalSuggetion?.isAllCaps = false
                }
            }
            R.id.ivAddTyreConfig -> {
                if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                    Common.collapse(llTyreConfigExpanded!!)
                    tvTyreConfig?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTyreConfig?.isAllCaps = false
                    ivAddTyreConfig?.setImageResource(R.mipmap.ic_add_icon)
//                    lltyreconfigbg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                } else {
//                    lltyreconfigbg?.setBackgroundDrawable(null)
                    ivAddTyreConfig?.setImageResource(R.mipmap.ic_minus_icon)
                    tvTyreConfig?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTyreConfig?.isAllCaps = false
                    Common.expand(llTyreConfigExpanded!!)
                    if (llServiceExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llServiceExpanded!!)
//                        Common.collapse(llUpdatedPlacement!!)
                        ivAddServices?.setImageResource(R.mipmap.ic_add_icon)
//                        llservicebg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }
                    if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTechnicalSuggestionExpanded!!)
                        ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_add_icon)
//                        lltechnicalbg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }

                    tvServices?.isAllCaps = false
                    tvTechnicalSuggetion?.isAllCaps = false
                }
            }
            R.id.ivAddTechnicalSuggestion -> {
                if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                    Common.collapse(llTechnicalSuggestionExpanded!!)
                    tvTechnicalSuggetion?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTechnicalSuggetion?.isAllCaps = false
                    ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_add_icon)
//                    lltechnicalbg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                } else {
//                    lltechnicalbg?.setBackgroundDrawable(null)
                    ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_minus_icon)
                    tvTechnicalSuggetion?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTechnicalSuggetion?.isAllCaps = false

                    Common.expand(llTechnicalSuggestionExpanded!!)
                    if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTyreConfigExpanded!!)
                        ivAddTyreConfig?.setImageResource(R.mipmap.ic_add_icon)
//                        lltechnicalbg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }
                    if (llServiceExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llServiceExpanded!!)
//                        Common.collapse(llUpdatedPlacement!!)
                        ivAddServices?.setImageResource(R.mipmap.ic_add_icon)
//                        llservicebg?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }
                    tvServices?.isAllCaps = false
                    tvTyreConfig?.isAllCaps = false
                }
            }
            R.id.lltechnical -> {
                ivAddTechnicalSuggestion?.performClick()
            }
            R.id.llservicecollapse -> {
                ivAddServices?.performClick()
            }
            R.id.lltyreconfig -> {
                ivAddTyreConfig?.performClick()
            }
//            R.id.ivInfoImg -> {
//                showBottomSheetdialog(pendingArr, "RR Pending", this, Common.btn_filled, "Proceed")
//            }
        }
    }

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        btnText: String

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

        tvTitleText.text = titleStr
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

        btnSend?.text = btnText
        btnSend.setOnClickListener {
            dialog?.dismiss()

            if (selectedPending?.equals("pattern")) {
//                selectedPending="pattern"
                val intent = Intent(this, VehiclePatternActivity::class.java)
                startActivity(intent)
            } else if (selectedPending?.equals("visual", ignoreCase = true)) {
//                selectedPending="visual"
                val intent = Intent(this, VisualDetailsActivity::class.java)
                startActivity(intent)
            }

        }

        dialog.show()

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var id = v?.id

        when (id) {

            R.id.ivTyre1 -> {
                var intent = Intent(this, CompletedVisualDetailActivity::class.java)
                intent.putExtra("title", "Detail - LF")
                startActivity(intent)
            }
            R.id.ivTyre2 -> {
                var intent = Intent(this, CompletedVisualDetailActivity::class.java)
                intent.putExtra("title", "Detail - RF")
                startActivity(intent)
            }
            R.id.ivTyre3 -> {
                var intent = Intent(this, CompletedVisualDetailActivity::class.java)
                intent.putExtra("title", "Detail - LR")
                startActivity(intent)
            }
            R.id.ivTyre4 -> {
                var intent = Intent(this, CompletedVisualDetailActivity::class.java)
                intent.putExtra("title", "Detail - RR")
                startActivity(intent)
            }
            R.id.ivPhoneCall -> {
                if (!Common.checkCallPermission(this)) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    val phone = "+91942829730011"
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$phone")
                    startActivity(callIntent)
                }
            }


        }
        return false
    }

}