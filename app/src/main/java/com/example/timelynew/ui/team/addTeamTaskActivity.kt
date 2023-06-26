package com.example.timelynew.ui.team


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.api.ActivityService
import com.example.timelynew.api.GroupService
import com.example.timelynew.api.UserService
import com.example.timelynew.dataClass.Group
import com.example.timelynew.dataClass.User
import com.example.timelynew.databinding.ActivityAddTeamTaskBinding
import com.example.timelynew.retrofit.RetrofitInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class addTeamTaskActivity : AppCompatActivity() {


    lateinit var listView: ListView
     var selectedDate:String? = null
    lateinit var binding:ActivityAddTeamTaskBinding
    private lateinit var retrofitService : UserService
    private lateinit var retrofitGroup:GroupService
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var grpList:ArrayList<String>
    private lateinit var grpMembersData:ArrayList<Group>
    var path:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team_task)
        supportActionBar?.hide()
        sharedPreferences = this.getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)
        retrofitGroup = RetrofitInstance.getInstance().create(GroupService::class.java)
         binding = DataBindingUtil.setContentView(this,R.layout.activity_add_team_task)
        retrofitService = RetrofitInstance.getInstance().create(UserService::class.java)
        val dialog = BottomSheetDialog(this)
        val inflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        grpList = ArrayList()
        grpMembersData = ArrayList()
        val view = inflater.inflate(R.layout.group_layout, null)
        val list = view.findViewById<ListView>(R.id.grpListView)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,grpList)


        val groupCall = retrofitGroup.getGroup(sharedPreferences.getLong("id",1))
        groupCall.enqueue(object :Callback<List<Group>>{
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if(response.isSuccessful){
                    grpMembersData = (response.body() as ArrayList<Group>?)!!
                   val iterator = response.body()!!.iterator()
                    while (iterator.hasNext()){
                        grpList.add(iterator.next().title)

                        adapter.notifyDataSetChanged()
                    }




                } else {
                    Snackbar.make( binding.addTeamTaskLayout,"Please try again...", Snackbar.LENGTH_LONG).setAction("close",{
                    }).show()
                }

            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Snackbar.make( binding.addTeamTaskLayout,"Please try again...", Snackbar.LENGTH_LONG).setAction("close",{
                }).show()
            }

        })

        var arrayList:ArrayList<String> = ArrayList<String>()
        var arrayList2:ArrayList<Int> = ArrayList<Int>()
        listView = binding.listView
        var arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList)
        listView.adapter = arrayAdapter




        binding.addMemberBtn.setOnClickListener {

            val call = retrofitService.getUser(binding.memberEdit.text.toString())
            call.enqueue(object :Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        Snackbar.make( binding.addTeamTaskLayout ,"Member added", Snackbar.LENGTH_SHORT).setAction("close",{
                        }).show()
                        arrayList.add(binding.memberEdit.text.toString())
                        arrayList2.add(response.body()!!.id.toInt())
                        arrayAdapter.notifyDataSetChanged()

                    } else {
                        Snackbar.make( binding.addTeamTaskLayout ,"Failed to add member or member does not exist", Snackbar.LENGTH_SHORT).setAction("close",{
                        }).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Snackbar.make( binding.addTeamTaskLayout ,"Failed to add member or member does not exist", Snackbar.LENGTH_SHORT).setAction("close",{
                    }).show()
                }


            })

        }

        binding.addGroupToList.setOnClickListener {
            list.adapter = adapter

            dialog.setContentView(view)
            dialog.show()
        }

        binding.filePickerTeam.setOnClickListener {



            val PICK_PDF_FILE = 2

            fun openFile(pickerInitialUri: Uri) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"

                    // Optionally, specify a URI for the file that should appear in the
                    // system file picker when it loads.
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
                }

                startActivityForResult(intent, PICK_PDF_FILE)
            }
            openFile(Uri.parse("Android/data/"))

        }


        list.setOnItemClickListener {parent, view, postion, id ->
            grpMembersData.get(postion).members.forEach { member ->
                arrayList.add(member)
            }
            grpMembersData.get(postion).members_id.forEach { id ->
                arrayList2.add(id.toInt())
            }

            arrayAdapter.notifyDataSetChanged()
            dialog.dismiss()

        }
        listView.setOnItemClickListener { parent, view, postion, id ->

            arrayList.removeAt(postion)
            arrayList2.removeAt(postion)
            arrayAdapter.notifyDataSetChanged()
        }


        binding.datePicker2.setOnClickListener {view ->
            clickDatePicker(view)

        }
        binding.add.setOnClickListener {
            var intent = Intent()
            intent.putExtra("title",binding.editTitle.text.toString())
            intent.putExtra("content",binding.editContent.text.toString())
            intent.putExtra("date",selectedDate)
            intent.putExtra("file",path)
            intent.putIntegerArrayListExtra("members",arrayList2)
            intent.putStringArrayListExtra("membersName",arrayList)
            setResult(RESULT_OK,intent)
            finish()
            Snackbar.make( binding.addTeamTaskLayout,arrayList2.toString(), Snackbar.LENGTH_LONG).setAction("close",{
            }).show()
        }




    }
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 2
            && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->

                val filePath = getFilePathFromUri(this, uri)
                if (filePath != null) {
                    path = filePath
                    binding.filePickerTeam.setText(path!!.substringAfterLast("/"))
                    // Use the file path as needed
                } else {
                    Log.e("AddActivity", "Failed to retrieve file path from URI")
                }
                // Perform operations on the document using its URI.
            }
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val contentResolver = context.contentResolver

        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val fileName = it.getString(columnIndex)

                val inputStream = contentResolver.openInputStream(uri)
                inputStream?.let { input ->
                    val file = File(context.cacheDir, fileName)
                    val outputStream = FileOutputStream(file)
                    outputStream.use { output ->
                        val buffer = ByteArray(4 * 1024) // 4KB buffer size
                        var read: Int
                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                        }
                        output.flush()
                    }
                    filePath = file.absolutePath
                }
            }
        }

        return filePath
    }

    fun clickDatePicker(view: View){
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, year, month, day->



            selectedDate = "$day/${month+1}/$year"

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = sdf.parse(selectedDate)

            binding.datePicker2.text = date.toString()
        },year,month,day).show()
    }
}