package com.codingsector.calleruichanger

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.telecom.Call
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.codingsector.calleruichanger.OngoingCall.call
import com.codingsector.calleruichanger.databinding.KeypadBtmBinding
import com.codingsector.calleruichanger.fromkolerapp.DialpadEditText
import com.codingsector.calleruichanger.fromkolerapp.DialpadKey
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.rxkotlin.addTo
import java.io.File
import java.io.IOException
import java.util.*

class CallActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private var number: String? = null

    private lateinit var answer: Button
    private lateinit var hangup: Button
    private lateinit var callInfo: TextView
    private lateinit var parentLayout: ConstraintLayout

    private var addCallButton1: ImageButton? = null
    private var recordingButton1: ImageButton? = null
    private var bluetoothButton1: ImageButton? = null
    private var speakerButton1: ImageButton? = null
    private var muteButton1: ImageButton? = null
    private var keypadButton1: ImageButton? = null
    private var callEndButton1: ImageButton? = null

    private var isMuted: Boolean = false
    private var isOn: Boolean = false
    private var isEnabled: Boolean = true
    private var callRecord: Boolean = true
    private var _isHolding: Boolean = true

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                    }
                    BluetoothAdapter.STATE_ON -> {
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                    }
                }
            }
        }
    }

    private var output: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialer)
        try {
            number = intent.data!!.schemeSpecificPart
        } catch (e: NullPointerException) {
            Log.e(TAG, "NullPointerException")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        // Register for broadcasts on BluetoothAdapter state change
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        application.registerReceiver(mReceiver, filter)

        answer = findViewById(R.id.answer)
        parentLayout = findViewById(R.id.parentLayout)
        parentLayout.setBackgroundResource(R.drawable._677866)
        answer.setOnClickListener {
            OngoingCall.answer()
        }

        hangup = findViewById(R.id.hangup)
        hangup.setOnClickListener {
            OngoingCall.hangup()
            startActivity(Intent(this, DialerActivity::class.java))
        }

        callEndButton1 = findViewById(R.id.callEndButton)
        callEndButton1?.setOnClickListener {
            OngoingCall.hangup()
            startActivity(Intent(this, DialerActivity::class.java))
        }

        muteButton1 = findViewById(R.id.muteButton)
        muteButton1?.setOnClickListener {
            muteMic()
        }

        speakerButton1 = findViewById(R.id.speakerButton)
        speakerButton1?.setOnClickListener {
            speakerOnOff()
        }

        bluetoothButton1 = findViewById(R.id.BluetoothButton)
        bluetoothButton1?.setOnClickListener {
            isEnabled = if (isEnabled) {
                setBluetooth(true)
                false
            } else {
                setBluetooth(false)
                true
            }
        }

        keypadButton1 = findViewById(R.id.keypadButton)
        keypadButton1?.setOnClickListener {
            showBottomSheetDialog()
            Log.e("TESTING", "")
        }

        addCallButton1 = findViewById(R.id.addCallButton)
        addCallButton1?.setOnClickListener {
            onHoldClick()
        }

        recordingButton1 = findViewById(R.id.recordingButton)
        recordingButton1?.setOnClickListener {
//            <uses- Manifest.permission android:name="android.permission.CAPTURE_AUDIO_OUTPUT"/>
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permission = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAPTURE_AUDIO_OUTPUT
                )
                ActivityCompat.requestPermissions(this, permission, 0)
            } else {
                startRecording()
            }
        }

        try {
            Log.d(TAG, "OngoingCall.state")
            OngoingCall.staTe
                .subscribe(::updateUi)
                .addTo(disposables)
            Log.d(TAG, ".add")

        } catch (e: OnErrorNotImplementedException) {
            e.printStackTrace()
        }
    }

    fun onHoldClick() {
        if (_isHolding) {
            call?.hold()
            _isHolding = !_isHolding
        } else {
            call?.unhold()
            _isHolding = !_isHolding
        }
    }

    // original
    @RequiresApi(Build.VERSION_CODES.N)
    private fun startRecording() {
//        getFilename()
        val out: String = SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(Date())
        output = Environment.getExternalStorageDirectory().absolutePath + /*"/CallerUIChanger*/"/recording$out.mp3"

        val callRecorder = MediaRecorder()
        val audioManager: AudioManager? = null
        try {
            callRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            callRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // with mic
            callRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)     // with mic
            callRecorder.setAudioChannels(1)
            callRecorder.setAudioSamplingRate(44100)
            callRecorder.setAudioEncodingBitRate(192000)

            callRecorder.setOutputFile(output)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        callRecord = if (callRecord) {
            Toast.makeText(this, "recording starts", Toast.LENGTH_SHORT).show()
            audioManager?.mode = AudioManager.MODE_IN_CALL
            audioManager?.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                0
            )
            try {
                callRecorder.prepare()
                callRecorder.start()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
            false
        } else {
            Toast.makeText(this, "recording stops", Toast.LENGTH_SHORT).show()
            audioManager?.mode = AudioManager.MODE_NORMAL
            try {
                callRecorder.stop()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
//                    callRecorder.reset()
            callRecorder.release()
            true
        }
    }

    private fun getFilename() {
        val filepath = Environment.getExternalStorageDirectory().path
        val file = File("$filepath/AUDIO_RECORDER_FOLDER")
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    private fun setBluetooth(enable: Boolean): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val isEnabled = bluetoothAdapter.isEnabled
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable()
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable()
        }
        // No need to change bluetooth state
        return true
    }

    private fun speakerOnOff() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (isOn) {
            isOn = false
            audioManager.mode = AudioManager.MODE_IN_CALL
            audioManager.mode = AudioManager.MODE_NORMAL
        } else {
            isOn = true
            audioManager.mode = AudioManager.MODE_NORMAL
            audioManager.mode = AudioManager.MODE_IN_CALL
        }
        audioManager.isSpeakerphoneOn = isOn
    }

    private fun muteMic() {
        val audioManager = baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (!isMuted) {
            if (audioManager.mode == AudioManager.MODE_IN_CALL || audioManager.mode == AudioManager.MODE_IN_COMMUNICATION) {
                audioManager.isMicrophoneMute = true
            }
            isMuted = true
        } else {
            if (audioManager.mode == AudioManager.MODE_IN_CALL || audioManager.mode == AudioManager.MODE_IN_COMMUNICATION) {
                audioManager.isMicrophoneMute = false
            }
            isMuted = false
        }
    }

    fun updateUi(state: Int) {
        Log.d(TAG, "updateUi")
        // add function new incoming call
        // this is where we can change the wallpaper

        callInfo = findViewById(R.id.callInfo)
        callInfo.text = "${
            state.asString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }\n$number"

        val listOfState = listOf(/*Call.STATE_DIALING, *//* Call.STATE_RINGING,*/Call.STATE_ACTIVE,
            Call.STATE_HOLDING
        )

        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
//            Call.STATE_ACTIVE,
        )
        addCallButton1 = findViewById(R.id.addCallButton)
        recordingButton1 = findViewById(R.id.recordingButton)
        bluetoothButton1 = findViewById(R.id.BluetoothButton)
        speakerButton1 = findViewById(R.id.speakerButton)
        keypadButton1 = findViewById(R.id.keypadButton)
        addCallButton1?.isVisible = state in listOfState
        recordingButton1?.isVisible = state in listOfState
        bluetoothButton1?.isVisible = state in listOfState
        speakerButton1?.isVisible = state in listOfState
        muteButton1?.isVisible = state in listOfState
        keypadButton1?.isVisible = state in listOfState
        callEndButton1?.isVisible = state in listOfState
    }

/*    @RequiresApi(Build.VERSION_CODES.O)

    fun newIncomingCall() {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null)
            // add image here
            .addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setClass(this, CallActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification as an ongoing high priority item; this ensure it will show as
        // a head up notification which slides down over top of the current content.
        val builder = Notification.Builder(this, YOUR_CHANNEL_ID)
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)

            // Set notification content intent to take user to the fullscreen UI if the user tap on the
            // notification body.
            .setContentIntent(pendingIntent)
            // Set full screen intent to trigger display of the fullscreen UI when the notification
            // manager deems it appropriate.
            .setFullScreenIntent(pendingIntent, true)

            // Setup notification content.
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Call Activity notification title")
            .setContentTitle("Call Activity notification content.")

        // Use builder.addAction(..)  to add button to answer or reject the call.
//            .addAction()

    }
*/

    override fun onStop() {
        super.onStop()
        disposables.clear()

        // Unregister broadcast listeners
        application.unregisterReceiver(mReceiver)
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }

        private const val TAG = "CallActivity"
    }

    override fun finish() {
        startActivity(Intent(this, DialerActivity::class.java))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.keypad_btm)
        var keyCode = 0
        val _binding by lazy { KeypadBtmBinding.inflate(layoutInflater) }

        val one = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_1)
        val edt = bottomSheetDialog.findViewById<DialpadEditText>(R.id.dialpad_edit_text)
        one?.setOnClickListener { it ->
            keyCode = KeyEvent.KEYCODE_1
            edt?.append("${_binding.dialpadEditText.text}1")
            call?.playDtmfTone('1')
            call?.stopDtmfTone()
        }
        val two = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_2)
        two?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_2
            edt?.append("${_binding.dialpadEditText.text}2")
            call?.playDtmfTone('2')
            call?.stopDtmfTone()
        }
        val three = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_3)
        three?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_3
            edt?.append("${_binding.dialpadEditText.text}3")
            call?.playDtmfTone('3')
            call?.stopDtmfTone()

        }
        val four = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_4)
        four?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_4
            edt?.append("${_binding.dialpadEditText.text}4")
            call?.playDtmfTone('4')
            call?.stopDtmfTone()

        }
        val five = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_5)
        five?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_5
            edt?.append("${_binding.dialpadEditText.text}5")
            call?.playDtmfTone('5')
            call?.stopDtmfTone()

        }
        val six = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_6)
        six?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_6
            edt?.append("${_binding.dialpadEditText.text}6")
            call?.playDtmfTone('6')
            call?.stopDtmfTone()

        }
        val sev = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_7)
        sev?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_7
            edt?.append("${_binding.dialpadEditText.text}7")
            call?.playDtmfTone('7')
            call?.stopDtmfTone()

        }
        val eight = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_8)
        eight?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_8
            edt?.append("${_binding.dialpadEditText.text}8")
            call?.playDtmfTone('8')
            call?.stopDtmfTone()

        }
        val nine = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_9)
        nine?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_9
            edt?.append("${_binding.dialpadEditText.text}9")
            call?.playDtmfTone('9')
            call?.stopDtmfTone()

        }
        val zero = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_0)
        zero?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_0
            edt?.append("${_binding.dialpadEditText.text}0")
            call?.playDtmfTone('0')
            call?.stopDtmfTone()

        }
        val hash = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_hex)
        hash?.setOnClickListener {
            keyCode = KeyEvent.KEYCODE_POUND
            edt?.append("${_binding.dialpadEditText.text}#")
            call?.playDtmfTone('3')
            call?.stopDtmfTone()

        }
        val star = bottomSheetDialog.findViewById<DialpadKey>(R.id.key_star)
        star?.setOnClickListener { it ->
            keyCode = KeyEvent.KEYCODE_STAR
            edt?.append("${_binding.dialpadEditText.text}*")
            call?.playDtmfTone('*')
            call?.stopDtmfTone()
        }
        bottomSheetDialog.show()
    }
}