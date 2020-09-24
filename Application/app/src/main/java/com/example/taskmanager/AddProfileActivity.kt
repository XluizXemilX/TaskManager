package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.classes.*
import com.example.taskmanager.parentUI.HomeActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_profile.*
import kotlinx.android.synthetic.main.icons_dialog.view.*

class AddProfileActivity : AppCompatActivity() {

    private lateinit var refUsers: DatabaseReference
    private  var selectedUserIcon: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        addP_btn.setOnClickListener {
            createProfile()
        }

        add_profile_change_pic.setOnClickListener {
            changePicture()
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
            userHashMap["picture"] = selectedUserIcon ?: Constants.USER_ICON_DEFAULT
            userHashMap["bank"]= "No Bank"
            userHashMap["balance"] = "0.00"
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
            DataBindingAdapters.setImageResourceByName(add_profile_picture, it.picture)
            alertDialog.dismiss()
        }
        dialogView.icons_recycler.adapter = adapter

        alertDialog.show()
    }
}