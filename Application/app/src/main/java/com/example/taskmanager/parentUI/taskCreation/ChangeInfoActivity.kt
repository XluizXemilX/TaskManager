package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.LoginActivity
import com.example.taskmanager.R
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.change_email_layout.*
import kotlinx.android.synthetic.main.change_password_layout.*
import kotlinx.android.synthetic.main.change_profile_pin_layout.*


class ChangeInfoActivity : AppCompatActivity() {

    lateinit var refProfile: Profile
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parameters = intent.extras
        if (parameters != null && parameters.containsKey("password")){
            setContentView(parameters.getInt("password"))
            save_password.setOnClickListener {
                savePassword()
            }

            change_password_back_arrow.setOnClickListener {
                finish()
            }
        }
        else if(parameters != null && parameters.containsKey("email")){
            setContentView(parameters.getInt("email"))
            save_email.setOnClickListener {
                saveEmail()
            }

            change_email_back_arrow.setOnClickListener {
                finish()
            }
        }
        else {
            setContentView(R.layout.change_profile_pin_layout)
            save_pin.setOnClickListener {
                savePin()
            }

            change_pin_back_arrow.setOnClickListener {
                finish()
            }
        }

        mAuth = FirebaseAuth.getInstance()
        refProfile = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_PROFILE, Profile::class.java, null)




    }

    private fun savePin(){
        val oldPin = old_pin.text.toString()
        val newPin = new_pin.text.toString()
        val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT,"")

        val firebaseRef = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users").child(refProfile.id)
        if(TextUtils.isEmpty(oldPin)){
            old_pin.error = "Old pin require"
        }
        else if(oldPin != refProfile.profilePin){
            old_pin.error = "Wrong pin"
        }
        else if(TextUtils.isEmpty(newPin)){
            new_pin.error = "New pin require"
        }
        else if(oldPin == newPin){
            Toast.makeText(this, "New pin cannot be the same as old pin.", Toast.LENGTH_LONG).show()
        }
        else{
            val userHashMap = HashMap<String, String>()  // holds user data

            userHashMap["profilePin"] = newPin

            firebaseRef.updateChildren(userHashMap.toMap())

                .addOnSuccessListener {
                    refProfile.profilePin = newPin
                    SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE, Profile::class.java, refProfile)
                    Toast.makeText(this, "Changes save.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }.addOnFailureListener {
                    Toast.makeText(
                        this@ChangeInfoActivity,
                        "Error Message: Could not complete the action, try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun saveEmail(){
        val oldEmail = old_email.text.toString()
        val newEmail = new_email.text.toString()
        val user = mAuth.currentUser
        val account = FirebaseDatabase.getInstance().reference.child("account").child(user!!.uid)
        if(TextUtils.isEmpty(oldEmail)){
            old_email.error = "Old email require"
        }
        else if(oldEmail != user!!.email){
            old_email.error = "Wrong email"
        }
        else if(TextUtils.isEmpty(newEmail)){
            new_email.error = "New email require"
        }
        else if(oldEmail == newEmail){
            Toast.makeText(this, "New email cannot be the same as old pin.", Toast.LENGTH_LONG).show()
        }
        else {
            user!!.updateEmail(newEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userHashMap = HashMap<String, String>()

                        userHashMap["email"] = newEmail

                        account.updateChildren(userHashMap.toMap())
                            .addOnSuccessListener {

                                Toast.makeText(this, "Changes save.", Toast.LENGTH_LONG).show()
                                FirebaseAuth.getInstance().signOut()
                                startActivity(Intent(this, LoginActivity::class.java))

                            }.addOnFailureListener {
                                Toast.makeText(
                                    this@ChangeInfoActivity,
                                    "Error Message: Could not complete the action, try again later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }



                    }
                }
        }
    }

    private fun savePassword(){
        val newPassword = new_password.text.toString()
        val confirmPassword = confirm_password.text.toString()
        val user = mAuth.currentUser
        val account = FirebaseDatabase.getInstance().reference.child("account").child(user!!.uid)
        if(TextUtils.isEmpty(newPassword)){
            new_password.error = "New password require"
        }
        else if(newPassword.length < 6){
            new_password.error = "New password requires 6-15 characters."
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            confirm_password.error = "New password require"
        }
        else if(newPassword != confirmPassword){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
        }
        else {
            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userHashMap = HashMap<String, String>()

                        userHashMap["password"] = newPassword

                        account.updateChildren(userHashMap.toMap())
                            .addOnSuccessListener {

                                Toast.makeText(this, "Changes save.", Toast.LENGTH_LONG).show()

                            }.addOnFailureListener {
                                Toast.makeText(
                                    this@ChangeInfoActivity,
                                    "Error Message: Could not complete the action, try again later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }
                }
        }
    }
}