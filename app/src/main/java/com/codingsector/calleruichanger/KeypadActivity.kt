package com.codingsector.calleruichanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.codingsector.calleruichanger.fragments.KeypadFragment
import com.codingsector.calleruichanger.fromkolerapp.dialpad.DialpadFragment

class KeypadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keypad)
        supportFragmentManager.commit {
//            addToBackStack("Keypad fragment")
            setReorderingAllowed(true)
            add<DialpadFragment>(R.id.fragmentContainerView)
        }

    }
}