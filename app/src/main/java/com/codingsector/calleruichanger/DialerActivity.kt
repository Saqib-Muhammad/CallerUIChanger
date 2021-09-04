package com.codingsector.calleruichanger

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.*
import com.codingsector.calleruichanger.fragments.CallLogsFragment
import com.codingsector.calleruichanger.fragments.ContactsFragment
import com.codingsector.calleruichanger.fragments.FavouriteContactsFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

const val REMOVE_ADDED_CALL = "Remove added call"

class DialerActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    lateinit var phoneNumberInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialer)
        phoneNumberInput = findViewById(R.id.phoneNumberInput)
        phoneNumberInput.setText(intent?.data?.schemeSpecificPart)

        val readContacts = findViewById<Button>(R.id.readContacts)
        readContacts.setOnClickListener {
            readContacts()
        }
        val recentCalls = findViewById<Button>(R.id.recentCalls)
        recentCalls.setOnClickListener {
            callLogs()
        }
        val favoContacts = findViewById<Button>(R.id.favButton)
        favoContacts.setOnClickListener {
            favouriteContacts()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()
//                requestRole()
        offerReplacingDefaultDialer()

        phoneNumberInput.setOnEditorActionListener { _, _, _ ->
            makeCall()
            true
        }
    }

    private fun makeCall() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val uri = "tel:${phoneNumberInput.text}".toUri()
            // how to make action call customize
            // pass image

            val intent = Intent(Intent.ACTION_CALL, uri)
            startActivity(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PERMISSION)
        }
    }

    private fun readContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ContactsFragment>(R.id.fragment_container_view)
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_PERMISSION_READ_CONTACTS
            )
        }
    }

    private fun callLogs() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) ==
                PackageManager.PERMISSION_GRANTED) {
            supportFragmentManager.commit{
                setReorderingAllowed(true)
                replace<CallLogsFragment>(R.id.fragment_container_view)
            }
        } else {
            requestPermissions(
            arrayOf(Manifest.permission.READ_CALL_LOG), REQUEST_PERMISSION_CALL_LOGS)
        }
    }

    private fun favouriteContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<FavouriteContactsFragment>(R.id.fragment_container_view)
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_PERMISSION_READ_FAVOURITE_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (/*requestCode == REQUEST_PERMISSION && */PackageManager.PERMISSION_GRANTED in grantResults) {
                    makeCall()
                }
                return
            }

            REQUEST_PERMISSION_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts()
                }
                return
            }

            REQUEST_PERMISSION_CALL_LOGS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callLogs()
                }
            }

            REQUEST_PERMISSION_READ_FAVOURITE_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    favouriteContacts()
                }
            }
        }
    }

    // Becoming a default Phone app
    private fun offerReplacingDefaultDialer() {
        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                .let(::startActivity)
        }
    }

    // user to see if they would like your app to be the new default phone app.
    /*@RequiresApi(Build.VERSION_CODES.Q)
    fun requestRole() {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, REQUEST_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ID) {
            // Your app is now the default dialer app
            Toast.makeText(this, "Your app is now the default dialer app", Toast.LENGTH_SHORT)
                .show()
        } else {
            // Your app is not not the default dialer app
            Toast.makeText(this, "Your app is not not the default dialer app", Toast.LENGTH_SHORT)
                .show()
        }
    }
*/

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    companion object {
        val REQUEST_PERMISSION_VOICE_CALL = 6
        const val REQUEST_PERMISSION = 10
        private const val REQUEST_ID = 1
        const val REQUEST_PERMISSION_READ_CONTACTS = 2
        const val REQUEST_PERMISSION_CALL_LOGS = 4
        const val REQUEST_PERMISSION_READ_FAVOURITE_CONTACTS = 5
    }
}