package com.walkins.aapkedoorstep.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.getFile
import com.example.technician.common.Common.Companion.setTint
import com.example.technician.common.Common.Companion.setTintNull
import com.example.technician.common.PrefManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.DB.DBClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.*
import com.walkins.aapkedoorstep.fragment.HomeFragment
import com.walkins.aapkedoorstep.fragment.NotificationFragment
import com.walkins.aapkedoorstep.fragment.ProfileFragment
import com.walkins.aapkedoorstep.fragment.ReportFragment
import com.walkins.aapkedoorstep.model.login.IssueResolveModel
import com.walkins.aapkedoorstep.model.login.comment.CommentListData
import com.walkins.aapkedoorstep.model.login.comment.CommentListModel
import com.walkins.aapkedoorstep.model.login.service.ServiceModelData
import com.walkins.aapkedoorstep.service.Actions
import com.walkins.aapkedoorstep.service.BackgroundService
import com.walkins.aapkedoorstep.service.ServiceState
import com.walkins.aapkedoorstep.service.getServiceState
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream
import java.util.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var dialogue: Dialog? = null
    private var serviceList: ArrayList<ServiceModelData>? = ArrayList()
    private var issueResolveArray: ArrayList<IssueResolveModel>? = ArrayList()
    private var commentModel: CommentListModel? = null
    private var commentList: ArrayList<CommentListData>? = ArrayList()
    private var commonViewModel: CommonViewModel? = null
    private var ivHome: ImageView? = null
    private var ivNotification: ImageView? = null
    private var ivReport: ImageView? = null
    private var ivProfile: ImageView? = null
    private var llhome: LinearLayout? = null
    private var llReport: LinearLayout? = null
    private var llNotification: LinearLayout? = null
    private var llProfile: LinearLayout? = null
    var tvUsername: TextView? = null
    private var notiCount = 0

    private var prefManager: PrefManager? = null
    private var loginViewModel: LoginActivityViewModel? = null
    private var selectedMenu: String? = null
    public var lltransparent: LinearLayout? = null
    public var homeSwipeRefresh: SwipeRefreshLayout? = null
    private lateinit var mDb: DBClass

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String
    private val PERMISSION_CODE = 1010;
    private val IMAGE_CAPTURE_CODE = 1011
    var image_uri: Uri? = null

    private var isFromNotification: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefManager = PrefManager(this)
        mDb = DBClass.getInstance(this)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        Log.e("getaccessToken", "" + prefManager?.getAccessToken())
        init()

        if (Common.isConnectedToInternet(this)) {
            val diff = Common.dateDifference(prefManager?.getAccessTokenExpireDate()!!)
            if (diff <= 1) {
                refreshToken()
            }

            callApiTogetToken()
            getServiceList()
            getCommentList()
            getIssueList()
        }

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val weekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        Log.e("getyear", "" + year + " " + weekOfYear)


        llhome?.performClick()
//        getNotificationCount()


    }

    private fun refreshToken() {
        loginViewModel?.refreshToken(
            "Basic ZG9vcnN0ZXA6MTIz", "refresh_token", prefManager?.getRefreshToken()
        )

        loginViewModel?.getLoginData()?.observe(this, Observer {
            if (it != null) {
                if (it.success) {
                    if (it.accessToken != null && !it.accessToken.equals("")) {
                        prefManager?.setAccessToken("Bearer " + it.accessToken)
                        prefManager?.setRefreshToken(it.refreshToken)
                        prefManager?.setToken(it.token)
                        prefManager?.setUuid(it.userDetailModel!!.uuid)

//                    firebaseAnalytics?.setUserId(it.userDetailModel?.sap_id!!);

                        prefManager?.setAccessTokenExpireDate(it.accessTokenExpiresAt)
                        if (it.userDetailModel?.owner_name != null) {
                            prefManager?.setOwnerName(it.userDetailModel?.owner_name)
                        }
                    }
                }
            }
        })

    }

    private fun getIssueList() {
        commonViewModel?.callApiListOfIssue(prefManager?.getAccessToken()!!, this)
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

                    prefManager?.saveIssueList(TyreConfigClass.issueList, issueResolveArray!!)
                    Common.hideLoader()
                } else {
                    Common.hideLoader()
                }
            } else {
                Common.hideLoader()
            }
        })
    }

    private fun getCommentList() {
        commonViewModel?.callApiGetComments(prefManager?.getAccessToken()!!, this)
        commonViewModel?.getCommentList()?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success) {

                    commentModel = it
                    commentList?.clear()
                    commentList?.addAll(it.data)
                    prefManager?.saveCommentList(TyreConfigClass.commentList, commentList)
                    Common.hideLoader()

                } else {
                    Common.hideLoader()
                    try {
                        showShortToast(it.error.get(0).message, this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } else {
                Common.hideLoader()
            }
        })
    }

    private fun getServiceList() {

        commonViewModel?.callApiGetService(prefManager?.getAccessToken()!!, this)
        commonViewModel?.getService()?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success) {

                    if (it.data.size > 0) {
                        serviceList?.clear()
                        serviceList?.addAll(it.data)

                        prefManager?.saveServiceList(TyreConfigClass.serviceList, serviceList)
                    }

                } else {
                    try {
                        showShortToast(it.error.get(0).message, this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getNotificationCount() {

        this.let {
            Common.showLoader(this)
            commonViewModel?.callApiGetNotificationCount(prefManager?.getAccessToken()!!, it)
            commonViewModel?.getNotiCount()?.observe(it, Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {
                        notiCount = it.data.count
//                        notiCount = 10
                        if (notiCount > 0) {
                            ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_count))
                            ivNotification?.setTintNull()
                        } else {
                            ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_icon))
                        }
                    } else {
                        if (it.error != null) {
                            if (it.error.get(0).message != null) {

                            }
                        }
                    }
                }
            })
        }
    }

    private fun callApiTogetToken() {
        try {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->

                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    Log.i("token", "+++" + task.result)
                    // Get new Instance ID token
                    val token = task.result?.token

                    Log.i("token", "+++" + token)
                    RemoveOrAddTokenForApi(token!!)
                })

        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("token", "+++" + e.cause + " " + e.message)

        }
    }

    private fun RemoveOrAddTokenForApi(token: String) {

        val jsonObject: JsonObject
        jsonObject = JsonObject()
        jsonObject.addProperty("token", token)

        this.let {
            commonViewModel?.callApiToSaveToken(jsonObject, prefManager?.getAccessToken()!!, it)
            commonViewModel?.getSaveToken()?.observe(it, Observer {
                if (it != null) {
                    if (it.success) {

//
                    } else {
                        if (it.error != null && it.error.size > 0) {
                            if (it.error.get(0).statusCode != null) {
                                if (it.error.get(0).statusCode == 400) {
                                    prefManager?.clearAll()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    this.let { it1 ->
                                        Common.showShortToast(
                                            it.error.get(0).message,
                                            it1
                                        )
                                    }
                                }
                            } else {
                                this.let { it1 ->
                                    Common.showShortToast(
                                        it.error.get(0).message,
                                        it1
                                    )
                                }
                            }
                        }
                    }
                } else {
                    this.let { it1 -> Common.showLongToast("Something Went Wrong", it1) }
                }
            })
        }
    }

    private fun init() {
        homeSwipeRefresh = findViewById(R.id.homeSwipeRefresh)
        lltransparent = findViewById(R.id.lltransparent)
        ivHome = findViewById(R.id.ivHome)
        ivReport = findViewById(R.id.ivReport)
        ivProfile = findViewById(R.id.ivProfile)
        ivNotification = findViewById(R.id.ivNotification)
        llhome = findViewById(R.id.llhome)
        llReport = findViewById(R.id.llReport)
        llProfile = findViewById(R.id.llProfile)
        llNotification = findViewById(R.id.llNotification)
        tvUsername = findViewById(R.id.tvUsername)

        if (intent != null) {
            if (intent.hasExtra("isFromNotification")) {
                if (intent.getStringExtra("isFromNotification") != null) {
                    isFromNotification = intent.getStringExtra("isFromNotification")
                }
            }
        }

        ivHome?.setOnClickListener(this)
        ivProfile?.setOnClickListener(this)
        ivReport?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        llhome?.setOnClickListener(this)
        llReport?.setOnClickListener(this)
        llNotification?.setOnClickListener(this)
        llProfile?.setOnClickListener(this)

        tvUsername?.text = getString(R.string.str_hello)

    }


    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, BackgroundService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.e("ENDLESS-SERVICE", "Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            Log.e("ENDLESS-SERVICE", "Starting the service in < 26 Mode")
            startService(it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        actionOnService(Actions.STOP)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(v: View?) {

        val i = v?.id
        when (i) {
            R.id.llhome, R.id.ivHome -> {

                replaceFragmenty(
                    fragment = HomeFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent
                )

                selectedMenu = "home"
                ivHome?.setTint(this, R.color.header_title)
                ivReport?.setTint(this, R.color.text_color1)
                if (notiCount > 0) {
                    ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_count))
                    ivNotification?.setTintNull()
                } else {
                    ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_icon))
                    ivNotification?.setTint(this, R.color.text_color1)
                }
                ivProfile?.setTint(this, R.color.text_color1)

            }
            R.id.llReport, R.id.ivReport -> {
                replaceFragmenty(
                    fragment = ReportFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent
                )

                selectedMenu = "report"
                ivHome?.setTint(this, R.color.text_color1)
                ivReport?.setTint(this, R.color.header_title)
                if (notiCount > 0) {
                    ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_count))
                    ivNotification?.setTintNull()
                } else {
                    ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_icon))
                    ivNotification?.setTint(this, R.color.text_color1)
                }
                ivProfile?.setTint(this, R.color.text_color1)

            }
            R.id.llNotification, R.id.ivNotification -> {

                selectedMenu = "notification"
                replaceFragmenty(
                    fragment = NotificationFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent
                )
                ivHome?.setTint(this, R.color.text_color1)
                ivReport?.setTint(this, R.color.text_color1)
//                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))

                ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_icon))

                ivNotification?.setTint(this, R.color.red_color)
                ivProfile?.setTint(this, R.color.text_color1)
                notiCount = 0

            }
            R.id.llProfile, R.id.ivProfile -> {

                selectedMenu = "profile"
                replaceFragmenty(
                    fragment = ProfileFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent
                )
                ivHome?.setTint(this, R.color.text_color1)
                ivReport?.setTint(this, R.color.text_color1)
                if (notiCount > 0) {
                    ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_count))
                    ivNotification?.setTintNull()
                } else {
                    ivNotification?.setImageDrawable(this.resources.getDrawable(R.drawable.ic_notification_icon))
                    ivNotification?.setTint(this, R.color.text_color1)
                }
                ivProfile?.setTint(this, R.color.header_title)
            }
        }
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onResume() {
        super.onResume()
        Log.e("selectedMenu", "" + BackgroundService.isServiceStarted)
        if (!BackgroundService.isServiceStarted) {
            actionOnService(Actions.START)
        }
        if (Common.isConnectedToInternet(this)) {
            getNotificationCount()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_CAPTURE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@MainActivity, image_uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

                    try {
                        var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                        if (fragment is ProfileFragment) {
                            fragment.ivProfileImg?.setImageURI(image_uri)
                        }

                        Log.e("imagesize", "" + imagePath?.length())
                        GlobalScope.launch(Dispatchers.IO) {
                            val compressedImageFile = Compressor.compress(this@MainActivity, imagePath!!) {
                                resolution(50, 50)
                                quality(80)
//                            size(1_097_152) // 2 MB
                            }
                            Log.i("imagePath", "++++" + imagePath.length())
                            Log.i("imagePath", "++++" + compressedImageFile.length())

                            runOnUiThread {
                                uploadImage(compressedImageFile!!, compressedImageFile?.inputStream()!!)
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    val inputStream: InputStream? =
//                        this.contentResolver?.openInputStream(image_uri!!)

                }

            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val auxFile = File(mCurrentPhotoPath)
                    Log.e("getfile00", "" + mCurrentPhotoPath + " " + Uri.parse(mCurrentPhotoPath))

                    try {
                        var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                        if (fragment is ProfileFragment) {
                            fragment.ivProfileImg?.setImageURI(Uri.parse(mCurrentPhotoPath))
                        }
                        GlobalScope.launch(Dispatchers.IO) {
                            val compressedImageFile = Compressor.compress(this@MainActivity, auxFile!!) {
                                resolution(50, 50)
                                quality(80)
//                            size(1_097_152) // 2 MB
                            }
                            Log.i("imagePath", "++++" + auxFile.length())
                            Log.i("imagePath", "++++" + compressedImageFile.length())
                            runOnUiThread {
                                uploadImage(compressedImageFile, compressedImageFile.inputStream())
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    val inputStream: InputStream? =
//                        this.contentResolver?.openInputStream(Uri.parse(mCurrentPhotoPath)!!)
//                    auxFile.let { uploadImage(it, auxFile.inputStream()) }
                }
            }

            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    //To get the File for further usage
                    val selectedImage = data?.data

                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@MainActivity, selectedImage)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }

                    try {
                        var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                        if (fragment is ProfileFragment) {
                            fragment.ivProfileImg?.setImageURI(selectedImage)
                        }

                        Log.i("imagePath", "++++" + imagePath)
                        Log.e("getfile0022", "" + selectedImage)
                        Log.e("imagesize", "" + imagePath?.length())
                        var compressedImageFile: File? = null
                        GlobalScope.launch(Dispatchers.IO) {
                            compressedImageFile = Compressor.compress(this@MainActivity, imagePath!!) {
                                resolution(50, 50)
                                quality(80)
//                            size(1_097_152) // 2 MB
                            }
                            Log.i("imagePath11", "++++" + imagePath.length())
                            Log.i("imagePath22", "++++" + compressedImageFile?.length())
                            runOnUiThread {
                                uploadImage(compressedImageFile!!, compressedImageFile?.inputStream()!!)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


//                    val inputStream: InputStream? =
//                        this.contentResolver?.openInputStream(selectedImage!!)


                }
            }
        }
    }

    fun openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                openCameraStart()
            }
        } else {
            //system os is < marshmallow
            openCameraStart()
        }


    }

    fun openGallery() {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions()
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
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (this?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } !== PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    val alertBuilder = android.app.AlertDialog.Builder(this)
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

    private fun openCameraStart() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCameraStart()
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
                        val file: File = this.let { Common.createFile(it) }

                        val uri: Uri = this.let {
                            FileProvider.getUriForFile(
                                it,
                                "com.walkins.aapkedoorstep.android.fileprovider",
                                file
                            )
                        }!!
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }

            124 -> {
                if (grantResults.get(1) != -1) {
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
                }
            }
        }
    }

    private fun uploadImage(imagePath: File, inputStream: InputStream) {
        this.let { showLoader(it) }

        val part = MultipartBody.Part.createFormData(
            "file", imagePath.name, RequestBody.create(
                MediaType.parse("image/*"),
                inputStream.readBytes()
            )
        )

        this.let {
            loginViewModel?.uploadImage(
                part,
                prefManager?.getAccessToken()!!, it,
                "profile"
            )
        }

        loginViewModel?.getImageUpload()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {
                    Log.e("getfile", "" + it.data.imageUrl)
//                    val fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                    hideLoader()
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()

                } else {
                    hideLoader()
                }
            } else {
                hideLoader()
            }
        })

    }

    fun showLoader(activity: Context) {
        try {
            if (dialogue != null) {
                if (dialogue?.isShowing!!) {
                    dialogue?.dismiss()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        try {
            dialogue = Dialog(activity)
            dialogue?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogue?.setContentView(R.layout.common_loader)
            dialogue?.setCancelable(false)
            dialogue?.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoader() {
        try {
            if (dialogue != null && dialogue?.isShowing!!) {
                dialogue?.dismiss()
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}