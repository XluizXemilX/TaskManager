package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.example.taskmanager.parentUI.HomeActivity
import com.google.android.material.slider.Slider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_recurrence.*
import java.lang.Exception


class TaskSettingsActivity : AppCompatActivity() , SublimePickerDialogFragment.IRecurrenceTask {

    private lateinit var refUsers: DatabaseReference
    private lateinit  var taskRef: Chore
    private lateinit var alertDialog: AlertDialog
    private var imageData: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_recurrence)

        imageData =intent.getByteArrayExtra(Constants.TASK_IMAGE_DATA)
        taskRef = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)

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
            sublimePickerDialogFragment.isCancelable = true
            sublimePickerDialogFragment.recurrenceListener = this
            sublimePickerDialogFragment.show(fragmentManager,null)

            //sublimePickerDialogFragment.dismiss()
        }
        task_due_date_btn.setOnClickListener {
            val fragmentManager = supportFragmentManager
            var sublimePickerDialogFragment = SublimePickerDialogFragment()
            var bundle = Bundle()
            // put arguments into bundle


            sublimePickerDialogFragment.arguments = bundle
            sublimePickerDialogFragment.isCancelable = true
            sublimePickerDialogFragment.recurrenceListener = this
            sublimePickerDialogFragment.show(fragmentManager,null)
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

            if (imageData !=null) {
                ImageUtils.uploadTaskImageToFirebase(imageData, object : ImageUtils.IImageUploaded {
                    override fun onSuccess(uri: String?) {
                        saveTask(uri)
                    }

                    override fun onFailure(ex: Exception?) {
                        Toast.makeText(
                            this@TaskSettingsActivity,
                            ex!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            } else {
                saveTask(null)
            }
        }
    }

    private fun saveTask(imageUrl: String?){
        val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")
        val pay = task_pay_et?.text.toString()

        refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id)
            .child("task")

        if( task_pay_et!= null)
            taskRef.pay = pay
        if(TextUtils.isEmpty(taskRef.recurrence) || TextUtils.isEmpty(taskRef.time)) {
            Toast.makeText(this, "Select a recurrence", Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(taskRef.dueDate) || TextUtils.isEmpty(taskRef.startDate)) {
            Toast.makeText(this, "Select a due date", Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(taskRef.pay) && taskRef.type == Constants.TYPE_JOB){
            Toast.makeText(this, "Enter a payment amount", Toast.LENGTH_SHORT).show()
            return
        }
        val userHashMap = HashMap<String, String?>()  // holds user data
        userHashMap[Constants.CURRENT_ACCOUNT] = id
        userHashMap["taskName"] = taskRef.taskName
        userHashMap["assignUser"] = taskRef.assignUser
        userHashMap["type"] = taskRef.type
        userHashMap["dueDate"] = taskRef.dueDate
        userHashMap["photoRequired"] = taskRef.photoRequired
        userHashMap["verificationRequire"] = taskRef.verificationRequire
        if(!TextUtils.isEmpty(taskRef.pay)) {
            userHashMap["pay"] = taskRef.pay
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            userHashMap["picture"] = imageUrl
        }
        userHashMap["status"] = Constants.STATUS_INCOMPLETE
        userHashMap["description"] = taskRef.description
        userHashMap["recurrence"] = taskRef.recurrence
        userHashMap["time"] = taskRef.time
        userHashMap["startDate"] = taskRef.startDate
        userHashMap["userPhoto"] = taskRef.userPhoto
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

    override fun onRecurrenceSet(selectedDate: SelectedDate?, hourOfDay: Int, minute: Int, recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?) {
        taskRef.recurrence = recurrenceOption.toString()
        taskRef.dueDate= DateUtils.formatDate(selectedDate?.secondDate?.time)
        taskRef.startDate = DateUtils.formatDate(selectedDate?.firstDate?.time)
        var hour: String
        var sum:Int
        if(hourOfDay > 12){
            sum = hourOfDay - 12
            hour = sum.toString() + "PM"
            taskRef.time = sum.toString() + ":" + minute.toString() + " PM"
        }
        else {
            hour = hourOfDay.toString() + "AM"
            taskRef.time = hourOfDay.toString() + ":" + minute.toString() + " AM"
        }
        task_reccurrence_btn.text = taskRef.recurrence + " @" + hour
        task_due_date_btn.text = taskRef.dueDate

        SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_TASK, Chore::class.java, taskRef)
    }
}