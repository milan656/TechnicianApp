package com.walkins.technician.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.walkins.technician.R
import com.walkins.technician.viewmodel.LoginActivityViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.walkins.technician.custom.BoldButton

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var prefManager: PrefManager
    private var lastLocation: Location? = null
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var edtLoginEmail: EditText

    private lateinit var btnLoginToDashBoard: BoldButton
    var firebaseAnalytics: FirebaseAnalytics? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        try {
            Common.isCalling = false
        } catch (e: Exception) {
            e.printStackTrace()
        }

        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)

//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                scheduleJob()
//            } else {
//
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
    }

    override fun onResume() {
        super.onResume()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                scheduleJob()
            } else {

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /* @TargetApi(Build.VERSION_CODES.N)
     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     private fun scheduleJob() {

         if (getDeviceName().toLowerCase().contains("oppo")) {
             val startServiceIntent = Intent(this, InternetServices::class.java)
             startService(startServiceIntent)
         }
     }*/

    private fun init() {
        // val retrofit = RetrofitCommonClass.getClient(this)
//        firebaseAnalytics = Firebase.analytics

        prefManager = PrefManager(this@LoginActivity)

        edtLoginEmail = findViewById<EditText>(R.id.edtLoginEmail)

        btnLoginToDashBoard = findViewById<BoldButton>(R.id.btnLoginToDashBoard)
        btnLoginToDashBoard.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnLoginToDashBoard -> userLogin()
        }
    }

    private fun userRecoverPassword() {
//        val intent = Intent(this, RecoveryPasswordActivity::class.java)
//        startActivity(intent)
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true

        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }

        return phrase.toString()
    }

    /*
    * User Login Function with validation
    * */
    private fun userLogin() {
        if (edtLoginEmail.text?.trim()?.length == 0) {
            edtLoginEmail.error = "User ID should not be empty"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }
        /* if (!isValidEmail(edtLoginEmail?.text.toString().trim())) {
             Common.showShortToast("Please enter valid email address", this@LoginActivity)
             return
         }*/
        /* val intent = Intent(this, MainActivity::class.java)
         startActivity(intent)
         finish()*/


        Common.showLoader(this@LoginActivity)


        val context = applicationContext
        val manager = context.packageManager

        var versionCode: Int = 0
        var deviceName: String? = getDeviceName()
        var androidOS = Build.VERSION.RELEASE

        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            versionCode = PackageInfoCompat.getLongVersionCode(info).toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        val fields = Build.VERSION_CODES::class.java.fields
        for (field in fields) {
            androidOS = field.name
        }

        loginViewModel.init(
            "222111".toLowerCase().trim({ it <= ' ' }),
            "12345".trim({ it <= ' ' }),
            "password",
            "Basic amt0eXJlOjEyMw==", versionCode, deviceName, androidOS, null
        )

        loginViewModel.getLoginData()?.observe(this@LoginActivity, Observer {

            Common.hideLoader()


            //  Common.hideLoader()
            if (it != null) {
                if (it.accessToken != null && !it.accessToken.equals("")) {
                    prefManager.setAccessToken("Bearer " + it.accessToken)
                    prefManager.setRefreshToken(it.refreshToken)
                    prefManager.setToken(it.token)
                    prefManager.setUuid(it.userDetailModel!!.uuid)
                    prefManager.setSapId(it.userDetailModel!!.sap_id)

//                    firebaseAnalytics?.setUserId(it.userDetailModel?.sap_id!!);

                    prefManager.setVideoUrl(it.videoURL)
                    prefManager.setAccessTokenExpireDate(it.accessTokenExpiresAt)
                    prefManager.setType(it.userDetailModel?.type!!)
                    if (it.dealerType != null) {
                        prefManager.setDealerType(it.dealerType)
                    }
//                    if (it.customer_class_type != null) {
//                        prefManager.setCustomerClassType(it.customer_class_type)
//                    }
//                    if (it.display_type != null) {
//                        prefManager.setDisplayType(it.display_type)
//                    }
//                    if (it.userDetailModel?.customer_class != null) {
//                        prefManager.setCustomerClass(it.userDetailModel?.customer_class)
//                    }
                    if (it.userDetailModel?.owner_name != null) {
                        prefManager.setOwnerName(it.userDetailModel?.owner_name)
                    }

                    var arrayList: ArrayList<String>? = ArrayList()

                   /* if (it.userDetailModel?.arrayListPermission != null) {

                        for (i in it.userDetailModel?.arrayListPermission?.indices!!) {
                            arrayList?.add(it.userDetailModel?.arrayListPermission?.get(i)!!)

                        }
                        when {
                            arrayList != null -> {
                                prefManager.setPermissionList(arrayList)
                            }
                        }
                    } else if (prefManager?.getValue("customerClass") != null && prefManager?.getValue(
                            "customerClass"
                        )
                            .equals("na", ignoreCase = true)
                    ) {
                        val intent = Intent(this, HomeSubDealerActivity::class.java)
                        intent.putExtra("videoUrl", it.videoURL)
                        startActivity(intent)
                        finish()
                        return@Observer
                    }
                    if (prefManager?.getValue("customerClass") != null && prefManager?.getValue(
                            "customerClass"
                        )
                            .equals("na", ignoreCase = true)
                    ) {
                        val intent = Intent(this, HomeSubDealerActivity::class.java)
                        intent.putExtra("videoUrl", it.videoURL)
                        startActivity(intent)
                        finish()
                        return@Observer
                    }

                    if (prefManager?.getValue("customerClass") != null && prefManager?.getValue("customerClass")
                            .equals("na", ignoreCase = true)
                    ) {
                        val intent = Intent(this, HomeSubDealerActivity::class.java)
                        intent.putExtra("videoUrl", it.videoURL)
                        startActivity(intent)
                        finish()
                        return@Observer
                    }*/


                    Log.e("getType", "" + it.userDetailModel!!.type)

                    val intent = Intent(this, VerifyOtpActivity::class.java)
                    startActivity(intent)
                    finish()

                    /*when {
                        prefManager?.getValue("customerClass") != null && prefManager.getValue(
                            "customerClass"
                        )
                            .equals("na", ignoreCase = true) -> {

                            val intent = Intent(this, HomeSubDealerActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()
                            return@Observer

                        }
                        prefManager?.getValue("customerClass") != null && prefManager.getValue(
                            "customerClass"
                        )
                            .equals("hy", ignoreCase = true) -> {

                            val intent = Intent(this, HomeSubDealerActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()
                            return@Observer

                        }
                        it.userDetailModel?.type.equals("area_manager") || it.userDetailModel?.type.equals(
                            "sales_officer"
                        ) -> {

                            val intent = Intent(this, HomeEmployeeActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()
                        }


                        it.userDetailModel?.type.equals("head_office") -> {
                            val intent = Intent(this, HomeEmployeeActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()

                        }
                        it.userDetailModel?.type.equals("project_manager") -> {
                            val intent = Intent(this, HomeEmployeeActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()

                        }
                        it.userDetailModel?.type.equals("zone_manager") -> {
                            val intent = Intent(this, HomeEmployeeActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()

                        }
                        it.userDetailModel?.type.equals("ztm") -> {
                            val intent = Intent(this, HomeEmployeeActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()

                        }
                        it.userDetailModel?.type.equals("region_manager") -> {
                            val intent = Intent(this, HomeEmployeeActivity::class.java)
                            intent.putExtra("videoUrl", it.videoURL)
                            startActivity(intent)
                            finish()

                        }



                        else -> {
                            try {
                                prefManager.clearAll()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }*/


                } else {
                    try {
                        loginFail(it.error.get(0).message)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    private fun loginFail(message: String) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)


        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout, null)

        val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val tv_dialogTitle = root.findViewById<TextView>(R.id.tv_dialogTitle)

        tv_dialogTitle?.setText("Oops!")

        tv_message.text = message
        btnYes.setOnClickListener { builder.dismiss() }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }


/*
    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLastLocation() {

        this?.let {
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

        }

        this?.let {
            fusedLocationClient?.lastLocation?.addOnCompleteListener(it) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result

                    Log.e("getlocdata", "" + lastLocation?.latitude)
                    Log.e("getlocdata", "" + lastLocation?.longitude)

                    if (lastLocation?.latitude != null && lastLocation?.longitude != null) {
//                        val jsonObject = JsonObject()
//                        jsonObject.addProperty("latitude", "" + lastLocation?.latitude)
//                        jsonObject.addProperty("longitude", "" + lastLocation?.longitude)
//                        userInfoViewModel?.callApiUpdateLocation(
//                            jsonObject,
//                            it,
//                            prefManager?.getAccessToken()!!
//                        )
//
//                        userInfoViewModel?.getUserInfo()?.observe(it, Observer {
//
//                            if (it != null) {
//                                if (it.success) {
//
//                                }
//                            }
//                        })
                    }
                } else {
                    Log.w("TAG", "getLastLocation:exception", task.exception)
                    showMessage("No location detected. Make sure location is enabled on the device.")
                }
            }
        }

    }

    private fun showMessage(string: String) {
        //        val container = findViewById<View>(R.id.linearLayout)
        //        if (container != null) {
        //            Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()
        //        }

        Log.e("getmsg", "" + string)
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {

        var permissionState: Int? = 0
        this.let {
            permissionState = ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        }
        return permissionState == PackageManager.PERMISSION_GRANTED

    }

    private fun startLocationPermissionRequest() {
        this.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }

    }

    private fun requestPermissions() {

        this.let {
            val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (shouldProvideRationale) {
                Log.i("TAG", "Displaying permission rationale to provide additional context.")
                showSnackbar("Location permission is needed for core functionality", "Okay",
                    View.OnClickListener {
                        startLocationPermissionRequest()
                    })
            } else {
                Log.i("TAG", "Requesting permission")
                startLocationPermissionRequest()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.e("TAG", "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i("TAG", "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
*/
}