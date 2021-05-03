package com.walkins.technician.activity

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import com.walkins.technician.R
import com.walkins.technician.common.MySMSBroadcastReceiver
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

        startSMSListener()

        Common.showLoader(this@LoginActivity)

        /* val context = applicationContext
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
         var technicianPassword = "9978785623"*/

//        val intent = Intent(this, VerifyOtpActivity::class.java)
//        intent.putExtra("number", "9978785623")
//        intent.putExtra("otp", "" + 1212)
//        startActivity(intent)
        val jsonObject = JsonObject()
        jsonObject.addProperty("phone_number", edtLoginEmail.text.toString())
        Log.e("getobject", "" + jsonObject)

        loginViewModel.initTwo(jsonObject)
        loginViewModel.sendOtp()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    val intent = Intent(this, VerifyOtpActivity::class.java)
                    intent.putExtra("number", edtLoginEmail.text?.toString())
                    startActivity(intent)
                } else {
                    if (it.error != null && it.error.get(0).message != null) {
                        loginFail(it.error.get(0).message, "Oops!")
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

        tv_dialogTitle?.setText(title)

        tv_message.text = message
        btnYes.setOnClickListener { builder.dismiss() }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
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

}