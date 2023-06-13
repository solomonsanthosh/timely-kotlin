package com.example.timelynew.ui.home


import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ButtonBarLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.timelynew.NotificationReceiver
import com.example.timelynew.R
import com.example.timelynew.api.ActivityService
import com.example.timelynew.api.UserService
import com.example.timelynew.dataClass.Activity
import com.example.timelynew.dataClass.User
import com.example.timelynew.viewModels.ActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ActivityAdaptor(var context: Context,var retrofitUser:UserService,var retrofitActivity:ActivityService,var layout:ConstraintLayout):
    RecyclerView.Adapter<ActivityAdaptor.ActivityHolder>() {
    var activities:ArrayList<Activity> = ArrayList()
    lateinit var intent: Intent
    var selectedPosition = -1
    class ActivityHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var title: TextView = itemView.findViewById(R.id.title)
        var content : TextView = itemView.findViewById(R.id.content)
        var date: TextView = itemView.findViewById(R.id.date)
        var assignby: TextView = itemView.findViewById(R.id.assignby)

        var card: CardView = itemView.findViewById(R.id.card)
        var layout:LinearLayout = itemView.findViewById(R.id.cardLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.activity_card,parent,false)

        return ActivityHolder(view)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        holder.title.text = activities.get(position).title
        holder.content.text = activities.get(position).content
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
//        val date: Date = sdf.parse(activities.get(position).date.toString())
        holder.date.text = activities.get(position).date.toString()
        holder.assignby.text = activities.get(position).assignedBy.split('@')[0].toString()

        if( holder.assignby.text == "self") {
            holder.assignby.visibility = View.GONE

        }  else if (activities.get(holder.adapterPosition).type == "group") {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.group))
            holder.title.setTextColor(context.getResources().getColor(R.color.white))
            holder.content.setTextColor(context.getResources().getColor(R.color.white))
            holder.date.setTextColor(context.getResources().getColor(R.color.white))
            holder.assignby.setTextColor(context.getResources().getColor(R.color.white))
        }
        else {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.group2))
            holder.title.setTextColor(context.getResources().getColor(R.color.black))
            holder.content.setTextColor(context.getResources().getColor(R.color.black))
            holder.date.setTextColor(context.getResources().getColor(R.color.black))
            holder.assignby.setTextColor(context.getResources().getColor(R.color.black))
        }

        holder.card.setOnClickListener {
            var intent = Intent(context,showActivity::class.java)
            intent.putExtra("title",activities.get(position).title)
            intent.putExtra("content",activities.get(position).content)
            intent.putExtra("date",activities.get(position).date.toString())
           context.startActivity(intent)

        }







        holder.card.setOnLongClickListener{
            Log.i("BRO", activities.get(holder.adapterPosition).type.toString())
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white))
            holder.date.setTextColor(context.getResources().getColor(R.color.black))
            holder.content.setTextColor(context.getResources().getColor(R.color.black))
            holder.title.setTextColor(context.getResources().getColor(R.color.black))

            val popupMenu: PopupMenu = PopupMenu(context, holder.card)

            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            if(activities.get(holder.adapterPosition).type != "self"){
                popupMenu.menu.findItem(R.id.action_update).setEnabled(false)
                popupMenu.menu.findItem(R.id.action_share).setEnabled(false)
            }
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                when(item.itemId) {
                    R.id.action_update -> {
                        intent = Intent(context, updateActivity::class.java)
                        intent.putExtra("title", holder.title.text)
                        intent.putExtra("id", activities.get(holder.adapterPosition).id)
                        intent.putExtra("content", holder.content.text)

                        intent.putExtra("year", holder.date.text.substring(0,4).toInt())
                        intent.putExtra("month", holder.date.text.substring(5,7).toInt()-1)
                        intent.putExtra("day", holder.date.text.substring(8,10).toInt())
                        context.startActivity(intent)
                    }

                    R.id.action_remainder ->handleRemainder(holder.title.text.toString())

                    R.id.action_share ->
                    {
                        val dialog = BottomSheetDialog(context)
                        val inflater =
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                       val sp = context.getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)

                        val view = inflater.inflate(R.layout.bottom_sheet_share, null)

                        dialog.setContentView(view)
                        dialog.show()
                        val btnClose = view.findViewById<ImageButton>(R.id.imageButton)
                        val email = view.findViewById<EditText>(R.id.emailShare)


                        btnClose.setOnClickListener {

                                val call = retrofitUser.getUser(email.text.toString())
                                call.enqueue(object : Callback<User> {
                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    if (response.isSuccessful) {
                                        var new_activity: Activity =
                                            activities.get(holder.adapterPosition)
                                        var idArray = java.util.ArrayList<Long>();
                                        idArray.add(response.body()!!.id)
                                        val body: MutableMap<String, Any> = HashMap()
                                        body["title"] = new_activity.title!!
                                        body["content"] = new_activity.content!!
                                        body["userid"] = idArray
                                        body["date"] = new_activity.date!!
                                        body["assignedBy"] =
                                            sp.getString("email", "null").toString()
                                       val postCall = retrofitActivity.postActivity(body)
                                        postCall.enqueue(object:Callback<Activity>{
                                            override fun onResponse(
                                                call: Call<Activity>,
                                                response: Response<Activity>
                                            ) {
                                                Snackbar.make(layout,"Activity successfully shared.", Snackbar.LENGTH_LONG).setAction("close",{
                                                }).show()
                                            }

                                            override fun onFailure(
                                                call: Call<Activity>,
                                                t: Throwable
                                            ) {
                                                Snackbar.make(layout,"Activity share failed.", Snackbar.LENGTH_LONG).setAction("close",{
                                                }).show()
                                            }
                                        })

                                        dialog.dismiss()
                                    } else {
                                        Snackbar.make(layout,"User search failed.", Snackbar.LENGTH_LONG).setAction("close",{
                                        }).show()
                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    Snackbar.make(layout,"User not fount.", Snackbar.LENGTH_LONG).setAction("close",{
                                    }).show()
                                }


                            })
                        }

                    }


                }

                true
            })
            popupMenu.show()
            popupMenu.setOnDismissListener {


                if( holder.assignby.text == "self") {
                    holder.layout.setBackgroundColor(context.getResources().getColor(R.color.background))
                    holder.date.setTextColor(context.getResources().getColor(R.color.date))
                    holder.content.setTextColor(context.getResources().getColor(R.color.white))
                    holder.title.setTextColor(context.getResources().getColor(R.color.white))

                }  else if (activities.get(holder.adapterPosition).type == "group") {
                    holder.layout.setBackgroundColor(context.getResources().getColor(R.color.group))
                    holder.title.setTextColor(context.getResources().getColor(R.color.white))
                    holder.content.setTextColor(context.getResources().getColor(R.color.white))
                    holder.date.setTextColor(context.getResources().getColor(R.color.white))
                    holder.assignby.setTextColor(context.getResources().getColor(R.color.white))
                }
                else {
                    holder.layout.setBackgroundColor(context.getResources().getColor(R.color.group2))
                    holder.title.setTextColor(context.getResources().getColor(R.color.black))
                    holder.content.setTextColor(context.getResources().getColor(R.color.black))
                    holder.date.setTextColor(context.getResources().getColor(R.color.black))
                    holder.assignby.setTextColor(context.getResources().getColor(R.color.black))
                }

            }

            return@setOnLongClickListener true

        }
    }
    fun setActivity(list:ArrayList<Activity>){
        this.activities = list
        notifyDataSetChanged()


    }
    fun handleRemainder(text:String){

        val date = selectDate(text)







    }

    fun process(time:Any,date:Any,text:String){
        val intent = Intent(context.applicationContext,NotificationReceiver::class.java)
        intent.putExtra("text",text)
        val pendingIntent = PendingIntent.getBroadcast(context.applicationContext,100,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dateandtime = "$date $time"

            val formatter: DateFormat = SimpleDateFormat("d-M-yyyy hh:mm")
            val date1 = formatter.parse(dateandtime)
            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.time, pendingIntent)
            Toast.makeText(
                context.applicationContext,
                date1.time.toString(),
                Toast.LENGTH_SHORT
            ).show()




    }
    fun selectDate(text:String) {                                                                     //this method performs the date picker task
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        var date = ""
        DatePickerDialog(context, DatePickerDialog.OnDateSetListener{ view, year, month, day->
           date = "${day}-${month + 1}-${year}"
            selectTime(date,text)
        },year,month,day).show()

    }


    fun selectTime(date:Any,text: String){
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        var time = ""
        mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//                FormatTime(hourOfDay,minute)
                time = "${hourOfDay}:${minute}"
                process(time,date,text)

            }
        }, hour, minute, false)
        mTimePicker.show()

    }


}