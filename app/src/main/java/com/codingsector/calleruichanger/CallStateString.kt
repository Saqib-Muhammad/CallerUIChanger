package com.codingsector.calleruichanger

import android.telecom.Call
import android.util.Log

fun Int.asString(): String = when(this) {
    Call.STATE_NEW -> "NEW"
    Call.STATE_RINGING -> "RINGING"
    Call.STATE_DIALING -> "DIALING"
    Call.STATE_ACTIVE -> "ACTIVE"
    Call.STATE_HOLDING -> "HOLDING"
    Call.STATE_DISCONNECTED -> "DISCONNECTED"
    Call.STATE_CONNECTING -> "CONNECTED"
    Call.STATE_DISCONNECTING -> "DISCONNECTING"
    Call.STATE_SELECT_PHONE_ACCOUNT -> "SELECT_PHONE_ACCOUNT"

    else -> {
        Log.w("CallStateString","Unknown state $this")
        "UNKNOWN"
    }
}