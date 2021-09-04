package com.codingsector.calleruichanger.fromkolerapp.dialpad

class DialpadPresenter<V : DialpadContract.View>(mvpView: V) :
DialpadContract.Presenter<V>{

    override fun onTextChanged(text: String?) {
    }

    override fun onKeyClick(keyCode: Int) {
    }
}