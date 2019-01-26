package com.rahul.hope

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.rahul.hope.adapters.MessageAdapter
import com.rahul.hope.models.Message
import kotlinx.android.synthetic.main.activity_chat.*
import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class ChatActivity : AppCompatActivity() {

    private var displayName = FirebaseAuth.getInstance().currentUser?.displayName!!

    private lateinit var mChatAdapter: MessageAdapter

    private lateinit var childEventListener : ChildEventListener
    private var mDatabase = FirebaseDatabase.getInstance()
    private var databaseReference = mDatabase.reference.child("messages")

    private var lastUserName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)



        userInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) =
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    sendFab.isEnabled = true
                } else {
                    sendFab.setEnabled(false)
                }

            override fun afterTextChanged(editable: Editable) {}
        })


        mChatAdapter = MessageAdapter(this)
        val messageLayout = LinearLayoutManager(this@ChatActivity, RecyclerView.VERTICAL, false)
        rvMessages.apply {
            layoutManager = messageLayout
            adapter = mChatAdapter
        }

        sendFab.setOnClickListener {
            val text = userInputEditText.text.toString()
            userInputEditText.setText("")

            databaseReference.push().setValue(Message(text, displayName))

        }

        childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                message?.let {message ->
                    if(lastUserName.isEmpty()) {
                        lastUserName = message.senderName
                        if(lastUserName == displayName) {
                            mChatAdapter.addNewUserMessage(message)
                        } else {
                            mChatAdapter.addNewOtherMessage(message)
                        }
                    } else {
                        if(message.senderName != lastUserName) {
                            lastUserName = message.senderName
                            if(lastUserName == displayName) {
                                mChatAdapter.addNewUserMessage(message)
                            } else {
                                mChatAdapter.addNewOtherMessage(message)
                            }
                        }
                        else {
                            if(lastUserName == displayName) {
                                mChatAdapter.addUserMessage(message)
                            } else {
                                mChatAdapter.addOtherMessage(message)
                            }
                        }
                    }

                    rvMessages.smoothScrollToPosition(mChatAdapter.itemCount - 1)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        }
    }

    override fun onResume() {
        super.onResume()
        databaseReference.addChildEventListener(childEventListener)
    }

    override fun onPause() {
        super.onPause()
        databaseReference.removeEventListener(childEventListener)
    }
}
