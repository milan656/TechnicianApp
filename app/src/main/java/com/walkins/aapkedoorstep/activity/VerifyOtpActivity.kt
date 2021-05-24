package com.walkins.aapkedoorstep.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.MySMSBroadcastReceiver
import com.walkins.aapkedoorstep.custom.BoldButton
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel

class VerifyOtpActivity : AppCompatActivity(), View.OnClickListener,
    MySMSBroadcastReceiver.OTPReceiveListener {
    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var prefManager: PrefManager
    private var tvResend: TextView? = null
    private var ivBackIcon: ImageView? = null
    private var btnVerify: Button? = null
    private var edtOtp1: EditText? = null
    private var edtOtp2: EditText? = null
    private var edtOtp3: EditText? = null
    private var edtOtp4: EditText? = null
    private var otp: StringBuilder? = StringBuilder()
    private var smsBroadcastReceiver: MySMSBroadcastReceiver? = null
    private var receiver: BroadcastReceiver? = null
    private var otpStr: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        prefManager = PrefManager(this@VerifyOtpActivity)
        init()


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        tvResend = findViewById(R.id.tvResend)
        btnVerify = findViewById(R.id.btnVerify)
        ivBackIcon = findViewById(R.id.ivBackIcon)

        edtOtp1 = findViewById(R.id.edtOtp1)
        edtOtp2 = findViewById(R.id.edtOtp2)
        edtOtp3 = findViewById(R.id.edtOtp3)
        edtOtp4 = findViewById(R.id.edtOtp4)

        btnVerify?.setOnClickListener(this)
        ivBackIcon?.setOnClickListener(this)

        if (intent != null) {
            if (intent.getStringExtra("otp") != null) {
                otpStr = intent.getStringExtra("otp")
            }
        }

        tvResend?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                callSendOTPApi()
                return false
            }

        })

//        tvResend?.text = Html.fromHtml("Didn't receive OTP?")
        startSMSListener()

        smsBroadcastReceiver = MySMSBroadcastReceiver(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)

        receiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action.equals("otp", ignoreCase = true)) {
                    val message = intent.getStringExtra("message")
                    // message is the fetching OTP
                    Log.e("getsmsotp0", "" + message)
                    edtOtp1?.setText("" + message?.get(0))
                    edtOtp2?.setText("" + message?.get(1))
                    edtOtp3?.setText("" + message?.get(2))
                    edtOtp4?.setText("" + message?.get(3))

//                    mOtp_et.setText(message)
                }
            }
        }

//        edtOtp1?.requestFocus()
        edtOtp1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (s != null && s.length != 0 && s.length == 1) {

                    edtOtp2?.requestFocus()
                    otp?.append(edtOtp1?.text?.toString()?.toInt())
                    Log.e("getcallbtn", "call8585")
                }

            }

        })
        edtOtp2?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length != 0 && s.length == 1) {
                    edtOtp3?.requestFocus()
                    otp?.append(edtOtp2?.text?.toString()?.toInt())
                    Log.e("getcallbtn", "call525")

                }
            }

        })
        edtOtp3?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length != 0 && s.length == 1) {
                    edtOtp4?.requestFocus()
                    otp?.append(edtOtp3?.text?.toString()?.toInt())
                    Log.e("getcallbtn", "call00")

                }

            }

        })
        edtOtp4?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length != 0 && s.length == 1) {
                    edtOtp4?.clearFocus()
                    otp?.append(edtOtp4?.text?.toString()?.toInt())
                    btnVerify?.performClick()
                    Log.e("getcallbtn", "call")
                }

            }

        })
    }

    private fun callSendOTPApi() {
        Common.showLoader(this)
        val jsonObject = JsonObject()
        jsonObject.addProperty("phone_number", intent?.getStringExtra("number"))
        Log.e("getobject", "" + jsonObject)

        loginViewModel.initTwo(jsonObject)
        loginViewModel.sendOtp()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {

                    if (it.message != null && !it.message.equals("")) {
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    if (it.error != null && it.error.get(0).message != null) {
                        loginFail(it.error.get(0).message, "Oops!")
                    }
                }
            }
        })
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

    private fun startSMSListener() {
        try {
            val client = SmsRetriever.getClient(this)
            val task = client.startSmsRetriever()
            task.addOnSuccessListener {
                // API successfully started
            }
            task.addOnFailureListener {
                // Fail to start API
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        val i = v?.id
        when (i) {
            R.id.btnVerify -> {
                if (edtOtp1?.text?.toString()?.length == 1 &&
                    edtOtp2?.text?.toString()?.length == 1 &&
                    edtOtp3?.text?.toString()?.length == 1 &&
                    edtOtp4?.text?.toString()?.length == 1
                ) {

                    userLogin()
                } else {
                    Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.ivBackIcon -> {
                Log.e("backclick", "cal")
                onBackPressed()
            }
        }
    }

    override fun onSuccess(intent: Intent?) {

    }

    override fun onOTPReceived(otp: String?) {
        Log.e("getotppp", "" + otp)
    }

    private fun userLogin() {

        /* if (!isValidEmail(edtLoginEmail?.text.toString().trim())) {
             Common.showShortToast("Please enter valid email address", this@LoginActivity)
             return
         }*/
        /* val intent = Intent(this, MainActivity::class.java)
         startActivity(intent)
         finish()*/


        Common.showLoader(this@VerifyOtpActivity)

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

        /*  edtLoginEmail.text?.toString()?.toLowerCase()?.trim({ it <= ' ' })?.let {
              loginViewModel.init(
                  it,
                  "jktyre@12345".trim({ it <= ' ' }),
                  "password",
                  "Basic ZG9vcnN0ZXA6MTIz=", versionCode, deviceName, androidOS, null
              )
          }*/
        /*"222111"?.toLowerCase()?.trim({ it <= ' ' })?.let {
            loginViewModel.init(
                it,
                "12345".trim({ it <= ' ' }),
                "password",
                "Basic ZG9vcnN0ZXA6MTIz==", versionCode, deviceName, androidOS, null
            )
        }*/

        var dealerLogin = "9898987411"
        var dealerPassword = "jktyre@12345"
        var technicianLogin = "9978785623"
        var technicianPassword = "1212"

        var OTP =
            edtOtp1?.text?.toString() + edtOtp2?.text?.toString() + edtOtp3?.text?.toString() + edtOtp4?.text?.toString()

        Log.e("getotp", "" + OTP)
        loginViewModel.init(
            intent?.getStringExtra("number")?.trim({ it <= ' ' })!!,
            OTP.trim({ it <= ' ' }),
            "password",
            "Basic ZG9vcnN0ZXA6MTIz", versionCode, deviceName, androidOS, null
        )

        loginViewModel.getLoginData()?.observe(this@VerifyOtpActivity, Observer {

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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()

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

    @SuppressLint("SetTextI18n")
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

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver!!, IntentFilter("otp"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver!!)
    }
}