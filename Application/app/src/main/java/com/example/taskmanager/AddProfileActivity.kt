package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.parentUI.HomeActivity
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_profile.*

class AddProfileActivity : AppCompatActivity() {

    private lateinit var refUsers: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        addP_btn.setOnClickListener {
            createProfile()
        }

        val types = resources.getStringArray(R.array.types)// array for the spinner
        if(profile_type_spn != null)
        {
            val adapter = ArrayAdapter(this,R.layout.spinner_item_custome, types)// array adapter for the values of the spinner
            profile_type_spn.adapter = adapter // sets the spinner adapter to the adapter
        }
        profile_type_spn.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 1){
                    pin_addP_et.visibility = View.GONE
                }
                else{
                    pin_addP_et.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@AddProfileActivity, "Type field is require!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createProfile() {
        val nickname: String = nickname_addP_et.text.toString().toLowerCase() // nickname field
        val profilePin: String = pin_addP_et.text.toString() // pin field

        //spinner

        //check if any of the fields are empty or do not complete the requirements
        if (nickname == "") {
            nickname_addP_et.error = "Nickname require"
            Toast.makeText(this@AddProfileActivity, "please enter Nickname.", Toast.LENGTH_SHORT)
                .show()
        } else if (profilePin == "" && pin_addP_et.visibility == View.VISIBLE) {
            pin_addP_et.error = "Pin require"
            Toast.makeText(this@AddProfileActivity, "please enter pin.", Toast.LENGTH_SHORT)
                .show()
        } else if(profilePin.length < 4 && pin_addP_et.visibility == View.VISIBLE){
            pin_addP_et.error = "Pin require"
            Toast.makeText(this@AddProfileActivity, "please a 4-digit pin.", Toast.LENGTH_SHORT)
                .show()

        } else {
            //saving data to database
            val id = SharedPrefsUtil.getInstance(this).get(Constants.CURRENT_ACCOUNT,"")// account id
            refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id).child("users") // database reference

            val userHashMap = HashMap<String, String>()  // holds user data
            userHashMap[Constants.CURRENT_ACCOUNT] = id
            userHashMap["nickname"] = nickname
            userHashMap["profilePin"] = profilePin
            userHashMap["type"] = profile_type_spn.selectedItem.toString()
            userHashMap["picture"]= "https://firebasestorage.googleapis.com/v0/b/choresapp-658eb.appspot.com/o/user_default_icon.xml?alt=media&token=b3fdf23a-12a2-4dd4-94a2-6ed93d91a27a"
            val pushRef = refUsers.push()
            val key = pushRef.key
            pushRef.setValue(userHashMap)

                .addOnSuccessListener {// if task is complete successful
                    //saves current user to use during the entire session
                    //saves to the database
                    val profile = Profile()
                    profile.fromMap(userHashMap)
                    profile.id = key
                    SharedPrefsUtil.getInstance(this).put(Constants.CURRENT_PROFILE, Profile::class.java, profile)
                    val intent =
                        Intent(
                            this@AddProfileActivity,
                            HomeActivity::class.java
                        ) // send user to create a house if task is completed
                    startActivity(intent)

                }
                .addOnFailureListener {// task fails
                    Toast.makeText(
                        this@AddProfileActivity,
                        "Error Message: Lost connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}