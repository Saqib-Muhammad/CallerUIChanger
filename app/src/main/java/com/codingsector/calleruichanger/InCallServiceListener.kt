package com.codingsector.calleruichanger

import android.telecom.InCallService

/**
 * Interface implemented by In-Call components that maintain a reference to the Telecom API
 * {@code InCallService} object. Clarifies the expectations associated with the relevant method
 * calls
 * */

interface InCallServiceListener {

    /**
     * Called once at 'InCallService' startup time with a valid instance. At
     * that time, there will be no existing 'Call's.
     *
     * @param inCallService The 'InCallService' object.
     * */
    fun setInCallService(inCallService: InCallService?)

    /**
     * Called once at 'InCallService' shutdown time. At that time, any 'Call's
     * will have transitioned through the disconnected state and will no longer exits.
     * */
    fun clearInCallService()
}