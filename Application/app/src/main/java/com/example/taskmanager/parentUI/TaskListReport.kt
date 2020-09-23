package com.example.taskmanager.parentUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.taskmanager.R
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_task_completed.*
import kotlinx.android.synthetic.main.activity_task_list_report.*

class TaskListReport : AppCompatActivity() {

    private var postListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list_report)

       setUpTaskList()
    }

    private fun setUpTaskList() {
        val currentReport = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_TASK_PROFILE, "")
        val listItemsIncomplete = arrayListOf<String>()
        val listItemsComplete = arrayListOf<String>()
        val listItemsFailed = arrayListOf<String>()
        if (postListener != null)
            return
        val refUsers = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("task")
        val taskRef = refUsers

        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    listItemsIncomplete.clear()
                    listItemsComplete.clear()
                    listItemsFailed.clear()
                    for (e in dataSnapshot.children){
                        val chore = e.getValue(Chore::class.java)
                        if(chore!!.assignUser == currentReport && chore.status == Constants.STATUS_INCOMPLETE) {
                            listItemsIncomplete.add(chore.taskName)
                        }
                        else if(chore.assignUser == currentReport && chore.status == Constants.STATUS_COMPLETE){
                            listItemsComplete.add(chore.taskName)
                        }
                        else if(chore.assignUser == currentReport && chore.status == Constants.STATUS_FAILED){
                            listItemsFailed.add(chore.taskName)
                        }
                    }

                    val adapterIncomplete = ArrayAdapter(this@TaskListReport, android.R.layout.simple_list_item_1, listItemsIncomplete)
                    task_upcoming_list.adapter = adapterIncomplete
                    val adapterComplete = ArrayAdapter(this@TaskListReport, android.R.layout.simple_list_item_1, listItemsComplete)
                    task_completed_list.adapter = adapterComplete
                    val adapterFailed = ArrayAdapter(this@TaskListReport, android.R.layout.simple_list_item_1, listItemsFailed)
                    task_failed_list.adapter = adapterFailed

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())

            }
        }
        taskRef.addValueEventListener(postListener as ValueEventListener)
    }
}