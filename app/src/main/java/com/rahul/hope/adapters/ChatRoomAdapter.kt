package com.rahul.hope.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.hope.R
import com.rahul.hope.data.database.ChatRoomEntry

class ChatRoomAdapter(val context: Context, private val itemClick : (ChatRoomEntry) -> Unit) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {
    private val allList = ArrayList<ChatRoomEntry>()

    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(inflater.inflate(R.layout.list_item_chatroom, parent, false), itemClick)
    }

    override fun getItemCount(): Int {
        return allList.size
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(allList[position])
    }

    inner class ChatRoomViewHolder(itemView: View, private val itemClick : (ChatRoomEntry) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.chatRoomTitle)
        fun bind(chatRoomEntry: ChatRoomEntry) {
            title.text = chatRoomEntry.chatRoomName
            itemView.setOnClickListener { itemClick(chatRoomEntry) }
        }
    }

    fun swapList(list: List<ChatRoomEntry>) {
        allList.clear()
        allList.addAll(list)
        notifyDataSetChanged()
    }
}