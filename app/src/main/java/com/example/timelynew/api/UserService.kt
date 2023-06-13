package com.example.timelynew.api

import android.text.Editable
import com.example.timelynew.dataClass.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserService {

    @GET("api/user/{email}")
     fun getUser(@Path("email") email: String):Call<User>


    @POST("api/users")
    fun getUsers(@Body body: MutableMap<String, Any>):Call <List<User>>

    @POST("api/user")
    fun createUser(@Body  body: MutableMap<String, Any>) :Call<User>

    @PUT("api/user/{email}/{token}")
    fun updateUser(@Path("email") email: String, @Path("token") token:String) :Call<User>



}