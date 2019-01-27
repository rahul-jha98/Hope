package com.rahul.hope.adapters

import android.content.Context
import android.location.GnssNavigationMessage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.hope.R
import com.rahul.hope.models.Message

class MessageAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList = ArrayList<Message>()
    private var typeList = ArrayList<Int>()

    private val USER_NEW = 1
    private val USER_CONT = 2
    private val OTHER_NEW = 3
    private val OTHER_CONT = 4

    private val inflater =  LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            USER_NEW -> NewMessageViewHolder(inflater.inflate(R.layout.message_user, parent, false))
            OTHER_NEW -> NewMessageViewHolder(inflater.inflate(R.layout.message_other, parent, false))
            USER_CONT -> ContMessageViewHolder(inflater.inflate(R.layout.message_user_same, parent, false))
            else -> ContMessageViewHolder(inflater.inflate(R.layout.message_other_same, parent, false))
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NewMessageViewHolder -> holder.bind(messageList[position])
            else -> (holder as ContMessageViewHolder).bind(messageList[position])
        }
    }

    inner class NewMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView = itemView.findViewById<TextView>(R.id.userNameTextView)
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
        fun bind(message: Message) {
            userNameTextView.text = message.senderName
            messageTextView.text = message.message
        }
    }

    inner class ContMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
        fun bind(message: Message) {
            messageTextView.text = message.message
        }
    }

    fun addNewUserMessage(message: Message) {
        messageList.add(message)
        typeList.add(USER_NEW)
        notifyItemInserted(messageList.size)
    }

    fun addUserMessage(message: Message) {
        messageList.add(message)
        typeList.add(USER_CONT)
        notifyItemInserted(messageList.size)
    }

    fun addNewOtherMessage(message: Message) {
        messageList.add(message)
        typeList.add(OTHER_NEW)
        notifyItemInserted(messageList.size)
    }

    fun addOtherMessage(message: Message) {
        messageList.add(message)
        typeList.add(OTHER_CONT)
        notifyItemInserted(messageList.size)
    }

    override fun getItemViewType(position: Int): Int {
        return typeList[position]
    }

    fun clear() {
        messageList.clear()
        typeList.clear()
    }
}