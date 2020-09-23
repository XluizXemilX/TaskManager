package com.example.taskmanager.parentMenuDrawer.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_manage_house.*
import kotlinx.android.synthetic.main.activity_switch_accounts.*
import kotlinx.android.synthetic.main.clear_chat_layout.view.*

class ManageHouse : AppCompatActivity(), GenericRecyclerAdapter.GenericRecyclerListener<Profile> {

    private lateinit var refUsers: DatabaseReference
    private var postListener: ValueEventListener? = null
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_house)

        house_member_back_arrow.setOnClickListener {
            finish()
        }

        val listProfiles = ArrayList<Profile>()
        refUsers = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("users")
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
                    manage_house_recycler.layoutManager = GridLayoutManager(this@ManageHouse, 2);
                    var adapter = GenericRecyclerAdapter(listProfiles, R.layout.row_profile_layout)
                    adapter.listener = thisContext
                    manage_house_recycler.adapter = adapter

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
            }

        }
        profileRef.addListenerForSingleValueEvent(postListener as ValueEventListener)

    }

    override fun onClick(d: Profile?) {
        deleteMember(d)
    }

    private fun deleteMember(profile: Profile?){
        val refProfile = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("users").child(profile!!.id)
        val refTask = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("task")
        val refMessages =FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("messages")
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.clear_chat_layout, null)
        dialogView.textView43.text = "You are about to delete this profile and anything related to it!"
        dialogView.dialog_cancel_btn.setOnClickListener { // if task creation is cancel
            alertDialog.cancel()
        }
        dialogView.dialog_ok_btn.setOnClickListener {
            postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){

                        for (e in dataSnapshot.children) {
                            val task = e.getValue(Chore::class.java)
                            if(task!!.assignUser == profile.nickname){
                                task.id = e.key
                                refTask.child(task.id).removeValue()
                            }
                        }

                        for (e in dataSnapshot.children) {
                            val message = e.getValue(Message::class.java)
                            if(message!!.name == profile.nickname){
                                message.id = e.key
                                refMessages.child(message.id).removeValue()
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
                }

            }
            refTask.addListenerForSingleValueEvent(postListener as ValueEventListener)
            refMessages.addListenerForSingleValueEvent(postListener as ValueEventListener)
            refProfile.removeValue()
            alertDialog.dismiss()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()

        alertDialog.show()
    }
}