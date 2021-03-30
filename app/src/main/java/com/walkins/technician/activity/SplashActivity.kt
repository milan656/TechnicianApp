package com.walkins.technician.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.technician.common.PrefManager
import com.walkins.technician.R


class SplashActivity : AppCompatActivity() {

    private var prefManager: PrefManager? = null
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /*  try {
              Fabric.with(this, Crashlytics())
          }catch (e : Exception){
              e.printStackTrace()
          }*/
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
            /*if (prefManager?.getIsLogin()!!) {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
            }*/
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
