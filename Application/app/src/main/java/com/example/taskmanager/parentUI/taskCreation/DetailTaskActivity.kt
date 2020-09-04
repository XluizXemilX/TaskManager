package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import kotlinx.android.synthetic.main.activity_detail_task.*

class DetailTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        task_detail_back_arrow.setOnClickListener {
            finish()
        }

        task_detail_next_btn.setOnClickListener {
            //adds more information to the task created before
            val intent =
                Intent(
                    this,
                    AssignTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }
    }
}