package com.walkins.aapkedoorstep.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.custom.BoldButton
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel

@SuppressLint("SetTextI18n")
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var prefManager: PrefManager
    private lateinit var edtLoginEmail: EditText

    private lateinit var btnLoginToDashBoard: BoldButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        smsPermission()

        try {
            Common.isCalling = false
        } catch (e: Exception) {
            e.printStackTrace()
        }

        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)

    }

    private fun smsPermission() {
        val pERMISSIONS = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
        )
        if (!hasPermissions(this, *pERMISSIONS)) {
            ActivityCompat.requestPermissions(this, pERMISSIONS, 1)
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

    }


    private fun init() {
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
            return
        }
        if (edtLoginEmail.text?.trim()?.length != 10) {
            edtLoginEmail.error = "Please Enter Valid Mobile number"
            return
        }

        val intent = Intent(this, VerifyOtpActivity::class.java)
        intent.putExtra("number", edtLoginEmail.text?.toString())
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
                        showDialogue("Oops!",it.error.get(0).message)
                    }
                }
            }
        })
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

    fun showDialogue(title: String, message: String) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT//        intent.putExtra("number", "9978785623")

        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(this).inflate(R.layout.common_dialogue_layout_service, null)

        val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val tvMessage = root.findViewById<TextView>(R.id.tv_message)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tvMessage.text = message
        tvTitleText?.gravity = Gravity.CENTER
        tvMessage?.gravity = Gravity.CENTER
        ivClose?.visibility = View.INVISIBLE
        btnYes.setOnClickListener {
            builder.dismiss()

        }
        builder.setView(root)
        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }
}