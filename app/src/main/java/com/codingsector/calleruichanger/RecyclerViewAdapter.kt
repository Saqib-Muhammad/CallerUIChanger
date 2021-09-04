package com.codingsector.calleruichanger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingsector.calleruichanger.model.Contact

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val contact: TextView? = view.findViewById(R.id.contactListItem)
    val number: TextView? = view.findViewById(R.id.number)
}

class RecyclerViewAdapter(private val contacts: ArrayList<Contact>) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contacts_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contact?.text = contacts[position].name
        holder.number?.text = contacts[position].mobile
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}