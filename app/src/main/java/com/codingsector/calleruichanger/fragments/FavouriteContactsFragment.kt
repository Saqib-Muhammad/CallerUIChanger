package com.codingsector.calleruichanger.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codingsector.calleruichanger.R
import com.codingsector.calleruichanger.model.FavouritesContacts

class FavouriteContactsFragment : Fragment() {

    private lateinit var favoContacts: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_get_favourite_contacts, container, false)
        val context = inflater.context
        favoContacts = view.findViewById(R.id.favContacts)
        getFavoriteContacts(context)
        return view
    }

    fun getFavoriteContacts(context: Context): List<FavouritesContacts> {
        favoContacts.text = ""

        var contactMap: HashMap<String, String>? = null
        val list: MutableList<FavouritesContacts> = ArrayList()

        val queryUri = ContactsContract.Contacts.CONTENT_URI.buildUpon()
            .appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
            .build()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.STARRED
        )

        val selection = ContactsContract.Contacts.STARRED + "='1'"

        val cursor = context.contentResolver.query(
            queryUri, projection, selection, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactID =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                val intent = Intent(Intent.ACTION_VIEW)
                val uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_URI, contactID.toString()
                )
                intent.data = uri
                val intentUriString = intent.toUri(0)

                val title = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactMap?.set(title, intentUriString)
                try {
                    // possibilities for errors
                    list.add(FavouritesContacts(contactMap?.get(title)!!))
                } catch (e: NullPointerException) {
                    Log.e("FavContactsFrag","NullPointerException")
                }

                try {
                    favoContacts.append(title + "\n")
                } catch (e: NullPointerException) {
                    Log.e("FavContactsFrag","NullPointerException")
                }
            }
        }

        cursor?.close()
        return list
    }

/*
    fun getFavoriteContacts(context: Context): List<FavouritesContactsModel> {
        favoContacts.text = ""

        var contactMap: HashMap<String, String>? = null
        val list: MutableList<FavouritesContactsModel> = ArrayList()

        val queryUri = ContactsContract.Contacts.CONTENT_URI.buildUpon()
            .appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
            .build()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.STARRED
        )

        val selection = ContactsContract.Contacts.STARRED + "='1'"

        val cursor = context.contentResolver.query(
            queryUri,
            projection, selection, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactID = cursor.getString(
                    cursor
                        .getColumnIndex(ContactsContract.Contacts._ID)
                )

                val intent = Intent(Intent.ACTION_VIEW)
                val uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_URI, contactID.toString()
                )
                intent.data = uri
                val intentUriString = intent.toUri(0)

                val title = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                )

                contactMap?.set(title, intentUriString)
                try {
                    // possibilities for errors
                    list.add(FavouritesContactsModel(contactMap?.get(title)!!))
                } catch (e: NullPointerException) {
                    Log.e("FavContactsFrag","NullPointerException")
                }

                try {
                    favoContacts.append(title + "\n")
                } catch (e: NullPointerException) {
                    Log.e("FavContactsFrag","NullPointerException")
                }
            }
        }

        cursor?.close()
        return list
    }
*/
}