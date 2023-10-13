package com.example.timelynew.model.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
      const val BASE_URL = "https://timely-server.onrender.com/"


       fun  getInstance(): Retrofit {

           return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()

        }
    }
}