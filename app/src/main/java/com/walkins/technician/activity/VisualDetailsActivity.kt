package com.walkins.technician.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramotion.fluidslider.FluidSlider
import com.theartofdev.edmodo.cropper.CropImage
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.TyreDetailCommonClass
import com.walkins.technician.common.onClickAdapter
import java.io.File

class VisualDetailsActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private var sliderIn: FluidSlider? = null
    private var multiSliderPsiOut: FluidSlider? = null
    private var multiSliderWeight: FluidSlider? = null
    var max = 50
    var min = 0
    var total = max - min

    private lateinit var mDb: DBClass

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

    private var tvTitle: TextView? = null
    private var tvCarphoto1: TextView? = null
    private var tvAddPhoto1: TextView? = null
    private var selectedTyre: String? = null

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String

    var image_uri: Uri? = null
    private val IMAGE_CAPTURE_RESULT = 1001
    private val PERMISSION_CODE = 1000;

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
    private var edtManufaturingDate: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_details)
        mDb = DBClass.getInstance(applicationContext)
        init()
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

        if (intent != null) {
            if (intent.hasExtra("selectedTyre")) {
                selectedTyre = intent.getStringExtra("selectedTyre")
            }
        }

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

        if (check == 10) {
            if (Common.commonPhotoChooseArr?.get(variable)?.equals("Gallery")) {
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions(this)
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
                Log.e("getslectedtyre", "" + selectedTyre)
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
                TyreDetailCommonClass.issueResolvedArr = issueResolveArr

                setResult(1004)
                finish()
            }
            R.id.ivOkSideWell -> {

                ivOkSideWell?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)

                sidewell = "Ok"
            }
            R.id.ivSugSideWell -> {

                ivSugSideWell?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                sidewell = "SUG"
            }
            R.id.ivReqSideWell -> {

                ivReqSideWell?.setImageResource(R.mipmap.ic_condition_down)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                sidewell = "REQ"
            }

            R.id.ivOkShoulder -> {
                ivOkShoulder?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                shoulder = "Ok"
            }
            R.id.ivSugShoulder -> {
                ivSugShoulder?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                shoulder = "SUG"
            }
            R.id.ivReqShoulder -> {
                ivReqShoulder?.setImageResource(R.mipmap.ic_condition_down)
                ivSugShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkShoulder?.setImageResource(R.mipmap.ic_blank_condition)
                shoulder = "REQ"
            }
            R.id.ivOkTreadDepth -> {
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                treadDepth = "Ok"
            }
            R.id.ivSugTreadDepth -> {
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                treadDepth = "SUG"
            }
            R.id.ivReqTreadDepth -> {
                ivReqTreadDepth?.setImageResource(R.mipmap.ic_condition_down)
                ivSugTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkTreadDepth?.setImageResource(R.mipmap.ic_blank_condition)
                treadDepth = "REQ"
            }
            R.id.ivOkTreadWear -> {
                ivOkTreadWear?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                treadWear = "Ok"
            }
            R.id.ivSugTreadWear -> {
                ivSugTreadWear?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                treadWear = "SUG"
            }
            R.id.ivReqTreadWear -> {
                ivReqTreadWear?.setImageResource(R.mipmap.ic_condition_down)
                ivSugTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkTreadWear?.setImageResource(R.mipmap.ic_blank_condition)
                treadWear = "REQ"
            }
            R.id.ivOkRimDamage -> {
                ivOkRimDamage?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                rimDamage = "Ok"
            }
            R.id.ivSugRimDamage -> {
                ivSugRimDamage?.setImageResource(R.mipmap.ic_condition_degrade)
                ivOkRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                rimDamage = "SUG"
            }
            R.id.ivReqRimDamage -> {
                ivReqRimDamage?.setImageResource(R.mipmap.ic_condition_down)
                ivSugRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkRimDamage?.setImageResource(R.mipmap.ic_blank_condition)
                rimDamage = "REQ"
            }
            R.id.ivOkbubble -> {
                ivOkbubble?.setImageResource(R.mipmap.ic_condition_ok)
                ivSugSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqSideWell?.setImageResource(R.mipmap.ic_blank_condition)
                bubble = "Ok"
            }
            R.id.ivSugbubble -> {
                ivSugbubble?.setImageResource(R.mipmap.ic_condition_degrade)
                ivSugbubble?.setImageResource(R.mipmap.ic_blank_condition)
                ivReqbubble?.setImageResource(R.mipmap.ic_blank_condition)
                bubble = "SUG"
            }
            R.id.ivReqbubble -> {
                ivReqbubble?.setImageResource(R.mipmap.ic_condition_down)
                ivSugbubble?.setImageResource(R.mipmap.ic_blank_condition)
                ivOkbubble?.setImageResource(R.mipmap.ic_blank_condition)
                bubble = "REQ"
            }


        }
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
                ivPickedImage1?.setImageURI(image_uri)
                CropImage.activity(image_uri)
                    .start(this)
            }


            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val auxFile = File(mCurrentPhotoPath)
                    Log.e("imagepath2", "" + auxFile)
                    var mImageBitmap = MediaStore.Images.Media.getBitmap(
                        this?.getContentResolver(),
                        Uri.parse(mCurrentPhotoPath)
                    );
                    Glide.with(this)
                        .load(Uri.fromFile(auxFile))
                        .into(ivPickedImage1!!)

                    CropImage.activity(Uri.fromFile(auxFile))
                        .start(this as Activity)

                    /* uploadProfileImage(auxFile)
                     Glide.with(this)
                         .load(Uri.fromFile(File(mCurrentPhotoPath)))
                         .into(imgProfile)*/
                }
            }

            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val selectedImage = data?.data
                    Log.e("imagepath2322", "" + selectedImage)

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        this?.let { Common.getFile(it, selectedImage) }
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    relTyrePhotoAdd?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.layout_bg_secondary_))
                    ivEditImg2?.visibility = View.VISIBLE
                    tvAddPhoto1?.visibility = View.GONE
                    tvCarphoto1?.visibility = View.GONE
                    Glide.with(this)
                        .load(Uri.fromFile(imagePath))
                        .into(ivPickedImage1!!)

                    Log.i("imagePath", "++++" + imagePath)

                    CropImage.activity(selectedImage)
                        .start(this as Activity)
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val selectedImage = result.uri

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        this?.let { Common.getFile(it, selectedImage) }
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                    Log.e("imagepath1", "" + imagePath + " " + selectedImage)
                    Glide.with(this)
                        .load(Uri.fromFile(imagePath))
                        .into(ivPickedImage1!!)

//                    ivProfileImg?.setImageURI(selectedImage)


//                    imagePath?.let { uploadCIPImage(it) }
                }
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
                if (grantResults.get(1) != -1) {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val file: File = this?.let { Common.createFile(it) }!!

                        mCurrentPhotoPath = file.absolutePath

                        val uri: Uri = this?.let {
                            FileProvider.getUriForFile(
                                it,
                                "com.walkins.technician.android.fileprovider",
                                file
                            )
                        }!!
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
        image_uri = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_RESULT)
    }
}

