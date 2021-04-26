package com.walkins.technician.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import com.walkins.technician.R
import com.walkins.technician.custom.BoldButton
import com.walkins.technician.viewmodel.LoginActivityViewModel

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

        /* MessageReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.i("Received_sms", "In Fragment ! " + messageText);
                if (LoginFragment.this.getContext() != null) {
                    mLoginVM.otp.set(messageText);
                }
            }
        });*/

        smsPermission()

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

    private fun smsPermission() {
        val PERMISSIONS = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
        )
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
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

    private fun login() {
        if (edtLoginEmail.text?.trim()?.length == 0) {
            edtLoginEmail.error = "Please Enter Mobile number"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }
        if (edtLoginEmail.text?.trim()?.length != 10) {
            edtLoginEmail.error = "Please Enter Valid Mobile number"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }

        val intent = Intent(this, VerifyOtpActivity::class.java)
        intent.putExtra("number", edtLoginEmail.text?.toString())
//        intent.putExtra("number", "9978785623")
        startActivity(intent)


    }

    private fun userRecoverPassword() {
//        val intent = Intent(this, RecoveryPasswordActivity::class.java)
//        startActivity(intent)
    }


    /*
    * User Login Function with validation
    * */
    private fun userLogin() {
        edtLoginEmail.requestFocus()
        if (edtLoginEmail.text?.trim()?.length == 0) {
            edtLoginEmail.error = "Please Enter Mobile number"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }
        if (edtLoginEmail.text?.trim()?.length != 10) {
            edtLoginEmail.error = "Please Enter Valid Mobile number"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }



        Common.showLoader(this@LoginActivity)

        val context = applicationContext
        val manager = context.packageManager

        var versionCode: Int = 0
        var deviceName: String? = Common.getDeviceName()
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


        var dealerLogin = "9898987411"
        var dealerPassword = "jktyre@12345"
        var technicianLogin = "9978785623"
        var technicianPassword = "9978785623"
        val jsonObject = JsonObject()
        jsonObject.addProperty("phone_number", edtLoginEmail.text.toString())

        loginViewModel.callApiSendOtp(jsonObject, "Basic ZG9vcnN0ZXA6MTIz", this)
        loginViewModel.sendOtp()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    val intent = Intent(this, VerifyOtpActivity::class.java)
                    intent.putExtra("otp", "" + it.data?.otp)
                    startActivity(intent)
                } else {

                }
            }
        })

        loginViewModel.init(
            technicianLogin.trim({ it <= ' ' }),
            technicianPassword.trim({ it <= ' ' }),
            "password",
            "Basic ZG9vcnN0ZXA6MTIz", versionCode, deviceName, androidOS, null
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

//                    firebaseAnalytics?.setUserId(it.userDetailModel?.sap_id!!);

                    prefManager.setAccessTokenExpireDate(it.accessTokenExpiresAt)
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
                    prefManager.isLogin(true)
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
                        loginFail(it.error.get(0).message, "Oops!")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    private fun loginFail(message: String, title: String) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)


        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout, null)

        val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val tv_dialogTitle = root.findViewById<TextView>(R.id.tvTitleText)

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