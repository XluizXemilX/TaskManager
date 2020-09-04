package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskmanager.parentUI.HomeActivity
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_profile.*

class AddProfileActivity : AppCompatActivity() {

    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        addP_btn.setOnClickListener {
            createProfile()
        }
    }

    private fun createProfile() {
        val nickname: String = nickname_addP_et.text.toString().toLowerCase()
        val userPin: String = pin_addP_et.text.toString()

        if (nickname == "") {
            Toast.makeText(this@AddProfileActivity, "please write House Name.", Toast.LENGTH_SHORT)
                .show()
        } else if (userPin == "") {
            Toast.makeText(this@AddProfileActivity, "please write House password.", Toast.LENGTH_SHORT)
                .show()
        } else {

            //firebaseUserId = mAuth.currentUser!!.uid
            val id = SharedPrefsUtil.getInstance(this).get("accountId","")
            refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users")

            val userHashMap = HashMap<String, Any>()  // holds user data
            userHashMap["accountId"] = id
            userHashMap["nickname"] = nickname
            userHashMap["userPin"] = userPin
            userHashMap["type"] = "parent"
            userHashMap["picture"]= R.drawable.pngtreevector_users_icon_4144740
            refUsers.push().setValue(userHashMap)
                .addOnSuccessListener {
                    val intent =
                        Intent(
                            this@AddProfileActivity,
                            HomeActivity::class.java
                        ) // send user to create a house if task is completed
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@AddProfileActivity,
                        "Error Message: Something is wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}