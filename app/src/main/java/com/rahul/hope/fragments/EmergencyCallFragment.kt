package com.rahul.hope.fragments


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rahul.hope.HopeApplication
import com.rahul.hope.R
import com.rahul.hope.adapters.ContactAdapter
import com.rahul.hope.listeners.JobListener
import com.rahul.hope.viewmodels.ContactsViewModel
import com.rahul.hope.viewmodels.RoomViewModelFactory
import kotlinx.android.synthetic.main.fragment_emergency_call.*
import kotlinx.android.synthetic.main.fragment_home.*


class EmergencyCallFragment : BottomSheetDialogFragment() {
    private lateinit var roomViewModelFactory: RoomViewModelFactory
    private lateinit var standardContactsAdapter : ContactAdapter
    private lateinit var personalContactsAdapter : ContactAdapter
    private var jobListener : JobListener? = null

    companion object {
        const val REQUEST_CONTACT = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency_call, container, false)
    }

    override fun onAttach(context: Context) {
        roomViewModelFactory = (activity?.application as HopeApplication).applicationComponent.getViewModelFactory()
        jobListener = (context as JobListener)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        standardContactsAdapter = ContactAdapter(activity!!) {
            jobListener?.makeCall(it.phoneNo)
        }
        personalContactsAdapter = ContactAdapter(activity!!) {
            jobListener?.makeCall(it.phoneNo)
        }
        standardContactsRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = standardContactsAdapter
        }
        personalContactsRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = personalContactsAdapter
        }

        val viewModel = ViewModelProviders.of(activity!! , roomViewModelFactory).get(ContactsViewModel::class.java)
        viewModel.standardEmergencyList.observe(this, Observer {
            it?.let { updatedList ->
                standardContactsAdapter.swapList(updatedList)
            }
        })

        viewModel.personalEmergencyList.observe(this, Observer {
            it?.let { updatedList ->
                personalContactsAdapter.swapList(updatedList)
            }
        })
        addContactButton.setOnClickListener {
            jobListener?.onJobRequested(REQUEST_CONTACT)
        }
    }

}
