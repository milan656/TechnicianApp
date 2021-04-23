package com.walkins.technician.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ramotion.fluidslider.FluidSlider
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.TyreDetailCommonClass
import com.walkins.technician.common.TyreKey
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.IssueResolveModel
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VisualDetailsActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var sliderIn: FluidSlider? = null
    private var multiSliderPsiOut: FluidSlider? = null
    private var multiSliderWeight: FluidSlider? = null
    var max = 50
    var min = 0
    var total = max - min

    private lateinit var mDb: DBClass
    private lateinit var prefManager: PrefManager
    private var cameraDialog: BottomSheetDialog? = null

    private var ivBack: ImageView? = null
    private var ivOkSideWell: ImageView? = null
    private var ivSugSideWell: ImageView? = null
    private var ivReqSideWell: ImageView? = null

    private var psiInTyreService: String? = null
    private var psiOutTyreService: String? = null
    private var weightTyreService: String? = null
    private var sidewell: String? = ""
    private var shoulder: String? = ""
    private var treadWear: String? = ""
    private var treadDepth: String? = ""
    private var rimDamage: String? = ""
    private var bubble: String? = ""

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
    private var ivPickedImage1: ImageView? = null
    private var ivEditImg2: ImageView? = null

    private var llOkSideWell: LinearLayout? = null
    private var llSugSideWell: LinearLayout? = null
    private var llREQSideWell: LinearLayout? = null
    private var llOkShoulder: LinearLayout? = null
    private var llSugShoulder: LinearLayout? = null
    private var llReqShoulder: LinearLayout? = null
    private var llOkTreadDepth: LinearLayout? = null
    private var llSugTreadDepth: LinearLayout? = null
    private var llReqTreadDepth: LinearLayout? = null
    private var llOkTreadWear: LinearLayout? = null
    private var llSugTreadWear: LinearLayout? = null
    private var llReqTreadWear: LinearLayout? = null
    private var llOkRimDamage: LinearLayout? = null
    private var llSugRimDamage: LinearLayout? = null
    private var llReqRimDamage: LinearLayout? = null
    private var llOkBubble: LinearLayout? = null
    private var llSugBubble: LinearLayout? = null
    private var llReqBubble: LinearLayout? = null

    private var tvTitle: TextView? = null
    private var tvCarphoto1: TextView? = null
    private var tvAddPhoto1: TextView? = null
    private var selectedTyre: String? = null

    private var issueResolvedRecycView: RecyclerView? = null
    private var selectedIssueArr: ArrayList<String>? = ArrayList()
    private var issueResolveArr = arrayListOf(
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment",
        "Improve this for the tyre in alignment"
    )

    private var issueResolveArray: ArrayList<IssueResolveModel>? = ArrayList()
    private var issueResolveAdapter: TyreSuggestionAdpater? = null
    private var relTyrePhotoAdd: RelativeLayout? = null
    private var btnDone: Button? = null
    private var weightFrame: FrameLayout? = null
    private var psiInFrame: FrameLayout? = null
    private var psiOutFrame: FrameLayout? = null
    private var edtManufaturingDate: EditText? = null


    // image picker code
    val REQUEST_IMAGE = 100
    val REQUEST_PERMISSION = 200
    private var imageFilePath = ""
    private var IMAGE_PICK_CODE = 1010;
    private var PERMISSION_CODE = 1011;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_details)
        mDb = DBClass.getInstance(this)
        prefManager = PrefManager(this)

        requestPermissionForImage()
        init()
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
        btnDone = findViewById(R.id.btnDone)
        ivBack = findViewById(R.id.ivBack)

        weightFrame = findViewById(R.id.weightFrame)
        psiOutFrame = findViewById(R.id.psiOutFrame)
        psiInFrame = findViewById(R.id.psiInFrame)
        tvAddPhoto1 = findViewById(R.id.tvAddPhoto1)
        tvCarphoto1 = findViewById(R.id.tvCarphoto1)

        edtManufaturingDate = findViewById(R.id.edtManufaturingDate)
        relTyrePhotoAdd = findViewById(R.id.relTyrePhotoAdd)
        issueResolvedRecycView = findViewById(R.id.issueResolvedRecycView)

        for (i in issueResolveArr.indices) {
            issueResolveArray?.add(IssueResolveModel(issueResolveArr.get(i) + " " + i, false))
        }
        issueResolveAdapter = TyreSuggestionAdpater(issueResolveArray!!, this, this, false)
        issueResolvedRecycView?.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        issueResolvedRecycView?.adapter = issueResolveAdapter
        issueResolveAdapter?.onclick = this

        relTyrePhotoAdd?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Visual Detail"

        if (intent != null) {
            if (intent.hasExtra("selectedTyre")) {
                selectedTyre = intent.getStringExtra("selectedTyre")
            }
        }

        tvCarphoto1?.setText("" + selectedTyre + " " + "Photo")

        btnDone?.setOnClickListener(this)

        if (TyreConfigClass.nitrogenRefillChecked || TyreConfigClass.nitrogenTopupChecked
        ) {
            psiOutFrame?.visibility = View.VISIBLE
            psiInFrame?.visibility = View.VISIBLE
        } else {
            psiOutFrame?.visibility = View.GONE
            psiInFrame?.visibility = View.GONE
        }
        if (!TyreConfigClass.nitrogenWheelBalancingChecked) {
            weightFrame?.visibility = View.GONE
        } else {
            weightFrame?.visibility = View.VISIBLE
        }

        ivOkSideWell = findViewById(R.id.ivOkSideWell)
        ivSugSideWell = findViewById(R.id.ivSugSideWell)
        ivReqSideWell = findViewById(R.id.ivReqSideWell)
        llOkSideWell = findViewById(R.id.llOkSideWell)
        llSugSideWell = findViewById(R.id.llSugSideWell)
        llREQSideWell = findViewById(R.id.llREQSideWell)

        ivOkShoulder = findViewById(R.id.ivOkShoulder)
        ivSugShoulder = findViewById(R.id.ivSugShoulder)
        ivReqShoulder = findViewById(R.id.ivReqShoulder)
        llOkShoulder = findViewById(R.id.llOkShoulder)
        llSugShoulder = findViewById(R.id.llSugShoulder)
        llReqShoulder = findViewById(R.id.llReqShoulder)

        llOkTreadDepth = findViewById(R.id.llOkTreadDepth)
        llSugTreadDepth = findViewById(R.id.llSugTreadDepth)
        llReqTreadDepth = findViewById(R.id.llReqTreadDepth)
        ivOkTreadDepth = findViewById(R.id.ivOkTreadDepth)
        ivSugTreadDepth = findViewById(R.id.ivSugTreadDepth)
        ivReqTreadDepth = findViewById(R.id.ivReqTreadDepth)

        llOkTreadWear = findViewById(R.id.llOkTreadWear)
        llSugTreadWear = findViewById(R.id.llSugTreadWear)
        llReqTreadWear = findViewById(R.id.llReqTreadWear)

        llOkRimDamage = findViewById(R.id.llOkRimDamage)
        llSugRimDamage = findViewById(R.id.llSugRimDamage)
        llReqRimDamage = findViewById(R.id.llReqRimDamage)

        llOkBubble = findViewById(R.id.llOkBubble)
        llSugBubble = findViewById(R.id.llSugBubble)
        llReqBubble = findViewById(R.id.llReqBubble)
        ivOkTreadWear = findViewById(R.id.ivOkTreadWear)
        ivSugTreadWear = findViewById(R.id.ivSugTreadWear)
        ivReqTreadWear = findViewById(R.id.ivReqTreadWear)

        ivOkRimDamage = findViewById(R.id.ivOkRimDamage)
        ivSugRimDamage = findViewById(R.id.ivSugRimDamage)
        ivReqRimDamage = findViewById(R.id.ivReqRimDamage)

        ivOkbubble = findViewById(R.id.ivOkbubble)
        ivSugbubble = findViewById(R.id.ivSugbubble)
        ivReqbubble = findViewById(R.id.ivReqbubble)
        ivPickedImage1 = findViewById(R.id.ivPickedImage1)
        ivEditImg2 = findViewById(R.id.ivEditImg2)

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

        llOkSideWell?.setOnClickListener(this)
        llSugSideWell?.setOnClickListener(this)
        llREQSideWell?.setOnClickListener(this)

        llOkShoulder?.setOnClickListener(this)
        llSugShoulder?.setOnClickListener(this)
        llReqShoulder?.setOnClickListener(this)

        llOkTreadDepth?.setOnClickListener(this)
        llSugTreadDepth?.setOnClickListener(this)
        llReqTreadDepth?.setOnClickListener(this)

        llOkTreadWear?.setOnClickListener(this)
        llSugTreadWear?.setOnClickListener(this)
        llReqTreadWear?.setOnClickListener(this)

        llOkRimDamage?.setOnClickListener(this)
        llSugRimDamage?.setOnClickListener(this)
        llReqRimDamage?.setOnClickListener(this)

        llOkBubble?.setOnClickListener(this)
        llSugBubble?.setOnClickListener(this)
        llReqBubble?.setOnClickListener(this)

        psiInSlider()
        psiOutSlider()
        weightSlider()

        if (selectedTyre.equals("LF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                    getData(json)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }
        if (selectedTyre.equals("LR")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                    getData(json)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }
        if (selectedTyre.equals("RF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    getData(json)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }
        if (selectedTyre.equals("RR")) {
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    getData(json)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }
        /* var thread = Thread {
             if (selectedTyre.equals("LF")) {

                 if (mDb.daoLF().getAll().size > 0) {
                     for (i in mDb.daoLF().getAll().indices) {
                         Log.e("getdetailss", "" + mDb.daoLF().getAll().get(i)?.manufaturingDate)
                         Log.e("getdetailss", "" + mDb.daoLF().getAll().get(i)?.psiInTyreService)
                         Log.e("getdetailss", "" + mDb.daoLF().getAll().get(i)?.psiOutTyreService)
                         Log.e("getdetailss", "" + mDb.daoLF().getAll().get(i)?.sidewell)
                         edtManufaturingDate?.setText(mDb.daoLF().getAll().get(i)?.manufaturingDate)
                         sliderIn?.bubbleText = mDb.daoLF().getAll().get(i)?.psiInTyreService
                         multiSliderWeight?.bubbleText =
                             mDb.daoLF().getAll().get(i)?.weightTyreService
                         multiSliderPsiOut?.bubbleText =
                             mDb.daoLF().getAll().get(i)?.psiOutTyreService

                         if (mDb.daoLF().getAll().get(i).sidewell.equals("Ok")) {
                             ivOkSideWell?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).sidewell.equals("SUG")) {
                             ivSugSideWell?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).sidewell.equals("REQ")) {
                             ivReqSideWell?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).shoulder.equals("Ok")) {
                             ivOkShoulder?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).shoulder.equals("SUG")) {
                             ivSugShoulder?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).shoulder.equals("REQ")) {
                             ivReqShoulder?.performClick()
                         }

                         if (mDb.daoLF().getAll().get(i).treadDepth.equals("REQ")) {
                             ivReqTreadDepth?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).treadDepth.equals("Ok")) {
                             ivOkTreadDepth?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).treadDepth.equals("SUG")) {
                             ivSugTreadDepth?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).treadWear.equals("REQ")) {
                             ivReqTreadWear?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).treadWear.equals("Ok")) {
                             ivOkTreadWear?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).treadWear.equals("SUG")) {
                             ivSugTreadWear?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).rimDamage.equals("REQ")) {
                             ivReqRimDamage?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).rimDamage.equals("Ok")) {
                             ivOkRimDamage?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).rimDamage.equals("SUG")) {
                             ivSugRimDamage?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).bubble.equals("REQ")) {
                             ivReqbubble?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).bubble.equals("Ok")) {
                             ivOkbubble?.performClick()
                         }
                         if (mDb.daoLF().getAll().get(i).bubble.equals("SUG")) {
                             ivSugbubble?.performClick()
                         }
                         try {
                             Glide.with(this)
                                 .load(mDb.daoLF().getAll().get(i).visualDetailPhotoUrl!!)
                                 .into(ivPickedImage1!!)
                         } catch (e: java.lang.Exception) {
                             e.printStackTrace()
                         }
                     }
                 }
             } else if (selectedTyre.equals("LR")) {
                 if (mDb.daoLR().getAll().size > 0) {
                     for (i in mDb.daoLR().getAll().indices) {
                         Log.e("getdetailss", "" + mDb.daoLR().getAll().get(i)?.vehicleMake)
                         Log.e("getdetailss", "" + mDb.daoLR().getAll().get(i)?.vehicleMakeId)
                         edtManufaturingDate?.setText(mDb.daoLR().getAll().get(i).manufaturingDate!!)
                         sliderIn?.bubbleText = mDb.daoLR().getAll().get(i).psiInTyreService
                         multiSliderPsiOut?.bubbleText =
                             mDb.daoLR().getAll().get(i).psiOutTyreService
                         multiSliderWeight?.bubbleText =
                             mDb.daoLR().getAll().get(i).weightTyreService

                         if (mDb.daoLR().getAll().get(i).sidewell.equals("Ok")) {
                             ivOkSideWell?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).sidewell.equals("SUG")) {
                             ivSugSideWell?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).sidewell.equals("REQ")) {
                             ivReqSideWell?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).shoulder.equals("Ok")) {
                             ivOkShoulder?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).shoulder.equals("SUG")) {
                             ivSugShoulder?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).shoulder.equals("REQ")) {
                             ivReqShoulder?.performClick()
                         }

                         if (mDb.daoLR().getAll().get(i).treadDepth.equals("REQ")) {
                             ivReqTreadDepth?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).treadDepth.equals("Ok")) {
                             ivOkTreadDepth?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).treadDepth.equals("SUG")) {
                             ivSugTreadDepth?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).treadWear.equals("REQ")) {
                             ivReqTreadWear?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).treadWear.equals("Ok")) {
                             ivOkTreadWear?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).treadWear.equals("SUG")) {
                             ivSugTreadWear?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).rimDamage.equals("REQ")) {
                             ivReqRimDamage?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).rimDamage.equals("Ok")) {
                             ivOkRimDamage?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).rimDamage.equals("SUG")) {
                             ivSugRimDamage?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).bubble.equals("REQ")) {
                             ivReqbubble?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).bubble.equals("Ok")) {
                             ivOkbubble?.performClick()
                         }
                         if (mDb.daoLR().getAll().get(i).bubble.equals("SUG")) {
                             ivSugbubble?.performClick()
                         }
                         try {
                             Glide.with(this)
                                 .load(mDb.daoLR().getAll().get(i).visualDetailPhotoUrl!!)
                                 .into(ivPickedImage1!!)
                         } catch (e: java.lang.Exception) {
                             e.printStackTrace()
                         }
                     }
                 }

             } else if (selectedTyre.equals("RF")) {
                 if (mDb.daoRF().getAll().size > 0) {
                     for (i in mDb.daoRF().getAll().indices) {
                         Log.e("getdetailss", "" + mDb.daoRF().getAll().get(i)?.manufaturingDate)
                         Log.e("getdetailss", "" + mDb.daoRF().getAll().get(i)?.psiInTyreService)
                         edtManufaturingDate?.setText(mDb.daoRF().getAll().get(i).manufaturingDate!!)
                         sliderIn?.bubbleText = mDb.daoRF().getAll().get(i).psiInTyreService
                         multiSliderWeight?.bubbleText =
                             mDb.daoRF().getAll().get(i).weightTyreService
                         multiSliderPsiOut?.bubbleText =
                             mDb.daoRF().getAll().get(i).psiOutTyreService

                         if (mDb.daoRF().getAll().get(i).sidewell.equals("Ok")) {
                             runOnUiThread {
                                 ivOkSideWell?.performClick()
                             }

                         }
                         if (mDb.daoRF().getAll().get(i).sidewell.equals("SUG")) {
                             runOnUiThread {
                                 ivSugSideWell?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).sidewell.equals("REQ")) {
                             runOnUiThread {
                                 ivReqSideWell?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).shoulder.equals("Ok")) {
                             runOnUiThread {
                                 ivOkShoulder?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).shoulder.equals("SUG")) {
                             runOnUiThread {
                                 ivSugShoulder?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).shoulder.equals("REQ")) {
                             runOnUiThread {
                                 ivReqShoulder?.performClick()
                             }
                         }

                         if (mDb.daoRF().getAll().get(i).treadDepth.equals("REQ")) {
                             runOnUiThread {
                                 ivReqTreadDepth?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).treadDepth.equals("Ok")) {
                             runOnUiThread {
                                 ivOkTreadDepth?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).treadDepth.equals("SUG")) {
                             runOnUiThread {
                                 ivSugTreadDepth?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).treadWear.equals("REQ")) {
                             runOnUiThread {
                                 ivReqTreadWear?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).treadWear.equals("Ok")) {
                             runOnUiThread {
                                 ivOkTreadWear?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).treadWear.equals("SUG")) {
                             runOnUiThread {
                                 ivSugTreadWear?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).rimDamage.equals("REQ")) {
                             runOnUiThread {
                                 ivReqRimDamage?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).rimDamage.equals("Ok")) {
                             runOnUiThread {
                                 ivOkRimDamage?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).rimDamage.equals("SUG")) {
                             runOnUiThread {
                                 ivSugRimDamage?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).bubble.equals("REQ")) {
                             runOnUiThread {
                                 ivReqbubble?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).bubble.equals("Ok")) {
                             runOnUiThread {
                                 ivOkbubble?.performClick()
                             }
                         }
                         if (mDb.daoRF().getAll().get(i).bubble.equals("SUG")) {
                             runOnUiThread {
                                 ivSugbubble?.performClick()
                             }
                         }
                         runOnUiThread {
                             try {
                                 Glide.with(this)
                                     .load(mDb.daoRF().getAll().get(i).visualDetailPhotoUrl!!)
                                     .into(ivPickedImage1!!)
                             } catch (e: java.lang.Exception) {
                                 e.printStackTrace()
                             }
                         }
                     }
                 }

             } else if (selectedTyre.equals("RR")) {
                 if (mDb.daoRR().getAll().size > 0) {
                     for (i in mDb.daoRR().getAll().indices) {
                         Log.e("getdetailss", "" + mDb.daoRR().getAll().get(i).vehicleMake)
                         Log.e("getdetailss", "" + mDb.daoRR().getAll().get(i).vehicleMakeId)
                         edtManufaturingDate?.setText(mDb.daoRR().getAll().get(i).manufaturingDate!!)
                         sliderIn?.bubbleText = mDb.daoRR().getAll().get(i).psiInTyreService
                         multiSliderPsiOut?.bubbleText =
                             mDb.daoRR().getAll().get(i).psiOutTyreService
                         multiSliderWeight?.bubbleText =
                             mDb.daoRR().getAll().get(i).weightTyreService

                         if (mDb.daoRR().getAll().get(i).sidewell.equals("Ok")) {
                             ivOkSideWell?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).sidewell.equals("SUG")) {
                             ivSugSideWell?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).sidewell.equals("REQ")) {
                             ivReqSideWell?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).shoulder.equals("Ok")) {
                             ivOkShoulder?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).shoulder.equals("SUG")) {
                             ivSugShoulder?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).shoulder.equals("REQ")) {
                             ivReqShoulder?.performClick()
                         }

                         if (mDb.daoRR().getAll().get(i).treadDepth.equals("REQ")) {
                             ivReqTreadDepth?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).treadDepth.equals("Ok")) {
                             ivOkTreadDepth?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).treadDepth.equals("SUG")) {
                             ivSugTreadDepth?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).treadWear.equals("REQ")) {
                             ivReqTreadWear?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).treadWear.equals("Ok")) {
                             ivOkTreadWear?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).treadWear.equals("SUG")) {
                             ivSugTreadWear?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).rimDamage.equals("REQ")) {
                             ivReqRimDamage?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).rimDamage.equals("Ok")) {
                             ivOkRimDamage?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).rimDamage.equals("SUG")) {
                             ivSugRimDamage?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).bubble.equals("REQ")) {
                             ivReqbubble?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).bubble.equals("Ok")) {
                             ivOkbubble?.performClick()
                         }
                         if (mDb.daoRR().getAll().get(i).bubble.equals("SUG")) {
                             ivSugbubble?.performClick()
                         }
                         try {
                             Glide.with(this)
                                 .load(mDb.daoRR().getAll().get(i).visualDetailPhotoUrl!!)
                                 .into(ivPickedImage1!!)
                         } catch (e: java.lang.Exception) {
                             e.printStackTrace()
                         }
                     }
                 }

             }

         }
         thread.start()*/
    }

    private fun getData(json: JsonObject) {
        Log.e("getobje", "" + json)
        if (json.get(TyreKey.manufaturingDate) != null) {

            edtManufaturingDate?.setText(json.get(TyreKey.manufaturingDate)?.asString!!)
        }
        if (json.get(TyreKey.psiInTyreService) != null) {

            sliderIn?.bubbleText = json.get(TyreKey.psiInTyreService)?.asString!!
        }

        if (json.get(TyreKey.weightTyreService) != null) {

            multiSliderWeight?.bubbleText =
                json.get(TyreKey.weightTyreService)?.asString!!
        }
        if (json.get(TyreKey.psiOutTyreService) != null) {

            multiSliderPsiOut?.bubbleText =
                json.get(TyreKey.psiOutTyreService)?.asString!!
        }
        if (json.get(TyreKey.psiInTyreService) != null) {
            psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString!!
        }
        if (json.get(TyreKey.psiOutTyreService) != null) {

            psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString!!
        }
        if (json.get(TyreKey.weightTyreService) != null) {
            weightTyreService = json.get(TyreKey.weightTyreService)?.asString!!
        }
        if (json.get(TyreKey.sidewell) != null) {
            if (json.get(TyreKey.sidewell)?.asString!!.equals("Ok")) {
                ivOkSideWell?.performClick()
                sidewell = "Ok"
            }
            if (json.get(TyreKey.sidewell)?.asString!!.equals("SUG")) {
                ivSugSideWell?.performClick()
                sidewell = "SUG"
            }
            if (json.get(TyreKey.sidewell)?.asString!!.equals("REQ")) {
                ivReqSideWell?.performClick()
                sidewell = "REQ"
            }
        }
        if (json.get(TyreKey.shoulder) != null) {

            if (json.get(TyreKey.shoulder)?.asString!!.equals("Ok")) {
                ivOkShoulder?.performClick()
                shoulder = "Ok"
            }
            if (json.get(TyreKey.shoulder)?.asString!!.equals("SUG")) {
                ivSugShoulder?.performClick()
                shoulder = "SUG"
            }
            if (json.get(TyreKey.shoulder)?.asString!!.equals("REQ")) {
                ivReqShoulder?.performClick()
                shoulder = "REQ"
            }
        }
        if (json.get(TyreKey.treadDepth) != null) {

            if (json.get(TyreKey.treadDepth)?.asString!!.equals("REQ")) {
                ivReqTreadDepth?.performClick()
                treadDepth = "REQ"
            }
            if (json.get(TyreKey.treadDepth)?.asString!!.equals("Ok")) {
                ivOkTreadDepth?.performClick()
                treadDepth = "Ok"
            }
            if (json.get(TyreKey.treadDepth)?.asString!!.equals("SUG")) {
                ivSugTreadDepth?.performClick()
                treadDepth = "SUG"
            }
        }
        if (json.get(TyreKey.treadWear) != null) {

            if (json.get(TyreKey.treadWear)?.asString!!.equals("REQ")) {
                ivReqTreadWear?.performClick()
                treadWear = "REQ"
            }
            if (json.get(TyreKey.treadWear)?.asString!!.equals("Ok")) {
                ivOkTreadWear?.performClick()
                treadWear = "Ok"
            }
            if (json.get(TyreKey.treadWear)?.asString!!.equals("SUG")) {
                ivSugTreadWear?.performClick()
                treadWear = "SUG"
            }
        }
        if (json.get(TyreKey.rimDamage) != null) {

            if (json.get(TyreKey.rimDamage)?.asString!!.equals("REQ")) {
                ivReqRimDamage?.performClick()
                rimDamage = "REQ"
            }
            if (json.get(TyreKey.rimDamage)?.asString!!.equals("Ok")) {
                ivOkRimDamage?.performClick()
                rimDamage = "Ok"
            }
            if (json.get(TyreKey.rimDamage)?.asString!!.equals("SUG")) {
                ivSugRimDamage?.performClick()
                rimDamage = "SUG"
            }
        }
        if (json.get(TyreKey.bubble) != null) {

            if (json.get(TyreKey.bubble)?.asString!!.equals("REQ")) {
                ivReqbubble?.performClick()
                rimDamage = "REQ"
            }
            if (json.get(TyreKey.bubble)?.asString!!.equals("Ok")) {
                ivOkbubble?.performClick()
                bubble = "OK"
            }
            if (json.get(TyreKey.bubble)?.asString!!.equals("SUG")) {
                ivSugbubble?.performClick()
                bubble = "SUG"
            }
        }
        if (json.get(TyreKey.isCameraSelectedVisualDetail) != null) {

            try {
//            Glide.with(this)
//                .load(json.get(TyreKey.visualDetailPhotoUrl)?.asString!!)
//                .into(ivPickedImage1!!)
                TyreDetailCommonClass.visualDetailPhotoUrl =
                    json.get(TyreKey.visualDetailPhotoUrl)?.asString

                TyreDetailCommonClass.isCameraSelectedVisualDetail =
                    json.get(TyreKey.isCameraSelectedVisualDetail)?.asBoolean!!

                Log.e("getstoredValue", "" + TyreDetailCommonClass.isCameraSelectedVisualDetail)
                Log.e("getstoredValue", "" + TyreDetailCommonClass.visualDetailPhotoUrl)
                if (json.get("isCameraSelectedVisualDetail")?.asBoolean!!) {

                    try {
                        ivPickedImage1?.setImageURI(Uri.parse(json.get(TyreKey.visualDetailPhotoUrl)?.asString))
                    } catch (e: java.lang.Exception) {
                        Log.e("getstoredValue0", "" + e.cause + " " + e.message)
                        e.printStackTrace()
                    }

                } else {
                    Log.e("getstoredValue0", "" + json.get(TyreKey.visualDetailPhotoUrl)?.asString)
                    var file:File=Common.getFile(json.get(TyreKey.visualDetailPhotoUrl)?.asString)
                    try {

                        ivPickedImage1?.setImageURI(Uri.parse(file.absolutePath))
                        Log.e("getstoredValue0", "" + file.absolutePath)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        Log.e("getstoredValue0", "" + e.message + " " + e.cause)
                    }

                }
                TyreDetailCommonClass.visualDetailPhotoUrl =
                    json.get(TyreKey.visualDetailPhotoUrl)?.asString

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (json.get(TyreKey.issueResolvedArr) != null) {

            var arr = json.get(TyreKey.issueResolvedArr)?.asJsonArray
            Log.e("getvalues", "" + arr)
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
            val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
            Log.e("getvalues", "" + arrlist)
            for (i in issueResolveArray?.indices!!) {

                for (j in arrlist.indices) {

                    if (issueResolveArray?.get(i)?.issueName.equals(arrlist.get(j))) {
                        issueResolveArray?.get(i)?.isSelected = true
                        selectedIssueArr?.add(issueResolveArray?.get(i)?.issueName!!)
                    }
                }
                Log.e("getvalues", "" + issueResolveArray?.get(i)?.issueName)
                Log.e("getvalues", "" + issueResolveArray?.get(i)?.isSelected)
            }

            issueResolveAdapter?.notifyDataSetChanged()
        }

        setData(json)

    }

    private fun setData(json: JsonObject) {
        Log.e("getvalselected", "" + json)
        if (selectedTyre != null && !selectedTyre.equals("")) {
            TyreDetailCommonClass.tyreType = selectedTyre
        }
        if (json.get(TyreKey.vehicleMake) != null) {
            TyreDetailCommonClass.vehicleMake = json.get(TyreKey.vehicleMake)?.asString
        }
        if (json.get(TyreKey.vehicleMakeId) != null) {
            TyreDetailCommonClass.vehicleMakeId = json.get(TyreKey.vehicleMakeId)?.asString
        }
        if (json.get(TyreKey.vehiclePattern) != null) {
            TyreDetailCommonClass.vehiclePattern = json.get(TyreKey.vehiclePattern)?.asString
        }
        if (json.get(TyreKey.vehiclePatternId) != null) {
            TyreDetailCommonClass.vehiclePatternId = json.get(TyreKey.vehiclePatternId)?.asString
        }
        if (json.get(TyreKey.vehicleSize) != null) {
            TyreDetailCommonClass.vehicleSize = json.get(TyreKey.vehicleSize)?.asString
        }
        if (json.get(TyreKey.vehicleSizeId) != null) {
            TyreDetailCommonClass.vehicleSizeId = json.get(TyreKey.vehicleSizeId)?.asString
        }
        if (json.get(TyreKey.manufaturingDate) != null) {
            TyreDetailCommonClass.manufaturingDate = json.get(TyreKey.manufaturingDate)?.asString
        }
        if (json.get(TyreKey.psiInTyreService) != null) {
            TyreDetailCommonClass.psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString
        }
        if (json.get(TyreKey.psiOutTyreService) != null) {
            TyreDetailCommonClass.psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString
        }
        if (json.get(TyreKey.weightTyreService) != null) {
            TyreDetailCommonClass.weightTyreService = json.get(TyreKey.weightTyreService)?.asString
        }
        if (json.get(TyreKey.sidewell) != null) {
            TyreDetailCommonClass.sidewell = json.get(TyreKey.sidewell)?.asString
        }
        if (json.get(TyreKey.shoulder) != null) {
            TyreDetailCommonClass.shoulder = json.get(TyreKey.shoulder)?.asString
        }
        if (json.get(TyreKey.treadDepth) != null) {
            TyreDetailCommonClass.treadDepth = json.get(TyreKey.treadDepth)?.asString
        }
        if (json.get(TyreKey.treadWear) != null) {
            TyreDetailCommonClass.treadWear = json.get(TyreKey.treadWear)?.asString
        }
        if (json.get(TyreKey.rimDamage) != null) {
            TyreDetailCommonClass.rimDamage = json.get(TyreKey.rimDamage)?.asString
        }
        if (json.get(TyreKey.bubble) != null) {
            TyreDetailCommonClass.bubble = json.get(TyreKey.bubble)?.asString
        }
        if (json.get(TyreKey.visualDetailPhotoUrl) != null) {
            TyreDetailCommonClass.visualDetailPhotoUrl =
                json.get(TyreKey.visualDetailPhotoUrl)?.asString
        }
        if (json.get(TyreKey.isCameraSelectedVisualDetail) != null) {
            TyreDetailCommonClass.isCameraSelectedVisualDetail =
                json.get(TyreKey.isCameraSelectedVisualDetail)?.asString?.toBoolean()!!
        }
        if (json.get(TyreKey.issueResolvedArr) != null) {
//            TyreDetailCommonClass.issueResolvedArr = json.get(TyreKey.issueResolvedArr)?.asJsonArray
        }
        if (json.get(TyreKey.chk1Make) != null) {
            TyreDetailCommonClass.chk1Make = json.get(TyreKey.chk1Make)?.asString
        }
        if (json.get(TyreKey.chk1Pattern) != null) {
            TyreDetailCommonClass.chk1Pattern = json.get(TyreKey.chk1Pattern)?.asString
        }
        if (json.get(TyreKey.chk1Size) != null) {
            TyreDetailCommonClass.chk1Size = json.get(TyreKey.chk1Size)?.asString
        }
        if (json.get(TyreKey.chk2Make) != null) {
            TyreDetailCommonClass.chk2Make = json.get(TyreKey.chk2Make)?.asString
        }
        if (json.get(TyreKey.chk2Pattern) != null) {
            TyreDetailCommonClass.chk2Pattern = json.get(TyreKey.chk2Pattern)?.asString
        }
        if (json.get(TyreKey.chk2Size) != null) {
            TyreDetailCommonClass.chk2Size = json.get(TyreKey.chk2Size)?.asString
        }
        if (json.get(TyreKey.chk3Make) != null) {
            TyreDetailCommonClass.chk3Make = json.get(TyreKey.chk3Make)?.asString
        }
        if (json.get(TyreKey.chk3Pattern) != null) {
            TyreDetailCommonClass.chk3Pattern = json.get(TyreKey.chk3Pattern)?.asString
        }
        if (json.get(TyreKey.chk3Size) != null) {
            TyreDetailCommonClass.chk3Size = json.get(TyreKey.chk3Size)?.asString
        }
        if (json.get(TyreKey.isCompleted) != null) {
            TyreDetailCommonClass.isCompleted =
                json.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
        }
    }

    fun psiInSlider() {
        sliderIn = findViewById<FluidSlider>(R.id.multiSlider1)
        sliderIn?.positionListener = { pos ->
            sliderIn?.bubbleText = "${min + (total * pos).toInt()}"
            Log.e("getvaluess", "" + sliderIn?.bubbleText)
            psiInTyreService = sliderIn?.bubbleText
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
            Log.e("getvaluess", "" + multiSliderPsiOut?.bubbleText)
            psiOutTyreService = multiSliderPsiOut?.bubbleText
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
            Log.e("getvaluess", "" + multiSliderWeight?.bubbleText)
            weightTyreService = multiSliderWeight?.bubbleText
        }
        multiSliderWeight?.position = 0.3f
        multiSliderWeight?.startText = "$min"
        multiSliderWeight?.endText = "$max"
        multiSliderWeight?.animation?.cancel()

    }

    override fun onPositionClick(variable: Int, check: Int) {

        Log.e("getposs", "" + variable + " " + check)

        if (check == 10) {
            if (cameraDialog != null && cameraDialog?.isShowing!!) {
                cameraDialog?.dismiss()
            }
            if (Common.commonPhotoChooseArr?.get(variable)?.equals("Gallery")) {
                openGallery()
            }
            if (Common.commonPhotoChooseArr?.get(variable)?.equals("Camera")) {
                openCamera()
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

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        cameraDialog =
            this.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        cameraDialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        cameraDialog?.window?.setLayout(width, height)
        cameraDialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        cameraDialog?.setContentView(view)

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
            cameraDialog?.dismiss()
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

            cameraDialog?.dismiss()

        }

        cameraDialog?.show()

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
                Log.e("getslectedtyre", "" + selectedTyre)

                if (checkValidation() == false) {
                    return
                }

                if (selectedTyre.equals("LF")) {

                    TyreConfigClass.LFVehicleVisualDetail = true
                } else if (selectedTyre.equals("LR")) {
                    TyreConfigClass.LRVehicleVisualDetail = true
                } else if (selectedTyre.equals("RF")) {
                    TyreConfigClass.RFVehicleVisualDetail = true
                } else if (selectedTyre.equals("RR")) {
                    TyreConfigClass.RRVehicleVisualDetail = true
                }

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

                TyreDetailCommonClass.tyreType = selectedTyre
                TyreDetailCommonClass.manufaturingDate = edtManufaturingDate?.text.toString()
                TyreDetailCommonClass.psiInTyreService = psiInTyreService
                TyreDetailCommonClass.psiOutTyreService = psiOutTyreService
                TyreDetailCommonClass.weightTyreService = weightTyreService
                TyreDetailCommonClass.sidewell = sidewell
                TyreDetailCommonClass.shoulder = shoulder
                TyreDetailCommonClass.treadWear = treadWear
                TyreDetailCommonClass.treadDepth = treadDepth
                TyreDetailCommonClass.rimDamage = rimDamage
                TyreDetailCommonClass.bubble = bubble

                TyreDetailCommonClass.issueResolvedArr = selectedIssueArr

                Log.e("getslider", "" + TyreDetailCommonClass.issueResolvedArr)
                Log.e("getslider", "" + TyreDetailCommonClass.psiInTyreService)
                Log.e("getslider", "" + TyreDetailCommonClass.psiOutTyreService)
                Log.e("getslider", "" + TyreDetailCommonClass.weightTyreService)
                Log.e("getslider", "" + TyreDetailCommonClass.vehicleMake)
                Log.e("getslider", "" + TyreDetailCommonClass.vehicleMakeId)
                Log.e("getslider", "" + TyreDetailCommonClass.vehiclePattern)
                Log.e("getslider", "" + TyreDetailCommonClass.vehiclePatternId)
                Log.e("getslider", "" + TyreDetailCommonClass.vehicleSize)
                Log.e("getslider", "" + TyreDetailCommonClass.vehicleSizeId)

                setResult(1004)
                finish()
            }
            R.id.ivOkSideWell, R.id.llOkSideWell -> {

                ivOkSideWell?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)

                sidewell = "Ok"
            }
            R.id.ivSugSideWell, R.id.llSugSideWell -> {

                ivSugSideWell?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                sidewell = "SUG"
            }
            R.id.ivReqSideWell, R.id.llREQSideWell -> {

                ivReqSideWell?.setImageResource(R.mipmap.ic_condition_down)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                sidewell = "REQ"
            }

            R.id.ivOkShoulder, R.id.llOkShoulder -> {
                ivOkShoulder?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                shoulder = "Ok"
            }
            R.id.ivSugShoulder, R.id.llSugShoulder -> {
                ivSugShoulder?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                shoulder = "SUG"
            }
            R.id.ivReqShoulder, R.id.llReqShoulder -> {
                ivReqShoulder?.setImageResource(R.mipmap.ic_condition_down)
                ivSugShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                shoulder = "REQ"
            }
            R.id.ivOkTreadDepth, R.id.llOkTreadDepth -> {
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                treadDepth = "Ok"
            }
            R.id.ivSugTreadDepth, R.id.llSugTreadDepth -> {
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                treadDepth = "SUG"
            }
            R.id.ivReqTreadDepth, R.id.llReqTreadDepth -> {
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_condition_down)
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                treadDepth = "REQ"
            }
            R.id.ivOkTreadWear, R.id.llOkTreadWear -> {
                ivOkTreadWear?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                treadWear = "Ok"
            }
            R.id.ivSugTreadWear, R.id.llSugTreadWear -> {
                ivSugTreadWear?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                treadWear = "SUG"
            }
            R.id.ivReqTreadWear, R.id.llReqTreadWear -> {
                ivReqTreadWear?.setImageResource(R.mipmap.ic_condition_down)
                ivSugTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                treadWear = "REQ"
            }
            R.id.ivOkRimDamage, R.id.llOkRimDamage -> {
                ivOkRimDamage?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                rimDamage = "Ok"
            }
            R.id.ivSugRimDamage, R.id.llSugRimDamage -> {
                ivSugRimDamage?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                rimDamage = "SUG"
            }
            R.id.ivReqRimDamage, R.id.llReqRimDamage -> {
                ivReqRimDamage?.setImageResource(R.mipmap.ic_condition_down)
                ivSugRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                rimDamage = "REQ"
            }
            R.id.ivOkbubble, R.id.llOkBubble -> {
                ivOkbubble?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                bubble = "Ok"
            }
            R.id.ivSugbubble, R.id.llSugBubble -> {
                ivSugbubble?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkbubble?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqbubble?.setImageResource(R.mipmap.ic_blank_condition)
                bubble = "SUG"
            }
            R.id.ivReqbubble, R.id.llReqBubble -> {
                ivReqbubble?.setImageResource(R.mipmap.ic_condition_down)
                ivSugbubble?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkbubble?.setImageResource(R.mipmap.ic_blank_condition)
                bubble = "REQ"
            }


        }
    }

    private fun checkValidation(): Boolean {
        var isValid = true

        if (edtManufaturingDate?.text?.toString().equals("")) {
            Toast.makeText(this, "Please enter Manufaturing Date", Toast.LENGTH_SHORT).show()
            isValid = false
        }
//        if (selectedIssueArr != null && selectedIssueArr?.size!! > 0) {
//
//        } else {
//            Toast.makeText(this, "Please Select Issue Resolve List", Toast.LENGTH_SHORT).show()
//            isValid = false
//        }
        if (sidewell.equals("") || shoulder.equals("") || treadWear.equals("") ||
            treadDepth.equals("") || rimDamage.equals("") || bubble.equals("")
        ) {
            Toast.makeText(this, "Please Select Icons", Toast.LENGTH_SHORT).show()
            isValid = false
        }


        return isValid
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ivPickedImage1?.setImageURI(Uri.parse(imageFilePath))
                ivPickedImage1?.visibility = View.VISIBLE
                ivEditImg2?.visibility = View.VISIBLE
                tvAddPhoto1?.visibility = View.GONE
                tvCarphoto1?.visibility = View.GONE
                TyreDetailCommonClass.visualDetailPhotoUrl = imageFilePath
                Log.e("getimageuri", "" + imageFilePath)
                TyreDetailCommonClass.isCameraSelectedVisualDetail = true
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show()
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            ivPickedImage1?.setImageURI(data?.data)
            ivPickedImage1?.visibility = View.VISIBLE
            ivEditImg2?.visibility = View.VISIBLE
            tvAddPhoto1?.visibility = View.GONE
            tvCarphoto1?.visibility = View.GONE

            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

            val imageName = "${System.currentTimeMillis()}"
            Common.saveImage(this, bitmap, imageName, ".png")

            TyreDetailCommonClass.visualDetailPhotoUrl = imageName + ".png"
            Log.e("getimageuri", "" + imageName)
            TyreDetailCommonClass.isCameraSelectedVisualDetail = false
        }
    }


    fun loadBitmap(fileName: String): Bitmap {

        var fileInputStream: FileInputStream? = null
        var bitmap: Bitmap? = null;
        try {
            fileInputStream = this.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return bitmap!!;
    }

    fun loadFromFile(filename: String?): Bitmap? {
        return try {
            val f = File(filename)
            if (!f.exists()) {
                return null
            }
            BitmapFactory.decodeFile(filename)
        } catch (e: java.lang.Exception) {
            null
        }
    }
}

