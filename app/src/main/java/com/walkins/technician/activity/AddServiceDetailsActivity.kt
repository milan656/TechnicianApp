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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.TyreSuggestionAdpater
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.custom.BoldButton
import java.io.File

class AddServiceDetailsActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter,
    View.OnTouchListener {

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String

    var image_uri: Uri? = null
    private val IMAGE_CAPTURE_RESULT = 1001
    private val PERMISSION_CODE = 1000;

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

    private var tvSkipService: TextView? = null
    private var tvTyreAddInfo: TextView? = null

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
        tvSkipService = findViewById(R.id.tvSkipService)
        tvTyreAddInfo = findViewById(R.id.tvTyreAddInfo)
        tvTechnicalSuggetion = findViewById(R.id.tvTechnicalSuggetion)
        tvServices = findViewById(R.id.tvServices)
        tvTyreConfig = findViewById(R.id.tvTyreConfig)

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

        relCarPhotoAdd1?.setOnClickListener(this)
        relCarPhotoAdd2?.setOnClickListener(this)
        tvSkipService?.setOnClickListener(this)

        ivTyre1?.setOnTouchListener(this)
        ivTyre2?.setOnTouchListener(this)
        ivTyre3?.setOnTouchListener(this)
        ivTyre4?.setOnTouchListener(this)
        tvTyreAddInfo?.setOnTouchListener(this)
        ivAddServices?.setOnTouchListener(this)
        ivAddTyreConfig?.setOnTouchListener(this)
        llTyreConfigExpanded?.setOnTouchListener(this)
        ivAddTechnicalSuggestion?.setOnTouchListener(this)

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

                showBottomSheetdialog(
                    Common.commonPhotoChooseArr,
                    "Choose From",
                    this,
                    Common.btn_filled
                )
            }
            R.id.tvSkipService -> {
                openSkipServiceDialogue()
            }
        }
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
            var intent = Intent(this, ServiceDetailActivity::class.java)
            startActivityForResult(intent, 106)
        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 0) {

            Log.e("getposition0", "" + suggestionArr?.get(variable))
        } else if (check == 1) {
            Log.e("getposition1", "" + reasonArray?.get(variable))
        } else {
            if (Common.commonPhotoChooseArr?.get(variable).equals("Gallery")) {
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

                }
            } else if (Common.commonPhotoChooseArr?.get(variable).equals("Camera")) {
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
            dialog.dismiss()
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val id = v?.id
        val intent = Intent(this, VehicleMakeActivity::class.java)
        when (id) {

            R.id.tvTyreAddInfo -> {

                startActivityForResult(intent, 1000)
            }
            R.id.ivAddServices -> {
                if (llServiceExpanded?.visibility == View.VISIBLE) {
                    Common.collapse(llServiceExpanded!!)
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = false
                } else {
                    tvServices?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvServices?.isAllCaps = true
                    Common.expand(llServiceExpanded!!)

                    if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTyreConfigExpanded!!)
                    }
                    if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTechnicalSuggestionExpanded!!)
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
                } else {
                    tvTyreConfig?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTyreConfig?.isAllCaps = true
                    Common.expand(llTyreConfigExpanded!!)
                    if (llServiceExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llServiceExpanded!!)
                    }
                    if (llTechnicalSuggestionExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTechnicalSuggestionExpanded!!)
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

                } else {
                    tvTechnicalSuggetion?.setTypeface(Typeface.DEFAULT_BOLD)
                    tvTechnicalSuggetion?.isAllCaps = true

                    Common.expand(llTechnicalSuggestionExpanded!!)
                    if (llTyreConfigExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llTyreConfigExpanded!!)
                    }
                    if (llServiceExpanded?.visibility == View.VISIBLE) {
                        Common.collapse(llServiceExpanded!!)
                    }
                    tvServices?.isAllCaps = false
                    tvTyreAddInfo?.isAllCaps = false
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

                    CropImage.activity(Uri.fromFile(auxFile))
                        .start(this)

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

                    /* val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                         getFile(this@ProfileActivity , selectedImage)
                     } else {
                         TODO("VERSION.SDK_INT < KITKAT")
                     }

                     Log.i("imagePath","++++"+imagePath)*/

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
                        val file: File = Common.createFile(this)

                        mCurrentPhotoPath = file.absolutePath

                        val uri: Uri = FileProvider.getUriForFile(
                            this,
                            "com.jkadvantage.android.fileprovider",
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