package com.example.timelynew.api

import com.example.timelynew.dataClass.Activity
import com.example.timelynew.dataClass.TeamTask
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActivityService {

    @GET("api/activity/{id}")
    fun getActivities(@Path("id") id:Long):Call<List<Activity>>

    @GET("api/team/{email}")
    fun getTeamTask(@Path("email") email:String):Call<List<TeamTask>>


    @POST("/api/activity")
    fun postActivity(@Body body: MutableMap<String, Any>):Call<Activity>
    @POST("/api/assignactivity")
    fun assignActivity(@Body body: MutableMap<String, Any>):Call<TeamTask>


    @PUT("/api/activity/{id}/{email}")
    fun updateActivity(@Path("id") id:Long,@Path("email") email:String):Call<Activity>


    @PUT("/api/activity/{id}")
    fun updateActivityContent(@Path("id") id:Long , @Body body: MutableMap<String, Any>): Call<Activity>
}