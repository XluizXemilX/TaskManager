package com.example.taskmanager.parentMenuDrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        back_profile_btn.setOnClickListener{
            finish()
        }
    }
}