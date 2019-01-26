package com.rahul.hope

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import kotlinx.android.synthetic.main.fragment_question_set1.*


@SuppressLint("ValidFragment")
class QuestionSet1 @SuppressLint("ValidFragment") constructor
    (var viewPager: ViewPager) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_question_set1, container, false)
        var button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener{
            if(!userInputEditText1.text.toString().isBlank()){
                if(radioButton2.isChecked){
                    Answers.Gender = "Male"
                }else{
                    Answers.Gender = "Female"
                }
                Answers.Age = userInputEditText1.text.toString()
                Answers.Race = spinner.selectedItem.toString()
                Answers.Bodyweight = spinner2.selectedItem.toString()
                viewPager.setCurrentItem(1)
            }
        }

        // Inflate the layout for this fragment
        return view
    }



}
