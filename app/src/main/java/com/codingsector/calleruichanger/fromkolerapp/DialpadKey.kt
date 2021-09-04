package com.codingsector.calleruichanger.fromkolerapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import com.codingsector.calleruichanger.R
import java.lang.UnsupportedOperationException

@SuppressLint("Recycle", "CustomViewStyleable")
class DialpadKey : LinearLayout {

    private val _viewManager by lazy { ViewManager(context) }

    var keyCode = 0
    private var _digitTextView: TextView
    private var _lettersTextView: TextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        gravity = Gravity.CENTER_HORIZONTAL

        _digitTextView = TextView(context, attrs, defStyleRes).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            setTextAppearance(R.style.Koler_Text_Headline2)
        }.also {
            addView(it)
        }

        _lettersTextView = TextView(context, attrs, defStyleRes).apply {
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            setPadding(0, _viewManager.getSizeInDp(5), 0, 0)
            try {
                setTextAppearance(R.style.Koler_Text_Caption)
            }catch(e: UnsupportedOperationException) {
                e.printStackTrace()
            }

        }.also {
            addView(it)
        }

        orientation = VERTICAL
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        background = _viewManager.selectableItemBackgroundBorderlessDrawable
        digit = context.obtainStyledAttributes(attrs, R.styleable.Koler_DialpadKey)
            .getString(R.styleable.Koler_DialpadKey_digit)

      // setPadding(_viewManager.getSizeInDp(7))
    }

    var digit: String?
        get() = _digitTextView.text.toString()
        set(value) {
            _digitTextView.text = value
            when (value) {
                "0" -> {
                    keyCode = KeyEvent.KEYCODE_0
                    _lettersTextView.text = context.getString(R.string.dialpad_0_letters)
                }
                "1" -> {
                    keyCode = KeyEvent.KEYCODE_1
                    _lettersTextView.setBackgroundResource(R.drawable.ic_voicemail_black_24dp)
                }
                "2" -> {
                    keyCode = KeyEvent.KEYCODE_2
                    _lettersTextView.text = context.getString(R.string.dialpad_2_letters)
                }
                "3" -> {
                    keyCode = KeyEvent.KEYCODE_3
                    _lettersTextView.text = context.getString(R.string.dialpad_3_letters)
                }
                "4" -> {
                    keyCode = KeyEvent.KEYCODE_4
                    _lettersTextView.text = context.getString(R.string.dialpad_4_letters)
                }
                "5" -> {
                    keyCode = KeyEvent.KEYCODE_5
                    _lettersTextView.text = context.getString(R.string.dialpad_5_letters)
                }
                "6" -> {
                    keyCode = KeyEvent.KEYCODE_6
                    _lettersTextView.text = context.getString(R.string.dialpad_6_letters)
                }
                "7" -> {
                    keyCode = KeyEvent.KEYCODE_7
                    _lettersTextView.text = context.getString(R.string.dialpad_7_letters)
                }
                "8" -> {
                    keyCode = KeyEvent.KEYCODE_8
                    _lettersTextView.text = context.getString(R.string.dialpad_8_letters)
                }
                "9" -> {
                    keyCode = KeyEvent.KEYCODE_9
                    _lettersTextView.text = context.getString(R.string.dialpad_9_letters)
                }
                "*" -> {
                    keyCode = KeyEvent.KEYCODE_STAR
                    _lettersTextView.text = context.getString(R.string.dialpad_star_letters)
                }
                "#" -> {
                    keyCode = KeyEvent.KEYCODE_POUND
                    _lettersTextView.text = context.getString(R.string.dialpad_pound_letters)
                }
                else -> {
                }
            }
        }
}