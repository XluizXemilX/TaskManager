package com.example.taskmanager.parentMenuDrawer.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.taskmanager.R
import com.example.taskmanager.SplashScreen
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.parentUI.JobsFragment
import com.example.taskmanager.parentUI.taskCreation.ChangeInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.clear_chat_layout.view.*
import kotlinx.android.synthetic.main.pin_validation.view.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mAuth =FirebaseAuth.getInstance()

        back_settings_parent.setOnClickListener {
            finish()
        }

        clear_chat.setOnClickListener {
            clearChat()
        }

        delete_account.setOnClickListener {
            deleteAccount()
        }

        house_members.setOnClickListener {
            startActivity(Intent(this, ManageHouse::class.java))
        }

        password_change.setOnClickListener {
            val intent=Intent(this, ChangeInfoActivity::class.java)
            intent.putExtra("password", R.layout.change_password_layout)
            startActivity(intent)
        }

        change_email.setOnClickListener {
            val intent=Intent(this, ChangeInfoActivity::class.java)
            intent.putExtra("email", R.layout.change_email_layout)
            startActivity(intent)
        }

        change_profile_pin.setOnClickListener {
            val intent=Intent(this, ChangeInfoActivity::class.java)
            startActivity(intent)

        }

    }

    private fun clearChat(){
        val refTable = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(this).get(
                Constants.CURRENT_ACCOUNT, "")).child("messages")
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.clear_chat_layout, null)
        dialogView.dialog_cancel_btn.setOnClickListener { // if task creation is cancel
            alertDialog.cancel()
        }
        dialogView.dialog_ok_btn.setOnClickListener {
            refTable.removeValue()
            alertDialog.dismiss()
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()

        alertDialog.show()
    }

    private fun deleteAccount(){
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.delete_account_layout, null)
        dialogView.dialog_cancel_btn.setOnClickListener { // if task creation is cancel
            alertDialog.cancel()
        }
        dialogView.dialog_ok_btn.setOnClickListener {

            val user = mAuth.currentUser!!

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, SplashScreen::class.java)
                        startActivity(intent)
                        Toast.makeText(this,"bye",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Try again",Toast.LENGTH_LONG).show()
                        alertDialog.dismiss()
                    }
                }
            FirebaseDatabase.getInstance().reference.child("account").child(user.uid).removeValue()
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()

        alertDialog.show()
    }
}