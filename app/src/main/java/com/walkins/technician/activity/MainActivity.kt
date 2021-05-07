package com.walkins.technician.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.getFile
import com.example.technician.common.Common.Companion.setTint
import com.example.technician.common.PrefManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.theartofdev.edmodo.cropper.CropImage
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.replaceFragmenty
import com.walkins.technician.fragment.HomeFragment
import com.walkins.technician.fragment.NotificationFragment
import com.walkins.technician.fragment.ProfileFragment
import com.walkins.technician.fragment.ReportFragment
import com.walkins.technician.service.Actions
import com.walkins.technician.service.EndlessService
import com.walkins.technician.service.ServiceState
import com.walkins.technician.service.getServiceState
import com.walkins.technician.viewmodel.LoginActivityViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream


class MainActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var ivHome: ImageView? = null
    private var ivNotification: ImageView? = null
    private var ivReport: ImageView? = null
    private var ivProfile: ImageView? = null
    private var llhome: LinearLayout? = null
    private var llReport: LinearLayout? = null
    private var llNotification: LinearLayout? = null
    private var llProfile: LinearLayout? = null
    open var tvUsername: TextView? = null

    private var prefManager: PrefManager? = null
    private var loginViewModel: LoginActivityViewModel? = null
    private var selectedMenu: String? = null
    public var lltransparent: LinearLayout? = null
    private lateinit var mDb: DBClass

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String
    private val PERMISSION_CODE = 1010;
    private val IMAGE_CAPTURE_CODE = 1011
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefManager = PrefManager(this)
        mDb = DBClass.getInstance(this)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        Log.e("getaccessToken", "" + prefManager?.getAccessToken())
        init()

        callApiTogetToken()

        var thread = Thread {
            if (mDb.daoClass().getAllVehicleType() != null && mDb.daoClass()
                    .getAllVehicleType().size > 0
            ) {
//                actionOnService(Actions.START)

            } else {
                actionOnService(Actions.START)
            }

        }
        thread.start()
    }

    private fun callApiTogetToken() {
        try {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    Log.i("token", "+++" + task.result)

                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result!!.token


                    Log.i("token", "+++" + token)

//                    RemoveOrAddTokenForApi(token)

                })


        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("token", "+++" + e.cause+" "+e.message)

        }
    }

    private fun init() {
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

        ivHome?.setOnClickListener(this)
        ivProfile?.setOnClickListener(this)
        ivReport?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        llhome?.setOnClickListener(this)
        llReport?.setOnClickListener(this)
        llNotification?.setOnClickListener(this)
        llProfile?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)

        tvUsername?.text = "Hello, " + ""

        llhome?.performClick()

    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
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
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))
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
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))
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
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_applied))
                ivProfile?.setTint(this, R.color.text_color1)


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
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))
                ivProfile?.setTint(this, R.color.header_title)


            }
        }
    }


    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onResume() {
        super.onResume()
        Log.e("selectedMenu", "" + selectedMenu)
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

                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                    if (fragment is ProfileFragment) {
                        fragment.ivProfileImg?.setImageURI(image_uri)
                    }
                    val inputStream: InputStream? =
                        this.contentResolver?.openInputStream(image_uri!!)
                    imagePath.let { uploadImage(it!!, inputStream!!) }
                }

            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val auxFile = File(mCurrentPhotoPath)
                    Log.e("getfile00", "" + mCurrentPhotoPath + " " + Uri.parse(mCurrentPhotoPath))

                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                    if (fragment is ProfileFragment) {
                        fragment.ivProfileImg?.setImageURI(Uri.parse(mCurrentPhotoPath))
                    }
                    val inputStream: InputStream? =
                        this.contentResolver?.openInputStream(Uri.parse(mCurrentPhotoPath)!!)
                    auxFile.let { uploadImage(it, inputStream!!) }
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

                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
                    if (fragment is ProfileFragment) {
                        fragment.ivProfileImg?.setImageURI(selectedImage)
                    }

                    Log.i("imagePath", "++++" + imagePath)
                    Log.e("getfile0022", "" + selectedImage)

                    val inputStream: InputStream? =
                        this.contentResolver?.openInputStream(selectedImage!!)
                    imagePath?.let { uploadImage(it, inputStream!!) }

                }
            }

//            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                val result = CropImage.getActivityResult(data)
//                Log.e("getdataa", "" + result?.error + " " + result?.isSuccessful)
//                if (resultCode == Activity.RESULT_OK) {
//
//                    //To get the File for further usage
//                    val selectedImage = result.uri
//                    Log.e("getdataa", "" + selectedImage)
//
//
//                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.mainContent)!!
//
//                    if (fragment is ProfileFragment) {
//                        fragment.ivProfileImg?.setImageURI(selectedImage)
//                    }
//                    val imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        this?.let { Common.getFile(it, selectedImage) }
//                    } else {
//                        TODO("VERSION.SDK_INT < KITKAT")
//                    }
//                    imagePath?.let { uploadImage(it) }
//                }
//            }
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

        } else {
            //  Common.showShortToast("Permission Granted",requireActivity())
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
        grantResults: IntArray
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
                                "com.walkins.technician.android.fileprovider",
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

    private fun uploadImage(imagePath: File,inputStream: InputStream) {
        this.let { Common.showLoader(it) }

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
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("getfile", "" + it.data.imageUrl)
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}