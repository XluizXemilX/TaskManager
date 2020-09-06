package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.pin_validation.view.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(this)


        mAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val firstName: String = firstName_register.text.toString() // firstname editext value
        val lastName: String = lastName_register.text.toString()    // last name edittext value
        val email: String = email_register.text.toString()          //email editext value
        val password: String = password_register.text.toString()    //password edittext value
        val dob: String = DOB_register.text.toString()              // date of birth edittext value

        if (firstName == "") {
            firstName_register.error = "First Name require"
            Toast.makeText(this@RegisterActivity, "please write First Name.", Toast.LENGTH_SHORT) // checks if first name is empty
                .show()
        } else if (lastName == "") {
            lastName_register.error = "LastName require"
            Toast.makeText(this@RegisterActivity, "please write Last Name.", Toast.LENGTH_SHORT) // checks if last name is empty
                .show()
        } else if (email == "") {
            email_register.error = "Email require"
            Toast.makeText(this@RegisterActivity, "please write email.", Toast.LENGTH_SHORT).show() //checks if email is empty
        } else if (password == "") {
            password_register.error = "Password require"
            Toast.makeText(this@RegisterActivity, "please write password", Toast.LENGTH_SHORT) //checks if password is empty
                .show()
        } else if (dob == "") {
            DOB_register.error = "Date of birth require"
            Toast.makeText(this@RegisterActivity, "please enter Date of Birth", Toast.LENGTH_SHORT) //checks if date of birth is empty
                .show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)  // creates user
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUserId = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("account").child(firebaseUserId)

                        val userHashMap = HashMap<String, Any>()  // holds user data
                        userHashMap["user"] = firstName + "_" +firebaseUserId
                        userHashMap["firstname"] = firstName
                        userHashMap["lastname"] = lastName
                        userHashMap["email"] = email
                        userHashMap["password"] = password
                        userHashMap["dob"] = dob

                        SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_ACCOUNT, firebaseUserId)
                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent =
                                        Intent(this@RegisterActivity, FirstProfileActivity::class.java) // send user to create a house if task is completed
                                    startActivity(intent)

                                }
                                else{

                                }
                            }
                    }
                    else{
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error Message: " + task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }
}
