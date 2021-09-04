package com.codingsector.calleruichanger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.codingsector.calleruichanger.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

internal var keypadNumber: Int? = null

class KeypadFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keypad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextPhone1 = view.findViewById<EditText>(R.id.editTextPhone)
        keypadNumber.apply {
            "," + editTextPhone1.text
        }

        // Data input button
        val button01: Button = view.findViewById(R.id.button1)
        val button02: Button = view.findViewById(R.id.button2)
        val button03: Button = view.findViewById(R.id.button3)
        val button04: Button = view.findViewById(R.id.button4)
        val button05: Button = view.findViewById(R.id.button5)
        val button06: Button = view.findViewById(R.id.button6)
        val button07: Button = view.findViewById(R.id.button7)
        val button08: Button = view.findViewById(R.id.button8)
        val button09: Button = view.findViewById(R.id.button9)
        val button00: Button = view.findViewById(R.id.button0)
        val buttonStar1: Button = view.findViewById(R.id.buttonStar)
        val buttonHash1: Button = view.findViewById(R.id.buttonHash)

        val listener = View.OnClickListener {
            val b = it as Button
            editTextPhone1.append(b.text)
        }
        button00.setOnClickListener(listener)
        button01.setOnClickListener(listener)
        button02.setOnClickListener(listener)
        button03.setOnClickListener(listener)
        button04.setOnClickListener(listener)
        button05.setOnClickListener(listener)
        button06.setOnClickListener(listener)
        button07.setOnClickListener(listener)
        button08.setOnClickListener(listener)
        button09.setOnClickListener(listener)
        buttonStar1.setOnClickListener(listener)
        buttonHash1.setOnClickListener(listener)

        val coordinatorLayout = view.findViewById<CoordinatorLayout>(R.id.coordinateLayout)
        val bottomSheet: View = coordinatorLayout.findViewById(R.id.keypadLayout)
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        behavior.peekHeight = 100
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // react to state change
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }
}