package com.codingsector.calleruichanger.fromkolerapp.dialpad

interface DialpadContract {
    interface View  {
        var number: String
        val isDialer: Boolean
        var isDeleteButtonVisible: Boolean
        var isAddContactButtonVisible: Boolean
        fun setSuggestionsFilter(filter: String)

        fun call()
        fun vibrate()
        fun backspace()
        fun playTone(keyCode: Int)
        fun invokeKey(keyCode: Int)
    }

    interface Presenter<V : View> {
        fun onKeyClick(keyCode: Int)
        fun onTextChanged(text: String?)
    }
}