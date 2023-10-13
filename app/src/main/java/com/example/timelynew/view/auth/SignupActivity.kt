package com.example.timelynew.view.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.timelynew.view.AppIntro
import com.example.timelynew.R
import com.example.timelynew.model.api.UserService
import com.example.timelynew.model.entity.User
import com.example.timelynew.model.retrofit.RetrofitInstance
import com.example.timelynew.databinding.ActivitySignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var tokenSharedPreferences: SharedPreferences
    lateinit var userSharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySignupBinding
    private lateinit var retrofitService: UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
        retrofitService = RetrofitInstance.getInstance().create(UserService::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        tokenSharedPreferences = this.getSharedPreferences("token",AppCompatActivity.MODE_PRIVATE)
        userSharedPreferences = this.getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)

        binding.apply {
            buttonSignup.setOnClickListener {
                if(!email.text.isNullOrEmpty() && !password.text.isNullOrEmpty()){
                    auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener { task ->

                        if(task.isSuccessful) {
                            val body: MutableMap<String, Any> = HashMap()
                            body["email"] = email.text.toString().trim()
                            body["token"] = tokenSharedPreferences.getString("fb","null")!!
                            val call = retrofitService.createUser(body)
                            call.enqueue(object:Callback<User>{
                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                    val editor = userSharedPreferences.edit()

                                    editor.putString("email",response.body()!!.email)
                                    editor.putLong("id",response.body()!!.id)
                                    editor.apply()
                                    Snackbar.make( signuplayout ,"Signup Successful", Snackbar.LENGTH_LONG).setAction("close",{
                                    }).show()
                                    var intent = Intent(this@SignupActivity, AppIntro::class.java)
                                    startActivity(intent)
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    Snackbar.make( signuplayout , t.message.toString(), Snackbar.LENGTH_LONG).setAction("close",{
                                    }).show()
                                }

                            })


//                        var intent = Intent(this@SignupActivity,MainActivity::class.java)
//                        startActivity(intent)

                        } else {

                            Snackbar.make( signuplayout , task.exception?.message.toString(), Snackbar.LENGTH_LONG).setAction("close",{
                            }).show()
                        }
                    }
                } else {
                    Snackbar.make( signuplayout , "Please fill the inputs", Snackbar.LENGTH_LONG).setAction("close",{
                    }).show()
                }

            }

            login.setOnClickListener {
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            }
        }
    }
}