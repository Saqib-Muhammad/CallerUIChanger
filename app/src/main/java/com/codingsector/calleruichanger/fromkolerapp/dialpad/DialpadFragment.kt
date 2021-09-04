package com.codingsector.calleruichanger.fromkolerapp.dialpad




import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.KEYCODE_DEL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.codingsector.calleruichanger.databinding.KeypadBtmBinding
import com.codingsector.calleruichanger.fromkolerapp.DialpadKey

class DialpadFragment : Fragment(), DialpadContract.View {
    private var _onTextChangedListener: (text: String?) -> Unit? = { _ -> }
    private val _binding by lazy { KeypadBtmBinding.inflate(layoutInflater) }
    private var _onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit? = { _, _ -> }

    override val isDialer: Boolean
        get() = TODO("Not yet implemented")

    override var number: String
        get() = _binding.dialpadEditText.text.toString()
        set(value) {
            _binding.dialpadEditText.setText(value)
        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //region dialpad view

    override fun call() {
        if (number.isEmpty()) {
        } else {
        }
    }

    override fun backspace() {
        _binding.dialpadEditText.onKeyDown(KEYCODE_DEL, KeyEvent(ACTION_DOWN, KEYCODE_DEL))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun playTone(keyCode: Int) {
    }

    override fun invokeKey(keyCode: Int) {
        _binding.dialpadEditText.onKeyDown(keyCode, KeyEvent(ACTION_DOWN, keyCode))
    }

    //endregion

    fun setOnTextChangedListener(onTextChangeListener: (text: String?) -> Unit?) {
        _onTextChangedListener = onTextChangeListener
    }

    fun setOnKeyDownListener(onKeyDownListener: (keyCode: Int, event: KeyEvent) -> Unit?) {
        _onKeyDownListener = onKeyDownListener
    }

    override var isDeleteButtonVisible: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    override var isAddContactButtonVisible: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun setSuggestionsFilter(filter: String) {
        TODO("Not yet implemented")
    }

    override fun vibrate() {
        TODO("Not yet implemented")
    }

    companion object {
        const val TAG = "dialpad_bottom_dialog_fragment"
        const val ARG_IS_DIALER = "dialer"
        const val ARG_NUMBER = "number"

        fun newInstance(isDialer: Boolean, number: String? = null) = DialpadFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_DIALER, isDialer)
                putString(ARG_NUMBER, number)
            }
        }
    }
}