package com.rahul.hope.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.hope.R
import com.rahul.hope.data.database.EmergencyContactEntry

class ContactAdapter (val context: Context, private val itemClick : (EmergencyContactEntry) -> Unit) : RecyclerView.Adapter<ContactAdapter.ContactsViewHolder>() {

    private val contactsList = ArrayList<EmergencyContactEntry>()

    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = inflater.inflate(R.layout.list_item_contact, parent, false)
        return ContactsViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = contactsList.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contactsList[position])
    }

    inner class ContactsViewHolder(itemView : View, private val itemClick : (EmergencyContactEntry) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        private val phoneNoTextView = itemView.findViewById<TextView>(R.id.phoneNoTextView)
        private val callButton = itemView.findViewById<ImageView>(R.id.callImageView)

        fun bind (contact : EmergencyContactEntry) {
            titleTextView.text = contact.name
            phoneNoTextView.text = contact.phoneNo.toString()
            callButton.setOnClickListener { itemClick(contact) }
        }
    }

    fun swapList(list : List<EmergencyContactEntry>) {
        contactsList.clear()
        contactsList.addAll(list)
        notifyDataSetChanged()
    }
}