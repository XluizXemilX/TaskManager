package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Constants.CURRENT_PROFILE
import com.example.taskmanager.parentUI.HomeActivity
import com.example.taskmanager.classes.GenericRecyclerAdapter
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_profile.view.*
import kotlinx.android.synthetic.main.activity_switch_accounts.*
import kotlinx.android.synthetic.main.pin_validation.view.*



class SwitchProfileActivity :  AppCompatActivity(), GenericRecyclerAdapter.GenericRecyclerListener<Profile> {

    private lateinit var refUsers: DatabaseReference
    private var postListener: ValueEventListener? = null
    private lateinit var alertDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_accounts)



        val profile = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
        if(profile == null || profile.type == "Child"){
            add_Profile_fbtn.hide()
        }
        add_Profile_fbtn.setOnClickListener{
            validateParentPin()
        }

        val listProfiles = ArrayList<Profile>()
        refUsers = FirebaseDatabase.getInstance().reference.child("account").child(SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT, "")).child("users")
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
                    profile_recyclerview.layoutManager = GridLayoutManager(this@SwitchProfileActivity, 2);
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

    override fun onClick(profile: Profile?) {

        if(profile!!.type == Constants.PARENT){
            validateParentPinLogin(profile)

        }
        else{
            SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE,Profile::class.java, profile)
            val intent =
                Intent(
                    this@SwitchProfileActivity,
                    HomeActivity::class.java
                ) // send user to create a house if task is completed
            startActivity(intent)
            finish()
        }

    }

    private fun validateParentPinLogin(profile: Profile?){

        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.pin_validation, null)
        dialogView.dialog_cancel_pin_btn.setOnClickListener { // if task creation is cancel
            alertDialog.cancel()
        }
        dialogView.dialog_ok_pin_btn.setOnClickListener {
            val pin = dialogView.pin_validation_et.text.toString()
            dialogView.pin_validation_et
            dialogView.pin_validation_et.setSingleLine()
            if(pin.isEmpty()){
                dialogView.pin_validation_et.error = "Enter the pin"
            }
            if(pin == profile!!.profilePin){
                SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE,Profile::class.java, profile)
                val intent =
                    Intent(
                        this@SwitchProfileActivity,
                        HomeActivity::class.java
                    ) // send user to create a house if task is completed
                startActivity(intent)
            }
            else{
                dialogView.pin_validation_et.error = "Incorrect pin"
            }
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()

        alertDialog.show()
    }

    private fun validateParentPin(){
        val profile = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.pin_validation, null)
        dialogView.dialog_cancel_pin_btn.setOnClickListener { // if task creation is cancel
            alertDialog.cancel()
        }
        dialogView.dialog_ok_pin_btn.setOnClickListener {
            val pin = dialogView.pin_validation_et.text.toString()
            if(pin.isEmpty()){
                dialogView.pin_validation_et.error = "Enter the pin"
            }
            if(pin == profile.profilePin){
                val intent =
                    Intent(
                        this@SwitchProfileActivity,
                        AddProfileActivity::class.java
                    ) // send user to create a house if task is completed
                startActivity(intent)
            }
            else{
                dialogView.pin_validation_et.error = "Incorrect pin"
            }
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()

        alertDialog.show()
    }

}