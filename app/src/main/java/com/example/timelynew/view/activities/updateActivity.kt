package com.example.timelynew.view.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.timelynew.MainActivity
import com.example.timelynew.R
import com.example.timelynew.model.api.ActivityService
import com.example.timelynew.model.entity.Activity
import com.example.timelynew.model.retrofit.RetrofitInstance
import com.example.timelynew.databinding.ActivityUpdateBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class updateActivity : AppCompatActivity() {
    lateinit var retrofitService: ActivityService
    lateinit var binding : ActivityUpdateBinding
    lateinit var selectedDate:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        supportActionBar?.hide()
        retrofitService = RetrofitInstance.getInstance().create(ActivityService::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update)
        val year = intent.getIntExtra("year",0)
        val month = intent.getIntExtra("month",0)
        val day = intent.getIntExtra("day",0)
        val id = intent.getLongExtra("id",0)
        selectedDate = "${day+1}/${month+1}/$year"
        binding.apply {
            val parser = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val parsed = parser.parse("${day}/${month+1}/${year}")
            editText1update.setText(intent.getStringExtra("title"))
            editText2update.setText(intent.getStringExtra("content"))
            datePickerupdate.setText(parsed.toString())
            datePickerupdate.setOnClickListener {view ->
                clickDatePicker(view,year,month,day)

            }
            update.setOnClickListener {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val date:Date = sdf.parse(selectedDate)
                val body :MutableMap<String,Any> = HashMap<String,Any>()
                body.put("title",editText1update.text.toString())
                body.put("content",editText2update.text.toString())
                body.put("date",date.time!!)

              val call = retrofitService.updateActivityContent(id,body)
                call.enqueue(object :Callback<Activity>{
                    override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                        Snackbar.make( binding.updateActivityLayout,"Activity successfully updated.", Snackbar.LENGTH_LONG).setAction("close",{
                        }).show()
                        var intent = Intent(this@updateActivity,MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<Activity>, t: Throwable) {
                        Snackbar.make( binding.updateActivityLayout,"Activity failed to update.", Snackbar.LENGTH_LONG).setAction("close",{
                        }).show()
                    }

                })
            }

        }
    }
    fun clickDatePicker(view: View,year:Int,month:Int,day:Int){


        Snackbar.make( binding.updateActivityLayout,year.toString(), Snackbar.LENGTH_LONG).setAction("close",{
        }).show()
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, year, month, day->


            val date2 = "${day}/${month+1}/$year"
            selectedDate = "${day+1}/${month+1}/$year"

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = sdf.parse(selectedDate)
            val date2Parsed = sdf.parse(date2)

            binding.datePickerupdate.text = date2Parsed.toString()
        },year,month,day).show()
    }

}