package com.example.taskmanager.parentUI.taskCreation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.ImageUtils
import com.example.taskmanager.classes.SharedPrefsUtil
import kotlinx.android.synthetic.main.activity_detail_task.*

class DetailTaskActivity : AppCompatActivity() {

    private var imageData: ByteArray? = null

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

            if(intent.resolveActivity(this.packageManager)!= null){
                startActivityForResult(intent,Constants.REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera",Toast.LENGTH_SHORT).show()
            }
        }

        task_gallery_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                ) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    requestPermissions(permission, Constants.PERMISSION_CODE_READ)
                    requestPermissions(permissionCoarse, Constants.PERMISSION_CODE_WRITE)
                } else {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, Constants.REQUEST_CODE)
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var image = data?.extras?.get("data") as Bitmap
            imageData = ImageUtils.getImageBytes(image)
            image_task_detail.setImageBitmap(image)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
            intent.putExtra(Constants.TASK_IMAGE_DATA, imageData)
            startActivity(intent)
        }
    }
}