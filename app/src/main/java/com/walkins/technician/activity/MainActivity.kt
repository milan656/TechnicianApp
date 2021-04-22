package com.walkins.technician.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.example.technician.common.Common.Companion.setTint
import com.example.technician.common.PrefManager
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.replaceFragmenty
import com.walkins.technician.fragment.HomeFragment
import com.walkins.technician.fragment.NotificationFragment
import com.walkins.technician.fragment.ProfileFragment
import com.walkins.technician.fragment.ReportFragment
import com.walkins.technician.service.Actions
import com.walkins.technician.service.EndlessService
import com.walkins.technician.service.ServiceState
import com.walkins.technician.service.getServiceState


class MainActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var ivHome: ImageView? = null
    private var ivNotification: ImageView? = null
    private var ivReport: ImageView? = null
    private var ivProfile: ImageView? = null
    private var llhome: LinearLayout? = null
    private var llReport: LinearLayout? = null
    private var llNotification: LinearLayout? = null
    private var llProfile: LinearLayout? = null
    private var tvUsername: TextView? = null

    private var prefManager: PrefManager? = null

    private var selectedMenu: String? = null
    public var lltransparent: LinearLayout? = null
    private lateinit var mDb: DBClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefManager = PrefManager(this)
        mDb = DBClass.getInstance(this)
        init()

        var thread = Thread {
            if (mDb.daoClass().getAllVehicleType() != null && mDb.daoClass()
                    .getAllVehicleType().size > 0
            ) {
                actionOnService(Actions.START)

            } else {
                actionOnService(Actions.START)
            }

        }
        thread.start()
    }

    private fun init() {
        lltransparent = findViewById(R.id.lltransparent)
        ivHome = findViewById(R.id.ivHome)
        ivReport = findViewById(R.id.ivReport)
        ivProfile = findViewById(R.id.ivProfile)
        ivNotification = findViewById(R.id.ivNotification)
        llhome = findViewById(R.id.llhome)
        llReport = findViewById(R.id.llReport)
        llProfile = findViewById(R.id.llProfile)
        llNotification = findViewById(R.id.llNotification)
        tvUsername = findViewById(R.id.tvUsername)

        ivHome?.setOnClickListener(this)
        ivProfile?.setOnClickListener(this)
        ivReport?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        llhome?.setOnClickListener(this)
        llReport?.setOnClickListener(this)
        llNotification?.setOnClickListener(this)
        llProfile?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)



        tvUsername?.text = "Hello, " + "Arun"

        llhome?.performClick()

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
            R.id.llhome,R.id.ivHome -> {

                replaceFragmenty(
                    fragment = HomeFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )

                selectedMenu = "home"
                ivHome?.setTint(this, R.color.header_title)
                ivReport?.setTint(this, R.color.text_color1)
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))
                ivProfile?.setTint(this, R.color.text_color1)

            }
            R.id.llReport,R.id.ivReport -> {
                replaceFragmenty(
                    fragment = ReportFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )

                selectedMenu = "report"
                ivHome?.setTint(this, R.color.text_color1)
                ivReport?.setTint(this, R.color.header_title)
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))
                ivProfile?.setTint(this, R.color.text_color1)


            }
            R.id.llNotification,R.id.ivNotification -> {

                selectedMenu = "notification"
                replaceFragmenty(
                    fragment = NotificationFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )
                ivHome?.setTint(this, R.color.text_color1)
                ivReport?.setTint(this, R.color.text_color1)
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_applied))
                ivProfile?.setTint(this, R.color.text_color1)


            }
            R.id.llProfile,R.id.ivProfile -> {

                selectedMenu = "profile"
                replaceFragmenty(
                    fragment = ProfileFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent
                )
                ivHome?.setTint(this, R.color.text_color1)
                ivReport?.setTint(this, R.color.text_color1)
                ivNotification?.setImageDrawable(this.resources?.getDrawable(R.drawable.ic_notification_icon))
                ivProfile?.setTint(this, R.color.header_title)


            }
        }
    }


    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onResume() {
        super.onResume()
        Log.e("selectedMenu", "" + selectedMenu)


    }
}