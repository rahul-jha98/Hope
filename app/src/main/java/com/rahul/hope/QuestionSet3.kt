package com.rahul.hope


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rahul.hope.data.DataRepository
import com.rahul.hope.data.network.ApiService
import com.rahul.hope.data.network.Base
import kotlinx.android.synthetic.main.fragment_question_set1.*
import kotlinx.android.synthetic.main.fragment_question_set3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionSet3 : Fragment() {

    private lateinit var apiClient : ApiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var repository: DataRepository
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
            getAnalysis()
        }
        return view
    }

    private fun getAnalysis() {
        val call = apiClient.sendDetails(
            Answers.Gender,
            Answers.Sexuality,
            Answers.Age,
            Answers.Income,
            Answers.Race,
            Answers.Bodyweight,
            Answers.Virgin,
            Answers.Friends,
            Answers.SocialFear,
            Answers.Depressed,
            Answers.Employment,
            Answers.Education
        )

        call.enqueue(object:Callback<Base>{
            override fun onFailure(call: Call<Base>, t: Throwable) {
                Toast.makeText(context, "Not able to submit", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                if(response.isSuccessful) {
                    response.body()?.let{
                        val editor = sharedPreferences!!.edit()
                        editor.putFloat(STATUS, it.predicted[0].toFloat() * 100)
                        editor.apply()
                        repository.init()
                        startActivity(Intent(activity!!, HomeActivity::class.java))
                        activity?.finish()
                    }
                } else {
                    Toast.makeText(context, "Something went wrong in submit", Toast.LENGTH_SHORT).show()
                }

            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        apiClient = (context.applicationContext as HopeApplication).applicationComponent.getApiService()
        repository =  (context.applicationContext as HopeApplication).applicationComponent.getRepository()
        sharedPreferences = context.getSharedPreferences(sharedPath, 0)
    }
}
