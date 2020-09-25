package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_mainlogin.*

class MainLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainlogin)

        createHouse_btn.setOnClickListener {
            val intent =
                Intent(this@MainLoginActivity, RegisterActivity::class.java)  // when button press send user to  RegisterActivity
            startActivity(intent)


        }

        loginIntoHouse_btn.setOnClickListener {
            val intent =
                Intent(this@MainLoginActivity, LoginActivity::class.java)  // when button press send user to LoginActivity
            startActivity(intent)

        }
    }

}