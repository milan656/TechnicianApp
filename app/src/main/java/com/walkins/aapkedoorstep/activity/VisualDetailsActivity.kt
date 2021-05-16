package com.walkins.aapkedoorstep.activity

import android.Manifest
import android.R.attr
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.FragmentActivity
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
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ramotion.fluidslider.FluidSlider
import com.walkins.aapkedoorstep.DB.DBClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.DialogueAdpater
import com.walkins.aapkedoorstep.adapter.TyreSuggestionAdpater
import com.walkins.aapkedoorstep.common.*
import com.walkins.aapkedoorstep.model.login.IssueResolveModel
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VisualDetailsActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {
    private var loginViewModel: LoginActivityViewModel? = null
    private var commonViewModel: CommonViewModel? = null
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
    private var tvTyreService: TextView? = null

    // image picker code
//    val REQUEST_IMAGE = 100
//    val REQUEST_PERMISSION = 200
//    private var imageFilePath = ""
//    private var IMAGE_PICK_CODE = 1010;
//    private var PERMISSION_CODE = 1011;

    var mediaPath: String? = null
    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String
    private val PERMISSION_CODE = 1010;
    private val IMAGE_CAPTURE_CODE = 1011
    var image_uri: Uri? = null

    companion object {
        var ok_status = "OK"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_details)
        mDb = DBClass.getInstance(this)
        prefManager = PrefManager(this)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
//        requestPermissionForImage()
        init()
    }

//    private fun requestPermissionForImage() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !==
//            PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
//                ),
//                REQUEST_PERMISSION
//            )
//        }
//    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        btnDone = findViewById(R.id.btnDone)
        ivBack = findViewById(R.id.ivBack)

        weightFrame = findViewById(R.id.weightFrame)
        psiOutFrame = findViewById(R.id.psiOutFrame)
        psiInFrame = findViewById(R.id.psiInFrame)
        tvAddPhoto1 = findViewById(R.id.tvAddPhoto1)
        tvCarphoto1 = findViewById(R.id.tvCarphoto1)
        tvTyreService = findViewById(R.id.tvTyreService)

        edtManufaturingDate = findViewById(R.id.edtManufaturingDate)
        relTyrePhotoAdd = findViewById(R.id.relTyrePhotoAdd)
        issueResolvedRecycView = findViewById(R.id.issueResolvedRecycView)

//        for (i in issueResolveArr.indices) {
//            issueResolveArray?.add(IssueResolveModel(issueResolveArr.get(i) + " " + i, false))
//        }
        issueResolveAdapter = TyreSuggestionAdpater(issueResolveArray!!, this, this, false, true)
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
        tvTyreService?.setText("" + selectedTyre + " " + "Tyre Service")

        btnDone?.setOnClickListener(this)

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
        ivEditImg2?.setOnClickListener(this)
        ivPickedImage1?.setOnClickListener(this)
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

//        getIssueList()

        if (prefManager.getIssueList(TyreConfigClass.issueList) != null &&
            prefManager.getIssueList(TyreConfigClass.issueList)?.size!! > 0
        ) {
            issueResolveArray?.clear()
            for (i in prefManager.getIssueList(TyreConfigClass.issueList)?.indices!!) {
                issueResolveArray?.add(
                    IssueResolveModel(
                        prefManager.getIssueList(TyreConfigClass.issueList)?.get(i)?.issueName!!, prefManager.getIssueList(TyreConfigClass.issueList)?.get(i)?.id!!, false
                    )
                )
            }
            issueResolveAdapter?.notifyDataSetChanged()
            getTyreWiseData()
        }

        if (prefManager?.getValue(TyreConfigClass.serviceDetailData) != null &&
            !prefManager.getValue(TyreConfigClass.serviceDetailData).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.serviceDetailData)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getservice", "" + json)

                if (json.get(TyreKey.serviceArr) != null) {

                    var arr = json.get(TyreKey.serviceArr)?.asJsonArray
                    Log.e("getvalues", "" + arr)
                    val gson = Gson()
                    val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
                    val arrlist: ArrayList<String> = gson.fromJson(arr?.toString(), type)
                    Log.e("getvalues", "" + arrlist)

                    psiOutFrame?.visibility = View.GONE
                    psiInFrame?.visibility = View.GONE
                    weightFrame?.visibility = View.GONE
                    for (j in arrlist.indices) {

                        if (arrlist.get(j).equals("Nitrogen Refill") || arrlist.get(j).equals("Nitrogen Top Up")) {
                            psiOutFrame?.visibility = View.VISIBLE
                            psiInFrame?.visibility = View.VISIBLE
                        }
                        if (arrlist.get(j).equals("Wheel Balancing")) {
                            weightFrame?.visibility = View.VISIBLE
                        }
                    }


                }

                /*if ((json.get(TyreKey.nitrogenTopup) != null && json.get(TyreKey.nitrogenTopup)?.asString.equals(
                        "true"
                    )) ||
                    (json.get(TyreKey.nitrogenRefil) != null && json.get(TyreKey.nitrogenRefil)?.asString.equals(
                        "true"
                    ))
                ) {
                    psiOutFrame?.visibility = View.VISIBLE
                    psiInFrame?.visibility = View.VISIBLE
                } else {
                    psiOutFrame?.visibility = View.GONE
                    psiInFrame?.visibility = View.GONE
                }

                if (json.get(TyreKey.wheelBalancing) != null && json.get(TyreKey.wheelBalancing)?.asString.equals(
                        "true"
                    )
                ) {
                    weightFrame?.visibility = View.VISIBLE
                } else {
                    weightFrame?.visibility = View.GONE
                }*/
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }

        if (prefManager.getValue("image_" + selectedTyre) != null &&
            !prefManager.getValue("image_" + selectedTyre).equals("")
        ) {
            Log.e("getimages1", "" + prefManager.getValue("image_" + selectedTyre))
//            ivPickedImage1?.setImageURI(Uri.parse(prefManager.getValue("image_" + selectedTyre)))

            try {
                Glide.with(this).load(prefManager.getValue("image_" + selectedTyre)).thumbnail(0.33f).into(ivPickedImage1!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            TyreDetailCommonClass.visualDetailPhotoUrl = prefManager.getValue("image_" + selectedTyre)
            ivPickedImage1?.visibility = View.VISIBLE
            ivEditImg2?.visibility = View.VISIBLE
            tvAddPhoto1?.visibility = View.GONE
            tvCarphoto1?.visibility = View.GONE
            relTyrePhotoAdd?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
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


    private fun getIssueList() {
        Common.showLoader(this)
        commonViewModel?.callApiListOfIssue(prefManager.getAccessToken()!!, this)
        commonViewModel?.getListOfIssue()?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success) {

                    issueResolveArray?.clear()
                    if (it.data != null && it.data.size > 0) {
                        for (i in it.data.indices) {
                            issueResolveArray?.add(
                                IssueResolveModel(
                                    it.data.get(i).name, it.data.get(i).id, false
                                )
                            )
                        }
                    }
                    issueResolveAdapter?.notifyDataSetChanged()
                    getTyreWiseData()
                    Common.hideLoader()
                } else {
                    getTyreWiseData()
                    Common.hideLoader()
                }
            } else {
                Common.hideLoader()
            }
        })
    }

    fun getTyreWiseData() {
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
    }

    private fun getData(json: JsonObject) {
        GlobalScope.launch(Dispatchers.Main) {
            launch(Dispatchers.Main) {
                Log.e("getobje", "" + json)
                if (json.get(TyreKey.manufaturingDate) != null) {

                    edtManufaturingDate?.setText(json.get(TyreKey.manufaturingDate)?.asString!!)
                }
                if (json.get(TyreKey.psiInTyreService) != null) {
                    psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString!!
                    sliderIn?.bubbleText = psiInTyreService
                    sliderIn?.animation?.start()
                    Log.e("getvaluess0", "" + sliderIn?.bubbleText)
                }
                if (json.get(TyreKey.psiOutTyreService) != null) {

                    psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString!!
                    multiSliderPsiOut?.bubbleText = psiOutTyreService
                    multiSliderPsiOut?.animation?.start()
                    Log.e("getvaluess11", "" + multiSliderPsiOut?.bubbleText)
                }
                if (json.get(TyreKey.weightTyreService) != null) {
                    weightTyreService = json.get(TyreKey.weightTyreService)?.asString!!
                    multiSliderWeight?.bubbleText = weightTyreService
                    multiSliderWeight?.animation?.start()
                    Log.e("getvaluess22", "" + multiSliderWeight?.bubbleText)
                }
                if (json.get(TyreKey.sidewell) != null) {
                    if (json.get(TyreKey.sidewell)?.asString!!.equals(ok_status)) {
                        ivOkSideWell?.performClick()
                        sidewell = ok_status
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

                    if (json.get(TyreKey.shoulder)?.asString!!.equals(ok_status)) {
                        ivOkShoulder?.performClick()
                        shoulder = ok_status
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
                    if (json.get(TyreKey.treadDepth)?.asString!!.equals(ok_status)) {
                        ivOkTreadDepth?.performClick()
                        treadDepth = ok_status
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
                    if (json.get(TyreKey.treadWear)?.asString!!.equals(ok_status)) {
                        ivOkTreadWear?.performClick()
                        treadWear = ok_status
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
                    if (json.get(TyreKey.rimDamage)?.asString!!.equals(ok_status)) {
                        ivOkRimDamage?.performClick()
                        rimDamage = ok_status
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
                    if (json.get(TyreKey.bubble)?.asString!!.equals(ok_status)) {
                        ivOkbubble?.performClick()
                        bubble = ok_status
                    }
                    if (json.get(TyreKey.bubble)?.asString!!.equals("SUG")) {
                        ivSugbubble?.performClick()
                        bubble = "SUG"
                    }
                }
                if (json.get(TyreKey.visualDetailPhotoUrl) != null
                    && !json.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")
                ) {

                    try {
                        Glide.with(this@VisualDetailsActivity).load(json.get(TyreKey.visualDetailPhotoUrl)?.asString)
                            .into(ivPickedImage1!!)
                        ivPickedImage1?.visibility = View.VISIBLE
                        ivEditImg2?.visibility = View.VISIBLE
                        tvAddPhoto1?.visibility = View.GONE
                        tvCarphoto1?.visibility = View.GONE
                        relTyrePhotoAdd?.setBackgroundDrawable(this@VisualDetailsActivity.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    } catch (e: Exception) {
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
                    if (issueResolveArray != null && issueResolveArray?.size!! > 0) {
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
                }

                setData(json)

            }
        }

    }

    private fun setData(json: JsonObject) {
        Log.e("getvalselected", "" + json)
        if (selectedTyre != null && !selectedTyre.equals("")) {
            TyreDetailCommonClass.tyreType = selectedTyre
        }
        if (TyreDetailCommonClass.vehicleMake.equals("")) {

            if (json.get(TyreKey.vehicleMake) != null && !json.get(TyreKey.vehicleMake)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleMake = json.get(TyreKey.vehicleMake)?.asString
            }
        }
        if (TyreDetailCommonClass.vehicleMakeId.equals("")) {

            if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleMakeId = json.get(TyreKey.vehicleMakeId)?.asString
            }
        }
        if (TyreDetailCommonClass.vehicleMakeURL.equals("")) {

            if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleMakeURL = json.get(TyreKey.vehicleMakeURL)?.asString
            }
        }
        if (TyreDetailCommonClass.vehiclePattern.equals("")) {

            if (json.get(TyreKey.vehiclePattern) != null && !json.get(TyreKey.vehiclePattern)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehiclePattern = json.get(TyreKey.vehiclePattern)?.asString
            }
        }
        if (TyreDetailCommonClass.vehiclePatternId.equals("")) {

            if (json.get(TyreKey.vehiclePatternId) != null && !json.get(TyreKey.vehiclePatternId)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehiclePatternId = json.get(TyreKey.vehiclePatternId)?.asString
            }
        }
        if (TyreDetailCommonClass.vehicleMakeURL.equals("")) {

            if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleMakeURL = json.get(TyreKey.vehicleMakeURL)?.asString
            }
        }
        if (TyreDetailCommonClass.vehicleSize.equals("")) {
            if (json.get(TyreKey.vehicleSize) != null && !json.get(TyreKey.vehicleSize)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleSize = json.get(TyreKey.vehicleSize)?.asString
            }
        }
        if (TyreDetailCommonClass.vehicleSizeId.equals("")) {
            if (json.get(TyreKey.vehicleSizeId) != null && !json.get(TyreKey.vehicleSizeId)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleSizeId = json.get(TyreKey.vehicleSizeId)?.asString
            }
        }

        if (json.get(TyreKey.manufaturingDate) != null && !json.get(TyreKey.manufaturingDate)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.manufaturingDate = json.get(TyreKey.manufaturingDate)?.asString
        }
        if (json.get(TyreKey.psiInTyreService) != null && !json.get(TyreKey.psiInTyreService)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString
        }
        if (json.get(TyreKey.psiOutTyreService) != null && !json.get(TyreKey.psiOutTyreService)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString
        }
        if (json.get(TyreKey.weightTyreService) != null && !json.get(TyreKey.weightTyreService)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.weightTyreService = json.get(TyreKey.weightTyreService)?.asString
        }
        if (json.get(TyreKey.sidewell) != null && !json.get(TyreKey.sidewell)?.asString.equals("")) {
            TyreDetailCommonClass.sidewell = json.get(TyreKey.sidewell)?.asString
        }
        if (json.get(TyreKey.shoulder) != null && !json.get(TyreKey.shoulder)?.asString.equals("")) {
            TyreDetailCommonClass.shoulder = json.get(TyreKey.shoulder)?.asString
        }
        if (json.get(TyreKey.treadDepth) != null && !json.get(TyreKey.treadDepth)?.asString.equals("")) {
            TyreDetailCommonClass.treadDepth = json.get(TyreKey.treadDepth)?.asString
        }
        if (json.get(TyreKey.treadWear) != null && !json.get(TyreKey.treadWear)?.asString.equals("")) {
            TyreDetailCommonClass.treadWear = json.get(TyreKey.treadWear)?.asString
        }
        if (json.get(TyreKey.rimDamage) != null && !json.get(TyreKey.rimDamage)?.asString.equals("")) {
            TyreDetailCommonClass.rimDamage = json.get(TyreKey.rimDamage)?.asString
        }
        if (json.get(TyreKey.bubble) != null && !json.get(TyreKey.bubble)?.asString.equals("")) {
            TyreDetailCommonClass.bubble = json.get(TyreKey.bubble)?.asString
        }
        if (json.get(TyreKey.visualDetailPhotoUrl) != null && !json.get(TyreKey.visualDetailPhotoUrl)?.asString.equals(
                ""
            )
        ) {
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
        /* if (json.get(TyreKey.chk1Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
             TyreDetailCommonClass.chk1Make = json.get(TyreKey.chk1Make)?.asString
         }
         if (json.get(TyreKey.chk1Pattern) != null && !json.get(TyreKey.chk1Pattern)?.asString.equals(
                 ""
             )
         ) {
             TyreDetailCommonClass.chk1Pattern = json.get(TyreKey.chk1Pattern)?.asString
         }
         if (json.get(TyreKey.chk1Size) != null && !json.get(TyreKey.chk1Size)?.asString.equals("")) {
             TyreDetailCommonClass.chk1Size = json.get(TyreKey.chk1Size)?.asString
         }
         if (json.get(TyreKey.chk2Make) != null && !json.get(TyreKey.chk2Make)?.asString.equals("")) {
             TyreDetailCommonClass.chk2Make = json.get(TyreKey.chk2Make)?.asString
         }
         if (json.get(TyreKey.chk2Pattern) != null && !json.get(TyreKey.chk2Pattern)?.asString.equals(
                 ""
             )
         ) {
             TyreDetailCommonClass.chk2Pattern = json.get(TyreKey.chk2Pattern)?.asString
         }
         if (json.get(TyreKey.chk2Size) != null && !json.get(TyreKey.chk2Size)?.asString.equals("")) {
             TyreDetailCommonClass.chk2Size = json.get(TyreKey.chk2Size)?.asString
         }
         if (json.get(TyreKey.chk3Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
             TyreDetailCommonClass.chk3Make = json.get(TyreKey.chk3Make)?.asString
         }
         if (json.get(TyreKey.chk3Pattern) != null && !json.get(TyreKey.chk3Pattern)?.asString.equals(
                 ""
             )
         ) {
             TyreDetailCommonClass.chk3Pattern = json.get(TyreKey.chk3Pattern)?.asString
         }
         if (json.get(TyreKey.chk3Size) != null && !json.get(TyreKey.chk3Size)?.asString.equals("")) {
             TyreDetailCommonClass.chk3Size = json.get(TyreKey.chk3Size)?.asString
         }*/
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
            if (Common.commonPhotoChooseArr.get(variable)?.equals("Gallery")) {
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission((this@VisualDetailsActivity))
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
            if (Common.commonPhotoChooseArr.get(variable).equals("Camera")) {
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(context: FragmentActivity?): Boolean {
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
                ) !== PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    context,
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


    private fun openCamera() {
//        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (pictureIntent.resolveActivity(packageManager) != null) {
//            var photoFile: File? = null
//            photoFile = try {
//                Common.createImageFile(this)
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//                return
//            }
//            imageFilePath = photoFile?.absolutePath!!
//            val photoUri: Uri =
//                FileProvider.getUriForFile(this, "$packageName.provider", photoFile!!)
//            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            startActivityForResult(pictureIntent, REQUEST_IMAGE)
//        }

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

//    private fun createImageFile(): File? {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val imageFileName = "IMG_" + timeStamp + "_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
//        imageFilePath = image.absolutePath
//        return image
//    }

//    private fun openGallery() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_DENIED
//            ) {
//                //permission denied
//                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
//                //show popup to request runtime permission
//                requestPermissions(permissions, PERMISSION_CODE);
//            } else {
//                //permission already granted
//                pickImageFromGallery();
//            }
//        } else {
//            //system OS is < Marshmallow
//            pickImageFromGallery();
//        }
//    }

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
                if (TyreDetailCommonClass.visualDetailPhotoUrl.equals("")) {
                    showBottomSheetdialog(
                        Common.commonPhotoChooseArr,
                        "Choose From",
                        this,
                        Common.btn_not_filled
                    )
                }
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivEditImg2 -> {
                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_not_filled
                )
            }
            R.id.ivPickedImage1 -> {
                Log.e("getimageurl", "" + TyreDetailCommonClass.visualDetailPhotoUrl)
                if (!TyreDetailCommonClass.visualDetailPhotoUrl.equals("")) {
                    showImage(TyreDetailCommonClass.visualDetailPhotoUrl!!)
                }
            }
            R.id.btnDone -> {
                Log.e("getslectedtyre", "" + selectedTyre)

                if (edtManufaturingDate?.text?.toString().equals("")) {
                    Toast.makeText(this, "Please enter Manufaturing Date", Toast.LENGTH_SHORT).show()
                    return

                }
//        if (selectedIssueArr != null && selectedIssueArr?.size!! > 0) {
//
//        } else {
//            Toast.makeText(this, "Please Select Issue Resolve List", Toast.LENGTH_SHORT).show()
//            return
//        }
                if (sidewell.equals("")
                ) {
                    Toast.makeText(this, "Please select side well condition", Toast.LENGTH_SHORT).show()
                    return
                }
                if (shoulder.equals("")) {
                    Toast.makeText(this, "Please select shoulder condition", Toast.LENGTH_SHORT).show()
                    return
                }
                if (treadWear.equals("")) {
                    Toast.makeText(this, "Please select Tread Wear condition", Toast.LENGTH_SHORT).show()
                    return
                }
                if (treadDepth.equals("")) {
                    Toast.makeText(this, "Please select Tread Depth condition", Toast.LENGTH_SHORT).show()
                    return
                }
                if (rimDamage.equals("")) {
                    Toast.makeText(this, "Please select Rim Damage condition", Toast.LENGTH_SHORT).show()
                    return
                }
                if (bubble.equals("")) {
                    Toast.makeText(this, "Please select Bulge/Bubble condition", Toast.LENGTH_SHORT).show()
                    return
                }

                selectedIssueArr?.clear()

                if (issueResolveArray != null) {
                    Log.e("issuelist", "" + issueResolveAdapter)
                    for (i in issueResolveArray?.indices!!) {
                        Log.e("issuelist", "" + issueResolveArray?.get(i))
                        if (issueResolveArray?.get(i)?.isSelected!!) {
                            selectedIssueArr?.add(
                                issueResolveArray?.get(i)?.issueName!!
                            )
                        }
                    }
                }

                if (selectedIssueArr != null && selectedIssueArr?.size!! > 0) {

                } else {
                    Toast.makeText(this, "Please select Issue resolve List", Toast.LENGTH_SHORT)
                        .show()
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
                Log.e("getslider", "" + TyreDetailCommonClass.vehicleMakeURL)
                Log.e("getslider", "" + TyreDetailCommonClass.chk1Pattern)
                Log.e("getslider", "" + TyreDetailCommonClass.chk2Pattern)
                Log.e("getslider", "" + TyreDetailCommonClass.chk3Pattern)

                if (weightFrame?.visibility == View.VISIBLE) {
                    TyreDetailCommonClass.weightTyreService = weightTyreService
                } else {
                    TyreDetailCommonClass.weightTyreService = ""
                }
                if (psiInFrame?.visibility == View.VISIBLE) {
                    TyreDetailCommonClass.psiInTyreService = psiInTyreService
                } else {
                    TyreDetailCommonClass.psiInTyreService = ""
                }
                if (psiOutFrame?.visibility == View.VISIBLE) {
                    TyreDetailCommonClass.psiOutTyreService = psiOutTyreService
                } else {
                    TyreDetailCommonClass.psiOutTyreService = ""
                }


                setResult(1004)
                finish()
            }
            R.id.ivOkSideWell, R.id.llOkSideWell -> {

                ivOkSideWell?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)

                sidewell = ok_status
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
                shoulder = ok_status
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
                treadDepth = ok_status
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
                treadWear = ok_status
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
                rimDamage = ok_status
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
                bubble = ok_status
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


        return isValid
    }


//    private fun pickImageFromGallery() {
//        //Intent to pick image
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_CODE)
//    }

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
                if (grantResults?.get(1) != -1) {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        /*val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val file: File = createFile()

                        val uri: Uri = FileProvider.getUriForFile(
                            this,
                            "com.walkins.aapkedoorstep.android.fileprovider",
                            file
                        )
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)*/

                        try {
                            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "image/*"
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(intent, PICK_IMAGE_REQUEST)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


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
                            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
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

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IMAGE_CAPTURE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@VisualDetailsActivity, image_uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

//                    val inputStream_ = contentResolver.openInputStream(image_uri!!)
//                    val exifInterface = ExifInterface(inputStream_!!)

                    Log.e(
                        "TAG",
                        "===${data?.dataString}"
                    )

                    ivPickedImage1?.setImageURI(image_uri)
                    ivPickedImage1?.visibility = View.VISIBLE
                    ivEditImg2?.visibility = View.VISIBLE
                    tvAddPhoto1?.visibility = View.GONE
                    tvCarphoto1?.visibility = View.GONE
                    relTyrePhotoAdd?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    Log.e("getimages", "" + prefManager.getValue("image_" + selectedTyre))
                    if (Common.isConnectedToInternet(this)) {
                        val inputStream: InputStream? = imagePath?.inputStream()
//                            this.contentResolver?.openInputStream(image_uri!!)
                        prefManager.removeValue("image_" + selectedTyre)
                        imagePath?.let { uploadImage(it, inputStream!!, "service-image") }
                    } else {
                        prefManager.setValue("image_" + selectedTyre, image_uri.toString())
                        TyreDetailCommonClass.visualDetailPhotoUrl = image_uri.toString()

                        setUriTyreWise(image_uri!!)
                    }
                }

            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val auxFile = File(mCurrentPhotoPath)

//                    val inputStream_ = contentResolver.openInputStream(Uri.parse(mCurrentPhotoPath))
//                    val exifInterface = ExifInterface(inputStream_!!)

                    Log.e(
                        "TAG",
                        "===${data?.dataString}"
                    )

                    Log.e("getfile00", "" + mCurrentPhotoPath + " " + Uri.parse(mCurrentPhotoPath))
                    ivPickedImage1?.setImageURI(Uri.parse(mCurrentPhotoPath))
                    ivPickedImage1?.visibility = View.VISIBLE
                    ivEditImg2?.visibility = View.VISIBLE
                    tvAddPhoto1?.visibility = View.GONE
                    tvCarphoto1?.visibility = View.GONE

                    relTyrePhotoAdd?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    Log.e("getimages", "" + prefManager.getValue("image_" + selectedTyre))
                    if (Common.isConnectedToInternet(this)) {
                        val inputStream: InputStream? = auxFile.inputStream()
//                            this.contentResolver?.openInputStream(Uri.parse(mCurrentPhotoPath)!!)
                        prefManager.removeValue("image_" + selectedTyre)
                        auxFile.let { uploadImage(it, inputStream!!, "service-image") }
                    } else {

                        prefManager.setValue("image_" + selectedTyre, Uri.parse(mCurrentPhotoPath).toString())
                        TyreDetailCommonClass.visualDetailPhotoUrl = Uri.parse(mCurrentPhotoPath).toString()
                        setUriTyreWise(Uri.parse(mCurrentPhotoPath)!!)
                    }
                }
            }

            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val selectedImage = data?.data

//                    val inputStream_ = contentResolver.openInputStream(data?.data!!)
//                    val exifInterface = ExifInterface(inputStream_!!)

                    Log.e(
                        "TAG",
                        "===${data?.dataString}"
                    )
                    try {
                        Glide.with(this)
                            .load(data?.dataString)
                            .thumbnail(0.33f)
                            .into(ivPickedImage1!!)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@VisualDetailsActivity, selectedImage)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

//                    ivPickedImage1?.setImageURI(selectedImage)
                    ivPickedImage1?.visibility = View.VISIBLE
                    ivEditImg2?.visibility = View.VISIBLE
                    tvAddPhoto1?.visibility = View.GONE
                    tvCarphoto1?.visibility = View.GONE
                    relTyrePhotoAdd?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))

                    if (Common.isConnectedToInternet(this)) {
                        val inputStream: InputStream? = imagePath?.inputStream()
//                            this.contentResolver?.openInputStream(selectedImage!!)
                        prefManager.removeValue("image_" + selectedTyre)
                        imagePath?.let { uploadImage(it, inputStream!!, "service-image") }
                    } else {
                        prefManager.setValue("image_" + selectedTyre, data?.dataString)
                        TyreDetailCommonClass.visualDetailPhotoUrl = selectedImage.toString()
                        setUriTyreWise(selectedImage!!)
                    }

                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path = getPath(context, uri)
            if (path != null && isLocal(path)) {
                return File(path)
            }
        }
        return null
    }


    fun isLocal(url: String?): Boolean {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://")
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {


        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split =
                    docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return "" + Environment.getExternalStorageDirectory() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split =
                    docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
            // ExternalStorageProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    private fun uploadImage(imagePath: File, inputStream: InputStream, type: String) {
        Common.showLoader(this)

//        val requestFile = RequestBody.create(
//            MediaType.parse("image/*"),
//            imagePath
//        )
//
//        val body = MultipartBody.Part.createFormData("file", imagePath.name, requestFile)

        val part = MultipartBody.Part.createFormData(
            "file", imagePath.name, RequestBody.create(
                MediaType.parse("image/*"),
                inputStream.readBytes()
            )
        )

        loginViewModel?.uploadImage(part, prefManager.getAccessToken()!!, this, type)

        loginViewModel?.getImageUpload()?.observe(this, androidx.lifecycle.Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("getfile", "" + it.data.imageUrl)
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
//                    try {
//                        Glide.with(this).load(it.data.imageUrl).into(ivPickedImage1!!)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                    ivPickedImage1?.visibility = View.VISIBLE
//                    ivEditImg2?.visibility = View.VISIBLE
//                    tvAddPhoto1?.visibility = View.GONE
//                    tvCarphoto1?.visibility = View.GONE
                    TyreDetailCommonClass.visualDetailPhotoUrl = it.data.imageUrl
                }
            }
        })
    }

    private fun showImage(posterUrl: String?) {
        val builder = AlertDialog.Builder(this@VisualDetailsActivity).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        builder.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

        val root = LayoutInflater.from(this@VisualDetailsActivity)
            .inflate(R.layout.dialogue_image, null)

        val tvTitleRemarks =
            root.findViewById<TextView>(R.id.tvTitleRemarks)
        val imgPoster =
            root.findViewById<ImageView>(R.id.imgPoster)


        Glide.with(this@VisualDetailsActivity)
            .load(posterUrl)
            .override(1600, 1600)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(0.33f)
            .placeholder(R.drawable.placeholder)
            .into(imgPoster)

        /*if (prefManager.getValue("image_" + selectedTyre)!=null &&
                !prefManager.getValue("image_" + selectedTyre).equals("")) {
            try {
                Glide.with(this).load(prefManager.getValue("image_" + selectedTyre))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgPoster)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
        tvTitleRemarks?.text = "View Tyre Image"

        val imgClose = root.findViewById<ImageView>(R.id.imgClose)


        imgClose.setOnClickListener { builder.dismiss() }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    fun setUriTyreWise(uri: Uri) {
        if (selectedTyre.equals("LF")) {
            TyreDetailCommonClass.tyre_Uri_LF = uri
        }
        if (selectedTyre.equals("LR")) {
            TyreDetailCommonClass.tyre_Uri_LR = uri
        }
        if (selectedTyre.equals("RF")) {
            TyreDetailCommonClass.tyre_Uri_RF = uri
        }
        if (selectedTyre.equals("RR")) {
            TyreDetailCommonClass.tyre_Uri_RR = uri
        }
    }
}

