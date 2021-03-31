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
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.example.technician.common.PrefManager
import com.walkins.technician.R
import com.walkins.technician.common.replaceFragmenty
import com.walkins.technician.fragment.HomeFragment
import com.walkins.technician.service.Actions
import com.walkins.technician.service.EndlessService
import com.walkins.technician.service.ServiceState
import com.walkins.technician.service.getServiceState


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var ivHome: ImageView? = null
    private var ivNotification: ImageView? = null
    private var ivHome1: ImageView? = null
    private var ivProfile: ImageView? = null
    private var llhome: LinearLayout? = null
    private var tvUsername: TextView? = null

    private var prefManager: PrefManager? = null
    private var ivFilter: ImageView? = null
    private var ivDot: ImageView? = null
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefManager = PrefManager(this)
        init()
//        actionOnService(Actions.START)

    }

    private fun init() {
        ivFilter = findViewById(R.id.ivFilter)
        ivDot = findViewById(R.id.ivDot)
        ivDot?.visibility = View.GONE
        ivHome = findViewById(R.id.ivHome)
        ivHome1 = findViewById(R.id.ivHome1)
        ivProfile = findViewById(R.id.ivProfile)
        ivNotification = findViewById(R.id.ivNotification)
        llhome = findViewById(R.id.llhome)
        tvUsername = findViewById(R.id.tvUsername)

        ivHome?.setOnClickListener(this)
        ivProfile?.setOnClickListener(this)
        ivHome1?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        ivFilter?.setOnClickListener(this)

        tvUsername?.text = "Hello, " + prefManager?.getOwnerName()

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
//                replaceFragmenty(
//                    fragment = NotificationFragment.newInstance("", ""),
//                    allowStateLoss = true,
//                    containerViewId = R.id.mainContent
//
//                )
                var intent = Intent(this, VehicleModelActivity::class.java)
                startActivity(intent)

            }
            R.id.ivProfile -> {
                /* replaceFragmenty(
                     fragment = ProfileFragment.newInstance("", ""),
                     allowStateLoss = true,
                     containerViewId = R.id.mainContent

                 )*/

                var intent = Intent(this, VehicleMakeActivity::class.java)
                startActivity(intent)

            }
            R.id.ivFilter -> {
                openDateSelection()
            }
        }
    }

    private fun openDateSelection() {
        val pickerPopWin = DatePickerPopWin.Builder(
            this@MainActivity
        ) { year, month, day, dateDesc ->
            Toast.makeText(this@MainActivity, dateDesc, Toast.LENGTH_SHORT).show()
            Log.e("getdatee", "" + dateDesc + " " + year + " " + month + " " + day)

            selectedDate = dateDesc
        }.textConfirm("CONFIRM") //text of confirm button
            .textCancel("CANCEL") //text of cancel button
            .btnTextSize(16) // button text size
            .viewTextSize(25) // pick view text size
            .colorCancel(Color.parseColor("#999999")) //color of cancel button
            .colorConfirm(Color.parseColor("#009900")) //color of confirm button
            .minYear(1990) //min year in loop
            .maxYear(2550) // max year in loop
            .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
            .dateChose("2013-11-11") // date chose when init popwindow
            .build()

        pickerPopWin?.cancelBtn?.setOnClickListener {
            pickerPopWin.dismissPopWin()
            if (!selectedDate.equals("")) {
                ivDot?.visibility = View.VISIBLE
            } else {
                ivDot?.visibility = View.GONE
            }
        }
        pickerPopWin?.confirmBtn?.setOnClickListener {

            pickerPopWin.dismissPopWin()
            ivDot?.visibility = View.VISIBLE
        }

        pickerPopWin?.showPopWin(this)
    }
}