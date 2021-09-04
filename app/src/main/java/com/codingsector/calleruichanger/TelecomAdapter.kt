package com.codingsector.calleruichanger

import android.annotation.SuppressLint
import android.os.Looper
import android.telecom.InCallService
import androidx.core.util.Preconditions

internal class TelecomAdapter internal constructor() : InCallServiceListener {

    private var mInCallService: InCallService? = null

    private var sInstance: TelecomAdapter? = null
    val instance: TelecomAdapter?
    get() {
        Preconditions.checkState(Looper.getMainLooper().thread == Thread.currentThread())
        if (sInstance == null) {
            sInstance = TelecomAdapter()
        }
        return sInstance
    }

    override fun setInCallService(inCallService: InCallService?) {
        mInCallService = inCallService
    }

    override fun clearInCallService() {
        mInCallService = null
    }

    fun canAddCall(): Boolean {
        // Default to true if we are not connected to telecom.
        return if (mInCallService == null) true else mInCallService!!.canAddCall()
    }
}