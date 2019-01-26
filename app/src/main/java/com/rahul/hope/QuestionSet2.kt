package com.rahul.hope


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_question_set2.*


@SuppressLint("ValidFragment")
class QuestionSet2 @SuppressLint("ValidFragment") constructor
    (var viewPager: ViewPager) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_question_set2, container, false)
        var button = view.findViewById<Button>(R.id.button2)
        button.setOnClickListener{

            Answers.Education = spinner3.selectedItem.toString()
            Answers.Employment = spinner4.selectedItem.toString()
            Answers.Income = spinner5.selectedItem.toString()
            if(radioButton4.isChecked){
                Answers.Depressed = "Yes"
            }else{
                Answers.Depressed = "No"
            }

            viewPager.setCurrentItem(2)
        }
        return view
    }


}
