package com.walkins.technician.activity

import android.Manifest
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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.getFile
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.theartofdev.edmodo.cropper.CropImage
import com.walkins.technician.DB.*
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.TyreDetailCommonClass
import com.walkins.technician.common.TyreKey
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.custom.BoldButton
import org.json.JSONObject
import java.io.File

class AddServiceDetailsActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter,
    View.OnTouchListener {
    private lateinit var prefManager: PrefManager
    private lateinit var mDb: DBClass
    var pendingArr: ArrayList<String>? = null
    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String
    var dialog: BottomSheetDialog? = null
    var image_uri: Uri? = null
    private val IMAGE_CAPTURE_RESULT = 1001
    private val PERMISSION_CODE = 1000;

    var imagePickerDialog: BottomSheetDialog? = null
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
    private var suggestionArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )
    var reasonArray = arrayListOf(
        "The car was unavailable",
        "Need more time to resolve",
        "The customer request a delay",
        "Need more time to resolve",
        "The customer request a delay"
    )
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

    private var ivPhoneCall: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_details)
        mDb = DBClass.getInstance(this)
        prefManager = PrefManager(this)
        init()

        var threa = Thread {


            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager?.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjects", "" + json)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager?.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjects", "" + json)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager?.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjects", "" + json)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager?.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjects", "" + json)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        threa.start()


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

        ivInfoImgLF = findViewById(R.id.ivInfoImgLF)
        ivInfoImgLR = findViewById(R.id.ivInfoImgLR)
        ivInfoImgRF = findViewById(R.id.ivInfoImgRF)
        ivInfoImgRR = findViewById(R.id.ivInfoImgRR)

        ivPhoneCall = findViewById(R.id.ivPhoneCall)

        ivInfoImgRR?.setOnClickListener(this)
        ivInfoImgRF?.setOnClickListener(this)
        ivInfoImgLR?.setOnClickListener(this)
        ivInfoImgLF?.setOnClickListener(this)

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
        cardtyreConfig?.setOnClickListener(this)
        cardtechinicalSuggestion?.setOnClickListener(this)
        cardservice?.setOnClickListener(this)
        ivAddServices?.setOnClickListener(this)
        ivAddTyreConfig?.setOnClickListener(this)
        ivAddTechnicalSuggestion?.setOnClickListener(this)
        ivEditImg1?.setOnClickListener(this)
        ivEditImg2?.setOnClickListener(this)


        ivTyre1?.setOnTouchListener(this)
        ivTyre2?.setOnTouchListener(this)
        ivTyre3?.setOnTouchListener(this)
        ivTyre4?.setOnTouchListener(this)
        llTyreConfigExpanded?.setOnTouchListener(this)
        ivPhoneCall?.setOnTouchListener(this)

        tyreSuggestionAdapter = TyreSuggestionAdpater(suggestionArr, this, this, false)
        tyreSuggestionAdapter?.onclick = this
        suggestionsRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        suggestionsRecycView?.adapter = tyreSuggestionAdapter

        ivBack?.setOnClickListener(this)

        checkChangeListener()

//        TyreConfigClass.LFCompleted = false
//        TyreConfigClass.LRCompleted = false
//        TyreConfigClass.RFCompleted = false
//        TyreConfigClass.RRCompleted = false
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
        TyreConfigClass.nitrogenTopupChecked = chkNitrogenTopup?.isChecked!!
        TyreConfigClass.nitrogenRefillChecked = chkNitrogenRefill?.isChecked!!
        TyreConfigClass.nitrogenTyreRotationChecked = chkTyreRotation?.isChecked!!
        TyreConfigClass.nitrogenWheelBalancingChecked = chkWheelBalacing?.isChecked!!
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
                Log.e("pendingArr000", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "LR Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )
            }
            R.id.ivInfoImgLF -> {
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
                Log.e("pendingArr0", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "LF Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )

            }
            R.id.ivInfoImgRR -> {
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
                Log.e("pendingArr1", "" + pendingArr)
                showBottomSheetdialog(
                    pendingArr!!,
                    "RR Pending",
                    this,
                    Common.btn_filled,
                    "Proceed"
                )
            }
            R.id.ivInfoImgRF -> {
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
                Log.e("pendingArr2", "" + pendingArr)
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
            R.id.cardservice -> {
                Log.e("clickcall", "0")
                ivAddServices?.performClick()
            }
            R.id.cardtyreConfig -> {
                Log.e("clickcall", "1")
                ivAddTyreConfig?.performClick()
            }
            R.id.cardtechinicalSuggestion -> {
                Log.e("clickcall", "2")
                ivAddTechnicalSuggestion?.performClick()
            }
        }
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
        tyreSuggestionAdapter = TyreSuggestionAdpater(reasonArray, this, this, true)
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

        if (check == 0) {

            Log.e("getposition0", "" + suggestionArr.get(variable))
        } else if (check == 1) {
            Log.e("getposition1", "" + reasonArray.get(variable))
        } else if (check == 10) {

            if (imagePickerDialog != null && imagePickerDialog?.isShowing!!) {
                imagePickerDialog?.dismiss()
            }
            if (Common.commonPhotoChooseArr.get(variable)?.equals("Gallery")) {
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions((this))
                } else {
                    try {
                        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (result == true) {
                    try {

                        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }

                } else {
                    //  Common.showShortToast("Permission Granted",requireActivity())
                }
            }
            if (Common.commonPhotoChooseArr?.get(variable)?.equals("Camera")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                    ) {
                        //permission was not enabled
                        val permission = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                    intent?.putExtra("selectedTyre", "LF")
                    intent?.putExtra("title", "Select Tyre Make - LF")
                    TyreConfigClass.selectedTyreConfigType = "LFpending"
                    TyreConfigClass.clickedTyre = "LF"
                    startActivityForResult(intent, 1000)
                } else {
                    intent = Intent(this, VehicleMakeActivity::class.java)
                    intent?.putExtra("selectedTyre", "LF")
                    intent?.putExtra("title", "Select Tyre Make - LF")
                    TyreConfigClass.selectedTyreConfigType = "LF"
                    TyreConfigClass.clickedTyre = "LF"
                    startActivityForResult(intent, 1000)
                }
            }
            R.id.ivTyre3 -> {

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


        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermissions(context: FragmentActivity?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } !== PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
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
                                Manifest.permission.CAMERA
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
                            Manifest.permission.CAMERA
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            IMAGE_CAPTURE_RESULT -> {
//                image_view.setImageURI(image_uri)

                Log.e("imagepath222", "" + image_uri)

                CropImage.activity(image_uri)
                    .start(this)
            }
            105 -> {
            }
            106 -> {

                openSkipServiceDialogue()

            }

            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val auxFile = File(mCurrentPhotoPath)
                    Log.e("imagepath2", "" + auxFile)

                    CropImage.activity(Uri.fromFile(auxFile))
                        .start(this)


                }
            }

            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    //To get the File for further usage
                    val selectedImage = data?.data
                    Log.e("imagepath2322", "" + selectedImage)

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@AddServiceDetailsActivity, selectedImage)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

                    Log.i("imagePath", "++++" + imagePath)

                    CropImage.activity(selectedImage)
                        .start(this)
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage

                    val selectedImage = result.uri

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Common.getFile(this@AddServiceDetailsActivity, selectedImage)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    Log.e("imagepath1", "" + imagePath + " " + selectedImage + " " + selectImage1)
                    Log.e("imagepath2", "" + Uri.fromFile(imagePath))

                    if (selectImage1) {
                        try {
//                            Glide.with(this)
//                                .load(Uri.fromFile(imagePath))
//                                .into(ivPickedImage!!)

                            ivPickedImage?.setImageBitmap(result?.bitmap)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Log.e("imagepath1", "" + e.cause + " " + e.message)
                        }
                        relCarPhotoAdd1?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                        ivEditImg1?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                    } else {
//                        Glide.with(this)
//                            .load(Uri.fromFile(imagePath))
//                            .into(ivPickedImage1!!)
                        ivPickedImage1?.setImageBitmap(result?.bitmap)
                        relCarPhotoAdd2?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto2?.visibility = View.GONE
                        tvCarphoto2?.visibility = View.GONE
                    }
//                    ivPickedImage?.setImageURI(selectedImage)


//                    imagePath?.let { uploadCIPImage(it) }
                }
            }
            1000 -> {
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

                for (i in TyreDetailCommonClass.issueResolvedArr?.indices!!) {
                    jsonArr.add(TyreDetailCommonClass.issueResolvedArr?.get(i))
                }
                json.add("issueResolvedArr", jsonArr)
                json.addProperty(
                    "visualDetailPhotoUrl",
                    TyreDetailCommonClass.visualDetailPhotoUrl
                )

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

                storeMake()

                Log.e("getjsonobject", "" + prefManager.getValue(TyreConfigClass.TyreRFObject))


                val thread = Thread {
                    /*if (TyreDetailCommonClass.tyreType.equals("LF")) {
                        Log.e("iscompleted::lf", "" + TyreConfigClass.LFCompleted)

                        var entity = TyreLFDetail()
                        entity.vehicleMake = TyreDetailCommonClass.vehicleMake
                        entity.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId
                        entity.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                        entity.vehiclePatternId = TyreDetailCommonClass.vehiclePatternId
                        entity.vehicleSize = TyreDetailCommonClass.vehicleSize
                        entity.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                        entity.manufaturingDate = TyreDetailCommonClass.manufaturingDate
                        entity.psiInTyreService = TyreDetailCommonClass.psiInTyreService
                        entity.psiOutTyreService = TyreDetailCommonClass.psiOutTyreService
                        entity.weightTyreService = TyreDetailCommonClass.weightTyreService
                        entity.sidewell = TyreDetailCommonClass.sidewell
                        entity.shoulder = TyreDetailCommonClass.shoulder
                        entity.treadWear = TyreDetailCommonClass.treadWear
                        entity.treadDepth = TyreDetailCommonClass.treadDepth
                        entity.rimDamage = TyreDetailCommonClass.rimDamage
                        entity.bubble = TyreDetailCommonClass.bubble
                        entity.issueResolvedArr = TyreDetailCommonClass.issueResolvedArr
                        entity.visualDetailPhotoUrl = TyreDetailCommonClass.visualDetailPhotoUrl

                        if (!TyreConfigClass.LFCompleted) {
                            if (mDb.daoLF().getAll().size > 0) {
                                mDb.daoLF().deleteAll()
                            }

                            mDb.daoLF().save(entity)

                            if (TyreDetailCommonClass.chk1Make.equals("RF")) {
                                val entityRF = TyreRFDetail()
                                entityRF.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityRF.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (TyreDetailCommonClass.chk1Pattern.equals("RF")) {
                                    entityRF.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityRF.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk1Size.equals("RF")) {
                                    entityRF.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityRF.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }

                                if (mDb.daoRF().getAll().size > 0) {
                                    mDb.daoRF().deleteAll()
                                }
                                mDb.daoRF().save(entityRF)
                            }
                            if (TyreDetailCommonClass.chk2Make.equals("LR")) {

                                val entityLR = TyreLRDetail()
                                entityLR.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityLR.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoLR().getAll().size > 0) {
                                    mDb.daoLR().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk2Pattern.equals("LR")) {
                                    entityLR.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityLR.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk2Size.equals("LR")) {
                                    entityLR.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityLR.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoLR().save(entityLR)
                            }
                            if (TyreDetailCommonClass.chk3Make.equals("RR")) {
                                val entityRR = TyreRRDetail()
                                entityRR.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityRR.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoRR().getAll().size > 0) {
                                    mDb.daoRR().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk3Pattern.equals("RR")) {
                                    entityRR.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityRR.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk3Size.equals("RR")) {
                                    entityRR.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityRR.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoRR().save(entityRR)
                            }

                        } else {
                            var id: Int = -1
                            for (i in mDb.daoLF().getAll().indices) {
                                Log.e("getview", "" + mDb.daoRF().getAll().get(i).id)
                                id = mDb.daoLF().getAll().get(i).id
                            }
                            entity.id = id
                            mDb.daoLF().update(entity)
                        }

                    } else if (TyreDetailCommonClass.tyreType.equals("LR")) {
                        Log.e("iscompleted::lr", "" + TyreConfigClass.LRCompleted)

                        var entity = TyreLRDetail()

                        entity.vehicleMake = TyreDetailCommonClass.vehicleMake
                        entity.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId
                        entity.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                        entity.vehiclePatternId = TyreDetailCommonClass.vehiclePatternId
                        entity.vehicleSize = TyreDetailCommonClass.vehicleSize
                        entity.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                        entity.manufaturingDate = TyreDetailCommonClass.manufaturingDate
                        entity.psiInTyreService = TyreDetailCommonClass.psiInTyreService
                        entity.psiOutTyreService = TyreDetailCommonClass.psiOutTyreService
                        entity.weightTyreService = TyreDetailCommonClass.weightTyreService
                        entity.sidewell = TyreDetailCommonClass.sidewell
                        entity.shoulder = TyreDetailCommonClass.shoulder
                        entity.treadWear = TyreDetailCommonClass.treadWear
                        entity.treadDepth = TyreDetailCommonClass.treadDepth
                        entity.rimDamage = TyreDetailCommonClass.rimDamage
                        entity.bubble = TyreDetailCommonClass.bubble
                        entity.issueResolvedArr = TyreDetailCommonClass.issueResolvedArr
                        entity.visualDetailPhotoUrl = TyreDetailCommonClass.visualDetailPhotoUrl

                        if (!TyreConfigClass.LRCompleted) {
                            if (mDb.daoLR().getAll().size > 0) {
                                mDb.daoLR().deleteAll()
                            }

                            if (TyreDetailCommonClass.chk1Make.equals("LF")) {
                                val entityLF = TyreLFDetail()
                                entityLF.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityLF.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (TyreDetailCommonClass.chk1Pattern.equals("LF")) {
                                    entityLF.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityLF.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk1Size.equals("LF")) {
                                    entityLF.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityLF.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }

                                if (mDb.daoLF().getAll().size > 0) {
                                    mDb.daoLF().deleteAll()
                                }
                                mDb.daoLF().save(entityLF)
                            }
                            if (TyreDetailCommonClass.chk2Make.equals("RF")) {

                                val entityRF = TyreRFDetail()
                                entityRF.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityRF.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoRF().getAll().size > 0) {
                                    mDb.daoRF().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk2Pattern.equals("RF")) {
                                    entityRF.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityRF.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk2Size.equals("RF")) {
                                    entityRF.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityRF.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoRF().save(entityRF)
                            }
                            if (TyreDetailCommonClass.chk3Make.equals("RR")) {
                                val entityRR = TyreRRDetail()
                                entityRR.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityRR.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoRR().getAll().size > 0) {
                                    mDb.daoRR().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk3Pattern.equals("RR")) {
                                    entityRR.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityRR.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk3Size.equals("RR")) {
                                    entityRR.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityRR.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoRR().save(entityRR)
                            }

                            mDb.daoLR().save(entity)
                        } else {
                            var id: Int = -1
                            for (i in mDb.daoLR().getAll().indices) {
                                Log.e("getview", "" + mDb.daoRF().getAll().get(i).id)
                                id = mDb.daoLR().getAll().get(i).id
                            }
                            entity.id = id

                            mDb.daoLR().update(entity)
                        }
                    } else if (TyreDetailCommonClass.tyreType.equals("RF")) {
                        Log.e("iscompleted::rf", "" + TyreConfigClass.RFCompleted)
                        var entity = TyreRFDetail()
                        entity.vehicleMake = TyreDetailCommonClass.vehicleMake
                        entity.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId
                        entity.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                        entity.vehiclePatternId = TyreDetailCommonClass.vehiclePatternId
                        entity.vehicleSize = TyreDetailCommonClass.vehicleSize
                        entity.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                        entity.manufaturingDate = TyreDetailCommonClass.manufaturingDate
                        entity.psiInTyreService = TyreDetailCommonClass.psiInTyreService
                        entity.psiOutTyreService = TyreDetailCommonClass.psiOutTyreService
                        entity.weightTyreService = TyreDetailCommonClass.weightTyreService
                        entity.sidewell = TyreDetailCommonClass.sidewell
                        entity.shoulder = TyreDetailCommonClass.shoulder
                        entity.treadWear = TyreDetailCommonClass.treadWear
                        entity.treadDepth = TyreDetailCommonClass.treadDepth
                        entity.rimDamage = TyreDetailCommonClass.rimDamage
                        entity.bubble = TyreDetailCommonClass.bubble
                        entity.issueResolvedArr = TyreDetailCommonClass.issueResolvedArr
                        entity.visualDetailPhotoUrl = TyreDetailCommonClass.visualDetailPhotoUrl

                        if (!TyreConfigClass.RFCompleted) {
                            if (mDb.daoRF().getAll().size > 0) {
                                mDb.daoRF().deleteAll()
                            }
                            mDb.daoRF().save(entity)

                            if (TyreDetailCommonClass.chk1Make.equals("LF")) {
                                val entityLF = TyreLFDetail()
                                entityLF.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityLF.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (TyreDetailCommonClass.chk1Pattern.equals("LF")) {
                                    entityLF.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityLF.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk1Size.equals("LF")) {
                                    entityLF.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityLF.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }

                                if (mDb.daoLF().getAll().size > 0) {
                                    mDb.daoLF().deleteAll()
                                }
                                mDb.daoLF().save(entityLF)
                            }
                            if (TyreDetailCommonClass.chk2Make.equals("LR")) {

                                val entityLR = TyreLRDetail()
                                entityLR.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityLR.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoLR().getAll().size > 0) {
                                    mDb.daoLR().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk2Pattern.equals("LR")) {
                                    entityLR.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityLR.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk2Size.equals("LR")) {
                                    entityLR.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityLR.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoLR().save(entityLR)
                            }
                            if (TyreDetailCommonClass.chk3Make.equals("RR")) {
                                val entityRR = TyreRRDetail()
                                entityRR.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityRR.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoRR().getAll().size > 0) {
                                    mDb.daoRR().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk3Pattern.equals("RR")) {
                                    entityRR.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityRR.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk3Size.equals("RR")) {
                                    entityRR.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityRR.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoRR().save(entityRR)
                            }

                        } else {
                            var id: Int = -1
                            for (i in mDb.daoRF().getAll().indices) {
                                Log.e("getview", "" + mDb.daoRF().getAll().get(i).id)
                                id = mDb.daoRF().getAll().get(i).id
                            }
                            entity.id = id

                            mDb.daoRF().update(entity)

                            for (i in mDb.daoRF().getAll().indices) {
                                Log.e("getupdated", "" + mDb.daoRF().getAll().get(i))
                            }
                        }

                    } else if (TyreDetailCommonClass.tyreType.equals("RR")) {
                        Log.e("iscompleted::rr", "" + TyreConfigClass.RRCompleted)

                        var entity = TyreRRDetail()
                        entity.vehicleMake = TyreDetailCommonClass.vehicleMake
                        entity.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId
                        entity.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                        entity.vehiclePatternId = TyreDetailCommonClass.vehiclePatternId
                        entity.vehicleSize = TyreDetailCommonClass.vehicleSize
                        entity.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                        entity.manufaturingDate = TyreDetailCommonClass.manufaturingDate
                        entity.psiInTyreService = TyreDetailCommonClass.psiInTyreService
                        entity.psiOutTyreService = TyreDetailCommonClass.psiOutTyreService
                        entity.weightTyreService = TyreDetailCommonClass.weightTyreService
                        entity.sidewell = TyreDetailCommonClass.sidewell
                        entity.shoulder = TyreDetailCommonClass.shoulder
                        entity.treadWear = TyreDetailCommonClass.treadWear
                        entity.treadDepth = TyreDetailCommonClass.treadDepth
                        entity.rimDamage = TyreDetailCommonClass.rimDamage
                        entity.bubble = TyreDetailCommonClass.bubble
                        entity.issueResolvedArr = TyreDetailCommonClass.issueResolvedArr
                        entity.visualDetailPhotoUrl = TyreDetailCommonClass.visualDetailPhotoUrl

                        if (!TyreConfigClass.RRCompleted) {

                            if (mDb.daoRR().getAll().size > 0) {
                                mDb.daoRR().deleteAll()
                            }

                            mDb.daoRR().save(entity)

                            if (TyreDetailCommonClass.chk1Make.equals("LF")) {
                                val entityLF = TyreLFDetail()
                                entityLF.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityLF.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (TyreDetailCommonClass.chk1Pattern.equals("LF")) {
                                    entityLF.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityLF.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk1Size.equals("LF")) {
                                    entityLF.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityLF.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }

                                if (mDb.daoLF().getAll().size > 0) {
                                    mDb.daoLF().deleteAll()
                                }
                                mDb.daoLF().save(entityLF)
                            }
                            if (TyreDetailCommonClass.chk2Make.equals("RF")) {

                                val entityRF = TyreRFDetail()
                                entityRF.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityRF.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoRF().getAll().size > 0) {
                                    mDb.daoRF().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk2Pattern.equals("RF")) {
                                    entityRF.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityRF.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk2Size.equals("RF")) {
                                    entityRF.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityRF.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoRF().save(entityRF)
                            }
                            if (TyreDetailCommonClass.chk3Make.equals("LR")) {
                                val entityLR = TyreLRDetail()
                                entityLR.vehicleMake = TyreDetailCommonClass.vehicleMake
                                entityLR.vehicleMakeId = TyreDetailCommonClass.vehicleMakeId

                                if (mDb.daoLR().getAll().size > 0) {
                                    mDb.daoLR().deleteAll()
                                }

                                if (TyreDetailCommonClass.chk3Pattern.equals("LR")) {
                                    entityLR.vehiclePattern = TyreDetailCommonClass.vehiclePattern
                                    entityLR.vehiclePatternId =
                                        TyreDetailCommonClass.vehiclePatternId
                                }
                                if (TyreDetailCommonClass.chk3Size.equals("LR")) {
                                    entityLR.vehicleSize = TyreDetailCommonClass.vehicleSize
                                    entityLR.vehicleSizeId = TyreDetailCommonClass.vehicleSizeId
                                }
                                mDb.daoLR().save(entityLR)
                            }
                        } else {
                            var id: Int = -1
                            for (i in mDb.daoRR().getAll().indices) {
                                Log.e("getview", "" + mDb.daoRF().getAll().get(i).id)
                                id = mDb.daoRR().getAll().get(i).id
                            }
                            entity.id = id

                            mDb.daoRR().update(entity)
                        }
                    }*/

                }
                thread.start()

                if (!TyreConfigClass.LFCompleted) {
                    if (TyreConfigClass.clickedTyre.equals("LF")) {
                        if (TyreConfigClass.selectedTyreConfigType.equals("LF")) {
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
                    if (TyreConfigClass.clickedTyre.equals("RF")) {
                        if (TyreConfigClass.selectedTyreConfigType.equals("RF")) {
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
                    if (TyreConfigClass.clickedTyre.equals("LR")) {
                        if (TyreConfigClass.selectedTyreConfigType.equals("LR")) {
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
                    if (TyreConfigClass.clickedTyre.equals("RR")) {

                        if (TyreConfigClass.selectedTyreConfigType.equals("RR")) {
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


            }
        }
    }

    private fun storeMake() {
        if (TyreDetailCommonClass.tyreType.equals("LF")) {
            val jsonrf = JsonObject()
            jsonrf.addProperty(TyreKey.tyreType, "RF")
            jsonrf.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk1Make.equals("RF,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk1Make.equals("RF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonrf.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk1Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonrf.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk1Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )

            jsonrf.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk1Size.equals("RF,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk1Size.equals("RF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )
            prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
//                    =================================================================
            val jsonlr = JsonObject()
            jsonlr.addProperty(TyreKey.tyreType, "LR")
            jsonlr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonlr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonlr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())
//                    ===========================
            val jsonrr = JsonObject()
            jsonrr.addProperty(TyreKey.tyreType, "RR")
            jsonrr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonrr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonrr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())

        }

//        ahithi baki nichenu
        if (TyreDetailCommonClass.tyreType.equals("LR")) {
            val jsonlr = JsonObject()
            jsonlr.addProperty(TyreKey.tyreType, "LF")
            jsonlr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonlr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonlr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlr.toString())
//                    ===========================================================
            val jsonrf = JsonObject()
            jsonrf.addProperty(TyreKey.tyreType, "RF")
            jsonrf.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonrf.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonrf.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
//                    ======================================================================
            val jsonrr = JsonObject()
            jsonrr.addProperty(TyreKey.tyreType, "RR")
            jsonrr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk3Make.equals("RF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonrr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonrr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk3Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk3Size.equals("RF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())
        }
        if (TyreDetailCommonClass.tyreType.equals("RF")) {

//                    if (TyreDetailCommonClass.chk1Make.equals("LF,true")){
            val jsonlf = JsonObject()
            jsonlf.addProperty(TyreKey.tyreType, "LF")
            jsonlf.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonlf.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonlf.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonlf.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonlf.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonlf.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())

//                    }
//                    ===========================================================
//                    if (TyreDetailCommonClass.chk2Make.equals("LR,true")){
            val jsonlr = JsonObject()
            jsonlr.addProperty(TyreKey.tyreType, "LR")
            jsonlr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk2Make.equals("LR,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonlr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonlr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk2Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk2Size.equals("LR,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())

//                    }
//                    ================================================
//                    if (TyreDetailCommonClass.chk3Make.equals("RR,true")){
            val jsonrr = JsonObject()
            jsonrr.addProperty(TyreKey.tyreType, "RR")
            jsonrr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk3Make.equals("RR,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonrr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonrr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk3Pattern.equals("RR,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonrr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk3Size.equals("RR,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreRRObject, jsonrr.toString())

        }
        if (TyreDetailCommonClass.tyreType.equals("RR")) {
            val jsonlf = JsonObject()
            jsonlf.addProperty(TyreKey.tyreType, "LF")
            jsonlf.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonlf.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk1Make.equals("LF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonlf.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonlf.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk1Pattern.equals("LF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonlf.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonlf.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk1Size.equals("LF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreLFObject, jsonlf.toString())
//                    ==================================================================
            val jsonrf = JsonObject()
            jsonrf.addProperty(TyreKey.tyreType, "RF")
            jsonrf.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk2Make.equals("RF,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonrf.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonrf.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk2Pattern.equals("RF,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonrf.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk2Size.equals("RF,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreRFObject, jsonrf.toString())
//                    ====================================================================
            val jsonlr = JsonObject()
            jsonlr.addProperty(TyreKey.tyreType, "LR")
            jsonlr.addProperty(
                TyreKey.vehicleMake,
                if (TyreDetailCommonClass.chk3Make.equals("LR,true")) TyreDetailCommonClass.vehicleMake else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleMakeId,
                if (TyreDetailCommonClass.chk3Make.equals("LR,true")) TyreDetailCommonClass.vehicleMakeId else ""
            )

            jsonlr.addProperty(
                TyreKey.vehiclePattern,
                if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePattern else ""
            )
            jsonlr.addProperty(
                TyreKey.vehiclePatternId,
                if (TyreDetailCommonClass.chk3Pattern.equals("LR,true")) TyreDetailCommonClass.vehiclePatternId else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSize,
                if (TyreDetailCommonClass.chk3Size.equals("LR,true")) TyreDetailCommonClass.vehicleSize else ""
            )
            jsonlr.addProperty(
                TyreKey.vehicleSizeId,
                if (TyreDetailCommonClass.chk3Size.equals("LR,true")) TyreDetailCommonClass.vehicleSizeId else ""
            )

            prefManager.setValue(TyreConfigClass.TyreLRObject, jsonlr.toString())

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
                if (grantResults.get(1) != -1) {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val file: File = Common.createFile(this)

                        mCurrentPhotoPath = file.absolutePath

                        val uri: Uri = FileProvider.getUriForFile(
                            this,
                            "com.walkins.technician.android.fileprovider",
                            file
                        )
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

                    }
                } else {

                }
            }

            124 -> {
                if (grantResults?.get(1) != -1) {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        try {
                            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "image/*"
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
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_RESULT)
    }


}