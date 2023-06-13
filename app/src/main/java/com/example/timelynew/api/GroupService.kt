package com.example.timelynew.api

import com.example.timelynew.dataClass.Activity
import com.example.timelynew.dataClass.Group
import com.example.timelynew.dataClass.TeamTask
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupService {

    @GET("api/group/{id}")
    fun getGroup(@Path("id") id:Long): Call<List<Group>>


    @POST("/api/group")
    fun postGroup(@Body body: MutableMap<String, Any>): Call<Group>
}