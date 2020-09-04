package com.example.taskmanager.parentMenuDrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.R
import kotlinx.android.synthetic.main.activity_bank_account.*

class BankAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)

        back_arrow_bank.setOnClickListener{
            finish()
        }
    }
}