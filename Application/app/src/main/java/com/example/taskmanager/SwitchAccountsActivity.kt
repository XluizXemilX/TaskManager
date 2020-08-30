package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.ParentUI.HomeActivity
import com.example.taskmanager.classes.GenericRecyclerAdapter
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_switch_accounts.*


class SwitchAccountsActivity :  AppCompatActivity(), GenericRecyclerAdapter.GenericRecyclerListener<Profile> {

    private lateinit var refUsers: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var postListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_accounts)

        mAuth= FirebaseAuth.getInstance()

        back_switch_account_btn.setOnClickListener {
            finish()
        }

        add_Profile_fbtn.setOnClickListener{
            val intent =
                Intent(
                    this@SwitchAccountsActivity,
                    AddProfileActivity::class.java
                ) // send user to create a house if task is completed
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
                        listProfiles.add(profile!!)
                    }
                    profile_recyclerview.layoutManager = GridLayoutManager(this@SwitchAccountsActivity, 2);
                    var adapter = GenericRecyclerAdapter(listProfiles, R.layout.row_profile_layout)
                    adapter.listener = thisContext
                    profile_recyclerview.adapter = adapter

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

        val intent =
            Intent(
                this@SwitchAccountsActivity,
                HomeActivity::class.java
            ) // send user to create a house if task is completed
        startActivity(intent)
    }
}