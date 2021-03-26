package com.walkins.technician.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.walkins.technician.R
import com.walkins.technician.common.replaceFragmenty
import com.walkins.technician.fragment.HomeFragment
import com.walkins.technician.fragment.NotificationFragment
import com.walkins.technician.fragment.ProfileFragment
import com.walkins.technician.service.Actions
import com.walkins.technician.service.EndlessService
import com.walkins.technician.service.ServiceState
import com.walkins.technician.service.getServiceState

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var ivHome: ImageView? = null
    private var ivNotification: ImageView? = null
    private var ivHome1: ImageView? = null
    private var ivProfile: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
//        actionOnService(Actions.START)

    }

    private fun init() {
        ivHome = findViewById(R.id.ivHome)
        ivHome1 = findViewById(R.id.ivHome1)
        ivProfile = findViewById(R.id.ivProfile)
        ivNotification = findViewById(R.id.ivNotification)

        ivHome?.setOnClickListener(this)
        ivProfile?.setOnClickListener(this)
        ivHome1?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)


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
            R.id.ivHome -> {

                replaceFragmenty(
                    fragment = HomeFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )
            }
            R.id.ivHome1 -> {
                replaceFragmenty(
                    fragment = HomeFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )

            }
            R.id.ivNotification -> {
                replaceFragmenty(
                    fragment = NotificationFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )

            }
            R.id.ivProfile -> {
                replaceFragmenty(
                    fragment = ProfileFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )

            }
        }
    }
}