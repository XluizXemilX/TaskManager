package com.example.taskmanager.parentUI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.taskmanager.parentUI.taskCreation.AddTaskActivity
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.R
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chore.*
import kotlinx.android.synthetic.main.fragment_jobs.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var alertDialog: AlertDialog
private lateinit var mAuth: FirebaseAuth
private lateinit var refUsers: DatabaseReference


/**
 * A simple [Fragment] subclass.
 * Use the [ChoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chore, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTaskList()
        val profile = SharedPrefsUtil.getInstance(context).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
        if(profile == null || profile.type == "Child"){
            addTask_Floating_btn.hide()
        }
        addTask_Floating_btn.setOnClickListener {
                val intent =
                    Intent(
                        context,
                        AddTaskActivity::class.java
                    ) // send user to create a house if task is completed
                startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var postListener: ValueEventListener? = null

    private fun setUpTaskList() {
        if (postListener != null)
            return

        val listItems = arrayListOf<String>()
        refUsers =FirebaseDatabase.getInstance().reference.child("account").child(SharedPrefsUtil.getInstance(context).get(Constants.CURRENT_ACCOUNT, "")).child("task")
        val taskRef = refUsers

        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    listItems.clear()
                    for (e in dataSnapshot.children){
                        val chore = e.getValue(Chore::class.java)
                        if(chore!!.type==Constants.TYPE_CHORE) {
                            listItems.add(chore.taskName)
                        }
                    }
                    val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, listItems)
                    listview_chore.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
            }
        }
        taskRef.addListenerForSingleValueEvent(postListener as ValueEventListener)
    }
    //onClick()
    //SharePrefUtil.getInstance(context).put(Constants.CURRENT_TASK, Chore::class.java, task)

}