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
import com.example.technician.common.PrefManager
import com.walkins.technician.R
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.replaceFragmenty
import com.walkins.technician.fragment.HomeFragment
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
    private var tvUsername: TextView? = null

    private var prefManager: PrefManager? = null
    private var ivFilter: ImageView? = null
    private var ivDot: ImageView? = null
    private var selectedDate: String? = null

    private var selectedMenu: String? = null

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
        ivReport = findViewById(R.id.ivReport)
        ivProfile = findViewById(R.id.ivProfile)
        ivNotification = findViewById(R.id.ivNotification)
        llhome = findViewById(R.id.llhome)
        tvUsername = findViewById(R.id.tvUsername)

        ivHome?.setOnClickListener(this)
        ivProfile?.setOnClickListener(this)
        ivReport?.setOnClickListener(this)
        ivNotification?.setOnClickListener(this)
        ivFilter?.setOnClickListener(this)

        tvUsername?.text = "Hello, " + prefManager?.getOwnerName()

        ivHome?.performClick()

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
                ivReport?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivHome?.setColorFilter(
                    ContextCompat.getColor(this, R.color.header_title),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivProfile?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivNotification?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );

                replaceFragmenty(
                    fragment = HomeFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )

                selectedMenu = "home"
            }
            R.id.ivReport -> {
/*
                replaceFragmenty(
                    fragment = HomeFragment.newInstance("", ""),
                    allowStateLoss = true,
                    containerViewId = R.id.mainContent

                )
*/
                ivReport?.setColorFilter(
                    ContextCompat.getColor(this, R.color.header_title),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivHome?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivProfile?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivNotification?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                selectedMenu = "report"
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)

            }
            R.id.ivNotification -> {
                ivReport?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivHome?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivProfile?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivNotification?.setColorFilter(
                    ContextCompat.getColor(this, R.color.header_title),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                selectedMenu = "notification"
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)

            }
            R.id.ivProfile -> {
                ivReport?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivHome?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivProfile?.setColorFilter(
                    ContextCompat.getColor(this, R.color.header_title),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                ivNotification?.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_color1),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                );
                selectedMenu = "profile"
                val intent = Intent(this, ProfileActivity::class.java)
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

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onResume() {
        super.onResume()
        Log.e("selectedMenu", ""+selectedMenu)
    }
}