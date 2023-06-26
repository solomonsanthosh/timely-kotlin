package com.example.timelynew.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.timelynew.R
import com.example.timelynew.databinding.ActivityAddBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream


import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*


class addActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddBinding
     var selectedDate: String? = null
    var path: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.hide()


        binding = DataBindingUtil.setContentView(this,R.layout.activity_add)
        binding.apply {



        datePicker.setOnClickListener {view ->
        clickDatePicker(view)

        }
        filepicker.setOnClickListener {



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

       add.setOnClickListener {

            var intent = Intent()
            intent.putExtra("title",binding.editText1.text.toString())
            intent.putExtra("content",binding.editText2.text.toString())
            intent.putExtra("date",selectedDate)

           intent.putExtra("file", path)
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
                    binding.filepicker.setText(path!!.substringAfterLast("/"))
                    // Use the file path as needed
                } else {
                    Log.e("AddActivity", "Failed to retrieve file path from URI")
                }
                // Perform operations on the document using its URI.
            }
        }
    }









//    fun uriToFilePath(uri: Uri): String? {
//        val projection = arrayOf(MediaStore.MediaColumns.DATA)
//        val cursor = this.contentResolver.query(uri, projection, null, null, null)
//        return if (cursor != null && cursor.moveToFirst()) {
//            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//            val path = cursor.getString(columnIndex)
//            cursor.close()
//            path
//        } else {
//            null
//        }
//
//    }
//    fun uriToFilePath(uri: Uri): String? {
//        val context = applicationContext
//        val contentResolver = context.contentResolver
//
//        // Check if the Uri uses the "file" scheme
//        if (ContentResolver.SCHEME_FILE == uri.scheme) {
//            return uri.path
//        }
//
//        // Check if the Uri uses the "content" scheme
//        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
//            val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
//            val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
//            cursor?.use {
//                if (it.moveToFirst()) {
//                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//                    return it.getString(columnIndex)
//                }
//            }
//        }
//
//        return null
//    }


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



}
