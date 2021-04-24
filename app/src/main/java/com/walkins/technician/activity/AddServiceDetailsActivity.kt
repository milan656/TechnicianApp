package com.walkins.technician.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.walkins.technician.DB.*
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.*
import com.walkins.technician.custom.BoldButton
import com.walkins.technician.datepicker.dialog.SingleDateAndTimePickerDialogDueDate
import com.walkins.technician.model.login.IssueResolveModel
import com.walkins.technician.viewmodel.ServiceViewModel
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddServiceDetailsActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter,
    View.OnTouchListener {
    private lateinit var prefManager: PrefManager
    private lateinit var mDb: DBClass
    var pendingArr: ArrayList<String>? = null
    var dialog: BottomSheetDialog? = null

    var imagePickerDialog: BottomSheetDialog? = null
    var serviceViewModel: ServiceViewModel? = null
    private var ivInfoAddService: ImageView? = null
    private var ivAddServices: ImageView? = null
    private var ivAddTyreConfig: ImageView? = null
    private var ivAddTechnicalSuggestion: ImageView? = null

    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null
    private var llServiceExpanded: LinearLayout? = null
    private var llTyreConfigExpanded: LinearLayout? = null
    private var llTechnicalSuggestionExpanded: LinearLayout? = null

    private var tvTechnicalSuggetion: TextView? = null
    private var tvTyreConfig: TextView? = null
    private var tvServices: TextView? = null

    private var cardservice: LinearLayout? = null
    private var cardtyreConfig: LinearLayout? = null
    private var cardtechinicalSuggestion: LinearLayout? = null

    private var selectImage1 = false

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
    private var selectedSuggestionArr: ArrayList<String>? = ArrayList()
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var suggestionArray: ArrayList<IssueResolveModel>? = ArrayList()
    var reasonArray = arrayListOf(
        "The car was unavailable",
        "Need more time to resolve",
        "The customer request a delay",
        "Need more time to resolve",
        "The customer request a delay"
    )
    private var reasonArrayList: ArrayList<IssueResolveModel>? = ArrayList()


    private var tyreSuggestionAdapter: TyreSuggestionAdpater? = null

    private var relCarPhotoAdd2: RelativeLayout? = null
    private var relCarPhotoAdd1: RelativeLayout? = null

    private var ivTyre1: ImageView? = null
    private var ivTyre2: ImageView? = null
    private var ivTyre3: ImageView? = null
    private var ivTyre4: ImageView? = null
    private var ivPickedImage: ImageView? = null
    private var ivPickedImage1: ImageView? = null
    private var ivEditImg1: ImageView? = null
    private var ivEditImg2: ImageView? = null
    private var tvAddPhoto1: TextView? = null
    private var tvAddPhoto2: TextView? = null
    private var tvCarphoto1: TextView? = null
    private var tvCarphoto2: TextView? = null

    private var tvSkipService: TextView? = null
    private var tvTyreAddInfo: TextView? = null

    private var ivtyreLeftFront: ImageView? = null
    private var ivtyreLeftRear: ImageView? = null
    private var ivTyreRightFront: ImageView? = null
    private var ivTyreRightRear: ImageView? = null

    private var ivInfoImgLF: ImageView? = null
    private var ivInfoImgRF: ImageView? = null
    private var ivInfoImgLR: ImageView? = null
    private var ivInfoImgRR: ImageView? = null
    private var btnSubmitAndComplete: TextView? = null

    private var ivPhoneCall: ImageView? = null
    private var ivDueDate: ImageView? = null
    private var tvNextServiceDueDate: TextView? = null
    private var edtMoreSuggestion: EditText? = null

    var simpleDateFormat: SimpleDateFormat? = null
    private var selectedDate: String? = null
    var singleBuilder: SingleDateAndTimePickerDialogDueDate.Builder? = null
    var lltransparent: LinearLayout? = null
    var llTyreRotation: LinearLayout? = null
    var llWheelbalancing: LinearLayout? = null
    var llNitrogenRefil: LinearLayout? = null
    var llNitrogenTopup: LinearLayout? = null

    private var radioLF_LR: RadioButton? = null
    private var radioLF_RR: RadioButton? = null
    private var radioRR_RF: RadioButton? = null
    private var radioRR_LF: RadioButton? = null
    private var radioLR_LF: RadioButton? = null
    private var radioLR_RF: RadioButton? = null
    private var radioRF_LR: RadioButton? = null
    private var radioRF_RR: RadioButton? = null

    // image picker code
    val REQUEST_IMAGE = 100
    val REQUEST_PERMISSION = 200
    private var imageFilePath = ""
    private var IMAGE_PICK_CODE = 1010;
    private var PERMISSION_CODE = 1011;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_details)
        mDb = DBClass.getInstance(this)
        prefManager = PrefManager(this)
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)

        requestPermissionForImage()
        init()


    }

    fun getStoredObjects() {
        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
            try {
                var jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)
                if (jsonLF.has(TyreKey.isCompleted)) {
                    if (jsonLF.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.LFCompleted =
                            jsonLF.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }

                if (TyreConfigClass.LFCompleted) {
                    ivTyre1?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                    ivtyreLeftFront?.visibility = View.VISIBLE
                    ivInfoImgLF?.visibility = View.GONE

                    try {
                        Glide.with(this)
                            .load(jsonLF.get(TyreKey.vehicleMakeURL))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .into(ivtyreLeftFront!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if ((jsonLF.get(TyreKey.vehicleMake) != null &&
                            !jsonLF.get(TyreKey.vehicleMake)?.asString.equals("") &&
                            !jsonLF.get(TyreKey.vehicleMakeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.LFVehicleMake = true
                }
                if ((jsonLF.get(TyreKey.vehiclePattern) != null && jsonLF.get(TyreKey.vehiclePatternId) != null &&
                            !jsonLF.get(TyreKey.vehiclePattern)?.asString.equals("") &&
                            !jsonLF.get(TyreKey.vehiclePatternId)?.asString.equals(""))
                ) {
                    TyreConfigClass.LFVehiclePattern = true
                }
                if ((jsonLF.get(TyreKey.vehicleSize) != null && jsonLF.get(TyreKey.vehicleSizeId) != null &&
                            !jsonLF.get(TyreKey.vehicleSize)?.asString.equals("") &&
                            !jsonLF.get(TyreKey.vehicleSizeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.LFVehicleSize = true
                }
                if ((jsonLF.get(TyreKey.manufaturingDate) != null &&
                            !jsonLF.get(TyreKey.manufaturingDate)?.asString.equals("") &&
                            (jsonLF.get(TyreKey.sidewell) != null && !jsonLF.get(TyreKey.sidewell)?.asString.equals(
                                ""
                            )) &&
                            (jsonLF.get(TyreKey.shoulder) != null && !jsonLF.get(TyreKey.shoulder)?.asString.equals(
                                ""
                            )) &&
                            (jsonLF.get(TyreKey.treadWear) != null && !jsonLF.get(TyreKey.treadWear)?.asString.equals(
                                ""
                            )) &&
                            (jsonLF.get(TyreKey.treadDepth) != null && !jsonLF.get(TyreKey.treadDepth)?.asString.equals(
                                ""
                            )) &&
                            (jsonLF.get(TyreKey.rimDamage) != null && !jsonLF.get(TyreKey.rimDamage)?.asString.equals(
                                ""
                            )) &&
                            (jsonLF.get(TyreKey.bubble) != null && !jsonLF.get(TyreKey.bubble)?.asString.equals(
                                ""
                            )))
                ) {
                    TyreConfigClass.LFVehicleVisualDetail = true
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.TyreRFObject)
            try {
                var jsonRF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjects", "" + jsonRF)
                if (jsonRF.has(TyreKey.isCompleted)) {
                    if (jsonRF.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.RFCompleted =
                            jsonRF.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }
                if (TyreConfigClass.RFCompleted) {
                    ivTyre3?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                    ivTyreRightFront?.visibility = View.VISIBLE
                    ivInfoImgRF?.visibility = View.GONE
                }
                if ((jsonRF.get(TyreKey.vehicleMake) != null &&
                            !jsonRF.get(TyreKey.vehicleMake)?.asString.equals("") &&
                            !jsonRF.get(TyreKey.vehicleMakeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RFVehicleMake = true
                    Log.e("rfstatus", "" + TyreConfigClass.RFVehicleMake)
                }
                if ((jsonRF.get(TyreKey.vehiclePattern) != null && jsonRF.get(TyreKey.vehiclePatternId) != null &&
                            !jsonRF.get(TyreKey.vehiclePattern)?.asString.equals("") &&
                            !jsonRF.get(TyreKey.vehiclePatternId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RFVehiclePattern = true
                    Log.e("rfstatus0", "" + TyreConfigClass.RFVehiclePattern)
                }
                if ((jsonRF.get(TyreKey.vehicleSize) != null && jsonRF.get(TyreKey.vehicleSizeId) != null &&
                            !jsonRF.get(TyreKey.vehicleSize)?.asString.equals("") &&
                            !jsonRF.get(TyreKey.vehicleSizeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RFVehicleSize = true
                    Log.e("rfstatus2", "" + TyreConfigClass.RFVehicleSize)
                }
                if ((jsonRF.get(TyreKey.manufaturingDate) != null &&
                            !jsonRF.get(TyreKey.manufaturingDate)?.asString.equals("") &&
                            (jsonRF.get(TyreKey.sidewell) != null && !jsonRF.get(TyreKey.sidewell)?.asString.equals(
                                ""
                            )) &&
                            (jsonRF.get(TyreKey.shoulder) != null && !jsonRF.get(TyreKey.shoulder)?.asString.equals(
                                ""
                            )) &&
                            (jsonRF.get(TyreKey.treadWear) != null && !jsonRF.get(TyreKey.treadWear)?.asString.equals(
                                ""
                            )) &&
                            (jsonRF.get(TyreKey.treadDepth) != null && !jsonRF.get(TyreKey.treadDepth)?.asString.equals(
                                ""
                            )) &&
                            (jsonRF.get(TyreKey.rimDamage) != null && !jsonRF.get(TyreKey.rimDamage)?.asString.equals(
                                ""
                            )) &&
                            (jsonRF.get(TyreKey.bubble) != null && !jsonRF.get(TyreKey.bubble)?.asString.equals(
                                ""
                            )))
                ) {
                    TyreConfigClass.RFVehicleVisualDetail = true
                    Log.e("rfstatus4", "" + TyreConfigClass.RFVehicleVisualDetail)
                }

                try {
                    Glide.with(this)
                        .load(jsonRF.get(TyreKey.vehicleMakeURL))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .into(ivTyreRightFront!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
            !prefManager?.getValue(TyreConfigClass.TyreLRObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.TyreLRObject)
            try {
                var jsonLR: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjects", "" + jsonLR)
                if (jsonLR.has(TyreKey.isCompleted)) {
                    if (jsonLR.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.LRCompleted =
                            jsonLR.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }
                if (TyreConfigClass.LRCompleted) {

                    ivTyre2?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                    ivtyreLeftRear?.visibility = View.VISIBLE
                    ivInfoImgLR?.visibility = View.GONE
                }

                if ((jsonLR.get(TyreKey.vehicleMake) != null && jsonLR.get(TyreKey.vehicleMakeId) != null &&
                            !jsonLR.get(TyreKey.vehicleMake)?.asString.equals("") &&
                            !jsonLR.get(TyreKey.vehicleMakeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.LRVehicleMake = true
                }
                if ((jsonLR.get(TyreKey.vehiclePattern) != null && jsonLR.get(TyreKey.vehiclePatternId) != null &&
                            !jsonLR.get(TyreKey.vehiclePattern)?.asString.equals("") &&
                            !jsonLR.get(TyreKey.vehiclePatternId)?.asString.equals(""))
                ) {
                    TyreConfigClass.LRVehiclePattern = true
                }
                if ((jsonLR.get(TyreKey.vehicleSize) != null && jsonLR.get(TyreKey.vehicleSizeId) != null &&
                            !jsonLR.get(TyreKey.vehicleSize)?.asString.equals("") &&
                            !jsonLR.get(TyreKey.vehicleSizeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.LRVehicleSize = true
                }
                if ((jsonLR.get(TyreKey.manufaturingDate) != null &&
                            !jsonLR.get(TyreKey.manufaturingDate)?.asString.equals("") &&
                            (jsonLR.get(TyreKey.sidewell) != null && !jsonLR.get(TyreKey.sidewell)?.asString.equals(
                                ""
                            )) &&
                            (jsonLR.get(TyreKey.shoulder) != null && !jsonLR.get(TyreKey.shoulder)?.asString.equals(
                                ""
                            )) &&
                            (jsonLR.get(TyreKey.treadWear) != null && !jsonLR.get(TyreKey.treadWear)?.asString.equals(
                                ""
                            )) &&
                            (jsonLR.get(TyreKey.treadDepth) != null && !jsonLR.get(TyreKey.treadDepth)?.asString.equals(
                                ""
                            )) &&
                            (jsonLR.get(TyreKey.rimDamage) != null && !jsonLR.get(TyreKey.rimDamage)?.asString.equals(
                                ""
                            )) &&
                            (jsonLR.get(TyreKey.bubble) != null && !jsonLR.get(TyreKey.bubble)?.asString.equals(
                                ""
                            )))
                ) {
                    TyreConfigClass.LRVehicleVisualDetail = true
                }

                try {
                    Glide.with(this)
                        .load(jsonLR.get(TyreKey.vehicleMakeURL))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .into(ivtyreLeftRear!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.TyreRRObject)
            try {
                var jsonRR: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjects", "" + jsonRR)
                if (jsonRR.has(TyreKey.isCompleted)) {
                    if (jsonRR.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.RRCompleted =
                            jsonRR.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }
                if (TyreConfigClass.RRCompleted) {
                    ivTyre4?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                    ivTyreRightRear?.visibility = View.VISIBLE
                    ivInfoImgRR?.visibility = View.GONE
                }
                if ((jsonRR.get(TyreKey.vehicleMake) != null &&
                            !jsonRR.get(TyreKey.vehicleMake)?.asString.equals("") &&
                            !jsonRR.get(TyreKey.vehicleMakeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RRVehicleMake = true
                }
                if ((jsonRR.get(TyreKey.vehiclePattern) != null && jsonRR.get(TyreKey.vehiclePatternId) != null &&
                            !jsonRR.get(TyreKey.vehiclePattern)?.asString.equals("") &&
                            !jsonRR.get(TyreKey.vehiclePatternId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RRVehiclePattern = true
                }
                if ((jsonRR.get(TyreKey.vehicleSize) != null && jsonRR.get(TyreKey.vehicleSizeId) != null &&
                            !jsonRR.get(TyreKey.vehicleSize)?.asString.equals("") &&
                            !jsonRR.get(TyreKey.vehicleSizeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RRVehicleSize = true
                }
                if ((jsonRR.get(TyreKey.manufaturingDate) != null &&
                            !jsonRR.get(TyreKey.manufaturingDate)?.asString.equals("") &&
                            (jsonRR.get(TyreKey.sidewell) != null && !jsonRR.get(TyreKey.sidewell)?.asString.equals(
                                ""
                            )) &&
                            (jsonRR.get(TyreKey.shoulder) != null && !jsonRR.get(TyreKey.shoulder)?.asString.equals(
                                ""
                            )) &&
                            (jsonRR.get(TyreKey.treadWear) != null && !jsonRR.get(TyreKey.treadWear)?.asString.equals(
                                ""
                            )) &&
                            (jsonRR.get(TyreKey.treadDepth) != null && !jsonRR.get(TyreKey.treadDepth)?.asString.equals(
                                ""
                            )) &&
                            (jsonRR.get(TyreKey.rimDamage) != null && !jsonRR.get(TyreKey.rimDamage)?.asString.equals(
                                ""
                            )) &&
                            (jsonRR.get(TyreKey.bubble) != null && !jsonRR.get(TyreKey.bubble)?.asString.equals(
                                ""
                            )))
                ) {
                    TyreConfigClass.RRVehicleVisualDetail = true
                    Log.e("visualtrue4", "" + TyreConfigClass.RRVehicleVisualDetail)
                }
                try {
                    Glide.with(this)
                        .load(jsonRR.get(TyreKey.vehicleMakeURL))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .into(ivTyreRightRear!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (prefManager?.getValue(TyreConfigClass.serviceDetailData) != null &&
            !prefManager.getValue(TyreConfigClass.serviceDetailData).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.serviceDetailData)

            try {
                var jsonService: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("serviceObj", "" + jsonService)

                if (jsonService.get(TyreKey.nitrogenTopup) != null && jsonService.get(TyreKey.nitrogenTopup)?.asString
                        .equals("true")
                ) {
                    Log.e("serviceObj", "" + true + "topup")
                    chkNitrogenTopup?.isChecked = true
                }
                if (jsonService.get(TyreKey.nitrogenRefil) != null && jsonService.get(TyreKey.nitrogenRefil)?.asString
                        .equals("true")
                ) {
                    Log.e("serviceObj", "" + true + "refil")
                    chkNitrogenRefill?.isChecked = true
                }
                if (jsonService.get(TyreKey.tyreRotation) != null && jsonService.get(TyreKey.tyreRotation)?.asString
                        .equals("true")
                ) {
                    Log.e("serviceObj", "" + true + "rotation")
                    chkTyreRotation?.isChecked = true
                }
                if (jsonService.get(TyreKey.wheelBalancing) != null && jsonService.get(TyreKey.wheelBalancing)?.asString
                        .equals("true")
                ) {
                    Log.e("serviceObj", "" + true + "wheel")
                    chkWheelBalacing?.isChecked = true
                }
                if (jsonService.get(TyreKey.nextDueDate) != null && !jsonService.get(TyreKey.nextDueDate)?.asString
                        .equals("")
                ) {
                    tvNextServiceDueDate?.setText(jsonService.get(TyreKey.nextDueDate)?.asString)
                }
                if (jsonService.get(TyreKey.moreSuggestion) != null && !jsonService.get(TyreKey.moreSuggestion)?.asString
                        .equals("")
                ) {
                    edtMoreSuggestion?.setText(jsonService.get(TyreKey.moreSuggestion)?.asString)
                }

                if (jsonService.get(TyreKey.radioGroupLF) != null && !jsonService.get(TyreKey.radioGroupLF).asString.equals(
                        ""
                    )
                ) {
                    if (jsonService.get(TyreKey.radioGroupLF).asString.equals("RRLF")) {
                        radioLF_RR?.isChecked = true
                    } else {
                        radioLF_LR?.isChecked = true
                    }
                }
                if (jsonService.get(TyreKey.radioGroupLR) != null && !jsonService.get(TyreKey.radioGroupLR).asString.equals(
                        ""
                    )
                ) {
                    if (jsonService.get(TyreKey.radioGroupLR).asString.equals("RFLR")) {
                        radioLR_RF?.isChecked = true
                    } else {
                        radioLR_LF?.isChecked = true
                    }
                }
                if (jsonService.get(TyreKey.radioGroupRF) != null && !jsonService.get(TyreKey.radioGroupRF).asString.equals(
                        ""
                    )
                ) {
                    if (jsonService.get(TyreKey.radioGroupRF).asString.equals("RRRF")) {
                        radioRF_RR?.isChecked = true
                    } else {
                        radioRF_LR?.isChecked = true
                    }
                }
                if (jsonService.get(TyreKey.radioGroupRR) != null && !jsonService.get(TyreKey.radioGroupRR).asString.equals(
                        ""
                    )
                ) {
                    if (jsonService.get(TyreKey.radioGroupRR).asString.equals("LFRR")) {
                        radioRR_LF?.isChecked = true
                    } else {
                        radioRR_RF?.isChecked = true
                    }
                }

                if (jsonService.get(TyreKey.technicalSuggestionArr) != null) {

                    var arr = jsonService.get(TyreKey.technicalSuggestionArr)?.asJsonArray
                    Log.e("getvalues", "" + arr)
                    val gson = Gson()
                    val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                    val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                    Log.e("getvalues", "" + arrlist)
                    for (i in suggestionArray?.indices!!) {

                        for (j in arrlist.indices) {

                            if (suggestionArray?.get(i)?.issueName.equals(arrlist.get(j))) {
                                suggestionArray?.get(i)?.isSelected = true
                                selectedSuggestionArr?.add(suggestionArray?.get(i)?.issueName!!)
                            }
                        }
                    }

                    tyreSuggestionAdapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun requestPermissionForImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION
            )
        }
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
        tvSkipService = findViewById(R.id.tvSkipService)
        tvTyreAddInfo = findViewById(R.id.tvTyreAddInfo)
        tvTechnicalSuggetion = findViewById(R.id.tvTechnicalSuggetion)
        tvServices = findViewById(R.id.tvServices)
        tvTyreConfig = findViewById(R.id.tvTyreConfig)
        cardservice = findViewById(R.id.cardservice)
        cardtyreConfig = findViewById(R.id.cardtyreConfig)
        cardtechinicalSuggestion = findViewById(R.id.cardtechinicalSuggestion)
        ivDueDate = findViewById(R.id.ivDueDate)
        tvNextServiceDueDate = findViewById(R.id.tvNextServiceDueDate)
        edtMoreSuggestion = findViewById(R.id.edtMoreSuggestion)
        btnSubmitAndComplete = findViewById(R.id.btnSubmitAndComplete)
        lltransparent = findViewById(R.id.lltransparent)
        llNitrogenRefil = findViewById(R.id.llNitrogenRefil)
        llNitrogenTopup = findViewById(R.id.llNitrogenTopup)
        llWheelbalancing = findViewById(R.id.llWheelbalancing)
        llTyreRotation = findViewById(R.id.llTyreRotation)

        ivInfoImgLF = findViewById(R.id.ivInfoImgLF)
        ivInfoImgLR = findViewById(R.id.ivInfoImgLR)
        ivInfoImgRF = findViewById(R.id.ivInfoImgRF)
        ivInfoImgRR = findViewById(R.id.ivInfoImgRR)

        radioLF_LR = findViewById(R.id.radioLF_LR)
        radioLF_RR = findViewById(R.id.radioLF_RR)
        radioRR_RF = findViewById(R.id.radioRR_RF)
        radioRR_LF = findViewById(R.id.radioRR_LF)
        radioLR_LF = findViewById(R.id.radioLR_LF)
        radioLR_RF = findViewById(R.id.radioLR_RF)
        radioRF_LR = findViewById(R.id.radioRF_LR)
        radioRF_RR = findViewById(R.id.radioRF_RR)

        ivPhoneCall = findViewById(R.id.ivPhoneCall)

        ivInfoImgRR?.setOnClickListener(this)
        ivInfoImgRF?.setOnClickListener(this)
        ivInfoImgLR?.setOnClickListener(this)
        ivInfoImgLF?.setOnClickListener(this)

        llNitrogenTopup?.setOnClickListener(this)
        llNitrogenRefil?.setOnClickListener(this)
        llTyreRotation?.setOnClickListener(this)
        llWheelbalancing?.setOnClickListener(this)

        ivDueDate?.setOnClickListener(this)
        btnSubmitAndComplete?.setOnClickListener(this)

        ivTyre1 = findViewById(R.id.ivTyre1)
        ivTyre2 = findViewById(R.id.ivTyre2)
        ivTyre3 = findViewById(R.id.ivTyre3)
        ivTyre4 = findViewById(R.id.ivTyre4)
        ivPickedImage = findViewById(R.id.ivPickedImage)
        ivPickedImage1 = findViewById(R.id.ivPickedImage1)
        ivEditImg1 = findViewById(R.id.ivEditImg1)
        ivEditImg2 = findViewById(R.id.ivEditImg2)
        tvCarphoto1 = findViewById(R.id.tvCarphoto1)
        tvCarphoto2 = findViewById(R.id.tvCarphoto2)
        tvAddPhoto1 = findViewById(R.id.tvAddPhoto1)
        tvAddPhoto2 = findViewById(R.id.tvAddPhoto2)

        ivPickedImage?.visibility = View.GONE
        ivPickedImage1?.visibility = View.GONE
        ivEditImg1?.visibility = View.GONE
        ivEditImg2?.visibility = View.GONE

        ivtyreLeftFront = findViewById(R.id.ivtyreLeftFront)
        ivtyreLeftRear = findViewById(R.id.ivtyreLeftRear)
        ivTyreRightFront = findViewById(R.id.ivTyreRightFront)
        ivTyreRightRear = findViewById(R.id.ivTyreRightRear)

        chkNitrogenRefill = findViewById(R.id.chkNitrogenRefill)
        chkNitrogenTopup = findViewById(R.id.chkNitrogenTopup)
        chkTyreRotation = findViewById(R.id.chkTyreRotation)
        chkWheelBalacing = findViewById(R.id.chkWheelBalacing)

        relCarPhotoAdd2 = findViewById(R.id.relCarPhotoAdd2)
        relCarPhotoAdd1 = findViewById(R.id.relCarPhotoAdd1)

        tvTitle?.text = "Add Service Details"
        ivInfoAddService?.setOnClickListener(this)

        relCarPhotoAdd1?.setOnClickListener(this)
        relCarPhotoAdd2?.setOnClickListener(this)
        tvSkipService?.setOnClickListener(this)
        ivAddServices?.setOnClickListener(this)
        ivAddTyreConfig?.setOnClickListener(this)
        ivAddTechnicalSuggestion?.setOnClickListener(this)
        ivEditImg1?.setOnClickListener(this)
        ivEditImg2?.setOnClickListener(this)

        cardtyreConfig?.setOnTouchListener(this)
        cardtechinicalSuggestion?.setOnTouchListener(this)
        cardservice?.setOnTouchListener(this)

        ivTyre1?.setOnTouchListener(this)
        ivTyre2?.setOnTouchListener(this)
        ivTyre3?.setOnTouchListener(this)
        ivTyre4?.setOnTouchListener(this)
        llTyreConfigExpanded?.setOnTouchListener(this)
        ivPhoneCall?.setOnTouchListener(this)

        for (i in suggestionArr.indices) {
            suggestionArray?.add(IssueResolveModel(suggestionArr.get(i) + " " + i, false))
        }
        for (i in reasonArray.indices) {
            reasonArrayList?.add(IssueResolveModel(reasonArray.get(i), false))
        }

        tyreSuggestionAdapter = TyreSuggestionAdpater(suggestionArray!!, this, this, false)
        tyreSuggestionAdapter?.onclick = this
        suggestionsRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        suggestionsRecycView?.adapter = tyreSuggestionAdapter

        ivBack?.setOnClickListener(this)

        getStoredObjects()

        checkChangeListener()

        edtMoreSuggestion?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (s != null && s?.toString().length > 0) {
                    prefManager?.setValue(
                        TyreKey.moreSuggestion,
                        edtMoreSuggestion?.text?.toString()
                    )
                    checkSubmitBtn()
                }
            }

        })

        tvNextServiceDueDate?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s?.toString().length > 2) {
                    TyreConfigClass.nextDueDate = tvNextServiceDueDate?.text?.toString()!!
                    prefManager?.setValue(
                        TyreKey.nextDueDate,
                        tvNextServiceDueDate?.text?.toString()
                    )
                    checkSubmitBtn()
                }
            }

        })

    }

    private fun checkChangeListener() {
        chkWheelBalacing?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                chkWheelBalacing?.isChecked = isChecked
                showHideUpdatedPlacement()

            }

        })
        chkTyreRotation?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                chkTyreRotation?.isChecked = isChecked
                showHideUpdatedPlacement()
            }

        })
        chkNitrogenTopup?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                chkNitrogenTopup?.isChecked = isChecked
                showHideUpdatedPlacement()
            }

        })
        chkNitrogenRefill?.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                Log.e("chkbox", "" + isChecked + " " + chkNitrogenRefill?.isChecked)
                chkNitrogenRefill?.isChecked = isChecked
                showHideUpdatedPlacement()
            }

        })
    }

    fun showHideUpdatedPlacement() {
        if (chkTyreRotation?.isChecked!!
        ) {
            if (llUpdatedPlacement?.visibility == View.GONE) {
                Common.expand(llUpdatedPlacement!!)
            }
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
                showBottomSheetdialogNormal(
                    Common.commonPhotoChooseArr,
                    "Address Details",
                    this,
                    Common.btn_filled,
                    false, "Palm Spring,", "Vastrapur Road,", "Opposite Siddhivinayak mandir,",
                    "Ahmedabad - 123456"
                )
            }
            R.id.ivBack -> {


                onBackPressed()
            }
            R.id.relCarPhotoAdd1, R.id.ivEditImg1 -> {
                selectImage1 = true
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }
            R.id.relCarPhotoAdd2, R.id.ivEditImg2 -> {
                selectImage1 = false
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }

            R.id.tvSkipService -> {
                openSkipServiceDialogue()
            }

            R.id.ivInfoImgLR -> {
                if (!checkService()) {

                    return
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.LRVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.LRVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.LRVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.LRVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                TyreConfigClass.pendingTyre = "LR"

                Log.e("pendingArrlr", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "LR Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )
            }
            R.id.ivInfoImgLF -> {
                if (!checkService()) {
                    return
                }
                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.LFVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.LFVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.LFVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.LFVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                TyreConfigClass.pendingTyre = "LF"
                Log.e("pendingArrlf", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "LF Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )

            }
            R.id.ivInfoImgRR -> {
                if (!checkService()) {
                    return
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.RRVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.RRVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.RRVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.RRVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                TyreConfigClass.pendingTyre = "RR"
                Log.e("pendingArrRR", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "RR Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )
            }
            R.id.ivInfoImgRF -> {
                if (!checkService()) {
                    return
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.RFVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.RFVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.RFVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.RFVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                TyreConfigClass.pendingTyre = "RF"
                Log.e("pendingArrRF", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "RF Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )
            }

            R.id.ivAddServices -> {
                if (llServiceExpanded?.visibility == View.VISIBLE) {
                    Common.collapse(llServiceExpanded!!)
                    Common.collapse(llUpdatedPlacement!!)
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = false
                    ivAddServices?.setImageResource(R.mipmap.ic_add_icon)
                } else {
                    showHideUpdatedPlacement()
                    ivAddServices?.setImageResource(R.mipmap.ic_minus_icon)
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = false
                    Common.expand(llServiceExpanded!!)

                    if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTyreConfigExpanded!!)
                        ivAddTyreConfig?.setImageResource(R.mipmap.ic_add_icon)
                    }
                    if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTechnicalSuggestionExpanded!!)
                        ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_add_icon)
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
                } else {
                    ivAddTyreConfig?.setImageResource(R.mipmap.ic_minus_icon)
                    tvTyreConfig?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTyreConfig?.isAllCaps = false
                    Common.expand(llTyreConfigExpanded!!)
                    if (llServiceExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llServiceExpanded!!)
                        Common.collapse(llUpdatedPlacement!!)
                        ivAddServices?.setImageResource(R.mipmap.ic_add_icon)
                    }
                    if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTechnicalSuggestionExpanded!!)
                        ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_add_icon)
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
                } else {
                    ivAddTechnicalSuggestion?.setImageResource(R.mipmap.ic_minus_icon)
                    tvTechnicalSuggetion?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTechnicalSuggetion?.isAllCaps = false

                    Common.expand(llTechnicalSuggestionExpanded!!)
                    if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTyreConfigExpanded!!)
                        ivAddTyreConfig?.setImageResource(R.mipmap.ic_add_icon)
                    }
                    if (llServiceExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llServiceExpanded!!)
                        Common.collapse(llUpdatedPlacement!!)
                        ivAddServices?.setImageResource(R.mipmap.ic_add_icon)
                    }
                    tvServices?.isAllCaps = false
                    tvTyreAddInfo?.isAllCaps = false
                }
            }
            R.id.ivDueDate -> {
                openDatePicker()
            }
            R.id.btnSubmitAndComplete -> {

                addServiceApiCall()
            }
            R.id.llNitrogenTopup -> {
                if (chkNitrogenTopup?.isChecked!!) {

                    chkNitrogenTopup?.isChecked = false
                } else {
                    chkNitrogenTopup?.isChecked = true
                }
            }
            R.id.llNitrogenRefil -> {
                if (chkNitrogenRefill?.isChecked!!) {

                    chkNitrogenRefill?.isChecked = false
                } else {
                    chkNitrogenRefill?.isChecked = true
                }

            }
            R.id.llWheelbalancing -> {
                if (chkWheelBalacing?.isChecked!!) {

                    chkWheelBalacing?.isChecked = false
                } else {
                    chkWheelBalacing?.isChecked = true
                }

            }
            R.id.llTyreRotation -> {
                if (chkTyreRotation?.isChecked!!) {

                    chkTyreRotation?.isChecked = false
                } else {
                    chkTyreRotation?.isChecked = true
                }

            }
        }
    }

    private fun storeServiceDetailData() {
        var jsonObject: JsonObject = JsonObject()

        jsonObject.addProperty(TyreKey.nitrogenTopup, "" + chkNitrogenTopup?.isChecked)
        jsonObject.addProperty(TyreKey.nitrogenRefil, "" + chkNitrogenRefill?.isChecked)
        jsonObject.addProperty(TyreKey.tyreRotation, "" + chkTyreRotation?.isChecked)
        jsonObject.addProperty(TyreKey.wheelBalancing, "" + chkWheelBalacing?.isChecked)

        jsonObject.addProperty(TyreKey.moreSuggestion, edtMoreSuggestion?.text?.toString())
        jsonObject.addProperty(TyreKey.nextDueDate, tvNextServiceDueDate?.text.toString())

        var jsonArray: JsonArray = JsonArray()

        selectedSuggestionArr?.clear()

        if (suggestionArray != null) {
            for (i in suggestionArray?.indices!!) {
                Log.e("issuelist", "" + suggestionArray?.get(i))
                if (suggestionArray?.get(i)?.isSelected!!) {
                    selectedSuggestionArr?.add(
                        suggestionArray?.get(i)?.issueName!!
                    )
                }
            }
        }

        if (selectedSuggestionArr != null && selectedSuggestionArr?.size!! > 0) {
            for (i in selectedSuggestionArr?.indices!!) {
                jsonArray.add(selectedSuggestionArr?.get(i))
            }
        }
        jsonObject.addProperty(
            TyreKey.technicalSuggestionArr,
            tvNextServiceDueDate?.text.toString()
        )
        jsonObject.add(TyreKey.technicalSuggestionArr, jsonArray)

        val radioGroup = findViewById<View>(R.id.rdGroupLF) as RadioGroup
        val radioButtonIDLF = radioGroup.checkedRadioButtonId
        val radioButtonLF = radioGroup.findViewById<View>(radioButtonIDLF) as RadioButton
        val selectedText = radioButtonLF.text?.toString() + "LF"
        jsonObject.addProperty(TyreKey.radioGroupLF, selectedText)

        val radioGroupRF = findViewById<View>(R.id.rdGroupRF) as RadioGroup
        val radioButtonIDRF = radioGroupRF.checkedRadioButtonId
        val radioButtonRF = radioGroupRF.findViewById<View>(radioButtonIDRF) as RadioButton
        val selectedTextRF = radioButtonRF.text?.toString() + "RF"
        jsonObject.addProperty(TyreKey.radioGroupRF, selectedTextRF)

        val radioGroupLR = findViewById<View>(R.id.rdGroupLR) as RadioGroup
        val radioButtonIDLR = radioGroupLR.checkedRadioButtonId
        val radioButtonLR = radioGroupLR.findViewById<View>(radioButtonIDLR) as RadioButton
        val selectedTextLR = radioButtonLR.text?.toString() + "LR"
        jsonObject.addProperty(TyreKey.radioGroupLR, selectedTextLR)

        val radioGroupRR = findViewById<View>(R.id.rdGroupRR) as RadioGroup
        val radioButtonIDRR = radioGroupRR.checkedRadioButtonId
        val radioButtonRR = radioGroupRR.findViewById<View>(radioButtonIDRR) as RadioButton
        val selectedTextRR = radioButtonRR.text?.toString() + "RR"
        jsonObject.addProperty(TyreKey.radioGroupRR, selectedTextRR)

        prefManager.setValue(TyreConfigClass.serviceDetailData, jsonObject.toString())

        Log.e("serviceObjStore", "" + jsonObject.toString())

    }

    private fun addServiceApiCall() {

        var jsonObject: JsonObject = JsonObject()

        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager?.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            try {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                var jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()

                if (jsonLF.get(TyreKey.vehicleMake) != null && !jsonLF.get(TyreKey.vehicleMake)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_make",
                        jsonLF.get(TyreKey.vehicleMake)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.vehiclePattern) != null && !jsonLF.get(TyreKey.vehiclePatternId)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_pattern",
                        jsonLF.get(TyreKey.vehiclePattern)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.vehicleSize) != null && !jsonLF.get(TyreKey.vehicleSize)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_size",
                        jsonLF.get(TyreKey.vehicleSize)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.manufaturingDate) != null && !jsonLF.get(TyreKey.manufaturingDate)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_manufacturing_date",
                        jsonLF.get(TyreKey.manufaturingDate)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.sidewell) != null && !jsonLF.get(TyreKey.sidewell)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_side_wall",
                        jsonLF.get(TyreKey.sidewell)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.shoulder) != null && !jsonLF.get(TyreKey.shoulder)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_shoulder",
                        jsonLF.get(TyreKey.shoulder)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.treadDepth) != null && !jsonLF.get(TyreKey.treadDepth)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_tread_depth",
                        jsonLF.get(TyreKey.treadDepth)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.treadWear) != null && !jsonLF.get(TyreKey.treadWear)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_tread_wear",
                        jsonLF.get(TyreKey.treadWear)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.rimDamage) != null && !jsonLF.get(TyreKey.rimDamage)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_rim_demage",
                        jsonLF.get(TyreKey.rimDamage)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.bubble) != null && !jsonLF.get(TyreKey.bubble)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_buldge_bubble",
                        jsonLF.get(TyreKey.bubble)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_psi_in",
                        jsonLF.get(TyreKey.psiInTyreService)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_psi_in",
                        jsonLF.get(TyreKey.psiInTyreService)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_psi_out",
                        jsonLF.get(TyreKey.psiOutTyreService)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.addProperty(
                        "front_left_tyre_weight",
                        jsonLF.get(TyreKey.weightTyreService)?.asString
                    )
                }
                if (jsonLF.get(TyreKey.issueResolvedArr) != null && !jsonLF.get(TyreKey.issueResolvedArr)?.asString?.equals(
                        ""
                    )!!
                ) {
                    jsonObject.add(
                        "front_left_issues_to_be_resolved",
                        jsonLF.get(TyreKey.issueResolvedArr)?.asJsonArray
                    )
                }


                /* serviceViewModel?.callApiAddService(
                     jsonObject,
                     prefManager.getAccessToken()!!,
                     this
                 )

                 serviceViewModel?.getAddService()?.observe(this, androidx.lifecycle.Observer {
                     if (it != null) {
                         if (it.success) {

                         } else {

                             if (it.error != null) {
                                 if (it.error.get(0)?.message != null) {
                                     showShortToast(TyreKey.something_went_wrong, this)
                                 }
                             }
                         }
                     }
                 })*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        /*{
                   "date_of_service": null,
                   "vehicle_id": 2,
                   "front_left_tyre_make": "jk tyre",
                   "front_left_tyre_pattern": "Estate N6",
                   "front_left_tyre_size": "165/80 R14",
                   "front_left_manufacturing_date": "0620",
                   "front_right_tyre_make": "jk tyre",
                   "front_right_tyre_pattern": "Estate N6",
                   "front_right_tyre_size": "165/80 R14",
                   "front_right_manufacturing_date": "0620",
                   "back_left_tyre_make": "jk tyre",
                   "back_left_tyre_pattern": "Estate N6",
                   "back_left_tyre_size": "165/80 R14",
                   "back_left_manufacturing_date": "0620",
                   "back_right_tyre_make": "jk tyre",
                   "back_right_tyre_pattern": "Estate N6",
                   "back_right_tyre_size": "165/80 R14",
                   "back_right_manufacturing_date": "0620",
                   "front_left_tyre_side_wall": "SUG",
                   "front_left_tyre_shoulder": "SUG",
                   "front_left_tyre_tread_depth": "OK",
                   "front_left_tyre_tread_wear": "REQ",
                   "front_left_tyre_rim_demage": "OK",
                   "front_left_tyre_buldge_bubble": "OK",
                   "front_right_tyre_side_wall": "OK",
                   "front_right_tyre_shoulder": "OK",
                   "front_right_tyre_tread_depth": "OK",
                   "front_right_tyre_tread_wear": "OK",
                   "front_right_tyre_rim_demage": "OK",
                   "front_right_tyre_buldge_bubble": "OK",
                   "back_left_tyre_side_wall": "REQ",
                   "back_left_tyre_shoulder": "REQ",
                   "back_left_tyre_tread_depth": "REQ",
                   "back_left_tyre_tread_wear": "REQ",
                   "back_left_tyre_rim_demage": "REQ",
                   "back_left_tyre_buldge_bubble": "REQ",
                   "back_right_tyre_side_wall": "OK",
                   "back_right_tyre_shoulder": "OK",
                   "back_right_tyre_tread_depth": "OK",
                   "back_right_tyre_tread_wear": "OK",
                   "back_right_tyre_rim_demage": "OK",
                   "back_right_tyre_buldge_bubble": "OK",
                   "front_left_tyre_psi_in": 35,
                   "front_left_tyre_psi_out": 30,
                   "front_left_tyre_weight": 33,
                   "front_right_tyre_psi_in": 25,
                   "front_right_tyre_psi_out": 28,
                   "front_right_tyre_weight": 30,
                   "back_left_tyre_psi_in": 33,
                   "back_left_tyre_psi_out": 32,
                   "back_left_tyre_weight": 30,
                   "back_right_tyre_psi_in": 30,
                   "back_right_tyre_psi_out": 30,
                   "back_right_tyre_weight": 36,
                   "front_left_issues_to_be_resolved": [
                   1
                   ],
                   "front_right_issues_to_be_resolved": [
                   1
                   ],
                   "back_left_issues_to_be_resolved": [
                   1
                   ],
                   "back_right_issues_to_be_resolved": [
                   1
                   ]
               }*/
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MONTH, 0)
        calendar.add(Calendar.YEAR, 0)
        val future: Date = calendar.getTime()
        Log.e("getfuturedate", "" + future)
        singleBuilder = SingleDateAndTimePickerDialogDueDate.Builder(this)
            .setTimeZone(TimeZone.getDefault())
            .bottomSheet()
            .curved() //.backgroundColor(Color.BLACK)
            //.mainColor(Color.GREEN)
            .displayHours(false)
            .displayMinutes(false)
            .displayDays(false)
            .displayMonth(true)
            .displayDaysOfMonth(true)
            .displayYears(true)
            .defaultDate(future)
            .displayMonthNumbers(true)
            .minDateRange(future) //.mustBeOnFuture()
            //.minutesStep(15)
            //.mustBeOnFuture()
            //.defaultDate(defaultDate)
            // .minDateRange(minDate)
            // .maxDateRange(maxDate)

            .title("Simple")
            .listener(object : SingleDateAndTimePickerDialogDueDate.Listener {
                override fun onDateSelected(date: Date?, str: String) {
                    lltransparent?.visibility = View.GONE
                    if (str.equals("")) {
                        simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
                        Log.e("getdatee", "" + simpleDateFormat?.format(date))
                        selectedDate = simpleDateFormat?.format(date)
                        Log.e("getdatee00", "" + selectedDate)
                        tvNextServiceDueDate?.text = selectedDate


                    } else if (str.equals("Reset")) {
                        Log.e("getdatee2", "" + selectedDate)

                        selectedDate = ""
                    } else if (str.equals("Close")) {
                        Log.e("getdatee1", "" + selectedDate)
                        if (selectedDate == null || selectedDate.equals("null") || selectedDate.equals(
                                ""
                            )
                        ) {

                        } else {

                        }
                    }
                }
            })

        lltransparent?.visibility = View.VISIBLE
        singleBuilder?.display()

    }

    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        msg: String,
        msg1: String,
        msg2: String,
        msg3: String,
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog =
            this?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btnOk)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr

        tv_message?.text = msg + "\n" + msg1 + "\n" + msg2 + "\n" + msg3

        if (msg.isNotEmpty()) {
            tv_message.visibility = View.VISIBLE
        }

        ivClose?.setOnClickListener {
            dialog?.dismiss()
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
        btnText: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        dialog =
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

            Log.e("pendingarr", "" + pendingArr?.get(0))
            var intent: Intent? = null
            if (pendingArr?.get(0).equals("Tyre Make")) {
                intent = Intent(this, VehicleMakeActivity::class.java)
            } else if (pendingArr?.get(0).equals("Tyre Pattern")) {
                intent = Intent(this, VehiclePatternActivity::class.java)
            } else if (pendingArr?.get(0).equals("Tyre Size")) {
                intent = Intent(this, VehicleSizeActivity::class.java)
            } else if (pendingArr?.get(0).equals("Visual Detail")) {
                intent = Intent(this, VisualDetailsActivity::class.java)
            }

            Common.setClearAllValues()
            if (TyreConfigClass.pendingTyre.equals("LF")) {

                intent?.putExtra("selectedTyre", "LF")
                intent?.putExtra("title", "Select Tyre Make - LF")
                TyreConfigClass.selectedTyreConfigType = "LFpending"
                TyreConfigClass.clickedTyre = "LF"

            } else if (TyreConfigClass.pendingTyre.equals("LR")) {
                intent?.putExtra("selectedTyre", "LR")
                intent?.putExtra("title", "Select Tyre Make - LR")
                TyreConfigClass.selectedTyreConfigType = "LRpending"
                TyreConfigClass.clickedTyre = "LR"
            } else if (TyreConfigClass.pendingTyre.equals("RF")) {
                intent?.putExtra("selectedTyre", "RF")
                intent?.putExtra("title", "Select Tyre Make - RF")
                TyreConfigClass.selectedTyreConfigType = "RFpending"
                TyreConfigClass.clickedTyre = "RF"
            } else if (TyreConfigClass.pendingTyre.equals("RR")) {
                intent?.putExtra("selectedTyre", "RR")
                intent?.putExtra("title", "Select Tyre Make - RR")
                TyreConfigClass.selectedTyreConfigType = "RRpending"
                TyreConfigClass.clickedTyre = "RR"
            }

            if (dialog != null && dialog?.isShowing!!) {
                dialog?.dismiss()
            }
            startActivityForResult(intent, 1000)
            /*if (selectedPending?.equals("pattern")) {
//                selectedPending="pattern"
                val intent = Intent(this, VehiclePatternActivity::class.java)
                startActivity(intent)
            } else if (selectedPending?.equals("visual", ignoreCase = true)) {
//                selectedPending="visual"
                val intent = Intent(this, VisualDetailsActivity::class.java)
                startActivity(intent)
            }*/
        }
        dialog?.show()

    }

    private fun openSkipServiceDialogue() {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder.window?.setLayout(width, height)
        builder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val root =
            LayoutInflater.from(this).inflate(R.layout.dialogue_service_skip, null)

        val btnConfirm = root.findViewById<BoldButton>(R.id.btnConfirm)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val pendingReasonRecycView = root.findViewById<RecyclerView>(R.id.pendingReasonRecycView)


        var tyreSuggestionAdapter: TyreSuggestionAdpater? = null
        tyreSuggestionAdapter = TyreSuggestionAdpater(reasonArrayList!!, this, this, true)
        tyreSuggestionAdapter?.onclick = this
        pendingReasonRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        pendingReasonRecycView?.adapter = tyreSuggestionAdapter

        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)

        tvTitleText?.text = "Provide Pending Reason"
        ivClose?.setOnClickListener {
            builder.dismiss()
        }
        btnConfirm.setOnClickListener {
            builder.dismiss()
            var intent = Intent(this, SkippedServiceDetailActivity::class.java)
            startActivityForResult(intent, 106)
        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 5) {

        }


        if (check == 0) {

            Log.e("getposition0", "" + suggestionArr.get(variable))
        } else if (check == 1) {
            Log.e("getposition1", "" + reasonArray.get(variable))
        } else if (check == 10) {

            if (imagePickerDialog != null && imagePickerDialog?.isShowing!!) {
                imagePickerDialog?.dismiss()
            }
            if (Common.commonPhotoChooseArr.get(variable)?.equals("Gallery")) {
                openGallery()
            }
            if (Common.commonPhotoChooseArr?.get(variable)?.equals("Camera")) {
                openCamera()
            }
        } else if (check == 11) {


        }


    }

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        imagePickerDialog =
            this.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        imagePickerDialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        imagePickerDialog?.window?.setLayout(width, height)
        imagePickerDialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        imagePickerDialog?.setContentView(view)

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
            imagePickerDialog?.dismiss()
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

            imagePickerDialog?.dismiss()

        }
        imagePickerDialog?.show()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val id = v?.id
        var intent: Intent? = null
//        val intent = Intent(this, VehicleMakeActivity::class.java)

        when (id) {

            R.id.ivTyre1 -> {

                if (!checkService()) {
                    return false
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.LFVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.LFVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.LFVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.LFVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                Log.e("pendingglf", "" + pendingArr)
                if (pendingArr?.size!! > 0) {
                    if (pendingArr?.get(0).equals("Tyre Make")) {
                        intent = Intent(this, VehicleMakeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Pattern")) {
                        intent = Intent(this, VehiclePatternActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Size")) {
                        intent = Intent(this, VehicleSizeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Visual Detail")) {
                        intent = Intent(this, VisualDetailsActivity::class.java)
                    }
                    Common.setClearAllValues()
                    intent?.putExtra("selectedTyre", "LF")
                    intent?.putExtra("title", "Select Tyre Make - LF")
                    TyreConfigClass.selectedTyreConfigType = "LFpending"
                    TyreConfigClass.clickedTyre = "LF"
                    startActivityForResult(intent, 1000)
                } else {
                    Log.e("pendingglf", "" + pendingArr)
                    intent = Intent(this, VehicleMakeActivity::class.java)
                    intent?.putExtra("selectedTyre", "LF")
                    intent?.putExtra("title", "Select Tyre Make - LF")
                    TyreConfigClass.selectedTyreConfigType = "LF"
                    TyreConfigClass.clickedTyre = "LF"
                    startActivityForResult(intent, 1000)
                }
            }
            R.id.ivTyre3 -> {
                if (!checkService()) {
                    return false
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.RFVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.RFVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.RFVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.RFVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                Log.e("pendingg", "" + pendingArr)
                if (pendingArr?.size!! > 0) {
                    Log.e("pendingg", "" + TyreConfigClass.RFCompleted)
                    if (pendingArr?.get(0).equals("Tyre Make")) {
                        intent = Intent(this, VehicleMakeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Pattern")) {
                        intent = Intent(this, VehiclePatternActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Size")) {
                        intent = Intent(this, VehicleSizeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Visual Detail")) {
                        intent = Intent(this, VisualDetailsActivity::class.java)
                    }
                    Common.setClearAllValues()
                    intent?.putExtra("selectedTyre", "RF")
                    intent?.putExtra("title", "Select Tyre Make - RF")
                    TyreConfigClass.selectedTyreConfigType = "RFpending"
                    TyreConfigClass.clickedTyre = "RF"
                    startActivityForResult(intent, 1000)
                } else {
                    Log.e("pendingg", "call0")
                    intent = Intent(this, VehicleMakeActivity::class.java)
                    intent.putExtra("selectedTyre", "RF")
                    intent.putExtra("title", "Select Tyre Make - RF")
                    TyreConfigClass.selectedTyreConfigType = "RF"
                    TyreConfigClass.clickedTyre = "RF"
                    startActivityForResult(intent, 1000)
                }
            }
            R.id.ivTyre2 -> {
                if (!checkService()) {
                    return false
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.LRVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.LRVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.LRVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.LRVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                Log.e("pendingg", "" + pendingArr)
                if (pendingArr?.size!! > 0) {
                    if (pendingArr?.get(0).equals("Tyre Make")) {
                        intent = Intent(this, VehicleMakeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Pattern")) {
                        intent = Intent(this, VehiclePatternActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Size")) {
                        intent = Intent(this, VehicleSizeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Visual Detail")) {
                        intent = Intent(this, VisualDetailsActivity::class.java)
                    }
                    Common.setClearAllValues()
                    intent?.putExtra("selectedTyre", "LR")
                    intent?.putExtra("title", "Select Tyre Make - LR")
                    TyreConfigClass.selectedTyreConfigType = "LRpending"
                    TyreConfigClass.clickedTyre = "LR"
                    startActivityForResult(intent, 1000)
                } else {
                    intent = Intent(this, VehicleMakeActivity::class.java)
                    intent?.putExtra("selectedTyre", "LR")
                    intent?.putExtra("title", "Select Tyre Make - LR")
                    TyreConfigClass.selectedTyreConfigType = "LR"
                    TyreConfigClass.clickedTyre = "LR"
                    startActivityForResult(intent, 1000)
                }
            }
            R.id.ivTyre4 -> {
                if (!checkService()) {
                    return false
                }

                pendingArr = arrayListOf<String>()

                if (TyreConfigClass.RRVehicleMake == false) {
                    pendingArr?.add("Tyre Make")
                }
                if (TyreConfigClass.RRVehiclePattern == false) {
                    pendingArr?.add("Tyre Pattern")
                }
                if (TyreConfigClass.RRVehicleSize == false) {
                    pendingArr?.add("Tyre Size")
                }
                if (TyreConfigClass.RRVehicleVisualDetail == false) {
                    pendingArr?.add("Visual Detail")
                }
                Log.e("pendingg", "" + pendingArr)
                if (pendingArr?.size!! > 0) {
                    if (pendingArr?.get(0).equals("Tyre Make")) {
                        intent = Intent(this, VehicleMakeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Pattern")) {
                        intent = Intent(this, VehiclePatternActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Tyre Size")) {
                        intent = Intent(this, VehicleSizeActivity::class.java)
                    } else if (pendingArr?.get(0).equals("Visual Detail")) {
                        intent = Intent(this, VisualDetailsActivity::class.java)
                    }
                    Common.setClearAllValues()
                    intent?.putExtra("selectedTyre", "RR")
                    intent?.putExtra("title", "Select Tyre Make - RR")
                    TyreConfigClass.selectedTyreConfigType = "RRpending"
                    TyreConfigClass.clickedTyre = "RR"
                    startActivityForResult(intent, 1000)
                } else {
                    intent = Intent(this, VehicleMakeActivity::class.java)
                    intent.putExtra("selectedTyre", "RR")
                    intent.putExtra("title", "Select Tyre Make - RR")
                    TyreConfigClass.selectedTyreConfigType = "RR"
                    TyreConfigClass.clickedTyre = "RR"
                    startActivityForResult(intent, 1000)
                }
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
            R.id.cardservice -> {
                ivAddServices?.performClick()
            }
            R.id.cardtyreConfig -> {
                if (checkService()) {

                    ivAddTyreConfig?.performClick()
                }
            }
            R.id.cardtechinicalSuggestion -> {
                ivAddTechnicalSuggestion?.performClick()
            }


        }
        return false
    }

    private fun checkService(): Boolean {
        if (chkNitrogenTopup?.isChecked!! || chkWheelBalacing?.isChecked!! || chkTyreRotation?.isChecked!! ||
            chkNitrogenRefill?.isChecked!!
        ) {
            return true
        } else {
            Toast.makeText(this, "Please Select Service", Toast.LENGTH_SHORT).show()
            return false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getcall", "call0" + requestCode)
        when (requestCode) {
            REQUEST_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    if (selectImage1) {
                        ivPickedImage?.setImageURI(Uri.parse(imageFilePath))
                        ivPickedImage?.visibility = View.VISIBLE
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        TyreConfigClass.CarPhoto_1 = imageFilePath
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    } else {
                        ivPickedImage1?.setImageURI(Uri.parse(imageFilePath))
                        ivPickedImage1?.visibility = View.VISIBLE
                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                        TyreConfigClass.CarPhoto_2 = imageFilePath
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    }
                    checkSubmitBtn()
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show()
                }
            }
            IMAGE_PICK_CODE -> {

                if (selectImage1) {
                    try {
                        ivPickedImage?.setImageURI(data?.data)
                        ivPickedImage?.visibility = View.VISIBLE
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        TyreConfigClass.CarPhoto_1 = data?.data?.toString()!!
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    } catch (e: Exception) {
                        Log.e("getexp", "" + e.message + " " + e.cause)
                        e.printStackTrace()

                    }

                } else {
                    try {
                        ivPickedImage1?.setImageURI(data?.data)
                        ivPickedImage1?.visibility = View.VISIBLE
                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                        TyreConfigClass.CarPhoto_2 = data?.data?.toString()!!
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    } catch (e: Exception) {
                        Log.e("getexp", "" + e.message + " " + e.cause)
                        e.printStackTrace()

                    }

                }
                checkSubmitBtn()
            }


            1000 -> {

                Log.e("getcall", "call0" + TyreConfigClass.clickedTyre)

                if (!TyreConfigClass.LFCompleted) {
                    Log.e("getcall", "call1")
                    if (TyreConfigClass.clickedTyre.equals("LF")) {
                        Log.e("getcall", "call2")
                        if (TyreConfigClass.selectedTyreConfigType.equals("LF")) {
                            Log.e("getcall", "call3")
                            ivTyre1?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                            ivtyreLeftFront?.visibility = View.VISIBLE
                            ivInfoImgLF?.visibility = View.GONE
                            TyreConfigClass.LFCompleted = true

                            try {
                                Glide.with(this)
                                    .load(TyreConfigClass.selectedMakeURL)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .into(ivTyreRightFront!!)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            Log.e("geturl", "" + TyreConfigClass.selectedMakeURL)
                        } else {
                            ivInfoImgLF?.visibility = View.VISIBLE
                        }
                    }
                }
                if (!TyreConfigClass.RFCompleted) {
                    Log.e("getcall", "call4")
                    if (TyreConfigClass.clickedTyre.equals("RF")) {
                        Log.e("getcall", "call5")
                        if (TyreConfigClass.selectedTyreConfigType.equals("RF")) {
                            Log.e("getcall", "call6")
                            ivTyre3?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                            ivTyreRightFront?.visibility = View.VISIBLE
                            TyreConfigClass.RFCompleted = true
                            try {
                                Glide.with(this)
                                    .load(TyreConfigClass.selectedMakeURL)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .into(ivTyreRightFront!!)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            Log.e("geturl", "" + TyreConfigClass.selectedMakeURL)
                            ivInfoImgRF?.visibility = View.GONE
                        } else {
                            ivInfoImgRF?.visibility = View.VISIBLE

                        }
                    }
                }
                if (!TyreConfigClass.LRCompleted) {
                    Log.e("getcall", "call6")
                    if (TyreConfigClass.clickedTyre.equals("LR")) {
                        Log.e("getcall", "call7")
                        if (TyreConfigClass.selectedTyreConfigType.equals("LR")) {
                            Log.e("getcall", "call8")
                            ivTyre2?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                            ivtyreLeftRear?.visibility = View.VISIBLE
                            ivInfoImgLR?.visibility = View.GONE
                            TyreConfigClass.LRCompleted = true

                            try {
                                Glide.with(this)
                                    .load(TyreConfigClass.selectedMakeURL)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .into(ivTyreRightFront!!)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            Log.e("geturl", "" + TyreConfigClass.selectedMakeURL)
                        } else {
                            ivInfoImgLR?.visibility = View.VISIBLE

                        }
                    }
                }
                if (!TyreConfigClass.RRCompleted) {
                    Log.e("getcall", "call9")
                    if (TyreConfigClass.clickedTyre.equals("RR")) {
                        Log.e("getcall", "call10")

                        if (TyreConfigClass.selectedTyreConfigType.equals("RR")) {
                            Log.e("getcall", "call11")
                            ivTyre4?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_completed_tyre_config))
                            ivTyreRightRear?.visibility = View.VISIBLE
                            ivInfoImgRR?.visibility = View.GONE
                            TyreConfigClass.RRCompleted = true

                            try {
                                Glide.with(this)
                                    .load(TyreConfigClass.selectedMakeURL)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .into(ivTyreRightFront!!)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            Log.e("geturl", "" + TyreConfigClass.selectedMakeURL)
                        } else {
                            ivInfoImgRR?.visibility = View.VISIBLE

                        }
                    }

                }
                Log.e("getcall", "call13")

                Log.e("getvaluesss", "" + TyreConfigClass.selectedTyreConfigType)


                Log.e("getvaluess_all", TyreDetailCommonClass.tyreType!!)
                Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMake!!)
                Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMakeId!!)
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

                Log.e("getvaluess_all--1 make", TyreDetailCommonClass.chk1Make!!)
                Log.e("getvaluess_all--2 make", TyreDetailCommonClass.chk2Make!!)
                Log.e("getvaluess_all--3 make", TyreDetailCommonClass.chk3Make!!)
                Log.e("getvaluess_all--1 patte", TyreDetailCommonClass.chk1Pattern!!)
                Log.e("getvaluess_all--2 patte", TyreDetailCommonClass.chk2Pattern!!)
                Log.e("getvaluess_all--3 patte", TyreDetailCommonClass.chk3Pattern!!)
                Log.e("getvaluess_all--1 size", TyreDetailCommonClass.chk1Size!!)
                Log.e("getvaluess_all--2 size", TyreDetailCommonClass.chk2Size!!)
                Log.e("getvaluess_all--3 size", TyreDetailCommonClass.chk3Size!!)

                var json = JsonObject()
                var jsonArr = JsonArray()
                json.addProperty(TyreKey.tyreType, TyreDetailCommonClass.tyreType)
                json.addProperty(TyreKey.vehicleMake, TyreDetailCommonClass.vehicleMake)
                json.addProperty(TyreKey.vehicleMakeId, TyreDetailCommonClass.vehicleMakeId)
                json.addProperty(TyreKey.vehicleMakeURL, TyreDetailCommonClass.vehicleMakeURL)
                json.addProperty(TyreKey.vehiclePattern, TyreDetailCommonClass.vehiclePattern)
                json.addProperty(TyreKey.vehiclePatternId, TyreDetailCommonClass.vehiclePatternId)
                json.addProperty(TyreKey.vehicleSize, TyreDetailCommonClass.vehicleSize)
                json.addProperty(TyreKey.vehicleSizeId, TyreDetailCommonClass.vehicleSizeId)
                json.addProperty(TyreKey.manufaturingDate, TyreDetailCommonClass.manufaturingDate)
                json.addProperty(TyreKey.psiInTyreService, TyreDetailCommonClass.psiInTyreService)
                json.addProperty(TyreKey.psiOutTyreService, TyreDetailCommonClass.psiOutTyreService)
                json.addProperty(TyreKey.weightTyreService, TyreDetailCommonClass.weightTyreService)
                json.addProperty(TyreKey.sidewell, TyreDetailCommonClass.sidewell)
                json.addProperty(TyreKey.shoulder, TyreDetailCommonClass.shoulder)
                json.addProperty(TyreKey.treadDepth, TyreDetailCommonClass.treadDepth)
                json.addProperty(TyreKey.treadWear, TyreDetailCommonClass.treadWear)
                json.addProperty(TyreKey.rimDamage, TyreDetailCommonClass.rimDamage)
                json.addProperty(TyreKey.bubble, TyreDetailCommonClass.bubble)

                json.addProperty(TyreKey.chk1Make, TyreDetailCommonClass.chk1Make)
                json.addProperty(TyreKey.chk2Make, TyreDetailCommonClass.chk2Make)
                json.addProperty(TyreKey.chk3Make, TyreDetailCommonClass.chk3Make)
                json.addProperty(TyreKey.chk1Pattern, TyreDetailCommonClass.chk1Pattern)
                json.addProperty(TyreKey.chk2Pattern, TyreDetailCommonClass.chk2Pattern)
                json.addProperty(TyreKey.chk3Pattern, TyreDetailCommonClass.chk3Pattern)
                json.addProperty(TyreKey.chk1Size, TyreDetailCommonClass.chk1Size)
                json.addProperty(TyreKey.chk2Size, TyreDetailCommonClass.chk2Size)
                json.addProperty(TyreKey.chk3Size, TyreDetailCommonClass.chk3Size)

                Log.e("iscompletedd0", "" + TyreConfigClass.LFCompleted)
                Log.e("iscompletedd1", "" + TyreConfigClass.LRCompleted)
                Log.e("iscompletedd11", "" + TyreConfigClass.RFCompleted)
                Log.e("iscompletedd2", "" + TyreConfigClass.RRCompleted)

                if (TyreDetailCommonClass.tyreType.equals("LF")) {
                    if (TyreConfigClass.LFCompleted) {
                        json.addProperty(TyreKey.isCompleted, "true")
                    }
                }
                if (TyreDetailCommonClass.tyreType.equals("LR")) {
                    if (TyreConfigClass.LRCompleted) {
                        json.addProperty(TyreKey.isCompleted, "true")
                    }
                }
                if (TyreDetailCommonClass.tyreType.equals("RF")) {
                    if (TyreConfigClass.RFCompleted) {
                        json.addProperty(TyreKey.isCompleted, "true")
                    }
                }
                if (TyreDetailCommonClass.tyreType.equals("RR")) {
                    if (TyreConfigClass.RRCompleted) {
                        json.addProperty(TyreKey.isCompleted, "true")
                    }
                }


                for (i in TyreDetailCommonClass.issueResolvedArr?.indices!!) {

                    jsonArr.add(TyreDetailCommonClass.issueResolvedArr?.get(i))
                }
                json.add(TyreKey.issueResolvedArr, jsonArr)
                json.addProperty(
                    TyreKey.visualDetailPhotoUrl,
                    TyreDetailCommonClass.visualDetailPhotoUrl
                )
                json.addProperty(
                    TyreKey.isCameraSelectedVisualDetail,
                    TyreDetailCommonClass.isCameraSelectedVisualDetail
                )


                storeMake()

                if (TyreDetailCommonClass.tyreType.equals("LF")) {
                    prefManager.setValue(TyreConfigClass.TyreLFObject, json.toString())
                }
                if (TyreDetailCommonClass.tyreType.equals("LR")) {
                    prefManager.setValue(TyreConfigClass.TyreLRObject, json.toString())
                }
                if (TyreDetailCommonClass.tyreType.equals("RF")) {
                    prefManager.setValue(TyreConfigClass.TyreRFObject, json.toString())
                }
                if (TyreDetailCommonClass.tyreType.equals("RR")) {
                    prefManager.setValue(TyreConfigClass.TyreRRObject, json.toString())
                }

                Log.e(
                    "getjsonobject--00--",
                    "" + prefManager.getValue(TyreConfigClass.TyreLFObject)
                )
                Log.e(
                    "getjsonobject--11--",
                    "" + prefManager.getValue(TyreConfigClass.TyreLRObject)
                )
                Log.e(
                    "getjsonobject--22--",
                    "" + prefManager.getValue(TyreConfigClass.TyreRFObject)
                )
                Log.e(
                    "getjsonobject--33--",
                    "" + prefManager.getValue(TyreConfigClass.TyreRRObject)
                )

                if (TyreConfigClass.CarPhoto_1 != null && !TyreConfigClass.CarPhoto_1.equals("")) {
                    ivPickedImage?.setImageURI(Uri.parse(TyreConfigClass.CarPhoto_1))
                }
                if (TyreConfigClass.CarPhoto_2 != null && !TyreConfigClass.CarPhoto_2.equals("")) {
                    ivPickedImage1?.setImageURI(Uri.parse(TyreConfigClass.CarPhoto_2))
                }
                getStoredObjects()
                checkSubmitBtn()
            }
        }
    }


    private fun checkSubmitBtn() {
        try {
            if (edtMoreSuggestion?.text?.toString() != null) {
                TyreConfigClass.moreSuggestions = edtMoreSuggestion?.text?.toString()!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.e("getimagess", "" + TyreConfigClass.moreSuggestions)

        if (tvNextServiceDueDate?.text?.toString() != null && !tvNextServiceDueDate?.text?.toString()
                .equals("")
        ) {

        } else {
//            Toast.makeText(this, "Next Due Date Not Selected", Toast.LENGTH_SHORT).show()
            return
        }

        if (!TyreConfigClass.nitrogenWheelBalancingChecked && !TyreConfigClass.nitrogenTyreRotationChecked &&
            !TyreConfigClass.nitrogenTopupChecked && !TyreConfigClass.nitrogenRefillChecked
        ) {
//            Toast.makeText(this, "Service Not Selected", Toast.LENGTH_SHORT).show()
            return
        }

        if (!TyreConfigClass.LFCompleted || !TyreConfigClass.RFCompleted || !TyreConfigClass.LRCompleted
            || !TyreConfigClass.RRCompleted
        ) {
//            Toast.makeText(this, "Tyre Not Completed", Toast.LENGTH_SHORT).show()
            return
        }
        if (TyreConfigClass.CarPhoto_1 != null && !TyreConfigClass.CarPhoto_1.equals("")) {

        } else {
//            Toast.makeText(this, "Photo 1 Not Selected", Toast.LENGTH_SHORT).show()
            return
        }
        if (TyreConfigClass.CarPhoto_2 != null && !TyreConfigClass.CarPhoto_2.equals("")) {

        } else {
//            Toast.makeText(this, "Photo 2 Not Selected", Toast.LENGTH_SHORT).show()
            return
        }

        btnSubmitAndComplete?.isClickable = true
        btnSubmitAndComplete?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.round_corner_button_yellow))


    }


    private fun storeMake() {
        if (TyreDetailCommonClass.tyreType.equals("LF")) {

            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                var lfObject: JSONObject? = JSONObject(str)
                Log.e("getobjlf", "" + lfObject.toString())
                Log.e("getobjlf", "" + TyreDetailCommonClass.chk1Make)

                if (TyreDetailCommonClass.chk1Make.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject?.remove(TyreKey.vehicleMake)
                        lfObject?.remove(TyreKey.vehicleMakeId)

                        lfObject?.put(
                            TyreKey.vehicleMake,
                            TyreDetailCommonClass.vehicleMake
                        )
                        lfObject?.put(
                            TyreKey.vehicleMakeId,
                            TyreDetailCommonClass.vehicleMakeId
                        )
                    }
                }

                if (TyreDetailCommonClass.chk1Pattern.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject?.remove(TyreKey.vehiclePattern)
                        lfObject?.remove(TyreKey.vehiclePatternId)
                        lfObject?.put(
                            TyreKey.vehiclePattern,
                            TyreDetailCommonClass.vehiclePattern
                        )
                        lfObject?.put(
                            TyreKey.vehiclePatternId,
                            TyreDetailCommonClass.vehiclePatternId
                        )
                    }
                }

                if (TyreDetailCommonClass.chk1Size.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject?.remove(TyreKey.vehicleSize)
                        lfObject?.remove(TyreKey.vehicleSizeId)
                        lfObject?.put(
                            TyreKey.vehicleSize,
                            TyreDetailCommonClass.vehicleSize
                        )
                        lfObject?.put(
                            TyreKey.vehicleSizeId,
                            TyreDetailCommonClass.vehicleSizeId
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonrf: JsonObject = JsonObject()
                try {
                    jsonrf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())

            } else {
                var jsonrf: JsonObject = JsonObject()
                jsonrf.addProperty(TyreKey.tyreType, "RF")
                if (TyreDetailCommonClass.chk1Make.equals("RF,true")) {
                    jsonrf.addProperty(
                        TyreKey.vehicleMake, TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk1Make.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }

                if (TyreDetailCommonClass.chk1Size.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }
                Log.e("getobjlf0", "" + jsonrf.toString())
                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
            }

//                    =================================================================

            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                var lfObject: JSONObject = JSONObject(str)
                Log.e("getobjlr", "" + lfObject.toString())
                Log.e("getobjlr", "" + TyreDetailCommonClass.chk2Make)
                Log.e("getobjlr", "" + TyreDetailCommonClass.chk2Pattern)
                Log.e("getobjlr", "" + TyreDetailCommonClass.chk2Size)



                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlr: JsonObject = JsonObject()
                try {
                    jsonlr = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())
            } else {
                val jsonlr = JsonObject()
                jsonlr.addProperty(TyreKey.tyreType, "LR")
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())
            }


//                    ===========================

            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                var lfObject: JSONObject = JSONObject(str)



                Log.e("getobjrr", "" + lfObject.toString())
                Log.e("getobjrr", "" + TyreDetailCommonClass.chk3Make)
                Log.e("getobjrr", "" + TyreDetailCommonClass.chk3Pattern)
                Log.e("getobjrr", "" + TyreDetailCommonClass.chk3Size)
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }

                val jsonParser = JsonParser()
                var jsonrr: JsonObject = JsonObject()
                try {
                    jsonrr = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())

            } else {
                val jsonrr = JsonObject()
                jsonrr.addProperty(TyreKey.tyreType, "RR")
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())
            }
        }

        if (TyreDetailCommonClass.tyreType.equals("LR")) {

            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                var lfObject: JSONObject = JSONObject(str)

                Log.e("getobjlr23", "" + lfObject.toString())
                Log.e("getobjlr23", "" + TyreDetailCommonClass.chk1Make)
                Log.e("getobjlr23", "" + TyreDetailCommonClass.chk1Pattern)
                Log.e("getobjlr23", "" + TyreDetailCommonClass.chk1Size)
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }

                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
            } else {
                val jsonlr = JsonObject()
                jsonlr.addProperty(TyreKey.tyreType, "LF")
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }

                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlr.toString())
            }

//                    ===========================================================

            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                var lfObject: JSONObject = JSONObject(str)

                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonlf.toString())
            } else {
                val jsonrf = JsonObject()
                jsonrf.addProperty(TyreKey.tyreType, "RF")
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
            }

//                    ======================================================================

            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                var lfObject: JSONObject = JSONObject(str)



                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonlf.toString())
            } else {
                val jsonrr = JsonObject()
                jsonrr.addProperty(TyreKey.tyreType, "RR")
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())
            }

        }
        if (TyreDetailCommonClass.tyreType.equals("RF")) {

            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                var lfObject: JSONObject = JSONObject(str)
                Log.e("getobjlf11", "" + lfObject.toString())
                Log.e("getobjlf11", "" + TyreDetailCommonClass.chk1Pattern)
                Log.e("getobjlf11", "" + TyreDetailCommonClass.vehiclePattern)
                Log.e("getobjlf11", "" + TyreDetailCommonClass.vehiclePatternId)
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            TyreDetailCommonClass.vehicleMake
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            TyreDetailCommonClass.vehicleMakeId
                        )
                    }
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            TyreDetailCommonClass.vehiclePattern
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            TyreDetailCommonClass.vehiclePatternId
                        )
                    }
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            TyreDetailCommonClass.vehicleSize
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            TyreDetailCommonClass.vehicleSizeId
                        )
                    }
                }

                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.e("getobjlf11", "" + jsonlf)
                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
            } else {
                val jsonlf = JsonObject()
                jsonlf.addProperty(TyreKey.tyreType, "LF")
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }
                Log.e("getobjlf1122", "" + jsonlf.toString())
                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
            }


//                    }
//                    ===========================================================
//                    if (TyreDetailCommonClass.chk2Make.equals("LR,true")){

            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                var lfObject: JSONObject = JSONObject(str)

                Log.e("getobjlr0", "" + lfObject.toString())
                Log.e("getobjlr0", "" + TyreDetailCommonClass.chk2Make)
                Log.e("getobjlr0", "" + TyreDetailCommonClass.chk2Pattern)
                Log.e("getobjlr0", "" + TyreDetailCommonClass.chk2Size)
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }

                }
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlf.toString())
            } else {
                val jsonlr = JsonObject()
                jsonlr.addProperty(TyreKey.tyreType, "LR")
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())
            }


//                    }
//                    ================================================
//                    if (TyreDetailCommonClass.chk3Make.equals("RR,true")){

            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                var lfObject: JSONObject = JSONObject(str)

                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }

                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonlf.toString())
            } else {
                val jsonrr = JsonObject()
                jsonrr.addProperty(TyreKey.tyreType, "RR")
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) {

                    jsonrr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())
            }


        }
        if (TyreDetailCommonClass.tyreType.equals("RR")) {

            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                var lfObject: JSONObject = JSONObject(str)



                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
            } else {
                val jsonlf = JsonObject()
                jsonlf.addProperty(TyreKey.tyreType, "LF")
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
            }

//                    ==================================================================

            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                var lfObject: JSONObject = JSONObject(str)



                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonlf.toString())
            } else {
                val jsonrf = JsonObject()
                jsonrf.addProperty(TyreKey.tyreType, "RF")
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
            }

//                    ====================================================================

            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                var lfObject: JSONObject = JSONObject(str)

                if (TyreDetailCommonClass.chk3Make.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehicleMake.equals("") &&
                        !TyreDetailCommonClass.vehicleMakeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleMake)
                        lfObject.remove(TyreKey.vehicleMakeId)
                        lfObject.put(
                            TyreKey.vehicleMake,
                            if (TyreDetailCommonClass.chk3Make.equals("LR,true")) TyreDetailCommonClass.vehicleMake else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleMakeId,
                            if (TyreDetailCommonClass.chk3Make.equals("LR,true")) TyreDetailCommonClass.vehicleMakeId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehiclePattern.equals("") &&
                        !TyreDetailCommonClass.vehiclePatternId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehiclePattern)
                        lfObject.remove(TyreKey.vehiclePatternId)
                        lfObject.put(
                            TyreKey.vehiclePattern,
                            if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePattern else ""
                        )
                        lfObject.put(
                            TyreKey.vehiclePatternId,
                            if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePatternId else ""
                        )
                    }
                }
                if (TyreDetailCommonClass.chk3Size.equals("LR,true")) {
                    if (!TyreDetailCommonClass.vehicleSize.equals("") &&
                        !TyreDetailCommonClass.vehicleSizeId.equals("")
                    ) {

                        lfObject.remove(TyreKey.vehicleSize)
                        lfObject.remove(TyreKey.vehicleSizeId)
                        lfObject.put(
                            TyreKey.vehicleSize,
                            if (TyreDetailCommonClass.chk3Size.equals("LR,true")) TyreDetailCommonClass.vehicleSize else ""
                        )
                        lfObject.put(
                            TyreKey.vehicleSizeId,
                            if (TyreDetailCommonClass.chk3Size.equals("LR,true")) TyreDetailCommonClass.vehicleSizeId else ""
                        )
                    }
                }
                val jsonParser = JsonParser()
                var jsonlf: JsonObject = JsonObject()
                try {
                    jsonlf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlf.toString())
            } else {
                val jsonlr = JsonObject()
                jsonlr.addProperty(TyreKey.tyreType, "LR")
                if (TyreDetailCommonClass.chk3Make.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                }
                if (TyreDetailCommonClass.chk3Make.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleMakeId,
                        TyreDetailCommonClass.vehicleMakeId
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                }
                if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSize,
                        TyreDetailCommonClass.vehicleSize
                    )
                }
                if (TyreDetailCommonClass.chk3Size.equals("LR,true")) {

                    jsonlr.addProperty(
                        TyreKey.vehicleSizeId,
                        TyreDetailCommonClass.vehicleSizeId
                    )
                }

                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())
            }


        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    private fun openCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            photoFile = try {
                createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }
            val photoUri: Uri =
                FileProvider.getUriForFile(this, "$packageName.provider", photoFile!!)
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(pictureIntent, REQUEST_IMAGE)
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = image.absolutePath
        return image
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onBackPressed() {
        storeServiceDetailData()
        super.onBackPressed()
    }
}