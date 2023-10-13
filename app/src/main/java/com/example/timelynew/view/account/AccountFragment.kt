package com.example.timelynew.view.account


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.timelynew.model.api.ActivityService
import com.example.timelynew.model.entity.Activity
import com.example.timelynew.databinding.FragmentAccountBinding
import com.example.timelynew.model.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!
    lateinit var retrofitService: ActivityService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        retrofitService = RetrofitInstance.getInstance().create(ActivityService::class.java)
        var call: Call<List<Activity>> = retrofitService.getAllActivities(sharedPreferences.getLong("id",0))
call.enqueue(object:Callback<List<Activity>>{
    override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
        if(response.isSuccessful) {

            val activityList: ArrayList<Activity> = ArrayList(response.body())
           binding.completed.setText("No of Completed Activities: ${activityList.filter { activity -> activity.status == "Completed"  }.count().toString()}")
            binding.pending.setText("No of Pending Activities: ${activityList.filter { activity -> activity.status == "Not Completed"  }.count().toString()}")

        }
    }

    override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
        TODO("Not yet implemented")
    }
})

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            grp.setOnClickListener {
                var intent = Intent(requireActivity(),ViewGroups::class.java)
                requireActivity().startActivity(intent)
            }
            user.setText(sharedPreferences.getString("email","user").toString())


        }





        return root
    }


}