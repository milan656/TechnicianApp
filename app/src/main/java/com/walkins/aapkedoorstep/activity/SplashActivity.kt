package com.walkins.aapkedoorstep.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.technician.common.PrefManager
import com.walkins.aapkedoorstep.R

@SuppressLint("SetTextI18n")
class SplashActivity : AppCompatActivity() {

    private var prefManager: PrefManager? = null
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startWorking()


    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }


    private fun startWorking() {
        prefManager = PrefManager(this@SplashActivity)

        waitForThreeSec()
    }

    private fun waitForThreeSec() {
        Handler().postDelayed({
            navigateScreen()
        }, SPLASH_TIME_OUT)

    }

    // Please click here to know about last year (up to March'19) Advantage Program Points.
    private fun navigateScreen() {

        try {
            if (prefManager?.getIsLogin()!!) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
