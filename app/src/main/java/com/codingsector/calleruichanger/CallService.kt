package com.codingsector.calleruichanger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import androidx.core.content.ContextCompat

internal const val YOUR_CHANNEL_ID = "Channel_ID"

class CallService : InCallService() {

    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)
        OngoingCall.call = call
        CallActivity.start(this, call!!)
//        IncomingCallNotification()
    }

    override fun onCallRemoved(call: Call?) {
        super.onCallRemoved(call)
        OngoingCall.call = null
    }

    fun IncomingCallNotification() {
            val channel =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel(
                        YOUR_CHANNEL_ID,
                        "Incoming Calls",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
        // other channel setup stuff goes here.

            // We'll use the default system ringtone for our incoming call notification channel. You can
            // use your own audio resource here.
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            val audioAttributesBuilder = AudioAttributes.Builder()
                // Setting the AudioAttributes is important as it identifies the purpose of your
                // notification sound.
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            channel.setSound(ringtoneUri, audioAttributesBuilder)

            val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mgr.createNotificationChannel(channel)
            }
}