package com.rahul.hope.data.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("query/")
    fun sendDetails(
        @Field("gender") gender: String,
        @Field("sexuality") sexuality: String,
        @Field("age") age: String,
        @Field("income") income: String,
        @Field("race") race: String,
        @Field("bodyweight") bodyWeight: String,
        @Field("virgin") virgin: String,
        @Field("friends") friends: String,
        @Field("social_fear") social_fear: String,
        @Field("depressed") depressed: String,
        @Field("employment") employment: String,
        @Field("edu_level") edu_level: String
    ) : Call<Base>

    @FormUrlEncoded
    @POST("text/")
    fun getGroup(
        @Field("text") text: String
    ) : Call<Query>
}