package com.example.taskmanager.parentMenuDrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_first_profile.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.icons_dialog.view.*

class ProfileActivity : AppCompatActivity() {
    lateinit var refProfile: Profile
    private lateinit var selectedUserIcon: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        refProfile = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
        change_nickname_et.setText(refProfile.nickname)
        DataBindingAdapters.setImageResourceByName(profile_page_picture, refProfile.picture)

        back_profile_btn.setOnClickListener{
            finish()
        }

        save_profile_change.setOnClickListener {
            saveChanges()
        }

        edit_profile_change_pic.setOnClickListener {
            changePicture()
        }
    }

    private fun saveChanges(){
        val nickname = change_nickname_et.text.toString()
        val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT,"")

        val firebaseRef = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users").child(refProfile.id)
        if(TextUtils.isEmpty(nickname)){
            change_nickname_et.error = "Nickname require"
        }
        else{
            val userHashMap = HashMap<String, String>()  // holds user data

            userHashMap["picture"] = selectedUserIcon
            userHashMap["nickname"] = nickname

            firebaseRef.updateChildren(userHashMap.toMap())

                .addOnSuccessListener {
                    refProfile.nickname = nickname
                    refProfile.picture = selectedUserIcon
                    updateAll()
                    SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE, Profile::class.java, refProfile)
                    Toast.makeText(this, "Changes save.", Toast.LENGTH_LONG).show()

                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)

                }.addOnFailureListener {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Error Message: Could not complete the action, try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun updateAll(){
        val refTask = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("task")
        val refMessages =FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("messages")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    for (e in dataSnapshot.children) {
                        val task = e.getValue(Chore::class.java)
                        if(task!!.assignUser == refProfile.nickname){
                            task.id = e.key
                            val taskHashMap = HashMap<String, String>()
                            taskHashMap["userPhoto"] = refProfile.picture
                            refTask.child(task.id).updateChildren(taskHashMap.toMap())
                        }
                    }

                    for (e in dataSnapshot.children) {
                        val message = e.getValue(Message::class.java)
                        if(message!!.name == refProfile.nickname){
                            message.id = e.key
                            val messageHashMap = HashMap<String, String>()
                            messageHashMap["picture"] = refProfile.picture
                            refMessages.child(message.id).updateChildren(messageHashMap.toMap())
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
            }

        }
        refTask.addListenerForSingleValueEvent(postListener)
        refMessages.addListenerForSingleValueEvent(postListener)

    }

    private fun changePicture(){

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.icons_dialog, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setOnDismissListener { }
        val alertDialog = dialogBuilder.create()

        val listProfiles = ArrayList<Profile>()
        ImageUtils.getUserIcons().forEach {
            val profile =  Profile()
            profile.picture = it
            listProfiles.add(profile)
        }
        dialogView.icons_recycler.layoutManager = GridLayoutManager(this, 2);
        var adapter = GenericRecyclerAdapter(listProfiles, R.layout.row_bank_layout)
        adapter.listener = GenericRecyclerAdapter.GenericRecyclerListener {

            selectedUserIcon = it.picture
            DataBindingAdapters.setImageResourceByName(profile_page_picture, it.picture)
            alertDialog.dismiss()
        }
        dialogView.icons_recycler.adapter = adapter

        alertDialog.show()
    }
}