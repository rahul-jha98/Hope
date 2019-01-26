package com.rahul.hope

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rahul.hope.listeners.LaunchBottomSheetListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeActivityFragment : Fragment() {
    private var launcherBottomSheetListener : LaunchBottomSheetListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        call911Button.setOnClickListener { launcherBottomSheetListener?.launchBottomSheet(1) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        launcherBottomSheetListener = context as LaunchBottomSheetListener
    }
}
