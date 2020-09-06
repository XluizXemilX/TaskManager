package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth= FirebaseAuth.getInstance()

        login_btn.setOnClickListener {
            loginAccount()
        }
    }

    private fun loginAccount(){
        val email: String = email_login.text.toString()
        val password: String = password_login.text.toString()

        if(email =="")
        {
            email_login.error = "Email require"
            Toast.makeText(this@LoginActivity, "please write email.", Toast.LENGTH_SHORT).show() //checks if email is empty
        }
        else if(password=="")
        {
            password_login.error = "Password require"
            Toast.makeText(this@LoginActivity, "please enter password.", Toast.LENGTH_SHORT).show() //checks if email is empty
        }
        else{

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val currentAccountId = task.result!!.user!!.uid
                        SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_ACCOUNT, currentAccountId)
                        val intent =
                            Intent(this@LoginActivity, SwitchProfileActivity::class.java) // send user to create a house if task is completed "needs to be change to Switch Activity
                        startActivity(intent)

                    }
                    else
                    {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error Message: " + task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}