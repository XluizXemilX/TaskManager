package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import com.example.taskmanager.AddProfileActivity
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.parentUI.HomeActivity

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_recurrence.*

class TaskSettingsActivity : AppCompatActivity() {

    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_recurrence)

        val taskRef = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)

        back_arrow_task_settings.setOnClickListener {
            finish()
        }

        info_recurrence_btn.setOnClickListener {
            // needs to display hint
        }

        info_duedate_btn.setOnClickListener {
            // needs to display hint
        }

        info_pay_btn.setOnClickListener {
            // needs to display hint
        }

        info_photo_proof_btn.setOnClickListener {
            // needs to display hint
        }

        info_approval_btn.setOnClickListener {

        }
        task_reccurrence_btn.setOnClickListener {
            val fragmentManager = supportFragmentManager
            var sublimePickerDialogFragment = SublimePickerDialogFragment()
            var bundle = Bundle()
            // put arguments into bundle
            sublimePickerDialogFragment.arguments = bundle
            sublimePickerDialogFragment.isCancelable = false
            sublimePickerDialogFragment.show(fragmentManager,null)
            //sublimePickerDialogFragment.dismiss()
        }

        photo_switch_task_settings.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                taskRef.photoRequired = "true"
            }
            else if(!isChecked){
                taskRef.photoRequired = "false"
            }
        }

        approval_switch_task_settings.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                taskRef.verificationRequire = "true"
            }
            else if(!isChecked){
                taskRef.verificationRequire = "false"
            }
        }

        finish_task_btn.setOnClickListener {
            // finish updates the values of the task into firebase and send the user to home screen
            //val dueDate =
            //val recurrence =
            //val pay =

            val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")
            refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id)
                .child("task")

            val userHashMap = HashMap<String, Any>()  // holds user data
            userHashMap[Constants.CURRENT_ACCOUNT] = id
            userHashMap["taskName"] = taskRef.taskName
            userHashMap["assignUser"] = taskRef.assignUser
            userHashMap["type"] = taskRef.type
            userHashMap["dueDate"] = ""
            userHashMap["photoRequired"] = taskRef.photoRequired
            userHashMap["verificationRequire"] = taskRef.verificationRequire
            userHashMap["pay"] = ""
            userHashMap["picture"] = ""
            userHashMap["status"] = Constants.STATUS_INCOMPLETE
            userHashMap["description"] = taskRef.description
            val pushRef = refUsers.push()
            pushRef.setValue(userHashMap)
                .addOnSuccessListener {

                    val intent =
                        Intent(
                            this@TaskSettingsActivity,
                            HomeActivity::class.java
                        ) // send user to create a house if task is completed
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error Message: Something is wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }
}