package com.walkins.aapkedoorstep

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.walkins.aapkedoorstep.service.Actions
import com.walkins.aapkedoorstep.service.BackgroundService
import com.walkins.aapkedoorstep.service.ServiceState
import com.walkins.aapkedoorstep.service.getServiceState

class StartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && getServiceState(context) == ServiceState.STARTED) {
            Intent(context, BackgroundService::class.java).also {
                it.action = Actions.START.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.e(
                        "EndService",
                        "Starting the service in >=26 Mode from a BroadcastReceiver"
                    )
                    context.startForegroundService(it)
                    return
                }
                Log.e("EndService", "Starting the service in < 26 Mode from a BroadcastReceiver")
                context.startService(it)
            }
        }
    }
}