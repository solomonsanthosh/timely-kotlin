package com.example.timelynew.api

import com.example.timelynew.dataClass.Activity
import com.example.timelynew.dataClass.TeamTask
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ActivityService {

    @GET("api/activity/{id}")
    fun getActivities(@Path("id") id:Long):Call<List<Activity>>

    @GET("api/team/{email}")
    fun getTeamTask(@Path("email") email:String):Call<List<TeamTask>>




    @Multipart
    @POST("/api/activity")
    fun postActivity(@PartMap body: Map<String, @JvmSuppressWildcards RequestBody>,@Part file: MultipartBody.Part?):Call<Activity>
    @Multipart
    @POST("/api/assignactivity")
    fun assignActivity(@PartMap body: Map<String, @JvmSuppressWildcards RequestBody>,@Part file: MultipartBody.Part?):Call<TeamTask>


    @PUT("/api/activity/{id}/{email}")
    fun updateActivity(@Path("id") id:Long,@Path("email") email:String):Call<Activity>


    @PUT("/api/activity/{id}")
    fun updateActivityContent(@Path("id") id:Long , @Body body: MutableMap<String, Any>): Call<Activity>


    @PUT("/api/activitypin/{userid}/{activity_id}")
    fun pinActivity(@Path("userid") userid:Long , @Path("activity_id") activity_id:Long ): Call<Activity>


    @PUT("/api/activityunpin/{activity_id}")
    fun unpinActivity(@Path("activity_id") activity_id:Long): Call<Activity>

}
