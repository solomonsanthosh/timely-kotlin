package com.example.timelynew.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.databinding.ActivityShowBinding

class showActivity : AppCompatActivity() {
    lateinit var binding:ActivityShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_show)

        binding = DataBindingUtil.setContentView(this@showActivity,R.layout.activity_show)

        binding.showTitle.text = intent.getStringExtra("title")
        binding.showContent.text = intent.getStringExtra("content")
        binding.showDate.text = intent.getStringExtra("date")


    }
}