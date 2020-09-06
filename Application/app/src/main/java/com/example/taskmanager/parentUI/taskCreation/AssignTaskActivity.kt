package com.example.taskmanager.parentUI.taskCreation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_assign_task.*

class AssignTaskActivity : AppCompatActivity(),
    GenericRecyclerAdapter.GenericRecyclerListener<Profile> {

    private lateinit var refUsers: DatabaseReference

    private var postListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_task)

        back_assign_task_btn.setOnClickListener {
            finish()
        }

        free_for_all_tv.setOnClickListener{
            //leaves the assign as empty or all(only works with jobs)
            //if(task.type = "job")
            val task = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)
            task.assignUser = ""
            SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_TASK, Chore::class.java, task)
            val intent =
                Intent(
                    this@AssignTaskActivity,
                    TaskSettingsActivity::class.java
                ) // send user to finish the task creation
            startActivity(intent)
        }


        val listProfiles = ArrayList<Profile>()
        refUsers = FirebaseDatabase.getInstance().reference.child("account").child(SharedPrefsUtil.getInstance(this).get("accountId", "")).child("users")
        val profileRef = refUsers
        val thisContext =  this
        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    listProfiles.clear()
                    for (e in dataSnapshot.children) {
                        val profile = e.getValue(Profile::class.java)
                        profile!!.id = e.key
                        listProfiles.add(profile)
                    }
                    assign_task_recyclerview.layoutManager = GridLayoutManager(this@AssignTaskActivity, 2);
                    var adapter = GenericRecyclerAdapter(listProfiles, R.layout.row_profile_layout)
                    adapter.listener = thisContext
                    assign_task_recyclerview.adapter = adapter

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
            }

        }
        profileRef.addListenerForSingleValueEvent(postListener as ValueEventListener)
    }

    override fun onClick(profile: Profile?) {

        // needs to select profile and set it for the task
        val task = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK, Chore::class.java, null)
        task.assignUser = profile!!.nickname
        SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_TASK, Chore::class.java, task)
        val intent =
            Intent(
                this@AssignTaskActivity,
                TaskSettingsActivity::class.java
            ) // send user to finish the task creation
        startActivity(intent)
    }
}