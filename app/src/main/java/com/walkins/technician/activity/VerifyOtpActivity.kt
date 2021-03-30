package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.walkins.technician.R
import java.lang.StringBuilder

class VerifyOtpActivity : AppCompatActivity(), View.OnClickListener {

    private var tvResend: TextView? = null
    private var btnVerify: Button? = null
    private var edtOtp1: EditText? = null
    private var edtOtp2: EditText? = null
    private var edtOtp3: EditText? = null
    private var edtOtp4: EditText? = null
    private var otp: StringBuilder? = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        init()
    }

    private fun init() {
        tvResend = findViewById(R.id.tvResend)
        btnVerify = findViewById(R.id.btnVerify)

        edtOtp1 = findViewById(R.id.edtOtp1)
        edtOtp2 = findViewById(R.id.edtOtp2)
        edtOtp3 = findViewById(R.id.edtOtp3)
        edtOtp4 = findViewById(R.id.edtOtp4)

        btnVerify?.setOnClickListener(this)
        btnVerify?.isEnabled = false

        tvResend?.text = Html.fromHtml("Didn't receive OTP? <font color='blue'>Resend<font/>")

        edtOtp1?.requestFocus()
        edtOtp1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (s != null && s.length != 0 && s.length == 1) {
                    otp?.append(s.toString())
                    edtOtp2?.requestFocus()
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
                    otp?.append(s.toString())
                    edtOtp3?.requestFocus()
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
                    otp?.append(s.toString())
                    edtOtp4?.requestFocus()
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
                    otp?.append(s.toString())
                    edtOtp4?.clearFocus()
                    btnVerify?.isEnabled = true
                }

            }

        })
    }

    override fun onClick(v: View?) {
        var i = v?.id
        if (i == R.id.btnVerify) {

            if (edtOtp1?.text?.toString()?.length == 1 &&
                edtOtp2?.text?.toString()?.length == 1 &&
                edtOtp3?.text?.toString()?.length == 1 &&
                edtOtp4?.text?.toString()?.length == 1
            ) {

                Log.e("verifyy", "" + otp)
                if (otp?.toString()?.length == 4) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

        }
    }
}