package com.example.timelynew.view.team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.databinding.ActivityShowMoreBinding

class ShowMoreActivity : AppCompatActivity() {
    lateinit var binding:ActivityShowMoreBinding
    lateinit var statusList: ArrayList<String>
    lateinit var memberList:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_more)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_more)
        supportActionBar?.hide()
        binding.apply {
            titleMore.text = intent.getStringExtra("title")
            contentMore.text = intent.getStringExtra("content")
            dateMore.text = intent.getStringExtra("date")
            statusList = intent.getStringArrayListExtra("status")!!
            memberList = intent.getStringArrayListExtra("members")!!


            val arr = ArrayList<String>()
           for(i in memberList.indices){
               arr.add("${memberList.get(i)}@${statusList.get(i)}")
           }


            val adaptor = ShowMoreAdaptor(this@ShowMoreActivity, arr)
            listMore.adapter = adaptor




        }
    }


}