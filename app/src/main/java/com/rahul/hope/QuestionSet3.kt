package com.rahul.hope


import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_question_set1.*
import kotlinx.android.synthetic.main.fragment_question_set3.*


class QuestionSet3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_question_set3, container, false)

        var button = view.findViewById<Button>(R.id.button3)
        button.setOnClickListener{
            if(!userInputEditText3.text.isNullOrBlank()){
                if(radioButton7.isChecked){
                    Answers.Sexuality = radioButton7.text.toString()
                }else if(radioButton6.isChecked){
                    Answers.Sexuality = radioButton6.text.toString()
                }else{
                    Answers.Sexuality = radioButton5.text.toString()
                }

                Answers.Friends = userInputEditText3.text.toString()

                if(radioButton9.isChecked){
                    Answers.SocialFear = radioButton9.text.toString()
                }else if(radioButton8.isChecked){
                    Answers.SocialFear = radioButton8.text.toString()
                }

                if(radioButton11.isChecked){
                    Answers.Virgin = radioButton11.text.toString()
                }else if(radioButton10.isChecked){
                    Answers.Virgin = radioButton10.text.toString()
                }

                Log.v("Answers:",Answers.Gender+" "+Answers.Age+" "+Answers.Bodyweight+" "+Answers.Depressed+" "+Answers.Education+" "+Answers.Employment+" "+Answers.Friends+" "+Answers.Income+" "+Answers.Race+" "+Answers.Sexuality+" "+Answers.SocialFear+" "+Answers.Virgin)
            }
        }
        return view
    }


}
