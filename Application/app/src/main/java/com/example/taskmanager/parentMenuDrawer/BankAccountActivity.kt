package com.example.taskmanager.parentMenuDrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.GenericRecyclerAdapter
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bank_account.*
import kotlinx.android.synthetic.main.activity_switch_accounts.*
import kotlinx.android.synthetic.main.row_bank_layout.*

class BankAccountActivity : AppCompatActivity(), GenericRecyclerAdapter.GenericRecyclerListener<Profile> {

    private lateinit var refUsers: DatabaseReference
    private var postListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)

        back_arrow_bank.setOnClickListener{
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
                    bank_recycler.layoutManager = GridLayoutManager(this@BankAccountActivity, 2);
                    var adapter = GenericRecyclerAdapter(listProfiles, R.layout.row_bank_layout)
                    adapter.listener = thisContext
                    bank_recycler.adapter = adapter
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

        //SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE,Profile::class.java, profile)
        //val intent =
        //    Intent(
        //        this@BankAccountActivity,
        //        HomeActivity::class.java
        //    ) // send user to create a house if task is completed
        //startActivity(intent)


    }
}