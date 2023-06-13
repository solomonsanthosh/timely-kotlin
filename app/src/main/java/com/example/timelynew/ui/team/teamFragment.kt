package com.example.timelynew.ui.team

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment

import com.example.timelynew.api.ActivityService
import com.example.timelynew.api.UserService
import com.example.timelynew.dataClass.TeamTask
import com.example.timelynew.dataClass.User

import com.example.timelynew.databinding.FragmentTeamBinding
import com.example.timelynew.retrofit.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class teamFragment : Fragment() {
    private var _binding: FragmentTeamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sharedPreferences: SharedPreferences
    lateinit var taskSharedPreferences: SharedPreferences
    private lateinit var retrofitService : ActivityService
    private lateinit var userRetrofitService : UserService
    private val binding get() = _binding!!

    lateinit var adaptor: TeamTaskAdaptor
    lateinit var taskList: ArrayList<TeamTask>
    lateinit var addTeamActivityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        sharedPreferences = requireActivity().getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
        taskSharedPreferences = requireActivity().getSharedPreferences("token",AppCompatActivity.MODE_PRIVATE)
        retrofitService = RetrofitInstance.getInstance().create(ActivityService::class.java)
        userRetrofitService = RetrofitInstance.getInstance().create(UserService::class.java)
        _binding = FragmentTeamBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val call = retrofitService.getTeamTask(sharedPreferences.getString("email",null)!!)

        taskList = ArrayList<TeamTask>()
        registerActivityResultLauncher()
        adaptor = TeamTaskAdaptor(requireActivity(),taskList)
        binding.list.adapter = adaptor
        binding.list.isClickable = true
        call.enqueue(object :Callback<List<TeamTask>>{
            override fun onResponse(
                call: Call<List<TeamTask>>,
                response: Response<List<TeamTask>>
            ) {
                if(response.isSuccessful){
                    taskList.addAll(ArrayList(response.body()))
                    adaptor.notifyDataSetChanged()
                    Toast.makeText(requireActivity(),response.body()?.size.toString(),Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<TeamTask>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        binding.list.setOnItemClickListener { adapterView, view, position, l ->

            var intent = Intent(requireActivity(),ShowMoreActivity::class.java)
            intent.putExtra("title",taskList.get(position).title)
            intent.putExtra("content",taskList.get(position).content)
            intent.putExtra("date",taskList.get(position).date.toString())
            intent.putExtra("members",taskList.get(position).members)
            intent.putExtra("status",taskList.get(position).status)
            requireActivity().startActivity(intent)


        }
        binding.addTeamTaskButton.setOnClickListener {
            var intent = Intent(requireActivity(),addTeamTaskActivity::class.java)
            addTeamActivityResultLauncher.launch(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            val policy:StrictMode.ThreadPolicy =  StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun registerActivityResultLauncher(){
        addTeamActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { resultActivity ->
                val resultCode = resultActivity.resultCode
                val data = resultActivity.data

                if(resultCode == android.app.Activity.RESULT_OK && data != null){

                    val title = data.getStringExtra("title")
                    val content = data.getStringExtra("content")
                    val dateString = data.getStringExtra("date")
                    val members = data.getIntegerArrayListExtra("members")
                    val memberNames = data.getStringArrayListExtra("membersName")
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    val date: Date = sdf.parse(dateString)
                    val statusList = ArrayList<String>()
                    for(name in memberNames!!){
                        statusList.add("Not Completed")
                    }
                    val body: MutableMap<String, Any> = HashMap()
                    body["title"] = title!!
                    body["content"] = content!!
                    body["userid"] = members!!
                    body["assignedBy"] = sharedPreferences.getString("email","self")!!
                    body["date"] = date.time!!
                    body["type"] = "group"


                   val call= retrofitService.assignActivity(body)
                    call.enqueue(object :Callback<TeamTask>{
                        override fun onResponse(call: Call<TeamTask>, response: Response<TeamTask>) {
                            taskList.add(TeamTask(title,content,date,memberNames!!,statusList))
                            adaptor.notifyDataSetChanged()

                            if(response.isSuccessful){
                                val body2: MutableMap<String, Any> = HashMap()
                                body2["title"] = title!!
                                body2["content"] = content!!
                                body2["date"] = date.time!!
                                body2["members"] = memberNames!!
                                body2["status"] = statusList!!


                                val memberNotifyCall =   userRetrofitService.getUsers(body2)
                                memberNotifyCall.enqueue(object :Callback<List<User>>{
                                    override fun onResponse(
                                        call: Call<List<User>>,
                                        response: Response<List<User>>
                                    ) {

                                        if(response.isSuccessful){
                                            val iterator = response.body()?.iterator()
                                            Snackbar.make( binding.teamLayout,iterator.toString(), Snackbar.LENGTH_LONG).setAction("close",{
                                            }).show()
                                            if (iterator != null) {

                                                while (iterator.hasNext()){

                                                    saveNotification("${sharedPreferences.getString("email","self")!!.split('@')[0]} assigned you a task!","${title.substring(0,10)}...",iterator.next())
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                                        Snackbar.make( binding.teamLayout,t.message.toString(), Snackbar.LENGTH_LONG).setAction("close",{
                                        }).show()
                                    }

                                })
                            }





                        }
                        override fun onFailure(call: Call<TeamTask>, t: Throwable) {
                            Snackbar.make( binding.teamLayout,t.message.toString(), Snackbar.LENGTH_LONG).setAction("close",{
                            }).show()
                        }

                    })



                }
            })
    }


    private fun saveNotification(title:String,body:String,user:User){
         val token:String = taskSharedPreferences.getString("fb","null")!!.toString()

        val client = OkHttpClient();
        val media: MediaType? = "application/json".toMediaTypeOrNull()
        val objNotif = JSONObject()
        val obj = JSONObject()

        try {
            obj.put("to",user.token)
            objNotif.put("body",body);
            objNotif.put("title",title);
            obj.put("data",objNotif)
            obj.put("priority","high")
            obj.put("content_available",true)
        }catch (e:JSONException){
            Log.i("ERROR",e.toString())
        }
        val body:RequestBody = RequestBody.create(media,obj.toString())
        val request: Request = Request.Builder().url("https://fcm.googleapis.com/fcm/send").post(body)
            .addHeader("Authorization","key=AAAA0LaCe1g:APA91bFY43z0RZjk6lkRqlhLJv5yPAjefiKDU_5vsrZMbEqC8jDL41jhlSsajA5989MpZX-8eBqMl23DM_U3oeYJO-paNJTCqsELZHlHbEoKBJVktA9ecV0un82QT5ZCagVY8sOI-AeC")
            .addHeader("Content-Type","application/json").build()
        val response = client.newCall(request).execute()


    }


}