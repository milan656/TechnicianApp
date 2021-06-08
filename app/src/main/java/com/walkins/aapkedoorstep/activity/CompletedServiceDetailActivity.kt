package com.walkins.aapkedoorstep.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.CompletedServiceAdapter
import com.walkins.aapkedoorstep.adapter.DialogueAdpater
import com.walkins.aapkedoorstep.adapter.PendingTyreSuggestionAdpater
import com.walkins.aapkedoorstep.common.*
import com.walkins.aapkedoorstep.custom.BoldButton
import com.walkins.aapkedoorstep.model.login.service.ServiceModelData
import com.walkins.aapkedoorstep.model.login.servicemodel.servicedata.ServiceDataByIdModel
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SetTextI18n")
class CompletedServiceDetailActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener,
    View.OnTouchListener {

    private var commonViewModel: CommonViewModel? = null
    private lateinit var prefManager: PrefManager
    private var pendingSuggestionsRecycView: RecyclerView? = null
    private var suggestionArr = arrayListOf<String>()

    private var serviceDateByIdModel: ServiceDataByIdModel? = null
    private var pendingArr = arrayListOf("Tyre Pattern", "Visual Detail - LF")
    private var tyreSuggestionAdapter: PendingTyreSuggestionAdpater? = null
    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var tvCurrentDateTime: TextView? = null
    private var llfooter: LinearLayout? = null
    private var tvtyreServiceInfo: TextView? = null
    private var selectedPending = ""

    private var serviceAdapter: CompletedServiceAdapter? = null

    private var tvTechnicalSuggetion: TextView? = null
    private var tvTyreConfig: TextView? = null
    private var tvServices: TextView? = null

    private var serviceList: ArrayList<ServiceModelData>? = ArrayList()

    private var ivTyre4: ImageView? = null
    private var ivTyre1: ImageView? = null
    private var ivTyre2: ImageView? = null
    private var ivTyre3: ImageView? = null

    private var carPhoto_1 = "carPhoto_1"
    private var carPhoto_2 = "carPhoto_2"

    private var ivPhoneCall: ImageView? = null

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

    private var ivTyreRightFront: ImageView? = null
    private var ivtyreLeftFront: ImageView? = null
    private var ivtyreLeftRear: ImageView? = null
    private var ivTyreRightRear: ImageView? = null
    private var ivInfoAddService: ImageView? = null

    private var tvMoreSuggestion: TextView? = null
    private var ivCarImage_1: ImageView? = null
    private var ivCarImage_2: ImageView? = null
    private var tvColor: TextView? = null
    private var tvMakeModel: TextView? = null
    private var tvRegNumber: TextView? = null
    private var llColor: LinearLayout? = null
    private var ivCarImage: ImageView? = null
//    private var serviceAdapter:

    private var uuid: String? = ""
    private var color: String = ""
    private var colorcode: String = ""
    private var makeModel: String = ""
    private var regNumber: String = ""
    private var carImage: String = ""
    private var address: String = ""
    private var formatedDate: String = ""

    private var tvUpdatePlacementLF: TextView? = null
    private var tvUpdatePlacementLR: TextView? = null
    private var tvUpdatePlacementRF: TextView? = null
    private var tvUpdatePlacementRR: TextView? = null
    private var llRotationLF: LinearLayout? = null
    private var llRotationRF: LinearLayout? = null
    private var llRotationLR: LinearLayout? = null
    private var llRotationRR: LinearLayout? = null
    private var llBackRotation: LinearLayout? = null
    private var llFrontRotation: LinearLayout? = null

    private var tvServicePersonName: TextView? = null
    private var phoneNumber: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_service_detail)
        prefManager = PrefManager(this)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        init()
    }

    private fun init() {

        lltyreconfigbg = findViewById(R.id.lltyreconfigbg)
        tvServicePersonName = findViewById(R.id.tvServicePersonName)
        llRotationLF = findViewById(R.id.llRotationLF)
        llRotationLR = findViewById(R.id.llRotationLR)
        llRotationRF = findViewById(R.id.llRotationRF)
        llRotationRR = findViewById(R.id.llRotationRR)
        llBackRotation = findViewById(R.id.llBackRotation)
        llFrontRotation = findViewById(R.id.llFrontRotation)

        serviceRecycView = findViewById(R.id.serviceRecycView)
        lltechnicalbg = findViewById(R.id.lltechnicalbg)
        llservicebg = findViewById(R.id.llservicebg)
        tvTechnicalSuggetion = findViewById(R.id.tvTechnicalSuggetion)
        tvServices = findViewById(R.id.tvServices)
        tvTyreConfig = findViewById(R.id.tvTyreConfig)
        tvMoreSuggestion = findViewById(R.id.tvMoreSuggestion)
        ivCarImage_1 = findViewById(R.id.ivCarImage_1)
        ivCarImage_2 = findViewById(R.id.ivCarImage_2)
        llfooter = findViewById(R.id.llfooter)

        tvUpdatePlacementRR = findViewById(R.id.tvUpdatePlacementRR)
        tvUpdatePlacementRF = findViewById(R.id.tvUpdatePlacementRF)
        tvUpdatePlacementLF = findViewById(R.id.tvUpdatePlacementLF)
        tvUpdatePlacementLR = findViewById(R.id.tvUpdatePlacementLR)

        llServiceExpanded = findViewById(R.id.llServiceExpanded)
        llTechnicalSuggestionExpanded = findViewById(R.id.llTechnicalSuggestionExpanded)
        llTyreConfigExpanded = findViewById(R.id.llTyreConfigExpanded)
        llUpdatedPlacement = findViewById(R.id.llUpdatedPlacement)
        ivPhoneCall = findViewById(R.id.ivPhoneCall)
        ivInfoAddService = findViewById(R.id.ivInfoAddService)

        tvColor = findViewById(R.id.tvColor)
        tvMakeModel = findViewById(R.id.tvMakeModel)
        tvRegNumber = findViewById(R.id.tvRegNumber)
        llColor = findViewById(R.id.llColor)
        ivCarImage = findViewById(R.id.ivCarImage)

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

        ivTyreRightRear = findViewById(R.id.ivTyreRightRear)
        ivtyreLeftRear = findViewById(R.id.ivtyreLeftRear)
        ivtyreLeftFront = findViewById(R.id.ivtyreLeftFront)
        ivTyreRightFront = findViewById(R.id.ivTyreRightFront)

        ivTyreRightRear?.visibility = View.VISIBLE
        ivtyreLeftRear?.visibility = View.VISIBLE
        ivtyreLeftFront?.visibility = View.VISIBLE
        ivTyreRightFront?.visibility = View.VISIBLE

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

        serviceAdapter = CompletedServiceAdapter(serviceList!!, this, this)
        serviceRecycView?.layoutManager = GridLayoutManager(
            this, 2,
            RecyclerView.VERTICAL,
            false
        )
        serviceRecycView?.adapter = serviceAdapter
        serviceAdapter?.onclick = this

        tvTitle?.text = "Report Details"

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
            if (intent.getStringExtra("uuid") != null) {
                uuid = intent.getStringExtra("uuid")
            }
            if (intent.getStringExtra("colorcode") != null) {
                colorcode = intent.getStringExtra("colorcode")!!
            }

            if (intent.getStringExtra("makeModel") != null) {
                makeModel = intent.getStringExtra("makeModel")!!
            }
            if (intent.getStringExtra("regNumber") != null) {
                regNumber = intent.getStringExtra("regNumber")!!
            }
            if (intent.getStringExtra("carImage") != null) {
                carImage = intent.getStringExtra("carImage")!!
            }
            if (intent.getStringExtra("color") != null) {
                color = intent.getStringExtra("color")!!
            }
            if (intent.getStringExtra("address") != null) {
                address = intent.getStringExtra("address")!!
            }
            if (intent.getStringExtra("formatedDate") != null) {
                formatedDate = intent.getStringExtra("formatedDate")!!
            }

        }

        tvColor?.text = color
        tvRegNumber?.text = regNumber
        tvMakeModel?.text = makeModel

        llColor?.setBackgroundColor(Color.parseColor(colorcode))

        try {
            Glide.with(this)
                .load(carImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_no_car_image)
                .into(ivCarImage!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ivBack?.setOnClickListener(this)
        tvtyreServiceInfo?.setOnClickListener(this)
        ivInfoAddService?.setOnClickListener(this)
        ivCarImage_1?.setOnClickListener(this)
        ivCarImage_2?.setOnClickListener(this)

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

//        tvCurrentDateTime?.text = Common.getCurrentDateTime()

        if (Common.isConnectedToInternet(this)) {
            getServiceDataById()
        } else {
            showDialogue("Oops!", "Your Internet is not connected", false)
        }

    }

    fun showDialogue(title: String, message: String, isBackPressed: Boolean) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout_service, null)

        val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tv_message.text = message
        tvTitleText?.gravity = Gravity.CENTER
        tv_message?.gravity = Gravity.CENTER
        ivClose?.visibility = View.INVISIBLE
        btnYes.setOnClickListener {
            builder.dismiss()

        }
        builder.setView(root)
        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    private fun getServiceDataById() {

        Common.showLoader(this)

        val jsonObject = JsonObject()
        jsonObject.addProperty("id", uuid)
        commonViewModel?.callApiGetServiceById(jsonObject, prefManager.getAccessToken()!!, this)
        commonViewModel?.getServiceById()?.observe(this, Observer {
            Log.e("modelget", "" + it.data)
            if (it != null) {
                if (it.success) {
                    serviceDateByIdModel = it
                    setTyreServiceData("")
                } else {
                    Common.hideLoader()
                    if (it.error != null) {
                        if (it.error?.get(0).message != null) {
                            showShortToast(it.error?.get(0).message, this)
                        }
                    }
                }
            } else {
                Common.hideLoader()
            }
        })
    }

    private fun setTyreServiceData(tyreType: String) {

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


        if (serviceDateByIdModel != null && serviceDateByIdModel?.data?.size!! > 0) {

            if (serviceDateByIdModel?.data?.get(0)?.service_user_name != null &&
                !serviceDateByIdModel?.data?.get(0)?.service_user_name.equals("")
            ) {
                tvServicePersonName?.text = serviceDateByIdModel?.data?.get(0)?.service_user_name
            }
            if (serviceDateByIdModel?.data?.get(0)?.service_user_mobile != null &&
                !serviceDateByIdModel?.data?.get(0)?.service_user_mobile.equals("")
            ) {
                phoneNumber = serviceDateByIdModel?.data?.get(0)?.service_user_mobile
                ivPhoneCall?.visibility = View.VISIBLE
            } else {
                ivPhoneCall?.visibility = View.GONE
            }
            Log.e("modelget", "" + serviceDateByIdModel?.data)
            if (serviceDateByIdModel?.data?.get(0)?.service != null && serviceDateByIdModel?.data?.get(0)?.service?.size!! > 0) {
                serviceList?.addAll(serviceDateByIdModel?.data?.get(0)?.service!!)
                Log.e("modelget", "" + serviceList?.size)
                serviceAdapter?.notifyDataSetChanged()
            }

            tvMoreSuggestion?.text = "" + serviceDateByIdModel?.data?.get(0)?.serviceSuggestions

            tvMakeModel?.text = "" + serviceDateByIdModel?.data?.get(0)?.make + " " + serviceDateByIdModel?.data?.get(0)?.model
            tvRegNumber?.text = "" + serviceDateByIdModel?.data?.get(0)?.regNumber

            Log.e("modelget", "" + tvRegNumber?.text + " " + serviceDateByIdModel?.data?.get(0)?.make)

            if (serviceDateByIdModel?.data?.get(0)?.front_left_tyre_wheel_rotation != null &&
                !serviceDateByIdModel?.data?.get(0)?.front_left_tyre_wheel_rotation.equals("")
            ) {
                tvUpdatePlacementLF?.text = serviceDateByIdModel?.data?.get(0)?.front_left_tyre_wheel_rotation
            }
            if (serviceDateByIdModel?.data?.get(0)?.front_right_tyre_wheel_rotation != null &&
                !serviceDateByIdModel?.data?.get(0)?.front_right_tyre_wheel_rotation.equals("")
            ) {
                tvUpdatePlacementRF?.text = serviceDateByIdModel?.data?.get(0)?.front_right_tyre_wheel_rotation
            }
            if (serviceDateByIdModel?.data?.get(0)?.back_left_tyre_wheel_rotation != null &&
                !serviceDateByIdModel?.data?.get(0)?.back_left_tyre_wheel_rotation.equals("")
            ) {
                tvUpdatePlacementLR?.text = serviceDateByIdModel?.data?.get(0)?.back_left_tyre_wheel_rotation
            }
            if (serviceDateByIdModel?.data?.get(0)?.back_right_tyre_wheel_rotation != null &&
                !serviceDateByIdModel?.data?.get(0)?.back_right_tyre_wheel_rotation.equals("")
            ) {
                tvUpdatePlacementRR?.text = serviceDateByIdModel?.data?.get(0)?.back_right_tyre_wheel_rotation
            }

            if (tvUpdatePlacementLF?.text?.toString()?.isEmpty()!!) {
                llRotationLF?.visibility = View.GONE
            }
            if (tvUpdatePlacementRR?.text?.toString()?.isEmpty()!!) {
                llRotationRR?.visibility = View.GONE
            }
            if (tvUpdatePlacementRF?.text?.toString()?.isEmpty()!!) {
                llRotationRF?.visibility = View.GONE
            }
            if (tvUpdatePlacementLR?.text?.toString()?.isEmpty()!!) {
                llRotationLR?.visibility = View.GONE
            }

            if (tvUpdatePlacementLF?.text?.toString()?.isEmpty()!! && tvUpdatePlacementRF?.text?.toString()?.isEmpty()!!) {
                llFrontRotation?.visibility = View.GONE
            }

            if (tvUpdatePlacementLR?.text?.toString()?.isEmpty()!! && tvUpdatePlacementRR?.text?.toString()?.isEmpty()!!) {
                llBackRotation?.visibility = View.GONE
            }
            if (tvUpdatePlacementLF?.text?.toString()?.isEmpty()!! && tvUpdatePlacementLR?.text?.toString()?.isEmpty()!!
                && tvUpdatePlacementRF?.text?.toString()?.isEmpty()!! && tvUpdatePlacementRR?.text?.toString()?.isEmpty()!!
            ) {
                llUpdatedPlacement?.visibility = View.GONE
            }

            try {
                Log.e("modelget", "" + serviceDateByIdModel?.data?.get(0)?.actualServiceDate)
                if (serviceDateByIdModel?.data?.get(0)?.actualServiceDate != null && !serviceDateByIdModel?.data?.get(0)?.actualServiceDate.equals("")) {
                    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val output = SimpleDateFormat("hh:mm aa, dd MMMM yyyy")

                    var formatDate = Common.addHour(serviceDateByIdModel?.data?.get(0)?.actualServiceDate, 5, 30)

                    var d: Date? = null
                    try {
                        d = input.parse(formatDate)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val formatted: String = output.format(d)
                    Log.e("DATE", "" + formatted)
                    try {
                        tvCurrentDateTime?.text = formatted
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (serviceDateByIdModel?.data?.get(0)?.carPhoto1 != null) {
                try {
                    Glide.with(this).load(serviceDateByIdModel?.data?.get(0)?.carPhoto1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder).into(ivCarImage_1!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                carPhoto_1 = serviceDateByIdModel?.data?.get(0)?.carPhoto1!!
                TyreConfigClass.CarPhoto_1 = carPhoto_1
            }
            Log.e("modelget", "" + serviceDateByIdModel?.data?.get(0)?.carPhoto1)
            Log.e("modelget", "" + serviceDateByIdModel?.data?.get(0)?.carPhoto2)
            if (serviceDateByIdModel?.data?.get(0)?.carPhoto2 != null) {
                try {
                    Glide.with(this).load(serviceDateByIdModel?.data?.get(0)?.carPhoto2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder).into(ivCarImage_2!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                carPhoto_2 = serviceDateByIdModel?.data?.get(0)?.carPhoto2!!
                TyreConfigClass.CarPhoto_2 = carPhoto_2
            }
            Log.e("modelget", "" + serviceDateByIdModel?.data?.get(0)?.front_left_tyre_make_image)
            if (serviceDateByIdModel?.data?.get(0)?.front_left_tyre_make_image != null) {
                try {
                    Glide.with(this).load(serviceDateByIdModel?.data?.get(0)?.front_left_tyre_make_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder).into(ivtyreLeftFront!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (serviceDateByIdModel?.data?.get(0)?.front_right_tyre_make_image != null) {

                try {
                    Glide.with(this).load(serviceDateByIdModel?.data?.get(0)?.front_right_tyre_make_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder).into(ivTyreRightFront!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (serviceDateByIdModel?.data?.get(0)?.back_left_tyre_make_image != null) {

                try {
                    Glide.with(this).load(serviceDateByIdModel?.data?.get(0)?.back_left_tyre_make_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder).into(ivtyreLeftRear!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (serviceDateByIdModel?.data?.get(0)?.back_right_tyre_make_image != null) {

                try {
                    Glide.with(this).load(serviceDateByIdModel?.data?.get(0)?.back_right_tyre_make_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder).into(ivTyreRightRear!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (serviceDateByIdModel?.data?.get(0)?.technicianSuggestions != null &&
                serviceDateByIdModel?.data?.get(0)?.technicianSuggestions?.size!! > 0
            ) {
                suggestionArr.clear()
                if (serviceDateByIdModel?.data?.get(0)?.technicianSuggestions?.size!! > 0) {
                    for (i in serviceDateByIdModel?.data?.get(0)?.technicianSuggestions?.indices!!) {
                        suggestionArr.add(serviceDateByIdModel?.data?.get(0)?.technicianSuggestions?.get(i)!!)
                    }
                }

                tyreSuggestionAdapter?.notifyDataSetChanged()
            }
            llfooter?.visibility = View.VISIBLE
            lltyreconfig?.isClickable = true
            lltyreconfig?.isEnabled = true
        } else {
            llfooter?.visibility = View.GONE
            lltyreconfig?.isClickable = false
            lltyreconfig?.isEnabled = false
        }

        Common.hideLoader()
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 0) {

            if (pendingArr.get(variable).equals("Tyre Pattern")) {
                selectedPending = "pattern"
//                val intent = Intent(this, VehiclePatternActivity::class.java)
//                startActivity(intent)
            } else if (pendingArr.get(variable).equals("Visual Detail - LF", ignoreCase = true)) {
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
            R.id.ivInfoAddService -> {
                showBottomSheetdialogNormal(
                    Common.commonPhotoChooseArr,
                    "Address Detail",
                    this,
                    Common.btn_filled,
                    false, Common.getStringBuilder(address)
                )
            }
            R.id.ivCarImage_1 -> {
                if (!carPhoto_1.equals("")) {
                    showImage(carPhoto_1)
                }
            }
            R.id.ivCarImage_2 -> {
                if (!carPhoto_2.equals("")) {
                    showImage(carPhoto_2)
                }
            }
        }
    }


    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        stringBuilder: StringBuilder,
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog =
            this?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btnOk)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        val str = stringBuilder.toString().replace(", ", "" + "\n").replace(",", "" + "\n")
        tv_message?.text = str

        if (str.isNotEmpty()) {
            tv_message.visibility = View.VISIBLE
        }

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }
        if (isBtnVisible) {
            btnSend.visibility = View.VISIBLE
        } else {
            btnSend.visibility = View.GONE
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

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        btnText: String,

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
                setDataByTyre("LF")

            }
            R.id.ivTyre2 -> {
                setDataByTyre("LR")

            }
            R.id.ivTyre3 -> {
                setDataByTyre("RF")

            }
            R.id.ivTyre4 -> {

                setDataByTyre("RR")

            }
            R.id.ivPhoneCall -> {
                if (!Common.checkCallPermission(this)) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    if (!phoneNumber.equals("")) {
                        val phone = "+91" + phoneNumber
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$phone")
                        startActivity(callIntent)
                    }
                }
            }


        }
        return false
    }

    private fun setDataByTyre(tyreType: String) {
        Common.setClearAllValues()
        TyreDetailCommonClass.tyreType = tyreType

        if (tyreType.equals("LF")) {
            TyreDetailCommonClass.vehicleMake = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreMake
            TyreDetailCommonClass.vehiclePattern = serviceDateByIdModel?.data?.get(0)?.frontLeftTyrePattern
            TyreDetailCommonClass.vehicleSize = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreSize
            TyreDetailCommonClass.vehicleMakeURL = serviceDateByIdModel?.data?.get(0)?.front_left_tyre_make_image
            TyreDetailCommonClass.manufaturingDate = serviceDateByIdModel?.data?.get(0)?.frontLeftManufacturingDate
            TyreDetailCommonClass.psiInTyreService = serviceDateByIdModel?.data?.get(0)?.frontLeftTyrePsiIn
            TyreDetailCommonClass.psiOutTyreService = serviceDateByIdModel?.data?.get(0)?.frontLeftTyrePsiOut
            TyreDetailCommonClass.weightTyreService = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreWeight
            TyreDetailCommonClass.sidewell = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreSideWall
            TyreDetailCommonClass.shoulder = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreShoulder
            TyreDetailCommonClass.treadWear = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreTreadWear
            TyreDetailCommonClass.treadDepth = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreTreadDepth
            TyreDetailCommonClass.bubble = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreBuldgeBubble
            TyreDetailCommonClass.rimDamage = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreRimDemage
            TyreDetailCommonClass.visualDetailPhotoUrl = serviceDateByIdModel?.data?.get(0)?.frontLeftTyreWheelImage

            TyreDetailCommonClass.isCompleted = true

            val issueResolvedArr: ArrayList<String>? = ArrayList()
            if (serviceDateByIdModel?.data?.get(0)?.frontLeftIssuesToBeResolved?.size!! > 0) {
                for (i in serviceDateByIdModel?.data?.get(0)?.frontLeftIssuesToBeResolved?.indices!!) {
                    issueResolvedArr?.add(serviceDateByIdModel?.data?.get(0)?.frontLeftIssuesToBeResolved?.get(i)!!)
                }
            }

            TyreDetailCommonClass.issueResolvedArr = issueResolvedArr
            var intent = Intent(this, CompletedVisualDetailActivity::class.java)
            intent.putExtra("title", "Detail - LF")
            startActivity(intent)
        } else if (tyreType.equals("LR")) {
            TyreDetailCommonClass.vehicleMake = serviceDateByIdModel?.data?.get(0)?.backLeftTyreMake
            TyreDetailCommonClass.vehiclePattern = serviceDateByIdModel?.data?.get(0)?.backLeftTyrePattern
            TyreDetailCommonClass.vehicleSize = serviceDateByIdModel?.data?.get(0)?.backLeftTyreSize
            TyreDetailCommonClass.vehicleMakeURL = serviceDateByIdModel?.data?.get(0)?.back_left_tyre_make_image
            TyreDetailCommonClass.manufaturingDate = serviceDateByIdModel?.data?.get(0)?.backLeftManufacturingDate
            TyreDetailCommonClass.psiInTyreService = serviceDateByIdModel?.data?.get(0)?.backLeftTyrePsiIn
            TyreDetailCommonClass.psiOutTyreService = serviceDateByIdModel?.data?.get(0)?.backLeftTyrePsiOut
            TyreDetailCommonClass.weightTyreService = serviceDateByIdModel?.data?.get(0)?.backLeftTyreWeight
            TyreDetailCommonClass.sidewell = serviceDateByIdModel?.data?.get(0)?.backLeftTyreSideWall
            TyreDetailCommonClass.shoulder = serviceDateByIdModel?.data?.get(0)?.backLeftTyreShoulder
            TyreDetailCommonClass.treadWear = serviceDateByIdModel?.data?.get(0)?.backLeftTyreTreadWear
            TyreDetailCommonClass.treadDepth = serviceDateByIdModel?.data?.get(0)?.backLeftTyreTreadDepth
            TyreDetailCommonClass.bubble = serviceDateByIdModel?.data?.get(0)?.backLeftTyreBuldgeBubble
            TyreDetailCommonClass.rimDamage = serviceDateByIdModel?.data?.get(0)?.backLeftTyreRimDemage
            TyreDetailCommonClass.visualDetailPhotoUrl = serviceDateByIdModel?.data?.get(0)?.backLeftTyreWheelImage

            TyreDetailCommonClass.isCompleted = true

            val issueResolvedArr: ArrayList<String>? = ArrayList()
            if (serviceDateByIdModel?.data?.get(0)?.backLeftIssuesToBeResolved?.size!! > 0) {
                for (i in serviceDateByIdModel?.data?.get(0)?.backLeftIssuesToBeResolved?.indices!!) {
                    issueResolvedArr?.add(serviceDateByIdModel?.data?.get(0)?.backLeftIssuesToBeResolved?.get(i)!!)
                }
            }

            TyreDetailCommonClass.issueResolvedArr = issueResolvedArr

            val intent = Intent(this, CompletedVisualDetailActivity::class.java)
            intent.putExtra("title", "Detail - LR")
            startActivity(intent)

        } else if (tyreType.equals("RF")) {
            TyreDetailCommonClass.vehicleMake = serviceDateByIdModel?.data?.get(0)?.frontRightTyreMake
            TyreDetailCommonClass.vehiclePattern = serviceDateByIdModel?.data?.get(0)?.frontRightTyrePattern
            TyreDetailCommonClass.vehicleSize = serviceDateByIdModel?.data?.get(0)?.frontRightTyreSize
            TyreDetailCommonClass.vehicleMakeURL = serviceDateByIdModel?.data?.get(0)?.front_right_tyre_make_image
            TyreDetailCommonClass.manufaturingDate = serviceDateByIdModel?.data?.get(0)?.frontRightManufacturingDate
            TyreDetailCommonClass.psiInTyreService = serviceDateByIdModel?.data?.get(0)?.frontRightTyrePsiIn
            TyreDetailCommonClass.psiOutTyreService = serviceDateByIdModel?.data?.get(0)?.frontRightTyrePsiOut
            TyreDetailCommonClass.weightTyreService = serviceDateByIdModel?.data?.get(0)?.front_right_tyre_weight
            TyreDetailCommonClass.sidewell = serviceDateByIdModel?.data?.get(0)?.frontRightTyreSideWall
            TyreDetailCommonClass.shoulder = serviceDateByIdModel?.data?.get(0)?.frontRightTyreShoulder
            TyreDetailCommonClass.treadWear = serviceDateByIdModel?.data?.get(0)?.frontRightTyreTreadWear
            TyreDetailCommonClass.treadDepth = serviceDateByIdModel?.data?.get(0)?.frontRightTyreTreadDepth
            TyreDetailCommonClass.bubble = serviceDateByIdModel?.data?.get(0)?.frontRightTyreBuldgeBubble
            TyreDetailCommonClass.rimDamage = serviceDateByIdModel?.data?.get(0)?.frontRightTyreRimDemage
            TyreDetailCommonClass.visualDetailPhotoUrl = serviceDateByIdModel?.data?.get(0)?.frontRightTyreWheelImage

            TyreDetailCommonClass.isCompleted = true

            val issueResolvedArr: ArrayList<String>? = ArrayList()
            if (serviceDateByIdModel?.data?.get(0)?.frontRightIssuesToBeResolved?.size!! > 0) {
                for (i in serviceDateByIdModel?.data?.get(0)?.frontRightIssuesToBeResolved?.indices!!) {
                    issueResolvedArr?.add(serviceDateByIdModel?.data?.get(0)?.frontRightIssuesToBeResolved?.get(i)!!)
                }
            }

            TyreDetailCommonClass.issueResolvedArr = issueResolvedArr
            val intent = Intent(this, CompletedVisualDetailActivity::class.java)
            intent.putExtra("title", "Detail - RF")
            startActivity(intent)

        } else if (tyreType.equals("RR")) {
            TyreDetailCommonClass.vehicleMake = serviceDateByIdModel?.data?.get(0)?.backRightTyreMake
            TyreDetailCommonClass.vehiclePattern = serviceDateByIdModel?.data?.get(0)?.backRightTyrePattern
            TyreDetailCommonClass.vehicleSize = serviceDateByIdModel?.data?.get(0)?.backRightTyreSize
            TyreDetailCommonClass.vehicleMakeURL = serviceDateByIdModel?.data?.get(0)?.back_right_tyre_make_image
            TyreDetailCommonClass.manufaturingDate = serviceDateByIdModel?.data?.get(0)?.backRightManufacturingDate
            TyreDetailCommonClass.psiInTyreService = serviceDateByIdModel?.data?.get(0)?.backRightTyrePsiIn
            TyreDetailCommonClass.psiOutTyreService = serviceDateByIdModel?.data?.get(0)?.backRightTyrePsiOut
            TyreDetailCommonClass.weightTyreService = serviceDateByIdModel?.data?.get(0)?.backRightTyreWeight
            TyreDetailCommonClass.sidewell = serviceDateByIdModel?.data?.get(0)?.backRightTyreSideWall
            TyreDetailCommonClass.shoulder = serviceDateByIdModel?.data?.get(0)?.backRightTyreShoulder
            TyreDetailCommonClass.treadWear = serviceDateByIdModel?.data?.get(0)?.backRightTyreTreadWear
            TyreDetailCommonClass.treadDepth = serviceDateByIdModel?.data?.get(0)?.backRightTyreTreadDepth
            TyreDetailCommonClass.bubble = serviceDateByIdModel?.data?.get(0)?.backRightTyreBuldgeBubble
            TyreDetailCommonClass.rimDamage = serviceDateByIdModel?.data?.get(0)?.backRightTyreRimDemage
            TyreDetailCommonClass.visualDetailPhotoUrl = serviceDateByIdModel?.data?.get(0)?.backRightTyreWheelImage

            TyreDetailCommonClass.isCompleted = true

            val issueResolvedArr: ArrayList<String>? = ArrayList()
            if (serviceDateByIdModel?.data?.get(0)?.backRightIssuesToBeResolved?.size!! > 0) {
                for (i in serviceDateByIdModel?.data?.get(0)?.backRightIssuesToBeResolved?.indices!!) {
                    issueResolvedArr?.add(serviceDateByIdModel?.data?.get(0)?.backRightIssuesToBeResolved?.get(i)!!)
                }
            }

            TyreDetailCommonClass.issueResolvedArr = issueResolvedArr

            val intent = Intent(this, CompletedVisualDetailActivity::class.java)
            intent.putExtra("title", "Detail - RR")
            startActivity(intent)
        }
    }

    private fun showImage(posterUrl: String?) {
        val builder = AlertDialog.Builder(this@CompletedServiceDetailActivity).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        builder.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

        val root = LayoutInflater.from(this@CompletedServiceDetailActivity)
            .inflate(R.layout.dialogue_image, null)

        val tvTitleRemarks =
            root.findViewById<TextView>(R.id.tvTitleRemarks)
        val imgPoster =
            root.findViewById<ImageView>(R.id.imgPoster)

        Glide.with(this@CompletedServiceDetailActivity)
            .load(posterUrl)
            .override(1600, 1600)
            .placeholder(R.drawable.placeholder)
            .into(imgPoster)

        tvTitleRemarks?.text = "View Car Image"

        val imgClose = root.findViewById<ImageView>(R.id.imgClose)

        imgClose.setOnClickListener { builder.dismiss() }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }
}