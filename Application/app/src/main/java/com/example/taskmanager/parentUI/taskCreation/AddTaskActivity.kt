package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        Add_task_button.setOnClickListener{
            val intent =
                Intent(
                    this@AddTaskActivity,
                    TaskTypeActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }

        back_arrow_task_creation.setOnClickListener {
            finish()
        }
    }
}