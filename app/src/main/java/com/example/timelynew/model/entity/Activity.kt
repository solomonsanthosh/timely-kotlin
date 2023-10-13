package com.example.timelynew.model.entity

data class Activity(
    val id:Long,
    val title:String,
    val content:String,
    val date:Any?,
    val userid:ArrayList<Long>,
    val assignedBy:String,
    val type:String,
    val pin:Boolean,
    val file: String?,
    val status:String
)
