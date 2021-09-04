package com.codingsector.calleruichanger.fragments

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codingsector.calleruichanger.R
import com.codingsector.calleruichanger.model.CallLogs

class CallLogsFragment : Fragment() {

    private lateinit var num1: TextView
    private var name1: TextView? = null
    private lateinit var duration1: TextView
    private var type1: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_call_logs, container, false)
        val context = inflater.context
        num1 = view.findViewById(R.id.num)
        name1 = view.findViewById(R.id.name)
        duration1 = view.findViewById(R.id.duration)
        type1 = view.findViewById(R.id.type)

        readCallLogs(context)
        return view
    }

    fun readCallLogs(context: Context): List<CallLogs> {
        num1.text = ""
        val list: MutableList<CallLogs> = ArrayList()

        val allCalls: Uri = Uri.parse("content://call_log/calls")
        val cursor: Cursor? = context.contentResolver.query(allCalls, null, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val num: String =
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))   // for number
                val name: String? =
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))  // for name
                val duration: String =
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))     // for duration
                val type: Int = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
                    .toInt()    // for call type, Incoming or out going.
                try {
                    list.add(CallLogs(name!!))
                } catch (e: NullPointerException) {
                    Log.e("CallLogsFragment", "NullPointerException")
                }

                num1.append(name + "\n")
                try {
                    name1?.append(num + "\n")
                } catch (e: NullPointerException) {
                    Log.e("CallLogsFragment", "NullPointerException")
                }
                duration1.append(duration + "\n")
                type1?.append(type.toString() + "\n")
            }
        }
        cursor?.close()
        return list
    }
}