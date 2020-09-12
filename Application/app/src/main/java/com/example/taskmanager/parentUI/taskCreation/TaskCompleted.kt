package com.example.taskmanager.parentUI.taskCreation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import kotlinx.android.synthetic.main.activity_task_completed.*

class TaskCompleted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_completed)

        val task = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)
        task_completed_name.text = task.taskName
        //task_completed_picture.setImageBitmap()   // missing picture system
        task_completed_duedate.text = task.dueDate
        task_completed_repeats.text = "Repeats" + task.recurrence
        task_completed_repeats.text = "Assigned to" + task.assignUser

        task_completed_back_arrow.setOnClickListener {
            finish()
        }

        task_done_btn.setOnClickListener {
            markAsDone()
        }
    }

    private fun markAsDone(){
        val profile = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
        val account = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")
        val task = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)

        // needs to edit the status of the task and mark it as finished or completed and save it to the firebase
    }
}