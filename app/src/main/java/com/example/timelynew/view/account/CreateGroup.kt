package com.example.timelynew.view.account

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.model.api.GroupService
import com.example.timelynew.model.api.UserService
import com.example.timelynew.model.entity.User
import com.example.timelynew.model.retrofit.RetrofitInstance
import com.example.timelynew.databinding.ActivityCreateGroupBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroup : AppCompatActivity() {

    lateinit var binding: ActivityCreateGroupBinding
    lateinit var memberList:ArrayList<String>
    lateinit var members_id:ArrayList<Int>
    @SuppressLint("ResourceType")

    lateinit var retrofitUser: UserService
    lateinit var retrofitGroup: GroupService
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)

        retrofitUser = RetrofitInstance.getInstance().create(UserService::class.java)
        retrofitGroup = RetrofitInstance.getInstance().create(GroupService::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_group)
        memberList = ArrayList()
        members_id= ArrayList()

        binding.apply {

            var adaptor = ArrayAdapter(this@CreateGroup,android.R.layout.simple_list_item_1,memberList)
            grpmemberlist.adapter = adaptor
           addGrpMember.setOnClickListener {
              val call = retrofitUser.getUser(grpmembername.text.toString())
               call.enqueue(object :Callback<User>{
                   override fun onResponse(call: Call<User>, response: Response<User>) {
                       if(response.isSuccessful){
                           memberList.add(response.body()!!.email.toString())
                           members_id.add(response.body()!!.id.toInt())
                           adaptor.notifyDataSetChanged()
                           Snackbar.make(createGroupLayout,"Member added Successfully.",Snackbar.LENGTH_LONG).setAction("close",{}).show()
                       } else {
                           Snackbar.make(createGroupLayout,"Member not found.",Snackbar.LENGTH_LONG).setAction("close",{}).show()
                       }

                   }

                   override fun onFailure(call: Call<User>, t: Throwable) {
                       Snackbar.make(createGroupLayout,"Member not found.",Snackbar.LENGTH_LONG).setAction("close",{}).show()
                   }
               })


           }


            addgrp.setOnClickListener {

                if(memberList.size !=0 && !grpname.text.isNullOrEmpty()){
                    var intent = Intent()
                    intent.putExtra("title",grpname.text.toString())
                    intent.putStringArrayListExtra("members",memberList)
                    intent.putIntegerArrayListExtra("members_id",members_id)
                    setResult(RESULT_OK,intent)
                    finish()
                } else {
                    Snackbar.make(createGroupLayout,"Please fill the inputs.",Snackbar.LENGTH_LONG).setAction("close",{}).show()
                }
            }

           }

        }
    }
