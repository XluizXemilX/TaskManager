package com.example.taskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_create_house.*

class CreateHouseActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_house)

        mAuth = FirebaseAuth.getInstance()

        finish_btn.setOnClickListener {
            createHouse()
        }
    }

    private fun createHouse(){
        val houseName: String = houseName_register.text.toString().toLowerCase()
        val housePassword: String = housePassword_register.text.toString()

        if(houseName == ""){
            Toast.makeText(this@CreateHouseActivity, "please write House Name.", Toast.LENGTH_SHORT)
                .show()
        }
        else if(housePassword == ""){
            Toast.makeText(this@CreateHouseActivity, "please write House password.", Toast.LENGTH_SHORT)
                .show()
        }
        else {

        }
    }
}