package com.example.taskmanager.parentUI.taskCreation

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_task.*
import kotlinx.android.synthetic.main.activity_task_completed.*
import java.lang.Exception

class TaskCompleted : AppCompatActivity() {

    private var imageData: ByteArray? = null
    val profile: Profile = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
    val task: Chore = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_completed)

        task_completed_name.text = task.taskName
        DataBindingAdapters.setImageUri(task_completed_picture, task.picture)
        task_completed_duedate.text = task.dueDate
        task_completed_repeats.text = "Repeats " + task.recurrence
        task_completed_assigned.text = "Assigned to " + task.assignUser

        task_completed_back_arrow.setOnClickListener {
            finish()
        }

        task_done_btn.setOnClickListener {
            if(task.assignUser == profile.nickname) {
                if (imageData != null) {
                    ImageUtils.uploadTaskImageToFirebase(
                        imageData,
                        object : ImageUtils.IImageUploaded {
                            override fun onSuccess(uri: String?) {
                                markAsDone(uri)
                            }

                            override fun onFailure(ex: Exception?) {
                                Toast.makeText(
                                    this@TaskCompleted,
                                    ex!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                } else {
                    markAsDone(null)
                }
            }
            else{
                Toast.makeText(this, "This task is not assign to you.", Toast.LENGTH_LONG).show()
            }
        }
        if(task.photoRequired == "false"){
            task_camera_btn_completed.visibility = View.GONE
        }
        task_camera_btn_completed.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(intent.resolveActivity(this.packageManager)!= null){
                startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK){
            var image = data?.extras?.get("data") as Bitmap
            imageData = ImageUtils.getImageBytes(image)
            picture_proof.setImageBitmap(image)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun markAsDone(imageUrl: String?){
        val account = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")
        val firebaseRef = FirebaseDatabase.getInstance().reference.child("account").child(account).child("task").child(task.id)

        // needs to edit the status of the task and mark it as finished or completed and save it to the firebase
        if(profile.nickname == task.assignUser){
            val userHashMap = HashMap<String, String?>()  // holds user data

            userHashMap["status"] = Constants.STATUS_COMPLETE
            if (!TextUtils.isEmpty(imageUrl)) {
                userHashMap["pictureProof"] = imageUrl
            }

            firebaseRef.updateChildren(userHashMap.toMap())

                .addOnSuccessListener {

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }.addOnFailureListener {
                    Toast.makeText(
                        this@TaskCompleted,
                        "Error Message: Could not complete the action, try again or later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}