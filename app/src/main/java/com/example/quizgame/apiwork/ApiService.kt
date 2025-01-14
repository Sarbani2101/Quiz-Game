package com.example.quizgame.apiwork

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api.php?amount=10&category=27&difficulty=easy&type=multiple")
    fun getObj() : Call<QuestionModel>


    @FormUrlEncoded
    @POST("api/auth/user/register/")
    fun registerUser(
        @Field("full_name") fullName: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("password2") confirmPassword: String
    ): Call<RegisterData>



    @FormUrlEncoded
    @POST("api/auth/user/login/")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginData>
}