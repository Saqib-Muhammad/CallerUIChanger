package com.codingsector.calleruichanger.fragments

import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingsector.calleruichanger.R
import com.codingsector.calleruichanger.RecyclerViewAdapter
import com.codingsector.calleruichanger.model.Contact

class ContactsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.contact_list_fragment, container, false)
        return view
    }

   /* private fun initViews(view: View) {
        textView = view.findViewById(R.id.textView)
    }*/

/*
    fun getContacts(context: Context): List<Contact>? {
        textView.text = ""
        val list: MutableList<Contact> = ArrayList()
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor?.count!! > 0) {
            while (cursor.moveToNext()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (cursorInfo?.moveToNext()!!) {
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val phoneNumber =
                            cursorInfo.getString(cursorInfo?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        list.add(Contact(name, phoneNumber))
                        textView.append(name + "\n")
                    }
                    cursorInfo.close()
                }
            }
            cursor.close()
        }
        return list
    }
*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<Contact>()
        val mAdapter = RecyclerViewAdapter(list)
        val contentResolver: ContentResolver? = context?.contentResolver
        var cursor: Cursor? = null
        try {
            cursor = contentResolver?.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
            )

        if (cursor?.count!! > 0) {
            while (cursor.moveToNext()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor? = contentResolver?.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (cursorInfo?.moveToNext()!!) {
                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val phoneNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        list.add(Contact(name, phoneNumber))
                    }
                    cursorInfo.close()
                }
            }
            cursor.close()
        }
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
        e.printStackTrace()
    }
        val contactList = view.findViewById<RecyclerView>(R.id.contactList)
        contactList.layoutManager = LinearLayoutManager(context)
        contactList.adapter = mAdapter
    }
}
