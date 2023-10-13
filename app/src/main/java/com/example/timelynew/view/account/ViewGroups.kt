package com.example.timelynew.view.account

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.model.api.GroupService
import com.example.timelynew.model.entity.Group
import com.example.timelynew.databinding.ActivityViewGroupsBinding
import com.example.timelynew.model.retrofit.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ViewGroups : AppCompatActivity() {

    lateinit var retrofitGroup : GroupService
    lateinit var grpList:ArrayList<Group>
    lateinit var grpListNames:ArrayList<String>
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding:ActivityViewGroupsBinding
    lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var adaptor:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_groups)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_groups)
        sharedPreferences = this.getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
        retrofitGroup = RetrofitInstance.getInstance().create(GroupService::class.java)
        grpList = ArrayList()
        grpListNames = ArrayList()
        registerActivityResultLauncher()
         adaptor = ArrayAdapter(this@ViewGroups,android.R.layout.simple_list_item_1,grpListNames)

        val call = retrofitGroup.getGroup(sharedPreferences.getLong("id",0))
        call.enqueue(object :Callback<List<Group>>{
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
               if(response.isSuccessful){

                   grpList  = (response.body() as ArrayList<Group>?)!!
                   grpList.forEach { grp ->
                       grpListNames.add(grp.title)
                   }

                   binding.grpList.adapter = adaptor
                   adaptor.notifyDataSetChanged()
               } else {
                   Snackbar.make( binding.grpLayout,"Please try again...", Snackbar.LENGTH_LONG).setAction("close",{
                   }).show()
               }

            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Snackbar.make( binding.grpLayout,"Please try again...", Snackbar.LENGTH_LONG).setAction("close",{
                }).show()
            }

        })


        binding.GroupCreateNavigate.setOnClickListener {
            var intent = Intent(this, CreateGroup::class.java)
            addActivityResultLauncher.launch(intent)

        }



    }
    fun registerActivityResultLauncher(){
        addActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { resultActivity ->
                val resultCode = resultActivity.resultCode
                val data = resultActivity.data

                if(resultCode == android.app.Activity.RESULT_OK && data != null){
                    val title = data.getStringExtra("title")
                    val members = data.getStringArrayListExtra("members")
                    val members_id = data.getIntegerArrayListExtra("members_id")

                    val body: MutableMap<String, Any> = HashMap()
                    body["title"] = title!!
                    body["members"] = members!!
                    body["members_id"] = members_id!!
                    body["userid"] = sharedPreferences.getLong("id",0)


                    val call = retrofitGroup.postGroup(body)
                    call.enqueue(object :Callback<Group>{
                        override fun onResponse(
                            call: Call<Group>,
                            response: Response<Group>
                        ) {

                            if(response.isSuccessful){

                                grpList.add(response.body()!!)
                                grpListNames.add(title)
                                adaptor.notifyDataSetChanged()
                                Snackbar.make( binding.grpLayout,"Group successfully added.", Snackbar.LENGTH_LONG).setAction("close",{
                                }).show()
                            }

                        }

                        override fun onFailure(call: Call<Group>, t: Throwable) {
                            Snackbar.make( binding.grpLayout,"Group creation failed. Please try again...", Snackbar.LENGTH_LONG).setAction("close",{
                            }).show()
                        }

                    })

                }
            })
    }
}