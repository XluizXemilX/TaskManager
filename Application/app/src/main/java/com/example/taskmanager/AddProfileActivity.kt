package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Profile
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
        val profilePin: String = pin_addP_et.text.toString()

        if (nickname == "") {
            nickname_addP_et.error = "Nickname require"
            Toast.makeText(this@AddProfileActivity, "please enter Nickname.", Toast.LENGTH_SHORT)
                .show()
        } else if (profilePin == "") {
            pin_addP_et.error = "Pin require"
            Toast.makeText(this@AddProfileActivity, "please enter pin.", Toast.LENGTH_SHORT)
                .show()
        } else if(profilePin.length < 4){
            pin_addP_et.error = "Pin require"
            Toast.makeText(this@AddProfileActivity, "please a 4-digit pin.", Toast.LENGTH_SHORT)
                .show()

        } else {

            //firebaseUserId = mAuth.currentUser!!.uid
            val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT,"")
            refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users")

            val userHashMap = HashMap<String, String>()  // holds user data
            userHashMap[Constants.CURRENT_ACCOUNT] = id
            userHashMap["nickname"] = nickname
            userHashMap["profilePin"] = profilePin
            userHashMap["type"] = ""
            userHashMap["picture"]= "DEFAULT_USER_ICON"
            val pushRef = refUsers.push()
            val key = pushRef.key
            pushRef.setValue(userHashMap)

                .addOnSuccessListener {
                    //needs to save current user
                    val profile = Profile()
                    profile.fromMap(userHashMap)
                    profile.id = key
                    SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE, Profile::class.java, profile)
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
                        "Error Message: Lost connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}