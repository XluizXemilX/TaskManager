package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_type.*

class TaskTypeActivity : AppCompatActivity() {

    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_type)


        back_arrow_task_type.setOnClickListener {
            finish()
        }

        chore_btn.setOnClickListener {
          //create chore firebase
            createChore()
            val intent =
                Intent(
                    this,
                    DetailTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }

        job_btn.setOnClickListener {
            //create task job firebase

            val intent =
                Intent(
                    this,
                    DetailTaskActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
        }
    }

    private fun createChore(){
        val id = SharedPrefsUtil.getInstance(this).get("accountId", "")
        refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id)
            .child("task")
        val userHashMap = HashMap<String, String>()
        userHashMap["accountId"] = id
        userHashMap["taskName"] = ""
        userHashMap["assignUser"] = ""
        userHashMap["type"] = "Chore"
        userHashMap["dueDate"] = ""
        userHashMap["photoRequired"] = ""
        userHashMap["verificationRequire"] = ""
        userHashMap["picture"] = ""
        val pushRef = refUsers.push()
        val key = pushRef.key
        pushRef.setValue(userHashMap)
            .addOnSuccessListener {
                val task = Chore()
                //task.
                //task.id = key

                val intent =
                    Intent(this, DetailTaskActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error Message: Lost connection", Toast.LENGTH_SHORT).show()
            }
    }
}