package com.walkins.technician.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.walkins.technician.R
import com.walkins.technician.service.Actions
import com.walkins.technician.service.EndlessService
import com.walkins.technician.service.ServiceState
import com.walkins.technician.service.getServiceState

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionOnService(Actions.START)

    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.e("ENDLESS-SERVICE","Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            Log.e("ENDLESS-SERVICE","Starting the service in < 26 Mode")
            startService(it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        actionOnService(Actions.STOP)
    }
}