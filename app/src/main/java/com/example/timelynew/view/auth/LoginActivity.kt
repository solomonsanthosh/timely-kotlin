package com.example.timelynew.view.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.timelynew.MainActivity
import com.example.timelynew.R
import com.example.timelynew.model.api.UserService
import com.example.timelynew.model.entity.User
import com.example.timelynew.databinding.ActivityLoginBinding
import com.example.timelynew.model.retrofit.RetrofitInstance
import com.example.timelynew.viewmodel.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
//import com.example.timelynew.viewModels.UserViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var tokenSharedPreferences: SharedPreferences

    // lateinit var factory:UserViewModelFactory
    lateinit var  viewModel:UserViewModel
    private lateinit var retrofitService: UserService;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        tokenSharedPreferences = this.getSharedPreferences("token",AppCompatActivity.MODE_PRIVATE)
        retrofitService = RetrofitInstance.getInstance().create(UserService::class.java)
        Log.i("PLSSSSSSSSSSSSS",tokenSharedPreferences.getString("fb","null")!!.toString())
        binding.apply {


             button.setOnClickListener {

                 FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                     if (task.isSuccessful) {


                         val token = task.result
                         if(!email.text.isNullOrEmpty() && !password.text.isNullOrEmpty())
                         {
                             auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener { task ->

                                 if(task.isSuccessful) {


                                     try {


                                         val call :Call<User> = retrofitService.getUser(email.text.toString())
                                         call.enqueue(object:Callback<User>{
                                             override fun onResponse(
                                                 call: Call<User>,
                                                 response: Response<User>
                                             ) {
//                                    factory = UserViewModelFactory(response.body()!!)
                                                 if(response.isSuccessful){
                                                     val user: User = response.body()!!

                                                     sharedPreferences = getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
                                                     val editor = sharedPreferences.edit()

                                                     editor.putString("email",user.email)
                                                     editor.putLong("id",user.id)
                                                     editor.apply()
                                                     if(user.token != tokenSharedPreferences.getString("fb",null)){
                                                         val call2 =  retrofitService.updateUser(user.email,tokenSharedPreferences.getString("fb","null")!!)
                                                         call2.enqueue(object:Callback<User>{
                                                             override fun onFailure(
                                                                 call: Call<User>,
                                                                 t: Throwable
                                                             ) {
                                                                 Log.i("PLSSSSSSSSSSSSS",tokenSharedPreferences.getString("fb","null")!!.toString())
                                                                 Toast.makeText(applicationContext, t.message.toString(),
                                                                     Toast.LENGTH_SHORT).show()
                                                             }

                                                             override fun onResponse(
                                                                 call: Call<User>,
                                                                 response: Response<User>
                                                             ) {
                                                                 Snackbar.make( layout ,"Token Registered",Snackbar.LENGTH_LONG).setAction("close",{
                                                                 }).show()

                                                             }

                                                         })
                                                     }





                                                     Snackbar.make( layout ,"Login Successful",Snackbar.LENGTH_LONG).setAction("close",{
                                                     }).show()

                                                     var intent = Intent(this@LoginActivity,
                                                         MainActivity::class.java)
                                                     startActivity(intent)
                                                 }
                                             }


                                             override fun onFailure(call: Call<User>, t: Throwable) {

                                                 Toast.makeText(applicationContext, t.message.toString(),
                                                     Toast.LENGTH_SHORT).show()
                                             }


                                         })

                                     } catch(e:Error){
                                         Snackbar.make( layout ,"Login Failed",Snackbar.LENGTH_LONG).setAction("close",{
                                         }).show()
                                     }








                                 } else {

                                     Snackbar.make( layout , task.exception?.message.toString(),Snackbar.LENGTH_LONG).setAction("close",{
                                     }).show()
                                 }
                             }
                         } else {
                             Snackbar.make( layout , "Please fill the inputs",Snackbar.LENGTH_LONG).setAction("close",{
                             }).show()
                         }
                     } else {
                         Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                         return@OnCompleteListener
                     }




                 })



             }
            signup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            }
        }
    }



    fun saveData(){

    }
}