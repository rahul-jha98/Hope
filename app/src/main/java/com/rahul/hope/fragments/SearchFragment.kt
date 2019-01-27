package com.rahul.hope.fragments


import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rahul.hope.HopeApplication

import com.rahul.hope.R
import com.rahul.hope.adapters.ChatRoomAdapter
import com.rahul.hope.adapters.ContactAdapter
import com.rahul.hope.data.DataRepository
import com.rahul.hope.data.database.ChatRoomEntry
import com.rahul.hope.data.network.ApiService
import com.rahul.hope.data.network.Query
import com.rahul.hope.listeners.JobListener
import com.rahul.hope.listeners.LaunchBottomSheetListener
import com.rahul.hope.viewmodels.RoomViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {
    private lateinit var roomViewModelFactory: DataRepository
    private lateinit var apiService: ApiService
    private var data : Query? = null
    private var launchBottomSheetListener: LaunchBottomSheetListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onAttach(context: Context) {
        roomViewModelFactory = (activity?.application as HopeApplication).applicationComponent.getRepository()
        apiService = (activity?.application as HopeApplication).applicationComponent.getApiService()
        launchBottomSheetListener = context as LaunchBottomSheetListener
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton.setOnClickListener {
            val text = queryTv.text.toString()
            searchButton.visibility = View.INVISIBLE
            apiService.getGroup(text).enqueue(object : Callback<Query> {
                override fun onFailure(call: Call<Query>, t: Throwable) {
                    resultTv.text = "Failed"
                }

                override fun onResponse(call: Call<Query>, response: Response<Query>) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            cancelButton.visibility = View.VISIBLE
                            resultTv.text = it.name
                            headTv.visibility = View.VISIBLE
                            resultTv.visibility = View.VISIBLE
                            joinButton.visibility = View.VISIBLE
                            launchBottomSheetListener?.launchBottomSheet(3)
                            data = it
                        }
                    } else {
                        resultTv.text = "Server error"
                    }

                }

            })
        }
        joinButton.setOnClickListener {
            data?.let{
                roomViewModelFactory.addChatRoom(ChatRoomEntry(it.name))
            }
        }

        cancelButton.setOnClickListener {
            cancelButton.visibility = View.GONE
            searchButton.visibility = View.VISIBLE
            headTv.visibility = View.GONE
            resultTv.visibility = View.GONE
            joinButton.visibility = View.GONE
            launchBottomSheetListener?.launchBottomSheet(3)
        }
    }
}
