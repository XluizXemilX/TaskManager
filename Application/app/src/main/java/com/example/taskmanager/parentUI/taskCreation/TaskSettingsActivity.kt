package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.AddProfileActivity
import com.example.taskmanager.R
import com.example.taskmanager.parentUI.HomeActivity
import kotlinx.android.synthetic.main.activity_task_recurrence.*

class TaskSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_recurrence)

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
            // needs to display hint
        }

        photo_switch_task_settings.setOnClickListener {
            // turn photo to true
        }

        approval_switch_task_settings.setOnClickListener {
            // turn approval to true
        }

        finish_task_btn.setOnClickListener {
            // finish updates the values of the task into firebase and send the user to home screen
            val intent =
                Intent(
                    this@TaskSettingsActivity,
                    HomeActivity::class.java
                ) // send user to home after task finished
            startActivity(intent)
        }
    }
}