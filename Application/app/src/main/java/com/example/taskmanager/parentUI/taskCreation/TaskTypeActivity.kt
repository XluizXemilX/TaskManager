package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
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

            val intent =
                Intent(
                    this,
                    DetailTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }

        job_btn.setOnClickListener {
            //create task job firebase

            val intent =
                Intent(
                    this,
                    DetailTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }
    }
}