package com.example.timelynew.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelynew.api.ActivityService
import com.example.timelynew.api.UserService
import com.example.timelynew.dataClass.Activity
import com.example.timelynew.retrofit.RetrofitInstance
import com.example.timelynew.databinding.FragmentActivityBinding
import com.example.timelynew.viewModels.ActivityViewModel
import com.example.timelynew.viewModels.ActivityViewModelFactory
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class activityFragment : Fragment() {

    lateinit var binding: FragmentActivityBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adaptor: ActivityAdaptor
    lateinit var retrofitService:ActivityService
    lateinit var retrofitServiceUser:UserService
    lateinit var factory:ActivityViewModelFactory

    lateinit var activities: List<Activity>
    lateinit var viewModel: ActivityViewModel
    lateinit var sharedPreferences:SharedPreferences
    lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


         retrofitService = RetrofitInstance.getInstance().create(ActivityService::class.java)
         retrofitServiceUser = RetrofitInstance.getInstance().create(UserService::class.java)
        binding = FragmentActivityBinding.inflate(inflater,container,false)



        sharedPreferences = requireActivity().getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)


        val view = binding.root
        recyclerView = binding.recycleview
        registerActivityResultLauncher()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adaptor = ActivityAdaptor(requireContext(),retrofitServiceUser,retrofitService,binding.activityLayout)

        recyclerView.adapter = adaptor


        var call: Call<List<Activity>> = retrofitService.getActivities(sharedPreferences.getLong("id",0))
        call.enqueue(object:Callback<List<Activity>>{
            override fun onResponse(
                call: Call<List<Activity>>,
                response: Response<List<Activity>>
            ) {

                if(response.isSuccessful){
                  val activityList:ArrayList<Activity> =  ArrayList(response.body())

                    factory = ActivityViewModelFactory(activityList)
                    viewModel = ViewModelProvider(requireActivity(),factory).get(ActivityViewModel::class.java)
                    viewModel.activitiesData.observe(viewLifecycleOwner,{  data ->
                        adaptor.setActivity(data)
                        activities = data
                    })


                }


            }

            override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
                Log.i("BRO",t.localizedMessage.toString())
            }

        })


        binding.floatBtn.setOnClickListener {
            var intent = Intent(requireActivity(),addActivity::class.java)
           addActivityResultLauncher.launch(intent)
        }




        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                var position = viewHolder.adapterPosition
                val act = activities.get(position)

                val call = retrofitService.updateActivity(act.id,sharedPreferences.getString("email",null)!!)
                call.enqueue(object:Callback<Activity>{
                    override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                       if(response.isSuccessful){
                           viewModel.removeActivity(act)
                           adaptor = ActivityAdaptor(requireContext(),retrofitServiceUser,retrofitService,binding.activityLayout)
                           recyclerView.adapter = adaptor
                           adaptor.setActivity(activities as ArrayList<Activity>)

                           Snackbar.make( binding.activityLayout,"Activity successfully completed.", Snackbar.LENGTH_LONG).setAction("close",{
                           }).show()
                       }
                    }

                    override fun onFailure(call: Call<Activity>, t: Throwable) {
                        Snackbar.make( binding.activityLayout,"Activity could not be completed.", Snackbar.LENGTH_LONG).setAction("close",{
                        }).show()
                    }

                })




            }


        }
        val ItemTouchHelp = ItemTouchHelper(simpleCallback)
        ItemTouchHelp.attachToRecyclerView(recyclerView)





        return view
    }


    fun registerActivityResultLauncher(){
        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { resultActivity ->
                val resultCode = resultActivity.resultCode
                val data = resultActivity.data

                if(resultCode == android.app.Activity.RESULT_OK && data != null){
                    val title = data.getStringExtra("title")
                    val content = data.getStringExtra("content")
                    val dateString = data.getStringExtra("date")
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    val date:Date = sdf.parse(dateString)

                    var idArray =  ArrayList<Long>();
                    idArray.add(sharedPreferences.getLong("id",0))

                    Snackbar.make( binding.activityLayout,idArray.get(0).toString(), Snackbar.LENGTH_LONG).setAction("close",{
                    }).show()
                    val activity = Activity(0,title!!,content!!, date ,idArray,"self","self")


                    val body: MutableMap<String, Any> = HashMap()
                    body["title"] = title!!
                    body["content"] = content!!
                    body["userid"] = idArray
                    body["date"] = date.time!!

                    val call = retrofitService.postActivity(body)
                    call.enqueue(object :Callback<Activity>{
                        override fun onResponse(
                            call: Call<Activity>,
                            response: Response<Activity>
                        ) {

                            if(response.isSuccessful){
                                viewModel.updateActivity(activity)
                                adaptor = ActivityAdaptor(requireContext(),retrofitServiceUser,retrofitService,binding.activityLayout)
                                recyclerView.adapter = adaptor
                                adaptor.setActivity(activities as ArrayList<Activity>)
                                Snackbar.make( binding.activityLayout,"Activity successfully added.", Snackbar.LENGTH_LONG).setAction("close",{
                                }).show()
                            }

                        }

                        override fun onFailure(call: Call<Activity>, t: Throwable) {
                            Snackbar.make( binding.activityLayout,"Activity creation failed. Please try again...", Snackbar.LENGTH_LONG).setAction("close",{
                            }).show()
                        }

                    })

                }
            })
    }





}