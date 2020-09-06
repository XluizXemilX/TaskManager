package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_type.*

class TaskTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_type)


        back_arrow_task_type.setOnClickListener {
            finish()
        }

        chore_btn.setOnClickListener {
          //create chore firebase
            createChore()

        }

        job_btn.setOnClickListener {
            //create task job firebase
            createJob()
            val intent =
                Intent(
                    this,
                    DetailTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }
    }

    private fun createChore(){
        val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")
        val task = Chore()
        task.accountId = id
        task.type = Constants.TYPE_CHORE
        SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_TASK,Chore::class.java, task)
        val intent =
            Intent(this, DetailTaskActivity::class.java)
        startActivity(intent)
    }

    private fun createJob(){
        val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")
        val task = Chore()
        task.accountId = id
        task.type = Constants.TYPE_JOB
        SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_TASK,Chore::class.java, task)
        val intent =
            Intent(this, DetailTaskActivity::class.java)
        startActivity(intent)
    }
}