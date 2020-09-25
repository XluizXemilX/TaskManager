package com.example.taskmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.classes.*
import com.example.taskmanager.parentMenuDrawer.settings.TutorialContainerActivity
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bank_account.*
import kotlinx.android.synthetic.main.activity_first_profile.*
import kotlinx.android.synthetic.main.icons_dialog.view.*
import kotlinx.android.synthetic.main.pin_validation.view.*

class FirstProfileActivity : AppCompatActivity() {


    private lateinit var refUsers: DatabaseReference
    private  var selectedUserIcon: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_profile)

        //mAuth = FirebaseAuth.getInstance()

        finish_btn.setOnClickListener {
            createProfile()
        }

        first_profile_chage_pic.setOnClickListener {
            changePicture()
        }
    }


    private fun createProfile() {
        val nickname: String = houseName_register.text.toString().toLowerCase()
        val profilePin: String = housePassword_register.text.toString()

        if (nickname == "") {
            houseName_register.error = "Nickname require"
            Toast.makeText(this@FirstProfileActivity, "please write Nickname.", Toast.LENGTH_SHORT)// checks for input
                .show()
        } else if (profilePin == "") {
            housePassword_register.error = "Pin Require"
            Toast.makeText(this@FirstProfileActivity, "please write Profile pin.", Toast.LENGTH_SHORT)//checks for input
                .show()
        } else if(profilePin.length < 4){
            housePassword_register.error = "Pin too short"
            Toast.makeText(this@FirstProfileActivity, "please enter a 4-digit pin", Toast.LENGTH_SHORT)//checks for input
                .show()
        }else {

            //firebaseUserId = mAuth.currentUser!!.uid
            val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT,"")
            refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users")

            val userHashMap = HashMap<String, String>()  // holds user data
            userHashMap[Constants.CURRENT_ACCOUNT] = id
            userHashMap["nickname"] = nickname
            userHashMap["profilePin"] = profilePin
            userHashMap["type"] = Constants.PARENT
            userHashMap["picture"] = selectedUserIcon ?: Constants.USER_ICON_DEFAULT
            userHashMap["bank"]= "No Bank"
            userHashMap["balance"]= "0.00"
            val pushRef = refUsers.push()
            val key = pushRef.key
            pushRef.setValue(userHashMap)

                .addOnSuccessListener {
                    //needs to save current user
                    val profile = Profile()
                    profile.fromMap(userHashMap)
                    profile.id = key
                    SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE, Profile::class.java, profile)
                    val intent =
                        Intent(
                            this@FirstProfileActivity,
                            TutorialContainerActivity::class.java
                        ) // send user to create a house if task is completed
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Toast.makeText(
                           this@FirstProfileActivity,
                           "Error Message: Lost connection",
                           Toast.LENGTH_SHORT
                       ).show()
                }
        }
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
            DataBindingAdapters.setImageResourceByName(first_profile_image, it.picture)
            alertDialog.dismiss()
        }
        dialogView.icons_recycler.adapter = adapter

        alertDialog.show()
    }
}