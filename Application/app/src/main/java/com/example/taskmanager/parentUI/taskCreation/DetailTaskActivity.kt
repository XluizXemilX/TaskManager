package com.example.taskmanager.parentUI.taskCreation

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.ImageUtils
import com.example.taskmanager.classes.SharedPrefsUtil
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.io.File


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
                startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    intent.type = "image/*"
                    intent.flags =
                        FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION //must for reading data from directory
                    startActivityForResult(intent, Constants.REQUEST_CODE_GALLERY)
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK){
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = this.contentResolver.query(data!!.data!!, filePathColumn, null, null, null)
            if (cursor == null || cursor.count < 1) {
                return
            }
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            if (columnIndex >= 0) {
                val bitmap = BitmapFactory.decodeFile(cursor.getString(columnIndex))
                image_task_detail.setImageBitmap(bitmap)
                imageData = ImageUtils.getImageBytes(bitmap)
            }

            cursor.close()
        }
        else if(requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK){
            var image = data?.extras?.get("data") as Bitmap
            imageData = ImageUtils.getImageBytes(image)
            image_task_detail.setImageBitmap(image)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setTaskInfo(){
        val task = SharedPrefsUtil.getInstance(this).get(
            Constants.CURRENT_TASK,
            Chore::class.java,
            null
        )
        val name = task_name_editText.text.toString()
        val description = task_description_et.text.toString()

        if(name.isEmpty()){
            task_name_editText.error = "Name require"
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
        }
        else {
            task.taskName = name
            task.description = description

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