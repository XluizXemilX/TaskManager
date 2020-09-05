package com.example.taskmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_first_profile.*

class FirstProfileActivity : AppCompatActivity() {


    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_profile)

        //mAuth = FirebaseAuth.getInstance()

        finish_btn.setOnClickListener {
            createProfile()
        }

        first_profile_chage_pic.setOnClickListener {
            pictureSelection()
        }
    }

    private fun createProfile() {
        val nickname: String = houseName_register.text.toString().toLowerCase()
        val profilePin: String = housePassword_register.text.toString()

        if (nickname == "") {
            Toast.makeText(this@FirstProfileActivity, "please write Nickname.", Toast.LENGTH_SHORT)// checks for input
                .show()
        } else if (profilePin == "") {
            Toast.makeText(this@FirstProfileActivity, "please write Profile pin.", Toast.LENGTH_SHORT)//checks for input
                .show()
        } else {

            //firebaseUserId = mAuth.currentUser!!.uid
            val id = SharedPrefsUtil.getInstance(this).get("accountId","")
            refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users")

            val userHashMap = HashMap<String, String>()  // holds user data
            userHashMap["accountId"] = id
            userHashMap["nickname"] = nickname
            userHashMap["profilePin"] = profilePin
            userHashMap["type"] = "Parent"
            userHashMap["picture"] = "DEFAULT_USER_ICON"
            val pushRef = refUsers.push()
            val key = pushRef.key
            pushRef.setValue(userHashMap)

                .addOnSuccessListener {
                    //needs to save current user
                    val profile = Profile()
                    profile.fromMap(userHashMap)
                    profile.id = key
                    SharedPrefsUtil.getInstance(this).put("CURRENT_PROFILE", Profile::class.java, profile)
                    val intent =
                        Intent(
                            this@FirstProfileActivity,
                            HomeActivity::class.java
                        ) // send user to create a house if task is completed
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Toast.makeText(
                           this@FirstProfileActivity,
                           "Error Message: Lost connection",
                           Toast.LENGTH_SHORT
                       ).show()
                }
        }
    }

    private fun pictureSelection(){
        val dialogView: View = LayoutInflater.from(this@FirstProfileActivity).inflate(R.layout.task_box, null) // dialog box

    }

}