package com.walkins.aapkedoorstep.activity

import android.Manifest
import android.R.attr.data
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.getFile
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.walkins.aapkedoorstep.DB.*
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.DialogueAdpater
import com.walkins.aapkedoorstep.adapter.ServiceAdapter
import com.walkins.aapkedoorstep.adapter.TyreSuggestionAdpater
import com.walkins.aapkedoorstep.common.*
import com.walkins.aapkedoorstep.custom.BoldButton
import com.walkins.aapkedoorstep.datepicker.dialog.SingleDateAndTimePickerDialogDueDate
import com.walkins.aapkedoorstep.model.login.IssueResolveModel
import com.walkins.aapkedoorstep.model.login.comment.CommentListData
import com.walkins.aapkedoorstep.model.login.comment.CommentListModel
import com.walkins.aapkedoorstep.model.login.service.ServiceModelData
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateData
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel
import com.walkins.aapkedoorstep.viewmodel.ServiceViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.lang.Runnable
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility", "SimpleDateFormat", "SetTextI18n")
class AddServiceDetailsActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter,
    View.OnTouchListener {
    private lateinit var prefManager: PrefManager
    private lateinit var mDb: DBClass

    private var loginViewModel: LoginActivityViewModel? = null
    private var commonViewModel: CommonViewModel? = null

    var pendingArr: ArrayList<String>? = null
    var dialog: BottomSheetDialog? = null
    var skipList: ArrayList<IssueResolveModel>? = null

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

    private var llUpdatedPlacement: LinearLayout? = null

    private var chkNitrogenTopup: CheckBox? = null
    private var chkNitrogenRefill: CheckBox? = null
    private var chkWheelBalacing: CheckBox? = null
    private var chkTyreRotation: CheckBox? = null

    private var suggestionsRecycView: RecyclerView? = null
    private var selectedSuggestionArr: ArrayList<String>? = ArrayList()
    private var selectedServiceArr: ArrayList<String>? = ArrayList()
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    private var suggestionArray: ArrayList<IssueResolveModel>? = ArrayList()

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
    private var selectedDate: String? = ""
    private var selectedDateNextServiceDue: String? = null
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
    private var radioGroupLF: RadioGroup? = null
    private var radioGroupLR: RadioGroup? = null
    private var radioGroupRF: RadioGroup? = null
    private var radioGroupRR: RadioGroup? = null

    private var serviceRecycView: RecyclerView? = null
    private var serviceList: ArrayList<ServiceModelData>? = ArrayList()
    private var commentList: ArrayList<CommentListData>? = ArrayList()
    private var serviceAdapter: ServiceAdapter? = null
    private var commentModel: CommentListModel? = null

    private var tvRegNumber: TextView? = null
    private var tvMakeModel: TextView? = null
    private var tvcolor: TextView? = null
    private var llbg: LinearLayout? = null
    private var ivCarImage: ImageView? = null

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String
    private val PERMISSION_CODE = 1010;
    private val IMAGE_CAPTURE_CODE = 1011
    var image_uri: Uri? = null

    private var color: String = ""
    private var makeModel: String = ""
    private var regNumber: String = ""
    private var carImage: String = ""
    private var uuid: String = ""
    private var colorCode: String = ""
    private var address: String = ""
    private var make_id: String = ""
    private var model_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_details)
        mDb = DBClass.getInstance(this)
        prefManager = PrefManager(this)
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)

//        requestPermissionForImage()
        init()


    }

    suspend fun getStoredObjects(checkUnCheck: String) {

        var LFVehicleURL = ""
        var LRVehicleURL = ""
        var RFVehicleURL = ""
        var RRVehicleURL = ""


        getSelectedService()

        Log.e("getserviceselected", "" + selectedServiceArr)
        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)
                if (jsonLF.has(TyreKey.isCompleted)) {
                    if (jsonLF.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.LFCompleted =
                            jsonLF.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }

                if (TyreConfigClass.LFCompleted) {
                    ivTyre1?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_completed_tyre_config
                        )
                    )
                    ivtyreLeftFront?.visibility = View.VISIBLE
                    ivInfoImgLF?.visibility = View.GONE
                }

                if (jsonLF.get(TyreKey.vehicleMakeURL) != null &&
                    !jsonLF.get(TyreKey.vehicleMakeURL)?.asString.equals("")
                ) {
                    LFVehicleURL = jsonLF.get(TyreKey.vehicleMakeURL)?.asString!!
                }

                Log.e("getMakeURL", "" + jsonLF.get(TyreKey.vehicleMakeURL)?.asString)
                try {
                    Glide.with(this@AddServiceDetailsActivity)
                        .load(jsonLF.get(TyreKey.vehicleMakeURL)?.asString)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.33f)
                        .placeholder(R.drawable.placeholder)
                        .into(ivtyreLeftFront!!)
                } catch (e: Exception) {
                    e.printStackTrace()
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
                Log.e("getserviceselectedlf_", "" + selectedServiceArr)
                if (selectedServiceArr?.contains("Wheel Balancing")!!) {
                    Log.e("getserviceselectedlf_", "" + selectedServiceArr?.contains("Wheel Balancing"))
                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.LFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LFVehicleVisualDetail = false

                    }
                }

                if (selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) {
                    Log.e("getserviceselectedlf_1", "" + selectedServiceArr?.contains("Nitrogen Refill")!! + "--" + selectedServiceArr?.contains("Nitrogen Top Up")!!)
                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.LFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LFVehicleVisualDetail = false
                    }
                    if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.LFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LFVehicleVisualDetail = false
                    }

                }

                if ((selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) && (selectedServiceArr?.contains("Wheel Balancing")!!)) {
                    Log.e("getserviceselectedlf_2", "" + selectedServiceArr)

                    var psiIn = ""
                    var psiOut = ""
                    var weight = ""
                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        psiIn = jsonLF.get(TyreKey.psiInTyreService)?.asString!!
                    }
                    if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        psiOut = jsonLF.get(TyreKey.psiOutTyreService)?.asString!!
                    }
                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        weight = jsonLF.get(TyreKey.weightTyreService)?.asString!!
                    }

                    if (!psiIn.equals("") && !psiOut.equals("") && !weight.equals("")) {
                        TyreConfigClass.LFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LFVehicleVisualDetail = false
                    }
                }
                Log.e("getserviceselectedlf_3", "" + TyreConfigClass.LFCompleted + " " + TyreConfigClass.LFVehicleVisualDetail)

                if (TyreConfigClass.LFCompleted) {
                    if (!TyreConfigClass.LFVehicleVisualDetail) {
                        jsonLF.remove(TyreKey.isCompleted)
                        jsonLF.addProperty(TyreKey.isCompleted, "false")
                        TyreConfigClass.LFCompleted = false

                        ivTyre1?.setImageDrawable(
                            this@AddServiceDetailsActivity.resources?.getDrawable(
                                R.drawable.ic_pending_tyre_config
                            )
                        )
                        ivInfoImgLF?.visibility = View.VISIBLE
                    }
                }
                Log.e("getserviceselected1", "" + TyreConfigClass.LFCompleted)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
            try {
                val jsonRF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getObjects", "" + jsonRF)
                if (jsonRF.has(TyreKey.isCompleted)) {
                    if (jsonRF.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.RFCompleted =
                            jsonRF.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }
                if (TyreConfigClass.RFCompleted) {
                    ivTyre3?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_completed_tyre_config
                        )
                    )
                    ivTyreRightFront?.visibility = View.VISIBLE
                    ivInfoImgRF?.visibility = View.GONE
                }
                if ((jsonRF.get(TyreKey.vehicleMake) != null &&
                            !jsonRF.get(TyreKey.vehicleMake)?.asString.equals("") &&
                            !jsonRF.get(TyreKey.vehicleMakeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RFVehicleMake = true
                    Log.e("rfStatus", "" + TyreConfigClass.RFVehicleMake)
                }
                if ((jsonRF.get(TyreKey.vehiclePattern) != null && jsonRF.get(TyreKey.vehiclePatternId) != null &&
                            !jsonRF.get(TyreKey.vehiclePattern)?.asString.equals("") &&
                            !jsonRF.get(TyreKey.vehiclePatternId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RFVehiclePattern = true
                    Log.e("rfStatus0", "" + TyreConfigClass.RFVehiclePattern)
                }
                if ((jsonRF.get(TyreKey.vehicleSize) != null && jsonRF.get(TyreKey.vehicleSizeId) != null &&
                            !jsonRF.get(TyreKey.vehicleSize)?.asString.equals("") &&
                            !jsonRF.get(TyreKey.vehicleSizeId)?.asString.equals(""))
                ) {
                    TyreConfigClass.RFVehicleSize = true
                    Log.e("rfStatus2", "" + TyreConfigClass.RFVehicleSize)
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
                    Log.e("rfStatus4", "" + TyreConfigClass.RFVehicleVisualDetail)
                }

                if (selectedServiceArr?.contains("Wheel Balancing")!!) {
                    if (jsonRF.get(TyreKey.weightTyreService) != null && !jsonRF.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.RFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RFVehicleVisualDetail = false
                    }
                }

                if (selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) {
                    if (jsonRF.get(TyreKey.psiInTyreService) != null && !jsonRF.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.RFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RFVehicleVisualDetail = false
                    }
                    if (jsonRF.get(TyreKey.psiOutTyreService) != null && !jsonRF.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.RFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RFVehicleVisualDetail = false
                    }
                }

                if ((selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) && (selectedServiceArr?.contains("Wheel Balancing")!!)) {
                    var psiIn = ""
                    var psiOut = ""
                    var weight = ""
                    if (jsonRF.get(TyreKey.psiInTyreService) != null && !jsonRF.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        psiIn = jsonRF.get(TyreKey.psiInTyreService)?.asString!!
                    }
                    if (jsonRF.get(TyreKey.psiOutTyreService) != null && !jsonRF.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        psiOut = jsonRF.get(TyreKey.psiOutTyreService)?.asString!!
                    }
                    if (jsonRF.get(TyreKey.weightTyreService) != null && !jsonRF.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        weight = jsonRF.get(TyreKey.weightTyreService)?.asString!!
                    }

                    if (!psiIn.equals("") && !psiOut.equals("") && !weight.equals("")) {
                        TyreConfigClass.RFVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RFVehicleVisualDetail = false
                    }
                }
                if (TyreConfigClass.RFCompleted) {
                    if (!TyreConfigClass.RFVehicleVisualDetail) {
                        jsonRF.remove(TyreKey.isCompleted)
                        jsonRF.addProperty(TyreKey.isCompleted, "false")
                        TyreConfigClass.RFCompleted = false

                        ivTyre3?.setImageDrawable(
                            this@AddServiceDetailsActivity.resources?.getDrawable(
                                R.drawable.ic_pending_tyre_config
                            )
                        )
                        ivInfoImgRF?.visibility = View.VISIBLE
                    }
                }

                Log.e("getservicesele", "" + TyreConfigClass.RFCompleted)
                if (jsonRF.get(TyreKey.vehicleMakeURL) != null &&
                    !jsonRF.get(TyreKey.vehicleMakeURL).asString.equals("")
                ) {
                    RFVehicleURL = jsonRF.get(TyreKey.vehicleMakeURL)?.asString!!
                }

                try {
                    Glide.with(this@AddServiceDetailsActivity)
                        .load(jsonRF.get(TyreKey.vehicleMakeURL)?.asString)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.33f)
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
            !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
            try {
                val jsonLR: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getObjects", "" + jsonLR)
                if (jsonLR.has(TyreKey.isCompleted)) {
                    if (jsonLR.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.LRCompleted =
                            jsonLR.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }
                if (TyreConfigClass.LRCompleted) {

                    ivTyre2?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_completed_tyre_config
                        )
                    )
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

                if (selectedServiceArr?.contains("Wheel Balancing")!!) {
                    if (jsonLR.get(TyreKey.weightTyreService) != null && !jsonLR.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.LRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LRVehicleVisualDetail = false
                    }
                }

                if (selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) {
                    if (jsonLR.get(TyreKey.psiInTyreService) != null && !jsonLR.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.LRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LRVehicleVisualDetail = false
                    }
                    if (jsonLR.get(TyreKey.psiOutTyreService) != null && !jsonLR.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.LRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LRVehicleVisualDetail = false
                    }
                }

                if ((selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) && (selectedServiceArr?.contains("Wheel Balancing")!!)) {
                    var psiIn = ""
                    var psiOut = ""
                    var weight = ""
                    if (jsonLR.get(TyreKey.psiInTyreService) != null && !jsonLR.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        psiIn = jsonLR.get(TyreKey.psiInTyreService)?.asString!!
                    }
                    if (jsonLR.get(TyreKey.psiOutTyreService) != null && !jsonLR.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        psiOut = jsonLR.get(TyreKey.psiOutTyreService)?.asString!!
                    }
                    if (jsonLR.get(TyreKey.weightTyreService) != null && !jsonLR.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        weight = jsonLR.get(TyreKey.weightTyreService)?.asString!!
                    }

                    if (!psiIn.equals("") && !psiOut.equals("") && !weight.equals("")) {
                        TyreConfigClass.LRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.LRVehicleVisualDetail = false
                    }
                }

                if (TyreConfigClass.LRCompleted) {
                    if (!TyreConfigClass.LRVehicleVisualDetail) {
                        jsonLR.remove(TyreKey.isCompleted)
                        jsonLR.addProperty(TyreKey.isCompleted, "false")
                        TyreConfigClass.LRCompleted = false

                        ivTyre2?.setImageDrawable(
                            this@AddServiceDetailsActivity.resources?.getDrawable(
                                R.drawable.ic_pending_tyre_config
                            )
                        )
                        ivInfoImgLR?.visibility = View.VISIBLE
                    }
                }
                Log.e("getservicesele12", "" + TyreConfigClass.LRCompleted)

                if (jsonLR.get(TyreKey.vehicleMakeURL) != null &&
                    !jsonLR.get(TyreKey.vehicleMakeURL).asString.equals("")
                ) {
                    LRVehicleURL = jsonLR.get(TyreKey.vehicleMakeURL)?.asString!!
                }

                try {
                    Glide.with(this@AddServiceDetailsActivity)
                        .load(jsonLR.get(TyreKey.vehicleMakeURL)?.asString)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.33f)
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
            val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
            try {
                val jsonRR: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getObjects", "" + jsonRR)
                if (jsonRR.has(TyreKey.isCompleted)) {
                    if (jsonRR.get(TyreKey.isCompleted) != null) {
                        TyreConfigClass.RRCompleted =
                            jsonRR.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
                    }
                }
                if (TyreConfigClass.RRCompleted) {
                    ivTyre4?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_completed_tyre_config
                        )
                    )
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
                    Log.e("visualTrue4", "" + TyreConfigClass.RRVehicleVisualDetail)
                }
                if (selectedServiceArr?.contains("Wheel Balancing")!!) {
                    if (jsonRR.get(TyreKey.weightTyreService) != null && !jsonRR.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.RRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RRVehicleVisualDetail = false
                    }
                }

                if (selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) {
                    if (jsonRR.get(TyreKey.psiInTyreService) != null && !jsonRR.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.RRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RRVehicleVisualDetail = false
                    }
                    if (jsonRR.get(TyreKey.psiOutTyreService) != null && !jsonRR.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        TyreConfigClass.RRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RRVehicleVisualDetail = false
                    }
                }

                if ((selectedServiceArr?.contains("Nitrogen Refill")!! || selectedServiceArr?.contains("Nitrogen Top Up")!!) && (selectedServiceArr?.contains("Wheel Balancing")!!)) {
                    var psiIn = ""
                    var psiOut = ""
                    var weight = ""
                    if (jsonRR.get(TyreKey.psiInTyreService) != null && !jsonRR.get(TyreKey.psiInTyreService)?.asString?.equals("")!!) {
                        psiIn = jsonRR.get(TyreKey.psiInTyreService)?.asString!!
                    }
                    if (jsonRR.get(TyreKey.psiOutTyreService) != null && !jsonRR.get(TyreKey.psiOutTyreService)?.asString?.equals("")!!) {
                        psiOut = jsonRR.get(TyreKey.psiOutTyreService)?.asString!!
                    }
                    if (jsonRR.get(TyreKey.weightTyreService) != null && !jsonRR.get(TyreKey.weightTyreService)?.asString?.equals("")!!) {
                        weight = jsonRR.get(TyreKey.weightTyreService)?.asString!!
                    }

                    if (!psiIn.equals("") && !psiOut.equals("") && !weight.equals("")) {
                        TyreConfigClass.RRVehicleVisualDetail = true
                    } else {
                        TyreConfigClass.RRVehicleVisualDetail = false
                    }
                }


                if (TyreConfigClass.RRCompleted) {
                    if (!TyreConfigClass.RRVehicleVisualDetail) {
                        jsonRR.remove(TyreKey.isCompleted)
                        jsonRR.addProperty(TyreKey.isCompleted, "false")
                        TyreConfigClass.RRCompleted = false

                        ivTyre4?.setImageDrawable(
                            this@AddServiceDetailsActivity.resources?.getDrawable(
                                R.drawable.ic_pending_tyre_config
                            )
                        )
                        ivInfoImgRR?.visibility = View.VISIBLE
                    }
                }
                Log.e("getservice22sele", "" + TyreConfigClass.RRCompleted)
                if (jsonRR.get(TyreKey.vehicleMakeURL) != null &&
                    !jsonRR.get(TyreKey.vehicleMakeURL).asString.equals("")
                ) {
                    RRVehicleURL = jsonRR.get(TyreKey.vehicleMakeURL)?.asString!!
                }

                try {
                    Glide.with(this@AddServiceDetailsActivity)
                        .load(jsonRR.get(TyreKey.vehicleMakeURL)?.asString)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.33f)
                        .placeholder(R.drawable.placeholder)
                        .into(ivTyreRightRear!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Log.e("isCpmpleted0", "" + LFVehicleURL)
        Log.e("isCpmpleted1", "" + LRVehicleURL)
        Log.e("isCpmpleted2", "" + RFVehicleURL)
        Log.e("isCpmpleted3", "" + RRVehicleURL)

        if (!LFVehicleURL.equals("")) {
            ivtyreLeftFront?.visibility = View.VISIBLE
            Log.e("getMakeURL", "" + LFVehicleURL)
            try {
                Glide.with(this@AddServiceDetailsActivity)
                    .load(LFVehicleURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.33f)
                    .placeholder(R.drawable.placeholder)
                    .into(ivtyreLeftFront!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (!RFVehicleURL.equals("")) {
            ivTyreRightFront?.visibility = View.VISIBLE
            Log.e("getMakeURL", "" + RFVehicleURL)
            try {
                Glide.with(this@AddServiceDetailsActivity)
                    .load(RFVehicleURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.33f)
                    .placeholder(R.drawable.placeholder)
                    .into(ivTyreRightFront!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (!LRVehicleURL.equals("")) {
            ivtyreLeftRear?.visibility = View.VISIBLE
            Log.e("getMakeURL", "" + LRVehicleURL)
            try {
                Glide.with(this@AddServiceDetailsActivity)
                    .load(LRVehicleURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.33f)
                    .placeholder(R.drawable.placeholder)
                    .into(ivtyreLeftRear!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (!RRVehicleURL.equals("")) {
            ivTyreRightRear?.visibility = View.VISIBLE
            Log.e("getMakeURL", "" + RRVehicleURL)
            try {
                Glide.with(this@AddServiceDetailsActivity)
                    .load(RRVehicleURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.33f)
                    .placeholder(R.drawable.placeholder)
                    .into(ivTyreRightRear!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (prefManager?.getValue("image_Car_1") != null &&
            !prefManager?.getValue("image_Car_1").equals("")
        ) {
            try {
                var imageUriDisplay: Uri? = null
                var imageUri: Uri? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUriDisplay = Uri.parse(prefManager.getValue("image_1_path"));
                    imageUri = Uri.parse(prefManager.getValue("image_Car_1"));
                } else {
                    imageUriDisplay = Uri.fromFile(File(prefManager.getValue("image_1_path")));
                    imageUri = Uri.fromFile(File(prefManager.getValue("image_Car_1")));
                }
                ivPickedImage?.setImageURI(imageUriDisplay)
                TyreConfigClass.car_1_uri = imageUri
                Log.e("getpath", "" + TyreConfigClass.car_1_uri)
                ivEditImg1?.visibility = View.VISIBLE
                tvAddPhoto1?.visibility = View.GONE
                tvCarphoto1?.visibility = View.GONE
                TyreConfigClass.CarPhoto_1 = prefManager.getValue("image_Car_1")
                Log.e("getimagee00", "---" + TyreConfigClass.CarPhoto_1)
                relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                ivPickedImage?.visibility = View.VISIBLE
            } catch (e: java.lang.Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        if (prefManager?.getValue("image_Car_2") != null &&
            !prefManager?.getValue("image_Car_2").equals("")
        ) {
            try {
                var imageUriDisplay: Uri? = null
                var imageUri: Uri? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUriDisplay = Uri.parse(prefManager.getValue("image_2_path"));
                    imageUri = Uri.parse(prefManager.getValue("image_Car_2"));
                } else {
                    imageUriDisplay = Uri.fromFile(File(prefManager.getValue("image_2_path")));
                    imageUri = Uri.fromFile(File(prefManager.getValue("image_Car_2")));
                }
                ivPickedImage1?.setImageURI(imageUriDisplay)
                TyreConfigClass.car_2_uri = imageUri
                Log.e("getpath", "" + TyreConfigClass.car_2_uri)
                ivEditImg2?.visibility = View.VISIBLE
                tvAddPhoto2?.visibility = View.GONE
                tvCarphoto2?.visibility = View.GONE
                TyreConfigClass.CarPhoto_2 = prefManager.getValue("image_Car_2")
                relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                ivPickedImage1?.visibility = View.VISIBLE
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }

        if (prefManager?.getValue("image_LF") != null && !prefManager?.getValue("image_LF").equals("")) {
            try {
                var imageUri: Uri? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = Uri.parse(prefManager.getValue("image_LF"));
                } else {
                    imageUri = Uri.fromFile(File(prefManager.getValue("image_LF")));
                }
                TyreDetailCommonClass.tyre_Uri_LF = imageUri
                Log.e("getpath", "" + TyreDetailCommonClass.tyre_Uri_LF)
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("getpathlf", "" + e.message)
            }

        }
        if (prefManager?.getValue("image_RF") != null && !prefManager?.getValue("image_RF").equals("")) {
            try {
                var imageUri: Uri? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = Uri.parse(prefManager.getValue("image_RF"));
                } else {
                    imageUri = Uri.fromFile(File(prefManager.getValue("image_RF")));
                }
                TyreDetailCommonClass.tyre_Uri_RF = imageUri
                Log.e("getpath", "" + TyreDetailCommonClass.tyre_Uri_RF)
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("getpathrf", "" + e.message)
            }

        }
        if (prefManager?.getValue("image_RR") != null &&
            !prefManager?.getValue("image_RR").equals("")
        ) {
            try {
                var imageUri: Uri? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = Uri.parse(prefManager.getValue("image_RR"));
                } else {
                    imageUri = Uri.fromFile(File(prefManager.getValue("image_RR")));
                }
                TyreDetailCommonClass.tyre_Uri_RR = imageUri
                Log.e("getpath", "" + TyreDetailCommonClass.tyre_Uri_RR)
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("getpathrr", "" + e.message)
            }

        }

        if (prefManager?.getValue("image_LR") != null &&
            !prefManager.getValue("image_LR").equals("")
        ) {
            try {
                var imageUri: Uri? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = Uri.parse(prefManager.getValue("image_LR"));
                } else {
                    imageUri = Uri.fromFile(File(prefManager.getValue("image_LR")));
                }
                TyreDetailCommonClass.tyre_Uri_LR = imageUri
                Log.e("getpath", "" + TyreDetailCommonClass.tyre_Uri_LR)
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("getpathlr", "" + e.message)
            }

        }


        checkSubmitBtn()

    }

    @SuppressLint("SetTextI18n")
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
        radioGroupLF = findViewById(R.id.rdGroupLF)
        radioGroupLR = findViewById(R.id.rdGroupLR)
        radioGroupRF = findViewById(R.id.rdGroupRF)
        radioGroupRR = findViewById(R.id.rdGroupRR)

        ivPhoneCall = findViewById(R.id.ivPhoneCall)
        serviceRecycView = findViewById(R.id.serviceRecycView)

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

        tvcolor = findViewById(R.id.tvcolor)
        tvMakeModel = findViewById(R.id.tvMakeModel)
        llbg = findViewById(R.id.llbg)
        tvRegNumber = findViewById(R.id.tvRegNumber)
        ivCarImage = findViewById(R.id.ivCarImage)



        if (intent != null) {
            if (intent.getStringExtra("color") != null) {
                color = intent.getStringExtra("color")!!
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
            if (intent.getStringExtra("uuid") != null) {
                uuid = intent.getStringExtra("uuid")!!
            }
            if (intent.getStringExtra("colorcode") != null) {
                colorCode = intent.getStringExtra("colorcode")!!
            }
            if (intent.getStringExtra("address") != null) {
                address = intent.getStringExtra("address")!!
            }
            if (intent.getStringExtra("make_id") != null) {
                make_id = intent.getStringExtra("make_id")!!
                if (!make_id.equals("")) {
                    TyreDetailCommonClass.make_id = make_id.toInt()
                }
            }
            if (intent.getStringExtra("model_id") != null) {
                model_id = intent.getStringExtra("model_id")!!
                if (!model_id.equals("")) {
                    TyreDetailCommonClass.model_id = model_id.toInt()
                }
            }

        }

        tvcolor?.text = color
        tvMakeModel?.text = makeModel
        tvRegNumber?.text = regNumber

        if (!colorCode.equals("")) {
            llbg?.setBackgroundColor(Color.parseColor(colorCode))
        }

        try {
            Glide.with(this)
                .load(carImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.33f)
                .placeholder(R.drawable.ic_no_car_image)
                .into(ivCarImage!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

        btnSubmitAndComplete?.isClickable = false
        btnSubmitAndComplete?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_blue))

        ivPickedImage?.setOnClickListener(this)
        ivPickedImage1?.setOnClickListener(this)

        ivPickedImage?.visibility = View.GONE
        ivPickedImage1?.visibility = View.GONE
        ivEditImg1?.visibility = View.GONE
        ivEditImg2?.visibility = View.GONE

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
            suggestionArray?.add(IssueResolveModel(suggestionArr[i] + " " + i, 0, false))
        }
        tyreSuggestionAdapter = TyreSuggestionAdpater(suggestionArray!!, this, this, false, true)
        tyreSuggestionAdapter?.onclick = this
        suggestionsRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        suggestionsRecycView?.adapter = tyreSuggestionAdapter

        serviceRecycView?.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        serviceAdapter = ServiceAdapter(serviceList!!, this, this)
        serviceRecycView?.adapter = serviceAdapter
        serviceAdapter?.onclick = this

        ivBack?.setOnClickListener(this)

        CoroutineScope(Dispatchers.Main).launch {
            getStoredObjects("")
        }


        tvNextServiceDueDate?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().length > 2) {
                    TyreConfigClass.nextDueDate = tvNextServiceDueDate?.text?.toString()!!
                    checkSubmitBtn()
                }
            }

        })

        val gson = Gson()
        var customObject: ServiceListByDateData? = null
        if (intent?.getStringExtra("serviceList") != null) {
            customObject = gson.fromJson(intent?.getStringExtra("serviceList"), ServiceListByDateData::class.java)
            Log.d("Location_Count", "" + customObject?.service)
        }

        if (prefManager.getServiceList(TyreConfigClass.serviceList) != null && prefManager.getServiceList(TyreConfigClass.serviceList).size > 0) {
            serviceList?.clear()
            for (i in prefManager.getServiceList(TyreConfigClass.serviceList).indices) {
                var model: ServiceModelData? = null

                if (customObject != null && customObject.service.size > 0) {
                    model = ServiceModelData(
                        prefManager.getServiceList(TyreConfigClass.serviceList)[i].id,
                        prefManager.getServiceList(TyreConfigClass.serviceList)[i].name,
                        prefManager.getServiceList(TyreConfigClass.serviceList)[i].image, false, true
                    )
                    for (j in customObject.service.indices) {
                        if (prefManager.getServiceList(TyreConfigClass.serviceList)[i].id ==
                            customObject.service.get(j).id
                        ) {
                            model = ServiceModelData(
                                prefManager.getServiceList(TyreConfigClass.serviceList)[i].id,
                                prefManager.getServiceList(TyreConfigClass.serviceList)[i].name,
                                prefManager.getServiceList(TyreConfigClass.serviceList)[i].image, false, false
                            )
                        }
                    }

                } else {
                    model = ServiceModelData(
                        prefManager.getServiceList(TyreConfigClass.serviceList)[i].id,
                        prefManager.getServiceList(TyreConfigClass.serviceList)[i].name,
                        prefManager.getServiceList(TyreConfigClass.serviceList)[i].image, false, false
                    )
                }
                serviceList?.add(model!!)
            }
            serviceAdapter?.notifyDataSetChanged()
        }

        if (prefManager.getCommentList(TyreConfigClass.commentList) != null && prefManager.getCommentList(TyreConfigClass.commentList).size > 0) {
            commentList?.clear()
            for (i in prefManager.getCommentList(TyreConfigClass.commentList).indices) {
                val model = CommentListData(
                    prefManager.getServiceList(TyreConfigClass.commentList)[i].id,
                    prefManager.getServiceList(TyreConfigClass.commentList)[i].name
                )
                commentList?.add(model)
            }
        }

        getServiceData()
    }


    private fun getServiceData() {
        if (prefManager?.getValue(TyreConfigClass.serviceDetailData) != null &&
            !prefManager.getValue(TyreConfigClass.serviceDetailData).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.serviceDetailData)

            try {
                val jsonService: JsonObject = JsonParser().parse(str).getAsJsonObject()
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
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val formatterDisplay = SimpleDateFormat("dd MMMM yyyy")
                    val dateInString = formatterDisplay.parse(tvNextServiceDueDate?.text?.toString()!!)
                    val displayDate = formatter.format(dateInString)

                    selectedDateNextServiceDue = displayDate
                    Log.e("getDatee", "" + selectedDateNextServiceDue)
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

                    val arr = jsonService.get(TyreKey.technicalSuggestionArr)?.asJsonArray
                    Log.e("getValues", "" + arr)
                    val gson = Gson()
                    val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                    val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                    Log.e("getValues", "" + arrlist)
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

                if (jsonService.get(TyreKey.serviceArr) != null) {

                    val arr = jsonService.get(TyreKey.serviceArr)?.asJsonArray
                    Log.e("getValues", "" + arr)
                    val gson = Gson()
                    val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                    val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                    Log.e("getValues", "" + arrlist)
                    for (i in serviceList?.indices!!) {

                        for (j in arrlist.indices) {

                            if (serviceList?.get(i)?.name.equals(arrlist[j])) {
                                serviceList?.get(i)?.isSelected = true
                                selectedServiceArr?.add(serviceList?.get(i)?.name!!)
                            }
                        }
                    }

                    serviceAdapter?.notifyDataSetChanged()
                }

                if (prefManager?.getValue("image_Car_1") != null && !prefManager?.getValue("image_Car_1").equals("")) {

                    Log.e("TAG", "getServiceData: ")
                } else {
                    if (jsonService.get(TyreKey.addServiceCarImage_1) != null &&
                        !jsonService.get(TyreKey.addServiceCarImage_1).asString.equals("")
                    ) {

                        TyreConfigClass.CarPhoto_1 = jsonService.get(TyreKey.addServiceCarImage_1).asString
                        Log.e("getimagee131", " --" + TyreConfigClass.CarPhoto_1)
                        try {
                            Glide.with(this@AddServiceDetailsActivity)
                                .load(TyreConfigClass.CarPhoto_1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.33f)
                                .placeholder(R.drawable.placeholder)
                                .into(ivPickedImage!!)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        ivPickedImage?.visibility = View.VISIBLE
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))


                    }
                }

                if (prefManager?.getValue("image_Car_2") != null && !prefManager.getValue("image_Car_2").equals("")) {
                    Log.e("TAG", "getServiceData: ")
                } else {
                    if (jsonService.get(TyreKey.addServiceCarImage_2) != null &&
                        !jsonService.get(TyreKey.addServiceCarImage_2).asString.equals("")
                    ) {

                        TyreConfigClass.CarPhoto_2 = jsonService.get(TyreKey.addServiceCarImage_2).asString
                        try {
                            Glide.with(this@AddServiceDetailsActivity)
                                .load(TyreConfigClass.CarPhoto_2)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.33f)
                                .placeholder(R.drawable.placeholder)
                                .into(ivPickedImage1!!)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        ivPickedImage1?.visibility = View.VISIBLE
                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    }

                }

                /*if (prefManager?.getValue("image_Car_1") != null &&
                    !prefManager.getValue("image_Car_1").equals("")
                ) {

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreConfigClass.car_1_uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    val myBitmap = BitmapFactory.decodeFile(imagePath?.absolutePath)
                    try {
*//*                        Glide.with(this).load(imagePath?.absolutePath).thumbnail(0.33f)
                            .into(ivPickedImage!!)*//*

//                        ivPickedImage?.setImageBitmap(myBitmap)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ivEditImg1?.visibility = View.VISIBLE
                    tvAddPhoto1?.visibility = View.GONE
                    tvCarphoto1?.visibility = View.GONE
                    TyreConfigClass.CarPhoto_1 = prefManager.getValue("image_Car_1")
                    relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    ivPickedImage?.visibility = View.VISIBLE

                }
                if (prefManager?.getValue("image_Car_2") != null &&
                    !prefManager?.getValue("image_Car_2").equals("")
                ) {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreConfigClass.car_2_uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    val myBitmap = BitmapFactory.decodeFile(imagePath?.absolutePath)
                    try {
*//*                        Glide.with(this).load(imagePath?.absolutePath).thumbnail(0.33f)
                            .into(ivPickedImage!!)*//*

//                        ivPickedImage1?.setImageBitmap(myBitmap)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ivEditImg2?.visibility = View.VISIBLE
                    tvAddPhoto2?.visibility = View.GONE
                    tvCarphoto2?.visibility = View.GONE
                    TyreConfigClass.CarPhoto_2 = prefManager?.getValue("image_Car_2")
                    relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    ivPickedImage1?.visibility = View.VISIBLE

                }*/


            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    fun showHideUpdatedPlacement(type: String, ischecked: Boolean) {
        Log.e("calltype", "" + type + " " + ischecked)
        if (type.equals("Type Rotation", ignoreCase = true)) {
            if (ischecked
            ) {
                if (llUpdatedPlacement?.visibility == View.GONE) {
                    if (type.equals("Type Rotation", ignoreCase = true)) {
                        Common.expand(llUpdatedPlacement!!)
                    }
                    if (llServiceExpanded?.visibility == View.GONE) {
                        Common.expand(llServiceExpanded!!)
                    }
                }
            } else {
                if (llUpdatedPlacement?.visibility == View.VISIBLE) {
                    Common.collapse(llUpdatedPlacement!!)
                }
            }
        }
        checkSubmitBtn()
    }

    override fun onClick(v: View?) {

        val id = v?.id
        when (id) {

            R.id.ivInfoAddService -> {
                showBottomSheetdialogNormal(
                    Common.commonPhotoChooseArr,
                    "Address Detail",
                    this,
                    Common.btn_filled,
                    false, Common.getStringBuilder(address)
                )
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivPickedImage -> {
                Log.e("getimagee", "--" + TyreConfigClass.CarPhoto_1)
                if (!TyreConfigClass.CarPhoto_1.equals("")) {
                    showImage(TyreConfigClass.CarPhoto_1)
                }
            }
            R.id.ivPickedImage1 -> {
                Log.e("getimagee2", "" + TyreConfigClass.CarPhoto_2)
                if (!TyreConfigClass.CarPhoto_2.equals("")) {
                    showImage(TyreConfigClass.CarPhoto_2)
                }
            }
            R.id.relCarPhotoAdd1 -> {
                selectImage1 = true
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }

            R.id.ivEditImg1 -> {

                selectImage1 = true
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )

            }
            R.id.relCarPhotoAdd2 -> {
                selectImage1 = false
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }
            R.id.ivEditImg2 -> {

                selectImage1 = false
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }

            R.id.tvSkipService -> {
                openSkipServiceDialogue("Provide Pending Reason", "", "")
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
//                    showHideUpdatedPlacement("")
                    ivAddServices?.setImageResource(R.mipmap.ic_minus_icon)
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = false
                    Common.expand(llServiceExpanded!!)
                    getSelectedService()
                    Log.e("calltype11", "" + selectedServiceArr?.contains("Type Rotation"))

                    if (selectedServiceArr?.contains("Type Rotation")!!) {
                        Common.expand(llUpdatedPlacement!!)
                    }

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

                checkSubmitBtn()
            }
            R.id.ivDueDate -> {
                openDatePicker()
            }
            R.id.btnSubmitAndComplete -> {

                addServiceApiCall()
            }

        }
    }

    private fun storeServiceDetailData() {
        val jsonObject = JsonObject()

        jsonObject.addProperty(TyreKey.moreSuggestion, edtMoreSuggestion?.text?.toString())
        jsonObject.addProperty(TyreKey.nextDueDate, tvNextServiceDueDate?.text.toString())
        jsonObject.addProperty(TyreKey.addServiceCarImage_1, TyreConfigClass.CarPhoto_1)
        jsonObject.addProperty(TyreKey.addServiceCarImage_2, TyreConfigClass.CarPhoto_2)

        val jsonArrayService: JsonArray? = JsonArray()

        selectedServiceArr?.clear()
        if (serviceList != null && serviceList?.size!! > 0) {
            for (i in serviceList?.indices!!) {
                if (serviceList?.get(i)?.isSelected!!) {
                    selectedServiceArr?.add(serviceList?.get(i)?.name!!)
                    jsonArrayService?.add(serviceList?.get(i)?.name)
                }
            }
        }

        val jsonArray = JsonArray()

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
        jsonObject.add(TyreKey.serviceArr, jsonArrayService)

        try {
            var selectedText: String? = ""
            if (radioLF_LR?.isChecked!!) {
                selectedText = radioLF_LR?.text?.toString() + "LF"
            } else {
                selectedText = radioLF_RR?.text?.toString() + "LF"
            }
            jsonObject.addProperty(TyreKey.radioGroupLF, selectedText)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        try {
            var selectedText: String? = ""
            if (radioRF_LR?.isChecked!!) {
                selectedText = radioRF_LR?.text?.toString() + "RF"
            } else {
                selectedText = radioRF_RR?.text?.toString() + "RF"
            }

            jsonObject.addProperty(TyreKey.radioGroupRF, selectedText)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        try {
            var selectedText: String? = ""
            if (radioLR_RF?.isChecked!!) {
                selectedText = radioLR_RF?.text?.toString() + "LR"
            } else {
                selectedText = radioLR_LF?.text?.toString() + "LR"
            }

            jsonObject.addProperty(TyreKey.radioGroupLR, selectedText)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        try {
            var selectedText: String? = ""
            if (radioRR_LF?.isChecked!!) {
                selectedText = radioRR_LF?.text?.toString() + "RR"
            } else {
                selectedText = radioRR_RF?.text?.toString() + "RR"
            }
            jsonObject.addProperty(TyreKey.radioGroupRR, selectedText)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        prefManager.setValue(TyreConfigClass.serviceDetailData, jsonObject.toString())

        Log.e("serviceObjStore", "" + jsonObject.toString())

    }

    private fun addServiceApiCall() {
        if (!Common.isConnectedToInternet(this)) {
            Common.showDialogue(this, "Oops!", "Your Internet is not connected", false)
            return
        }

        showDialogueTwo("Add Service Detail", "Are you sure? You want to Add Your service ?")

//        Common.showLoader(this)

    }

    private fun uploadImageIfExist() {

        var counter = 0

        if (prefManager.getValue("image_LF") != null && !prefManager.getValue("image_LF").equals("")) {

            Log.e("getimagepathlf", "" + prefManager.getValue("image_LF") + " -- " + TyreDetailCommonClass.tyre_Uri_LF)
            try {
                if (TyreDetailCommonClass.tyre_Uri_LF != null) {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreDetailCommonClass.tyre_Uri_LF!!)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    counter = counter + 1
                    val inputStream = imagePath?.inputStream()
                    uploadImage(imagePath!!, inputStream!!, "LF")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager.getValue("image_RF") != null && !prefManager.getValue("image_RF").equals("")) {

            Log.e("getimagepathrf", "" + prefManager.getValue("image_RF") + " -- " + TyreDetailCommonClass.tyre_Uri_RF)
            if (TyreDetailCommonClass.tyre_Uri_RF != null) {
                try {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreDetailCommonClass.tyre_Uri_RF!!)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    counter = counter + 1
                    val inputStream = imagePath?.inputStream()
//                val inputStream: InputStream? =
//                    this.contentResolver?.openInputStream(TyreDetailCommonClass.tyre_Uri_RF!!)

                    uploadImage(imagePath!!, inputStream!!, "RF")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (prefManager.getValue("image_LR") != null && !prefManager.getValue("image_LR").equals("")) {

            Log.e("getimagepathlr", "" + prefManager.getValue("image_LR") + " -- " + TyreDetailCommonClass.tyre_Uri_LR)
            if (TyreDetailCommonClass.tyre_Uri_LR != null) {
                try {
                    counter = counter + 1
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreDetailCommonClass.tyre_Uri_LR!!)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    val inputStream = imagePath?.inputStream()
//                val inputStream: InputStream? =
//                    this.contentResolver?.openInputStream(TyreDetailCommonClass.tyre_Uri_LR!!)


                    uploadImage(imagePath!!, inputStream!!, "LR")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (prefManager.getValue("image_RR") != null && !prefManager.getValue("image_RR").equals("")) {
            Log.e("getimagepatrr", "" + prefManager.getValue("image_RR") + " -- " + TyreDetailCommonClass.tyre_Uri_RR)
            if (TyreDetailCommonClass.tyre_Uri_RR != null) {
                try {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreDetailCommonClass.tyre_Uri_RR!!)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    Log.e("getimagepath2", "" + prefManager.getValue("image_RR"))
                    counter = counter + 1

                    val inputStream = imagePath?.inputStream()
//                val inputStream: InputStream? =
//                    this.contentResolver?.openInputStream(TyreDetailCommonClass.tyre_Uri_RR!!)

                    uploadImage(imagePath!!, inputStream!!, "RR")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (prefManager.getValue("image_Car_1") != null &&
            !prefManager.getValue("image_Car_1").equals("")
        ) {
            if (TyreConfigClass.car_1_uri != null) {
                try {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreConfigClass.car_1_uri!!)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    Log.e("getimagepath2", "" + "" + imagePath?.name + " ")
                    Log.e("getimagepath2", "" + TyreConfigClass.car_1_uri?.path)
                    counter = counter + 1
//                val inputStream: InputStream? =
//                    this.contentResolver?.openInputStream(TyreConfigClass.car_1_uri!!)

                    uploadImage(imagePath!!, imagePath.inputStream(), "car_1")

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (prefManager.getValue("image_Car_2") != null &&
            !prefManager.getValue("image_Car_2").equals("")
        ) {
            if (TyreConfigClass.car_2_uri != null) {
                try {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, TyreConfigClass.car_2_uri!!)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    Log.e("getimagepath2", "" + "" + imagePath?.name + " ")
                    Log.e("getimagepath2", "" + TyreConfigClass.car_2_uri?.path)
                    counter = counter + 1
//                val inputStream: InputStream? =
//                    this.contentResolver?.openInputStream(TyreConfigClass.car_2_uri!!)

                    uploadImage(imagePath!!, imagePath.inputStream(), "car_2")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        var WaitTime = 0L
        if (counter == 6) {
            WaitTime = 18200L
        } else if (counter == 5) {
            WaitTime = 15200L
        } else if (counter == 4) {
            WaitTime = 12200L
        } else if (counter == 3) {
            WaitTime = 9200L
        } else if (counter == 2) {
            WaitTime = 6200L
        } else if (counter == 1) {
            WaitTime = 3200L
        } else if (counter == 0) {
            WaitTime = 300L
        }

        Common.showLoader(this)
        val handler = Handler()
        handler.postDelayed(Runnable {
            val jsonObject = JsonObject()
//        jsonObject.addProperty("date_of_service", Common.getCurrentDateTimeSimpleFormat())
            jsonObject.addProperty("uuid", uuid)

            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                try {
                    val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()

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
                    } else {
                        jsonObject.addProperty(
                            "front_left_tyre_psi_in",
                            ""
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
                    } else {
                        jsonObject.addProperty(
                            "front_left_tyre_psi_out",
                            ""
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
                    } else {
                        jsonObject.addProperty(
                            "front_left_tyre_weight",
                            ""
                        )
                    }

                    if (jsonLF.get(TyreKey.visualDetailPhotoUrl) != null
                        && !jsonLF.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                    ) {

                        jsonObject.addProperty("front_left_tyre_wheel_image", jsonLF.get(TyreKey.visualDetailPhotoUrl)?.asString)
                    } else {
                        jsonObject.addProperty("front_left_tyre_wheel_image", "")
                    }


                    if (jsonLF.get(TyreKey.issueResolvedArr) != null) {

                        val arr = jsonLF.get(TyreKey.issueResolvedArr).asJsonArray
                        Log.e("getValues", "" + arr)
                        val gson = Gson()
                        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                        val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                        val jsonArr = JsonArray()
                        for (i in arrlist.indices) {
                            jsonArr.add(arrlist[i])
                        }
                        jsonObject.add("front_left_issues_to_be_resolved", jsonArr)
                    }


//               front_right_tyre_make

                    if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
                    ) {
                        try {
                            val str_ = prefManager.getValue(TyreConfigClass.TyreRFObject)
                            val jsonRF: JsonObject = JsonParser().parse(str_).getAsJsonObject()

                            if (jsonRF.get(TyreKey.vehicleMake) != null && !jsonRF.get(TyreKey.vehicleMake)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_make", jsonRF.get(TyreKey.vehicleMake)?.asString)
                            }
                            if (jsonRF.get(TyreKey.vehiclePattern) != null && !jsonRF.get(TyreKey.vehiclePattern)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_pattern", jsonRF.get(TyreKey.vehiclePattern)?.asString)
                            }
                            if (jsonRF.get(TyreKey.vehicleSize) != null && !jsonRF.get(TyreKey.vehicleSize)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_size", jsonRF.get(TyreKey.vehicleSize)?.asString)
                            }
                            if (jsonRF.get(TyreKey.manufaturingDate) != null && !jsonRF.get(TyreKey.manufaturingDate)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_manufacturing_date", jsonRF.get(TyreKey.manufaturingDate)?.asString)
                            }
                            if (jsonRF.get(TyreKey.sidewell) != null && !jsonRF.get(TyreKey.sidewell)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_side_wall", jsonRF.get(TyreKey.sidewell)?.asString)
                            }
                            if (jsonRF.get(TyreKey.shoulder) != null && !jsonRF.get(TyreKey.shoulder)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_shoulder", jsonRF.get(TyreKey.shoulder)?.asString)
                            }
                            if (jsonRF.get(TyreKey.treadDepth) != null && !jsonRF.get(TyreKey.treadDepth)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_tread_depth", jsonRF.get(TyreKey.treadDepth)?.asString)
                            }
                            if (jsonRF.get(TyreKey.treadWear) != null && !jsonRF.get(TyreKey.treadWear)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_tread_wear", jsonRF.get(TyreKey.treadWear)?.asString)
                            }
                            if (jsonRF.get(TyreKey.rimDamage) != null && !jsonRF.get(TyreKey.rimDamage)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_rim_demage", jsonRF.get(TyreKey.rimDamage)?.asString)
                            }
                            if (jsonRF.get(TyreKey.bubble) != null && !jsonRF.get(TyreKey.bubble)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_buldge_bubble", jsonRF.get(TyreKey.bubble)?.asString)
                            }
                            if (jsonRF.get(TyreKey.psiInTyreService) != null && !jsonRF.get(TyreKey.psiInTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_psi_in", jsonRF.get(TyreKey.psiInTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("front_right_tyre_psi_in", "")
                            }
                            if (jsonRF.get(TyreKey.psiOutTyreService) != null && !jsonRF.get(TyreKey.psiOutTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_psi_out", jsonRF.get(TyreKey.psiOutTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("front_right_tyre_psi_out", "")
                            }
                            if (jsonRF.get(TyreKey.weightTyreService) != null && !jsonRF.get(TyreKey.weightTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("front_right_tyre_weight", jsonRF.get(TyreKey.weightTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("front_right_tyre_weight", "")
                            }

                            if (jsonRF.get(TyreKey.visualDetailPhotoUrl) != null
                                && !jsonRF.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                            ) {
                                jsonObject.addProperty("front_right_tyre_wheel_image", jsonRF.get(TyreKey.visualDetailPhotoUrl)?.asString)
                            } else {
                                jsonObject.addProperty("front_right_tyre_wheel_image", "")
                            }

                            if (jsonRF.get(TyreKey.issueResolvedArr) != null) {

                                val arr = jsonRF.get(TyreKey.issueResolvedArr).asJsonArray
                                Log.e("getValues", "" + arr)
                                val gson = Gson()
                                val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                                val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                                val jsonArr = JsonArray()
                                for (i in arrlist.indices) {
                                    jsonArr.add(arrlist[i])
                                }
                                jsonObject.add("front_right_issues_to_be_resolved", jsonArr)
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()

                        }
                    }

//                back_left_tyre_make

                    if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
                    ) {
                        try {
                            val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                            val jsonLR: JsonObject = JsonParser().parse(str).getAsJsonObject()

                            if (jsonLR.get(TyreKey.vehicleMake) != null && !jsonLR.get(TyreKey.vehicleMake)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_make", jsonLR.get(TyreKey.vehicleMake)?.asString)
                            }
                            if (jsonLR.get(TyreKey.vehiclePattern) != null && !jsonLR.get(TyreKey.vehiclePattern)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_pattern", jsonLR.get(TyreKey.vehiclePattern)?.asString)
                            }
                            if (jsonLR.get(TyreKey.vehicleSize) != null && !jsonLR.get(TyreKey.vehicleSize)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_size", jsonLR.get(TyreKey.vehicleSize)?.asString)
                            }
                            if (jsonLR.get(TyreKey.manufaturingDate) != null && !jsonLR.get(TyreKey.manufaturingDate)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_manufacturing_date", jsonLR.get(TyreKey.manufaturingDate)?.asString)
                            }
                            if (jsonLR.get(TyreKey.sidewell) != null && !jsonLR.get(TyreKey.sidewell)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_side_wall", jsonLR.get(TyreKey.sidewell)?.asString)
                            }
                            if (jsonLR.get(TyreKey.shoulder) != null && !jsonLR.get(TyreKey.shoulder)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_shoulder", jsonLR.get(TyreKey.shoulder)?.asString)
                            }
                            if (jsonLR.get(TyreKey.treadDepth) != null && !jsonLR.get(TyreKey.treadDepth)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_tread_depth", jsonLR.get(TyreKey.treadDepth)?.asString)
                            }
                            if (jsonLR.get(TyreKey.treadWear) != null && !jsonLR.get(TyreKey.treadWear)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_tread_wear", jsonLR.get(TyreKey.treadWear)?.asString)
                            }
                            if (jsonLR.get(TyreKey.rimDamage) != null && !jsonLR.get(TyreKey.rimDamage)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_rim_demage", jsonLR.get(TyreKey.rimDamage)?.asString)
                            }
                            if (jsonLR.get(TyreKey.bubble) != null && !jsonLR.get(TyreKey.bubble)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_buldge_bubble", jsonLR.get(TyreKey.bubble)?.asString)
                            }
                            if (jsonLR.get(TyreKey.psiInTyreService) != null && !jsonLR.get(TyreKey.psiInTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_psi_in", jsonLR.get(TyreKey.psiInTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("back_left_tyre_psi_in", "")
                            }
                            if (jsonLR.get(TyreKey.psiOutTyreService) != null && !jsonLR.get(TyreKey.psiOutTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_psi_out", jsonLR.get(TyreKey.psiOutTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("back_left_tyre_psi_out", "")
                            }
                            if (jsonLR.get(TyreKey.weightTyreService) != null && !jsonLR.get(TyreKey.weightTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("back_left_tyre_weight", jsonLR.get(TyreKey.weightTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("back_left_tyre_weight", "")
                            }

                            if (jsonLR.get(TyreKey.visualDetailPhotoUrl) != null
                                && !jsonLR.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                            ) {
                                jsonObject.addProperty("back_left_tyre_wheel_image", jsonLR.get(TyreKey.visualDetailPhotoUrl)?.asString)
                            } else {
                                jsonObject.addProperty("back_left_tyre_wheel_image", "")
                            }

                            if (jsonLR.get(TyreKey.issueResolvedArr) != null) {

                                val arr = jsonLR.get(TyreKey.issueResolvedArr).asJsonArray
                                Log.e("getValues", "" + arr)
                                val gson = Gson()
                                val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                                val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                                val jsonArr = JsonArray()
                                for (i in arrlist.indices) {
                                    jsonArr.add(arrlist[i])
                                }
                                jsonObject.add("back_left_issues_to_be_resolved", jsonArr)
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()

                        }
                    }

//                back_right_tyre_make

                    if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
                    ) {
                        try {
                            val str_ = prefManager.getValue(TyreConfigClass.TyreRRObject)
                            val jsonRR: JsonObject = JsonParser().parse(str_).getAsJsonObject()

                            if (jsonRR.get(TyreKey.vehicleMake) != null && !jsonRR.get(TyreKey.vehicleMake)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_make", jsonRR.get(TyreKey.vehicleMake)?.asString)
                            }
                            if (jsonRR.get(TyreKey.vehiclePattern) != null && !jsonRR.get(TyreKey.vehiclePattern)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_pattern", jsonRR.get(TyreKey.vehiclePattern)?.asString)
                            }
                            if (jsonRR.get(TyreKey.vehicleSize) != null && !jsonRR.get(TyreKey.vehicleSize)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_size", jsonRR.get(TyreKey.vehicleSize)?.asString)
                            }
                            if (jsonRR.get(TyreKey.manufaturingDate) != null && !jsonRR.get(TyreKey.manufaturingDate)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_manufacturing_date", jsonRR.get(TyreKey.manufaturingDate)?.asString)
                            }
                            if (jsonRR.get(TyreKey.sidewell) != null && !jsonRR.get(TyreKey.sidewell)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_side_wall", jsonRR.get(TyreKey.sidewell)?.asString)
                            }
                            if (jsonRR.get(TyreKey.shoulder) != null && !jsonRR.get(TyreKey.shoulder)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_shoulder", jsonRR.get(TyreKey.shoulder)?.asString)
                            }
                            if (jsonRR.get(TyreKey.treadDepth) != null && !jsonRR.get(TyreKey.treadDepth)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_tread_depth", jsonRR.get(TyreKey.treadDepth)?.asString)
                            }
                            if (jsonRR.get(TyreKey.treadWear) != null && !jsonRR.get(TyreKey.treadWear)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_tread_wear", jsonRR.get(TyreKey.treadWear)?.asString)
                            }
                            if (jsonRR.get(TyreKey.rimDamage) != null && !jsonRR.get(TyreKey.rimDamage)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_rim_demage", jsonRR.get(TyreKey.rimDamage)?.asString)
                            }
                            if (jsonRR.get(TyreKey.bubble) != null && !jsonRR.get(TyreKey.bubble)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_buldge_bubble", jsonRR.get(TyreKey.bubble)?.asString)
                            }
                            if (jsonRR.get(TyreKey.psiInTyreService) != null && !jsonRR.get(TyreKey.psiInTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_psi_in", jsonRR.get(TyreKey.psiInTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("back_right_tyre_psi_in", "")
                            }
                            if (jsonRR.get(TyreKey.psiOutTyreService) != null && !jsonRR.get(TyreKey.psiOutTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_psi_out", jsonRR.get(TyreKey.psiOutTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("back_right_tyre_psi_out", "")
                            }
                            if (jsonRR.get(TyreKey.weightTyreService) != null && !jsonRR.get(TyreKey.weightTyreService)?.asString.equals("")) {
                                jsonObject.addProperty("back_right_tyre_weight", jsonRR.get(TyreKey.weightTyreService)?.asString)
                            } else {
                                jsonObject.addProperty("back_right_tyre_weight", "")
                            }

                            if (jsonRR.get(TyreKey.visualDetailPhotoUrl) != null
                                && !jsonRR.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                            ) {
                                jsonObject.addProperty("back_right_tyre_wheel_image", jsonRR.get(TyreKey.visualDetailPhotoUrl)?.asString)
                            } else {
                                jsonObject.addProperty("back_right_tyre_wheel_image", "")
                            }
                            if (jsonRR.get(TyreKey.issueResolvedArr) != null) {

                                val arr = jsonRR.get(TyreKey.issueResolvedArr).asJsonArray
                                Log.e("getValues", "" + arr)
                                val gson = Gson()
                                val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                                val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                                val jsonArr = JsonArray()
                                for (i in arrlist.indices) {
                                    jsonArr.add(arrlist[i])
                                }
                                jsonObject.add("back_right_issues_to_be_resolved", jsonArr)
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()

                        }
                    }

                    jsonObject.addProperty("service_suggestions", edtMoreSuggestion?.text?.toString())
                    jsonObject.addProperty("next_service_due", selectedDateNextServiceDue)
                    jsonObject.addProperty("car_photo_1", TyreConfigClass.CarPhoto_1)
                    jsonObject.addProperty("car_photo_2", TyreConfigClass.CarPhoto_2)
                    jsonObject.addProperty("status", "complete")

                    val jsonArrayService = JsonArray()

                    selectedServiceArr?.clear()
                    if (serviceList != null && serviceList?.size!! > 0) {
                        for (i in serviceList?.indices!!) {
                            if (serviceList?.get(i)?.isSelected!!) {
                                selectedServiceArr?.add(serviceList?.get(i)?.name!!)
                                jsonArrayService.add(serviceList?.get(i)?.id)
                            }
                        }
                    }

                    val jsonArraySuggestion = JsonArray()

                    selectedSuggestionArr?.clear()

                    if (suggestionArray != null) {
                        for (i in suggestionArray?.indices!!) {
                            if (suggestionArray?.get(i)?.isSelected!!) {
                                selectedSuggestionArr?.add(
                                    suggestionArray?.get(i)?.issueName!!
                                )
                            }
                        }
                    }

                    if (selectedSuggestionArr != null && selectedSuggestionArr?.size!! > 0) {
                        for (i in selectedSuggestionArr?.indices!!) {
                            jsonArraySuggestion.add(selectedSuggestionArr?.get(i))
                        }
                    }

                    jsonObject.add("service", jsonArrayService)
                    jsonObject.add("technician_suggestions", jsonArraySuggestion)

//                    "front_left_tyre_wheel_rotation": "LR",
//                    "back_left_tyre_wheel_rotation": "LF",
//                    "front_right_tyre_wheel_rotation": "RR",
//                    "back_right_tyre_wheel_rotation": "RF",


                    var selectedTextLF: String? = ""
                    var selectedTextRF: String? = ""
                    var selectedTextLR: String? = ""
                    var selectedTextRR: String? = ""
                    try {
                        if (radioGroupLF?.checkedRadioButtonId != -1) {
                            if (radioLF_LR?.isChecked!!) {
                                selectedTextLF = "LR"
                            } else {
                                selectedTextLF = "RR"
                            }
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

                    try {
                        if (radioGroupRF?.checkedRadioButtonId != -1) {
                            if (radioRF_LR?.isChecked!!) {
                                selectedTextRF = "LR"
                            } else {
                                selectedTextRF = "RR"
                            }
                        }

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

                    try {
                        if (radioGroupLR?.checkedRadioButtonId != -1) {
                            if (radioLR_RF?.isChecked!!) {
                                selectedTextLR = "RF"
                            } else {
                                selectedTextLR = "LF"
                            }
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

                    try {
                        if (radioGroupRR?.checkedRadioButtonId != -1) {
                            if (radioRR_LF?.isChecked!!) {
                                selectedTextRR = "LF"
                            } else {
                                selectedTextRR = "RF"
                            }
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

                    jsonObject.addProperty("front_left_tyre_wheel_rotation", selectedTextLF)
                    jsonObject.addProperty("back_left_tyre_wheel_rotation", selectedTextLR)
                    jsonObject.addProperty("front_right_tyre_wheel_rotation", selectedTextRF)
                    jsonObject.addProperty("back_right_tyre_wheel_rotation", selectedTextRR)

                    Log.e("getfinalobject", "" + jsonObject)
                    Log.e("getObjectT__", "" + jsonObject)

                    serviceViewModel?.callApiAddService(
                        jsonObject,
                        prefManager.getAccessToken()!!,
                        this
                    )

                    serviceViewModel?.getAddService()?.observe(this, androidx.lifecycle.Observer {
                        if (it != null) {
                            if (it.success) {

                                Common.hideLoader()

                                showDialogue("Success", "Your Service Added Successfully", true)

                            } else {
                                Common.hideLoader()

                                try {
                                    showShortToast(TyreKey.something_went_wrong, this)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        } else {
                            Common.hideLoader()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Common.hideLoader()
        }, WaitTime)

    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MONTH, 0)
        calendar.add(Calendar.YEAR, 0)
        var future: Date? = null
        var setdefault: Date? = null

        setdefault = calendar.getTime()
        if (selectedDate.equals("")) {
            future = calendar.getTime()
        } else {
//            Wed May 19 12:55:46 GMT+05:30 2021
            var date_: Date? = null
            val formatter = SimpleDateFormat("dd MMM yyyy")
            try {
                date_ = formatter.parse(selectedDate)
                Log.e("formated_date ", date_.toString() + "")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            future = date_
        }
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
            .minDateRange(setdefault) //.mustBeOnFuture()
            //.minutesStep(15)
            //.mustBeOnFuture()
            //.defaultDate(defaultDate)
            // .minDateRange(minDate)
            // .maxDateRange(maxDate)

            .title("Simple")
            .listener(object : SingleDateAndTimePickerDialogDueDate.Listener {
                override fun onDateSelected(date: Date, str: String) {
                    lltransparent?.visibility = View.GONE
                    if (str.equals("")) {
                        simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
                        Log.e("getdatee", "" + simpleDateFormat?.format(date))
                        selectedDate = simpleDateFormat?.format(date)
                        Log.e("getdatee00", "" + selectedDate)
                        tvNextServiceDueDate?.text = selectedDate

                        selectedDateNextServiceDue = selectedDate
                        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        val formatterDisplay = SimpleDateFormat("dd MMMM yyyy")
                        val dateInString = formatterDisplay.parse(selectedDate!!)
                        val displayDate = formatter.format(dateInString)

                        selectedDateNextServiceDue = displayDate

                    } else if (str.equals("Reset")) {
                        Log.e("getdatee2", "" + selectedDate)
                        selectedDate = ""
                        selectedDateNextServiceDue = ""
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
        stringBuilder: StringBuilder
    ) {
        val nullparent: ViewGroup? = null
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, nullparent, false)
        val dialog =
            this.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

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
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        btnText: String

    ) {
        val nullparent: ViewGroup? = null
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, nullparent, false)
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
        val arrayAdapter = context?.let { DialogueAdpater(array, it, this) }
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
        }
        dialog?.show()

    }

    private fun openSkipServiceDialogue(titleStr: String, stringExtra: String?, s: String) {
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

        try {
            skipList = ArrayList<IssueResolveModel>()

            if (commentList?.size!! > 0) {
                for (i in commentList?.indices!!) {
                    skipList?.add(IssueResolveModel(commentList?.get(i)?.name!!, commentList?.get(i)?.id!!, false))
                    Log.e("getdta", "" + commentList?.get(i)?.name!! + " " + commentList?.get(i)?.id!! + " " + false)
                }
            } else {
                if (prefManager.getCommentList(TyreConfigClass.commentList) != null && prefManager.getCommentList(TyreConfigClass.commentList)?.size > 0) {
                    commentList?.clear()
                    for (i in prefManager.getCommentList(TyreConfigClass.commentList)?.indices) {
                        val model = CommentListData(
                            prefManager.getServiceList(TyreConfigClass.serviceList)?.get(i)?.id!!,
                            prefManager.getServiceList(TyreConfigClass.serviceList)?.get(i)?.name
                        )
                        commentList?.add(model)
                    }
                }
                for (i in commentList?.indices!!) {
                    skipList?.add(IssueResolveModel(commentList?.get(i)?.name!!, commentList?.get(i)?.id!!, false))
                    Log.e("getdta", "" + commentList?.get(i)?.name!! + " " + commentList?.get(i)?.id!! + " " + false)
                }
            }

            for (i in skipList?.indices!!) {
                if (!stringExtra.equals("")) {
                    if (stringExtra?.toInt() == skipList?.get(i)?.id) {
                        skipList?.get(i)?.isSelected = true
                    }
                }
            }

            val tyreSuggestionAdapter = TyreSuggestionAdpater(skipList!!, this, this, true, false)
            tyreSuggestionAdapter.onclick = this
            pendingReasonRecycView?.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            pendingReasonRecycView?.adapter = tyreSuggestionAdapter

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)

        tvTitleText?.text = titleStr
        ivClose?.setOnClickListener {
            builder.dismiss()
        }
        btnConfirm.setOnClickListener {

            if (!Common.isConnectedToInternet(this)) {
                Common.showDialogue(this, "Oops!", "Your Internet is not connected", false)
                return@setOnClickListener
            }

            val jsonArr = JsonArray()

            for (i in skipList?.indices!!) {
                if (skipList?.get(i)?.isSelected!!) {
                    Log.e("getselected", "" + skipList?.get(i)?.issueName)
                    jsonArr.add(skipList?.get(i)?.id)
                }
            }

            if (jsonArr.size() == 0) {
                Toast.makeText(this, "Please Select Reason", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            builder.dismiss()
            Common.showLoader(this)
            val jsonObject = JsonObject()

            jsonObject.addProperty("uuid", uuid)
            jsonObject.add("comment_id", jsonArr)

            if (s.equals("")) {
                jsonObject.addProperty("status", "skip")
            }

            if (s.equals("update")) {
                serviceViewModel?.callApiUpdateService(
                    jsonObject,
                    prefManager.getAccessToken()!!,
                    this
                )
            } else {
                serviceViewModel?.callApiAddService(
                    jsonObject,
                    prefManager.getAccessToken()!!,
                    this
                )
            }

            serviceViewModel?.getAddService()?.observe(this, androidx.lifecycle.Observer {
                if (it != null) {
                    if (it.success) {
                        Common.hideLoader()
                        val intent = Intent(this, SkippedServiceDetailActivity::class.java)
                        intent.putExtra("color", color)
                        intent.putExtra("makeModel", makeModel)
                        intent.putExtra("regNumber", regNumber)
                        intent.putExtra("carImage", carImage)
                        intent.putExtra("uuid", uuid)
                        intent.putExtra("colorcode", colorCode)
                        intent.putExtra("address", address)
                        intent.putExtra("which", "skip_dialogue")

                        var reason: String? = ""
                        var reasonId: Int? = -1
                        if (skipList?.size!! > 0) {
                            for (i in skipList?.indices!!) {
                                if (skipList?.get(i)?.isSelected!!) {
                                    reason = skipList?.get(i)?.issueName
                                    reasonId = skipList?.get(i)?.id
                                    Log.e("getreason", "" + reason)
                                }
                            }
                        }
                        intent.putExtra("reason", reason)
                        intent.putExtra("reasonId", "" + reasonId)
                        startActivityForResult(intent, 106)
                    } else {
                        Common.hideLoader()
                        showShortToast("Something Went Wrong", this)
                    }
                } else {
                    Common.hideLoader()
                    showShortToast("Something Went Wrong", this)
                }
            })

        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 11) {
            Log.e("getserviceclick", "" + serviceList?.get(variable)?.name + " " + serviceList?.get(variable)?.isSelected)

            showHideUpdatedPlacement(serviceList?.get(variable)?.name!!, serviceList?.get(variable)?.isSelected!!)
            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    if (!serviceList?.get(variable)?.isSelected!!) {
                        checkServiceUnCheck(variable)
                    } else {
                        checkServiceCheck(variable)
                    }
                }
            }


        }
        if (check == 0) {

            Log.e("getposition0", "" + suggestionArr[variable])
        } else if (check == 10) {

            if (imagePickerDialog != null && imagePickerDialog?.isShowing!!) {
                imagePickerDialog?.dismiss()
            }
            if (Common.commonPhotoChooseArr[variable].equals("Gallery")) {
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission((this@AddServiceDetailsActivity))
                } else {
                    try {
                        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                        }
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }
                if (result == true) {
                    try {

                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                        }
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }

                }
            }
            if (Common.commonPhotoChooseArr[variable].equals("Camera")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.MANAGE_DOCUMENTS)
                        == PackageManager.PERMISSION_DENIED
                    ) {
                        //permission was not enabled
                        val permission = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_DOCUMENTS
                        )
                        //show popup to request permission
                        requestPermissions(permission, PERMISSION_CODE)
                    } else {
                        //permission already granted
                        openCamera()
                    }
                } else {
                    //system os is < marshmallow
                    openCamera()
                }
            }
        }

    }

    private fun checkServiceCheck(variable: Int) {
        getSelectedService()

        if (selectedServiceArr?.contains("Nitrogen Top Up")!! || selectedServiceArr?.contains("Nitrogen Refill")!!) {
            var count = 0
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {

                            Log.e("TAG", "checkServiceCheck: ")
                        } else {
                            count = count + 1
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {

                            Log.e("TAG", "checkServiceCheck: ")
                        } else {
                            count = count + 1
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {

                        } else {
                            count = count + 1
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {

                        } else {
                            count = count + 1
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            if (count > 0) {
                showDialogueTwoForService(
                    "Enable Service", "Are You sure ? You want to enable this service ?\n" + "" +
                            "You need to add Psi-In and Psi-Out Details for those completed tyre details.", "", true
                )
            }
        }

        Log.e("getservicecall1", "" + selectedServiceArr?.contains("Wheel Balancing"))
        if (selectedServiceArr?.contains("Wheel Balancing")!!) {
            var count = 0
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)
                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {

                        } else {
                            count = count + 1
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {

                        } else {
                            count = count + 1
                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {

                        } else {
                            count = count + 1
                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {
                        if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {

                        } else {
                            count = count + 1
                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            Log.e("getservicecall1", "" + count)
            if (count > 0) {

                showDialogueTwoForService(
                    "Enable Service", "Are You sure ? You want to enable this service ?\n" + "" +
                            "You need to add Weight Details for those completed tyre details.", "wheelBalancing", true
                )
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            getStoredObjects("")
        }

    }

    suspend fun checkServiceUnCheck(position: Int) {
        getSelectedService()

        Log.e("getservicecall", "" + selectedServiceArr)
        Log.e("getservicecall", "" + serviceList?.get(position)?.isDisable+" "+serviceList?.get(position)?.name)

        if (!selectedServiceArr?.contains("Nitrogen Top Up")!! && !selectedServiceArr?.contains("Nitrogen Refill")!!) {
            var count = 0
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            if (count > 0) {
                showDialogueTwoForService("Remove Service", "Are You sure ? You want to remove Nitrogen Refill and Nitrogen Top up Service ?", "", false)
            }
        }

        Log.e("getservicecall1", "" + selectedServiceArr?.contains("Wheel Balancing"))
        if (!selectedServiceArr?.contains("Wheel Balancing")!!) {
            var count = 0
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjectslf", "" + jsonLF)

                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        count = count + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            Log.e("getservicecall1", "" + count)
            if (count > 0) {

                showDialogueTwoForService("Remove Service", "Are You sure ? You want to remove Wheel Balancing Service ?", "wheelBalancing", false)

            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            getStoredObjects("")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(context: FragmentActivity?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (this.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } !== PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.MANAGE_DOCUMENTS
                ) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.MANAGE_DOCUMENTS
                    )
                ) {
                    val alertBuilder = android.app.AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(
                        android.R.string.yes
                    ) { dialog, which ->
                        requestPermissions(
                            arrayOf<String>(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.MANAGE_DOCUMENTS
                            ),
                            123
                        )
                    }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {

                    requestPermissions(
                        arrayOf<String>(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.MANAGE_DOCUMENTS

                        ), 123
                    );

                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }

    }

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val nullparent: ViewGroup? = null
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, nullparent, false)
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
        val arrayAdapter = context?.let { DialogueAdpater(array, it, this) }
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
                    intent.putExtra("selectedTyre", "LF")
                    intent.putExtra("title", "Select Tyre Make - LF")
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
                    intent.putExtra("selectedTyre", "LR")
                    intent.putExtra("title", "Select Tyre Make - LR")
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

        getSelectedService()
        if (selectedServiceArr?.size!! > 0
        ) {
            return true
        } else {
            Toast.makeText(this, "Please Select Service", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getcall", "call0" + requestCode)
        when (requestCode) {

            106 -> {
                Log.e("get106", "" + requestCode + " " + resultCode)
                if (resultCode == RESULT_OK) {
                    if (data?.hasExtra("back")!!) {
                        if (data.getStringExtra("back").equals("true")) {
                            finish()
                        }
                    } else {
                        openSkipServiceDialogue("Change Pending Reason", data.getStringExtra("reasonId"), "update")
                    }
                }
            }
            IMAGE_CAPTURE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, image_uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

                    if (selectImage1) {

                        ivPickedImage?.setImageURI(image_uri)
                        ivPickedImage?.visibility = View.VISIBLE
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    } else {
                        ivPickedImage1?.setImageURI(image_uri)
                        ivPickedImage1?.visibility = View.VISIBLE

                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }

//                    ivEditImg2?.visibility = View.VISIBLE
//                    tvAddPhoto1?.visibility = View.GONE
//                    tvCarphoto1?.visibility = View.GONE
//                    relTyrePhotoAdd?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    if (Common.isConnectedToInternet(this)) {
                        val inputStream: InputStream? = imagePath?.inputStream()

                        if (selectImage1) {
                            prefManager.removeValue("image_Car_1")
                        } else {
                            prefManager.removeValue("image_Car_2")
                        }
                        imagePath?.let { uploadImage(it, inputStream!!, "") }
                    } else {

                        if (selectImage1) {
                            prefManager.setValue("image_Car_1", image_uri.toString())
                            prefManager?.setValue("image_1_path", imagePath?.path)
                            TyreConfigClass.CarPhoto_1 = image_uri.toString()
                            TyreConfigClass.car_1_uri = image_uri
                        } else {
                            prefManager.setValue("image_Car_2", image_uri.toString())
                            prefManager?.setValue("image_2_path", imagePath?.path)
                            TyreConfigClass.CarPhoto_2 = image_uri.toString()
                            TyreConfigClass.car_2_uri = image_uri
                        }

                        storeServiceDetailData()
                        checkSubmitBtn()

                    }
                }

            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val auxFile = File(mCurrentPhotoPath)
                    Log.e("getFile", "" + mCurrentPhotoPath + " " + Uri.parse(mCurrentPhotoPath))
                    if (selectImage1) {
                        ivPickedImage?.setImageURI(Uri.parse(mCurrentPhotoPath))
                        ivPickedImage?.visibility = View.VISIBLE
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    } else {
                        ivPickedImage1?.setImageURI(Uri.parse(mCurrentPhotoPath))
                        ivPickedImage1?.visibility = View.VISIBLE

                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }


                    if (Common.isConnectedToInternet(this)) {
                        val inputStream: InputStream? = auxFile.inputStream()
//                            this.contentResolver?.openInputStream(Uri.parse(mCurrentPhotoPath)!!)
                        if (selectImage1) {
                            prefManager.removeValue("image_Car_1")
                        } else {
                            prefManager.removeValue("image_Car_2")
                        }
                        auxFile.let { uploadImage(it, inputStream!!, "") }
                    } else {

                        if (selectImage1) {
                            prefManager.setValue("image_Car_1", Uri.parse(mCurrentPhotoPath).toString())
                            prefManager.setValue("image_1_path", auxFile.path)
                            TyreConfigClass.CarPhoto_1 = Uri.parse(mCurrentPhotoPath).toString()
                            TyreConfigClass.car_1_uri = Uri.parse(mCurrentPhotoPath)
                            Log.e("getimagee1311", "--" + TyreConfigClass.CarPhoto_1)
                        } else {
                            prefManager.setValue("image_Car_2", Uri.parse(mCurrentPhotoPath).toString())
                            prefManager.setValue("image_2_path", auxFile.path)
                            TyreConfigClass.CarPhoto_2 = Uri.parse(mCurrentPhotoPath).toString()
                            TyreConfigClass.car_2_uri = Uri.parse(mCurrentPhotoPath)
                        }
                        storeServiceDetailData()
                        checkSubmitBtn()

                    }
                }
            }

            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val selectedImage = data?.data

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, selectedImage)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

                    Log.i("imagePath", "++++" + imagePath)
                    if (selectImage1) {
                        ivPickedImage?.setImageURI(selectedImage)
                        ivPickedImage?.visibility = View.VISIBLE
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    } else {
                        ivPickedImage1?.setImageURI(selectedImage)
                        ivPickedImage1?.visibility = View.VISIBLE

                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    }

                    if (Common.isConnectedToInternet(this)) {
                        val inputStream: InputStream? = imagePath?.inputStream()
//                            this.contentResolver?.openInputStream(selectedImage!!)
                        if (selectImage1) {
                            prefManager.removeValue("image_Car_1")
                        } else {
                            prefManager.removeValue("image_Car_2")
                        }
                        imagePath?.let { uploadImage(it, inputStream!!, "") }
                    } else {

                        if (selectImage1) {
                            prefManager.setValue("image_Car_1", selectedImage.toString())
                            prefManager?.setValue("image_1_path", imagePath?.path)
                            TyreConfigClass.CarPhoto_1 = selectedImage.toString()
                            TyreConfigClass.car_1_uri = selectedImage
                            Log.e("getimagee13144", "---" + TyreConfigClass.CarPhoto_1)
                        } else {
                            prefManager.setValue("image_Car_2", selectedImage.toString())
                            prefManager?.setValue("image_2_path", imagePath?.path)
                            TyreConfigClass.CarPhoto_2 = selectedImage.toString()
                            TyreConfigClass.car_2_uri = selectedImage
                        }
                        storeServiceDetailData()
                        checkSubmitBtn()
                    }

                }
            }

            1000 -> {

                Log.e("getCall", "call0" + TyreConfigClass.clickedTyre)
                CoroutineScope(Dispatchers.Main).launch {

                    if (!TyreConfigClass.LFCompleted) {
                        if (TyreConfigClass.clickedTyre.equals("LF")) {
                            if (TyreConfigClass.selectedTyreConfigType.equals("LF")) {
                                ivTyre1?.setImageDrawable(
                                    this@AddServiceDetailsActivity.resources?.getDrawable(
                                        R.drawable.ic_completed_tyre_config
                                    )
                                )
                                ivtyreLeftFront?.visibility = View.VISIBLE
                                ivInfoImgLF?.visibility = View.GONE
                                TyreConfigClass.LFCompleted = true

                                try {
                                    Glide.with(this@AddServiceDetailsActivity)
                                        .load(TyreDetailCommonClass.vehicleMakeURL)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .thumbnail(0.33f)
                                        .placeholder(R.drawable.placeholder)
                                        .into(ivTyreRightFront!!)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                Log.e("getUrl", "" + TyreDetailCommonClass.vehicleMakeURL)
                            } else {
                                ivInfoImgLF?.visibility = View.VISIBLE
                            }
                        }
                    }
                    if (!TyreConfigClass.RFCompleted) {
                        if (TyreConfigClass.clickedTyre.equals("RF")) {
                            if (TyreConfigClass.selectedTyreConfigType.equals("RF")) {
                                ivTyre3?.setImageDrawable(
                                    this@AddServiceDetailsActivity.resources?.getDrawable(
                                        R.drawable.ic_completed_tyre_config
                                    )
                                )
                                ivTyreRightFront?.visibility = View.VISIBLE
                                TyreConfigClass.RFCompleted = true
                                try {
                                    Glide.with(this@AddServiceDetailsActivity)
                                        .load(TyreDetailCommonClass.vehicleMakeURL)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .thumbnail(0.33f)
                                        .placeholder(R.drawable.placeholder)
                                        .into(ivTyreRightFront!!)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                Log.e("getUrl", "" + TyreDetailCommonClass.vehicleMakeURL)
                                ivInfoImgRF?.visibility = View.GONE
                            } else {
                                ivInfoImgRF?.visibility = View.VISIBLE

                            }
                        }
                    }
                    if (!TyreConfigClass.LRCompleted) {
                        if (TyreConfigClass.clickedTyre.equals("LR")) {
                            if (TyreConfigClass.selectedTyreConfigType.equals("LR")) {
                                ivTyre2?.setImageDrawable(
                                    this@AddServiceDetailsActivity.resources?.getDrawable(
                                        R.drawable.ic_completed_tyre_config
                                    )
                                )
                                ivtyreLeftRear?.visibility = View.VISIBLE
                                ivInfoImgLR?.visibility = View.GONE
                                TyreConfigClass.LRCompleted = true

                                try {
                                    Glide.with(this@AddServiceDetailsActivity)
                                        .load(TyreDetailCommonClass.vehicleMakeURL)
                                        .thumbnail(0.33f)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .into(ivTyreRightFront!!)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                Log.e("getUrl", "" + TyreDetailCommonClass.vehicleMakeURL)
                            } else {
                                ivInfoImgLR?.visibility = View.VISIBLE

                            }
                        }
                    }
                    if (!TyreConfigClass.RRCompleted) {
                        if (TyreConfigClass.clickedTyre.equals("RR")) {
                            if (TyreConfigClass.selectedTyreConfigType.equals("RR")) {
                                ivTyre4?.setImageDrawable(
                                    this@AddServiceDetailsActivity.resources?.getDrawable(
                                        R.drawable.ic_completed_tyre_config
                                    )
                                )
                                ivTyreRightRear?.visibility = View.VISIBLE
                                ivInfoImgRR?.visibility = View.GONE
                                TyreConfigClass.RRCompleted = true

                                try {
                                    Glide.with(this@AddServiceDetailsActivity)
                                        .load(TyreDetailCommonClass.vehicleMakeURL)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .thumbnail(0.33f)
                                        .placeholder(R.drawable.placeholder)
                                        .into(ivTyreRightFront!!)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                Log.e("getUrl", "" + TyreDetailCommonClass.vehicleMakeURL)
                            } else {
                                ivInfoImgRR?.visibility = View.VISIBLE

                            }
                        }

                    }
                    Log.e("getValuesss", "" + TyreConfigClass.selectedTyreConfigType)

                    Log.e("getValuesss_all", TyreDetailCommonClass.tyreType!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehicleMake!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehicleMakeId!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehicleMakeURL!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehiclePattern!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehiclePatternId!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehicleSize!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.vehicleSizeId!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.manufaturingDate!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.psiInTyreService!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.psiOutTyreService!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.weightTyreService!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.sidewell!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.shoulder!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.treadDepth!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.treadWear!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.rimDamage!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.bubble!!)
                    Log.e("getValuesss_all", "" + TyreDetailCommonClass.issueResolvedArr!!)
                    Log.e("getValuesss_all", TyreDetailCommonClass.visualDetailPhotoUrl!!)

                    Log.e("getValuesss_all--1 make", TyreDetailCommonClass.chk1Make!!)
                    Log.e("getValuesss_all--2 make", TyreDetailCommonClass.chk2Make!!)
                    Log.e("getValuesss_all--3 make", TyreDetailCommonClass.chk3Make!!)
                    Log.e("getValuesss_all--1 pat", TyreDetailCommonClass.chk1Pattern!!)
                    Log.e("getValuesss_all--2 pat", TyreDetailCommonClass.chk2Pattern!!)
                    Log.e("getValuesss_all--3 pat", TyreDetailCommonClass.chk3Pattern!!)
                    Log.e("getValuesss_all--1 size", TyreDetailCommonClass.chk1Size!!)
                    Log.e("getValuesss_all--2 size", TyreDetailCommonClass.chk2Size!!)
                    Log.e("getValuesss_all--3 size", TyreDetailCommonClass.chk3Size!!)
                    if (TyreDetailCommonClass.tyre_Uri_LF != null) {
                        Log.e("getValuesss_all--3 uri", "" + TyreDetailCommonClass.tyre_Uri_LF!!)
                    }
                    if (TyreDetailCommonClass.tyre_Uri_LR != null) {
                        Log.e("getValuesss_all--3 uri", "" + TyreDetailCommonClass.tyre_Uri_LR!!)
                    }
                    if (TyreDetailCommonClass.tyre_Uri_RF != null) {
                        Log.e("getValuesss_all--3 uri", "" + TyreDetailCommonClass.tyre_Uri_RF!!)
                    }
                    if (TyreDetailCommonClass.tyre_Uri_RR != null) {
                        Log.e("getValuesss_all--3 uri", "" + TyreDetailCommonClass.tyre_Uri_RR!!)
                    }

                    val json = JsonObject()
                    val jsonArr = JsonArray()
                    json.addProperty(TyreKey.tyreType, TyreDetailCommonClass.tyreType)
                    json.addProperty(TyreKey.vehicleMake, TyreDetailCommonClass.vehicleMake)
                    json.addProperty(TyreKey.vehicleMakeId, TyreDetailCommonClass.vehicleMakeId)
                    json.addProperty(
                        TyreKey.vehicleMakeURL,
                        TyreDetailCommonClass.vehicleMakeURL
                    )
                    json.addProperty(
                        TyreKey.vehiclePattern,
                        TyreDetailCommonClass.vehiclePattern
                    )
                    json.addProperty(
                        TyreKey.vehiclePatternId,
                        TyreDetailCommonClass.vehiclePatternId
                    )
                    json.addProperty(TyreKey.vehicleSize, TyreDetailCommonClass.vehicleSize)
                    json.addProperty(TyreKey.vehicleSizeId, TyreDetailCommonClass.vehicleSizeId)
                    json.addProperty(
                        TyreKey.manufaturingDate,
                        TyreDetailCommonClass.manufaturingDate
                    )
                    json.addProperty(
                        TyreKey.psiInTyreService,
                        TyreDetailCommonClass.psiInTyreService
                    )
                    json.addProperty(
                        TyreKey.psiOutTyreService,
                        TyreDetailCommonClass.psiOutTyreService
                    )
                    json.addProperty(
                        TyreKey.weightTyreService,
                        TyreDetailCommonClass.weightTyreService
                    )
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

                    Log.e("isCompletedd0", "" + TyreConfigClass.LFCompleted)
                    Log.e("isCompletedd1", "" + TyreConfigClass.LRCompleted)
                    Log.e("isCompletedd11", "" + TyreConfigClass.RFCompleted)
                    Log.e("isCompletedd2", "" + TyreConfigClass.RRCompleted)

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

                    /* if (TyreConfigClass.CarPhoto_1 != null && !TyreConfigClass.CarPhoto_1.equals(
                             ""
                         )
                     ) {
                         ivPickedImage?.setImageURI(Uri.parse(TyreConfigClass.CarPhoto_1))
                     }
                     if (TyreConfigClass.CarPhoto_2 != null && !TyreConfigClass.CarPhoto_2.equals(
                             ""
                         )
                     ) {
                         ivPickedImage1?.setImageURI(Uri.parse(TyreConfigClass.CarPhoto_2))
                     }*/

                    getStoredObjects("")

                    checkSubmitBtn()
                }

            }
        }
    }


    private fun checkSubmitBtn() {

        btnSubmitAndComplete?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_blue))
        btnSubmitAndComplete?.isClickable = false

        try {
            if (edtMoreSuggestion?.text?.toString() != null) {
                TyreConfigClass.moreSuggestions = edtMoreSuggestion?.text?.toString()!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.e("getImagess", "" + TyreConfigClass.moreSuggestions)

        if (selectedDateNextServiceDue != null && !selectedDateNextServiceDue
                .equals("")
        ) {

        } else {
//            Toast.makeText(this, "Please Select Next Due Date", Toast.LENGTH_SHORT).show()
            return
        }

        var count = 0
        var tyreRotation = false
        for (i in serviceList?.indices!!) {
            if (serviceList?.get(i)?.isSelected!!) {
                count = count + 1
                if (serviceList?.get(i)?.name.equals("Type Rotation", ignoreCase = true)) {
                    tyreRotation = true
                }
            }
        }
        if (count == 0
        ) {
            return
        }

        if (tyreRotation) {

            if (radioGroupLF?.checkedRadioButtonId == -1 && radioGroupLR?.checkedRadioButtonId == -1 &&
                radioGroupRF?.checkedRadioButtonId == -1 && radioGroupRR?.checkedRadioButtonId == -1
            ) {
                Toast.makeText(this, "Please Select Update Placement", Toast.LENGTH_SHORT).show()
                return
            }
/*
            if (radioGroupLR?.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please Select Left Right Update Placement", Toast.LENGTH_SHORT).show()
                return
            }
            if (radioGroupRF?.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please Select Right Front Update Placement", Toast.LENGTH_SHORT).show()
                return
            }
            if (radioGroupRR?.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please Select Right Rear Update Placement", Toast.LENGTH_SHORT).show()
                return
            }
*/
        }

        Log.e("isCpmpleted00", "" + TyreConfigClass.LFCompleted)
        Log.e("isCpmpleted11", "" + TyreConfigClass.LRCompleted)
        Log.e("isCpmpleted22", "" + TyreConfigClass.RRCompleted)
        Log.e("isCpmpleted33", "" + TyreConfigClass.RFCompleted)

        if (!TyreConfigClass.LFCompleted || !TyreConfigClass.RFCompleted || !TyreConfigClass.LRCompleted
            || !TyreConfigClass.RRCompleted
        ) {
//            Toast.makeText(this, "Tyre Not Completed", Toast.LENGTH_SHORT).show()
            return
        }
        if (TyreConfigClass.CarPhoto_1.equals("")) {

            return
        }
        if (TyreConfigClass.CarPhoto_2.equals("")) {

            return
        }
        Log.e("isCpmpleted33", "button is clickable")
//        Toast.makeText(this, "button is clickable", Toast.LENGTH_SHORT).show()
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
                Log.e("getObjlf", "" + lfObject.toString())

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
                        lfObject?.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                var jsonrf = JsonObject()
                try {
                    jsonrf = jsonParser.parse(lfObject.toString()) as JsonObject
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())

            } else {
                var jsonrf = JsonObject()
                jsonrf.addProperty(TyreKey.tyreType, "RF")
                if (TyreDetailCommonClass.chk1Make.equals("RF,true")) {
                    jsonrf.addProperty(
                        TyreKey.vehicleMake, TyreDetailCommonClass.vehicleMake
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonrf.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                Log.e("getObjlf0", "" + jsonrf.toString())
                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
            }

//                    =================================================================

            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {

                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                var lfObject = JSONObject(str)
                Log.e("getObjlr", "" + lfObject.toString())

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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                var jsonlr = JsonObject()
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
                    Log.e(
                        "getMakeUrlLR", "" + TyreDetailCommonClass.chk2Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )

                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonlr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                var lfObject = JSONObject(str)

                Log.e("getObjrr", "" + lfObject.toString())
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                var jsonrr = JsonObject()
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
                    Log.e(
                        "getMakeUrlRR", "" + TyreDetailCommonClass.chk3Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonrr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                var lfObject = JSONObject(str)

                Log.e("getObjLF", "" + lfObject.toString())
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                    Log.e(
                        "getMakeUrlLF", "" + TyreDetailCommonClass.chk1Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonlr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                Log.e("getObjRF", "" + lfObject.toString())
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

                        lfObject.put(TyreKey.vehicleMakeURL, TyreDetailCommonClass.vehicleMakeURL)
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
                Log.e("getObjRF111", "" + prefManager.getValue(TyreConfigClass.TyreRFObject))
            } else {
                val jsonrf = JsonObject()
                jsonrf.addProperty(TyreKey.tyreType, "RF")
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) {

                    jsonrf.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                    Log.e(
                        "getMakeUrlRF", "" + TyreDetailCommonClass.chk2Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )

                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonrf.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                Log.e("getObjRF222", "" + prefManager.getValue(TyreConfigClass.TyreRFObject))
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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

                    Log.e(
                        "getMakeUrlRR", "" + TyreDetailCommonClass.chk3Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonrr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                Log.e("getObjlf11", "" + lfObject.toString())
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                Log.e("getObjlf11", "" + jsonlf)
                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
            } else {
                val jsonlf = JsonObject()
                jsonlf.addProperty(TyreKey.tyreType, "LF")
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) {

                    jsonlf.addProperty(
                        TyreKey.vehicleMake,
                        TyreDetailCommonClass.vehicleMake
                    )
                    Log.e(
                        "getMakeUrlLF", "" + TyreDetailCommonClass.chk1Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonlf.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                Log.e("getObjLf1122", "" + jsonlf.toString())
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

                Log.e("getObjlr0", "" + lfObject.toString())
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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

                    Log.e(
                        "getMakeUrlLR", "" + TyreDetailCommonClass.chk2Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonlr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                    Log.e(
                        "getMakeUrlRR", "" + TyreDetailCommonClass.chk3Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonrr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }

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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                    Log.e(
                        "getMakeUrlLF", "" + TyreDetailCommonClass.chk1Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonlf.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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

                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                    Log.e(
                        "getMakeUrlRF", "" + TyreDetailCommonClass.chk2Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonrf.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                        lfObject.put(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
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
                    Log.e(
                        "getMakeUrlLR", "" + TyreDetailCommonClass.chk3Make + " " +
                                TyreDetailCommonClass.vehicleMakeURL
                    )
                    if (!TyreDetailCommonClass.vehicleMakeURL.equals("")) {
                        jsonlr.addProperty(
                            TyreKey.vehicleMakeURL,
                            TyreDetailCommonClass.vehicleMakeURL
                        )
                    }
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
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Common.hideLoader()
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            123 -> {
                if (grantResults[1] != -1) {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        try {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "image/*"
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                            }
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(intent, PICK_IMAGE_REQUEST)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            124 -> {
                if (grantResults[1] != -1) {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        try {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "image/*"
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                            }
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(intent, PICK_IMAGE_REQUEST)

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }
                } else {

                }
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onPause() {
        storeServiceDetailData()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        removeDataExceptServiceData()
    }

    fun removeDataExceptServiceData() {
        prefManager.removeValue(TyreConfigClass.TyreLFObject)
        prefManager.removeValue(TyreConfigClass.TyreRRObject)
        prefManager.removeValue(TyreConfigClass.TyreRFObject)
        prefManager.removeValue(TyreConfigClass.TyreLRObject)
        if (prefManager?.getValue("image_LF") != null) {
            prefManager.removeValue("image_LF")
        }
        if (prefManager?.getValue("image_LR") != null) {
            prefManager.removeValue("image_LR")
        }
        if (prefManager?.getValue("image_RF") != null) {
            prefManager.removeValue("image_RF")
        }
        if (prefManager?.getValue("image_RR") != null) {
            prefManager.removeValue("image_RR")
        }

        Common.setClearAllValues()
        Common.setFalseAllTyreStatus()
    }

    fun removeAllTyreAndServiceDetails() {
        GlobalScope.launch(Dispatchers.Main) {
            launch(Dispatchers.Main) {

                prefManager.removeValue(TyreConfigClass.TyreLFObject)
                prefManager.removeValue(TyreConfigClass.TyreRRObject)
                prefManager.removeValue(TyreConfigClass.TyreRFObject)
                prefManager.removeValue(TyreConfigClass.TyreLRObject)
                prefManager.removeValue(TyreConfigClass.serviceDetailData)

                if (prefManager?.getValue("image_LF") != null) {
                    prefManager.removeValue("image_LF")
                }
                if (prefManager?.getValue("image_LR") != null) {
                    prefManager.removeValue("image_LR")
                }
                if (prefManager?.getValue("image_RF") != null) {
                    prefManager.removeValue("image_RF")
                }
                if (prefManager?.getValue("image_RR") != null) {
                    prefManager.removeValue("image_RR")
                }
                Common.setClearAllValues()

                chkNitrogenTopup?.isChecked = false
                chkNitrogenRefill?.isChecked = false
                chkTyreRotation?.isChecked = false
                chkWheelBalacing?.isChecked = false
                edtMoreSuggestion?.setText("")
                tvNextServiceDueDate?.setText("")

                TyreConfigClass.CarPhoto_2 = ""
                TyreConfigClass.CarPhoto_1 = ""
                Log.e("getimagee131544", "---" + TyreConfigClass.CarPhoto_1)

                Common.setFalseAllTyreStatus()

                if (serviceList != null && serviceList?.size!! > 0) {
                    for (i in serviceList?.indices!!) {
                        if (serviceList?.get(i)?.isSelected!!) {
                            serviceList?.get(i)?.isSelected = false
                        }
                    }
                }

                if (suggestionArray != null && suggestionArray?.size!! > 0) {
                    for (i in suggestionArray?.indices!!) {
                        if (suggestionArray?.get(i)?.isSelected!!) {
                            suggestionArray?.get(i)?.isSelected = false
                        }
                    }
                }

//                getStoredObjects("")
            }
        }

    }

    private fun uploadImage(imagePath: File, inputStream: InputStream, type: String) {
        Common.showLoader(this)
        val part = MultipartBody.Part.createFormData(
            "file", imagePath.name, RequestBody.create(
                MediaType.parse("image/*"),
                inputStream.readBytes()
            )
        )
        loginViewModel?.uploadImage(part, prefManager.getAccessToken()!!, this, "service-image")

        loginViewModel?.getImageUpload()?.observe(this, androidx.lifecycle.Observer {

            if (it != null) {
                if (it.success) {
                    Log.e("getFile", "" + it.data.imageUrl)
//                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()

                    Log.e("getuploadType", "" + type)

                    if (!type.equals("")) {
                        if (type.equals("car_1")) {
                            TyreConfigClass.CarPhoto_1 = it.data.imageUrl
                            Log.e("getimagee13155", "---" + TyreConfigClass.CarPhoto_1)
                            prefManager.removeValue("image_Car_1")
                        }
                        if (type.equals("car_2")) {
                            TyreConfigClass.CarPhoto_2 = it.data.imageUrl
                            prefManager.removeValue("image_Car_2")
                        }
                        if (type.equals("LF")) {
                            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
                            ) {
                                val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                                try {
                                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                                    if (jsonLF.get(TyreKey.visualDetailPhotoUrl) != null &&
                                        !jsonLF.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                                    ) {
                                        jsonLF.remove(TyreKey.visualDetailPhotoUrl)
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    } else {
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    }
                                    Log.e("getObjectT__" + type, "" + jsonLF)
                                    prefManager.removeValue("image_LF")
                                    prefManager.setValue(TyreConfigClass.TyreLFObject, jsonLF.toString())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (type.equals("RR")) {
                            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
                            ) {
                                val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                                try {
                                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                                    Log.e("getobjectslf", "" + jsonLF)
                                    if (jsonLF.get(TyreKey.visualDetailPhotoUrl) != null &&
                                        !jsonLF.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                                    ) {
                                        jsonLF.remove(TyreKey.visualDetailPhotoUrl)
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    } else {
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    }
                                    Log.e("getObjectT__" + type, "" + jsonLF)
                                    prefManager.removeValue("image_RR")
                                    prefManager.setValue(TyreConfigClass.TyreRRObject, jsonLF.toString())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (type.equals("LR")) {
                            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
                            ) {
                                val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                                try {
                                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                                    Log.e("getobjectslf", "" + jsonLF)
                                    if (jsonLF.get(TyreKey.visualDetailPhotoUrl) != null &&
                                        !jsonLF.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                                    ) {
                                        jsonLF.remove(TyreKey.visualDetailPhotoUrl)
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    } else {
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    }
                                    Log.e("getObjectT__" + type, "" + jsonLF)
                                    prefManager.removeValue("image_LR")
                                    prefManager.setValue(TyreConfigClass.TyreLRObject, jsonLF.toString())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (type.equals("RF")) {
                            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
                            ) {
                                val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                                try {
                                    val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                                    Log.e("getobjectslf", "" + jsonLF)
                                    if (jsonLF.get(TyreKey.visualDetailPhotoUrl) != null &&
                                        !jsonLF.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                                    ) {
                                        jsonLF.remove(TyreKey.visualDetailPhotoUrl)
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    } else {
                                        jsonLF.addProperty(TyreKey.visualDetailPhotoUrl, it.data.imageUrl)
                                    }
                                    Log.e("getObjectT__" + type, "" + jsonLF)
                                    prefManager.removeValue("image_RF")
                                    prefManager.setValue(TyreConfigClass.TyreRFObject, jsonLF.toString())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    } else {
                        if (selectImage1) {
                            TyreConfigClass.CarPhoto_1 = it.data.imageUrl
                            Log.e("getimagee13155", "---" + TyreConfigClass.CarPhoto_1)
                        } else {
                            TyreConfigClass.CarPhoto_2 = it.data.imageUrl
                        }
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }

                    storeServiceDetailData()
                    checkSubmitBtn()
                    if (type.equals("")) {
                        Common.hideLoader()
                    }
                } else {
                    Common.hideLoader()
                }
            } else {
                Common.hideLoader()
            }
        })
    }


    private fun showImage(posterUrl: String?) {
        val builder = AlertDialog.Builder(this@AddServiceDetailsActivity).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        builder.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

        val root = LayoutInflater.from(this@AddServiceDetailsActivity)
            .inflate(R.layout.dialogue_image, null)

        val tvTitleRemarks =
            root.findViewById<TextView>(R.id.tvTitleRemarks)
        val imgPoster =
            root.findViewById<ImageView>(R.id.imgPoster)

        Glide.with(this@AddServiceDetailsActivity)
            .load(posterUrl)
            .override(1600, 1600)
            .thumbnail(0.33f)
            .placeholder(R.drawable.placeholder)
            .into(imgPoster)

        tvTitleRemarks?.text = getString(R.string.str_view_car_image)

        val imgClose = root.findViewById<ImageView>(R.id.imgClose)
        imgClose.setOnClickListener { builder.dismiss() }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }


    fun showDialogue(title: String, message: String, isBackPressed: Boolean) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout, null)

        val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tv_message.text = message
        btnYes.setOnClickListener {
            builder.dismiss()
            try {
                removeAllTyreAndServiceDetails()
                if (isBackPressed) {
                    this.finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (isBackPressed) {
                    this.finish()
                }
            }
        }
        builder.setView(root)
        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    fun showDialogueTwo(title: String, message: String) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout_option, null)

        val btn_ok = root.findViewById<BoldButton>(R.id.btn_ok)
        val btn_cancel = root.findViewById<BoldButton>(R.id.btn_cancel)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val tv_message = root.findViewById<TextView>(R.id.tv_messageTitle)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tv_message.text = message
        btn_ok.setOnClickListener {
            builder.dismiss()
            uploadImageIfExist()
        }
        btn_cancel?.setOnClickListener {
            builder.dismiss()
        }
        ivClose?.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    fun showDialogueTwoForService(title: String, message: String, service: String, iscompleted: Boolean) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout_option, null)

        val btn_ok = root.findViewById<BoldButton>(R.id.btn_ok)
        val btn_cancel = root.findViewById<BoldButton>(R.id.btn_cancel)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val tv_message = root.findViewById<TextView>(R.id.tv_messageTitle)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tv_message.text = message
        btn_ok.setOnClickListener {
            builder.dismiss()
            if (iscompleted) {
                enableCompletedService(service)
            } else {
                removeNitrojenServiceFromObject(service, iscompleted)
            }
        }
        btn_cancel?.setOnClickListener {
            builder.dismiss()

            if (serviceList?.size!! > 0) {
                for (i in serviceList?.indices!!) {

                    if (service.equals("")) {

                        if (iscompleted) {
                            if (serviceList?.get(i)?.name?.equals("Nitrogen Refill")!! && serviceList?.get(i)?.isSelected!!) {
                                serviceList?.get(i)?.isSelected = false
                            }
                            if (serviceList?.get(i)?.name?.equals("Nitrogen Top Up")!! && serviceList?.get(i)?.isSelected!!) {
                                serviceList?.get(i)?.isSelected = false
                            }
                        } else {
                            if (serviceList?.get(i)?.name?.equals("Nitrogen Refill")!! && !serviceList?.get(i)?.isSelected!!) {
                                serviceList?.get(i)?.isSelected = true
                            }
                            if (serviceList?.get(i)?.name?.equals("Nitrogen Top Up")!! && !serviceList?.get(i)?.isSelected!!) {
                                serviceList?.get(i)?.isSelected = true
                            }
                        }
                    } else {
                        if (iscompleted) {
                            if (serviceList?.get(i)?.name?.equals("Wheel Balancing")!! && serviceList?.get(i)?.isSelected!!) {
                                serviceList?.get(i)?.isSelected = false
                            }

                        } else {
                            if (serviceList?.get(i)?.name?.equals("Wheel Balancing")!! && !serviceList?.get(i)?.isSelected!!) {
                                serviceList?.get(i)?.isSelected = true
                            }

                        }
                    }

                    /* if (service.equals("")) {
                         if (serviceList?.get(i)?.name?.equals("Nitrogen Refill")!! && !serviceList?.get(i)?.isSelected!!) {

                             Log.e("getselecletd",""+serviceList?.get(i)?.name+" "+iscompleted)
                             if (!iscompleted) {
                                 serviceList?.get(i)?.isSelected = true
                             } else {
                                 serviceList?.get(i)?.isSelected = false
                             }
                         }
                         if (serviceList?.get(i)?.name?.equals("Nitrogen Top Up")!! && !serviceList?.get(i)?.isSelected!!) {
                             Log.e("getselecletd1",""+serviceList?.get(i)?.name+" "+iscompleted)
                             if (!iscompleted) {
                                 serviceList?.get(i)?.isSelected = true
                             } else {
                                 serviceList?.get(i)?.isSelected = false
                             }
                         }
                     } else {
                         if (serviceList?.get(i)?.name?.equals("Wheel Balancing")!! && !serviceList?.get(i)?.isSelected!!) {

                             if (!iscompleted) {
                                 serviceList?.get(i)?.isSelected = true
                             } else {
                                 serviceList?.get(i)?.isSelected = false
                             }
                         }
                     }*/
                }
            }
            serviceAdapter?.notifyDataSetChanged()
            GlobalScope.launch(Dispatchers.Main) {
                getStoredObjects("")
            }
        }
        ivClose?.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    private fun enableCompletedService(service: String) {

        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {

                    jsonLF.remove(TyreKey.isCompleted)
                    jsonLF.addProperty(TyreKey.isCompleted, "false")

                    TyreConfigClass.LFVehicleVisualDetail = false
                    ivTyre1?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_pending_tyre_config
                        )
                    )
                    ivInfoImgLF?.visibility = View.VISIBLE

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {

                    jsonLF.remove(TyreKey.isCompleted)
                    jsonLF.addProperty(TyreKey.isCompleted, "false")

                    TyreConfigClass.LRVehicleVisualDetail = false
                    ivTyre2?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_pending_tyre_config
                        )
                    )
                    ivInfoImgLR?.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {

                    jsonLF.remove(TyreKey.isCompleted)
                    jsonLF.addProperty(TyreKey.isCompleted, "false")

                    TyreConfigClass.RFVehicleVisualDetail = false
                    ivTyre3?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_pending_tyre_config
                        )
                    )
                    ivInfoImgRF?.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (jsonLF.get(TyreKey.isCompleted) != null && jsonLF.get(TyreKey.isCompleted)?.asString?.equals("true")!!) {

                    jsonLF.remove(TyreKey.isCompleted)
                    jsonLF.addProperty(TyreKey.isCompleted, "false")

                    TyreConfigClass.RRVehicleVisualDetail = false
                    ivTyre4?.setImageDrawable(
                        this@AddServiceDetailsActivity.resources?.getDrawable(
                            R.drawable.ic_pending_tyre_config
                        )
                    )
                    ivInfoImgRR?.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            getStoredObjects("")
        }
    }

    private fun removeNitrojenServiceFromObject(service: String, iscompleted: Boolean) {
//        wheelBalancing
        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreLFObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (service.equals("wheelBalancing")) {
                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.weightTyreService)
                        jsonLF.addProperty(TyreKey.weightTyreService, "")
                    }
                } else {
                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiInTyreService)
                        jsonLF.addProperty(TyreKey.psiInTyreService, "")
                    }
                    if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiOutTyreService)
                        jsonLF.addProperty(TyreKey.psiOutTyreService, "")
                    }
                }
                prefManager.setValue(TyreConfigClass.TyreLFObject, jsonLF.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreLRObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (service.equals("wheelBalancing")) {
                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.weightTyreService)
                        jsonLF.addProperty(TyreKey.weightTyreService, "")
                    }

                } else {
                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiInTyreService)
                        jsonLF.addProperty(TyreKey.psiInTyreService, "")
                    }
                    if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiOutTyreService)
                        jsonLF.addProperty(TyreKey.psiOutTyreService, "")
                    }
                }

                prefManager.setValue(TyreConfigClass.TyreLRObject, jsonLF.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreRFObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (service.equals("wheelBalancing")) {
                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.weightTyreService)
                        jsonLF.addProperty(TyreKey.weightTyreService, "")
                    }

                } else {
                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiInTyreService)
                        jsonLF.addProperty(TyreKey.psiInTyreService, "")
                    }
                    if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiOutTyreService)
                        jsonLF.addProperty(TyreKey.psiOutTyreService, "")
                    }
                }

                prefManager.setValue(TyreConfigClass.TyreRFObject, jsonLF.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
        ) {
            val str = prefManager.getValue(TyreConfigClass.TyreRRObject)
            try {
                val jsonLF: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjectslf", "" + jsonLF)

                if (service.equals("wheelBalancing")) {
                    if (jsonLF.get(TyreKey.weightTyreService) != null && !jsonLF.get(TyreKey.weightTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.weightTyreService)
                        jsonLF.addProperty(TyreKey.weightTyreService, "")
                    }

                } else {
                    if (jsonLF.get(TyreKey.psiInTyreService) != null && !jsonLF.get(TyreKey.psiInTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiInTyreService)
                        jsonLF.addProperty(TyreKey.psiInTyreService, "")
                    }
                    if (jsonLF.get(TyreKey.psiOutTyreService) != null && !jsonLF.get(TyreKey.psiOutTyreService).asString.equals("")) {
                        jsonLF.remove(TyreKey.psiOutTyreService)
                        jsonLF.addProperty(TyreKey.psiOutTyreService, "")
                    }
                }
                prefManager.setValue(TyreConfigClass.TyreRRObject, jsonLF.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            getStoredObjects("")
        }

    }

    fun getSelectedService() {
        selectedServiceArr?.clear()
        if (serviceList != null && serviceList?.size!! > 0) {
            for (i in serviceList?.indices!!) {
                if (serviceList?.get(i)?.isSelected!! && !serviceList?.get(i)?.isDisable!!) {
                    selectedServiceArr?.add(serviceList?.get(i)?.name!!)
                }
            }
        }
    }
}