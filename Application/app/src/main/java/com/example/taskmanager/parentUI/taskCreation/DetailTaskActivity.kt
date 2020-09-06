package com.example.taskmanager.parentUI.taskCreation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.SharedPrefsUtil
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.io.File

private lateinit var photoFile: File
class DetailTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        task_detail_back_arrow.setOnClickListener {
            finish()
        }

        task_detail_next_btn.setOnClickListener {
            //adds more information to the task created before
            setTaskInfo()

        }

        task_camera_btn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

//            photoFile = getPhotoFile(Constants.FILE_NAME_PHOTO)
//
//            val fileProvider = FileProvider.getUriForFile(this, "com.example.taskmanager.parentUI.taskCreation.fileprovider",
//                photoFile)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if(intent.resolveActivity(this.packageManager)!= null){
                startActivityForResult(intent,Constants.REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera",Toast.LENGTH_SHORT).show()
            }
        }

        task_gallery_btn.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var image = data?.extras?.get("data") as Bitmap
            image_task_detail.setImageBitmap(image)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getPhotoFile(filename: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg",storageDirectory)
    }

    private fun setTaskInfo(){
        val task = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)
        val name = task_name_editText.text.toString()
        val description = task_description_et.text.toString()
        //val picture
        if(name.isEmpty()){
            task_name_editText.error = "Name require"
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
        }
        else {
            task.taskName = name
            task.description = description
            //task.picture = picture
            SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_TASK, Chore::class.java, task)
            val intent =
                Intent(
                    this,
                    AssignTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }

    }
}