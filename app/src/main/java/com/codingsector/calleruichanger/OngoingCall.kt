package com.codingsector.calleruichanger

import android.telecom.Call
import android.telecom.VideoProfile
import android.util.Log
import io.reactivex.subjects.BehaviorSubject

private const val TAG = "OngoingCall"

object OngoingCall {
    val staTe: BehaviorSubject<Int> = BehaviorSubject.create()

    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call?, state: Int) {
            Log.d(TAG, call.toString())
            staTe.onNext(state)
        }
    }

    var call: Call? = null
    set(value) {
        field?.unregisterCallback(callback)
        value?.let {
            it.registerCallback(callback)
            staTe.onNext(it.state)
        }
        field = value
    }

    fun answer() {
        call?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun hangup() {
        call?.disconnect()
    }
}