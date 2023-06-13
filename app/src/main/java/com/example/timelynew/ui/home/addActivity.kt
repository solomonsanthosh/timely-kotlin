package com.example.timelynew.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.*

class addActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddBinding
    lateinit var selectedDate:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add)
        binding.apply {



        datePicker.setOnClickListener {view ->
        clickDatePicker(view)

        }

       add.setOnClickListener {

            var intent = Intent()
            intent.putExtra("title",binding.editText1.text.toString())
            intent.putExtra("content",binding.editText2.text.toString())
            intent.putExtra("date",selectedDate)
            setResult(RESULT_OK,intent)
            finish()

        }
        }

    }

    fun clickDatePicker(view:View){
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view,year,month,day->



             selectedDate = "$day/${month+1}/$year"

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = sdf.parse(selectedDate)

            binding.datePicker.text = date.toString()
        },year,month,day).show()
    }

}