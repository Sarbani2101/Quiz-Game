package com.example.quizgame.apiwork

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserService {
    val base_url =  ("https://opentdb.com/")
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(service: Class<T>) : T{
        return retrofit.create(service)
    }


    private val BASE_URL = "https://swarupapp.in/django-eshop/"
    private val client = OkHttpClient
        .Builder()
        .build()
    private val retrofit_api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()


    fun <T> buildService(service : Class<T>) :T{
        return retrofit_api.create(service)
    }
}