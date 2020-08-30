package com.example.taskmanager.ParentUI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.taskmanager.classes.Chore
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chore.*
import kotlinx.android.synthetic.main.task_box.view.*

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

//    override fun onDestroy() {
//        postListener?.let { refUsers.removeEventListener(it) }
//
//        super.onDestroy()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTaskList()

        addTask_Floating_btn.setOnClickListener {

            val id = SharedPrefsUtil.getInstance(context).get("accountId", "")
            val profileId = SharedPrefsUtil.getInstance(context).get("userId", "")
            val user = FirebaseDatabase.getInstance().reference.child("account").child(id)
                .child("users").child(profileId).child("type")
            val profile = Profile::class
            if(user.equals("parent")){
                Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show()
            }
            createTask()
            setUpTaskList()

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

    private fun createTask() {

        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.task_box, null) // dialog box

        dialogView.dialog_cancel_task_btn.setOnClickListener { // if task creation is cancel
            alertDialog.cancel()
        }
        //dialogView.add_picture.setOnClickListener {
        //    val intent =
        //        Intent(MediaStore.ACTION_IMAGE_CAPTURE) // send user to create a house if task is completed
        //    startActivity(intent)
        //}
        dialogView.dialog_save_task_btn.setOnClickListener {    // if task is save
            val taskName = dialogView.task_name_et.text.toString()
            val person = dialogView.task_person_et.text.toString()
            val dueDate = dialogView.task_dueDate_et.text.toString()

            if (taskName == "") {
                Toast.makeText(context, "please write Task Name.", Toast.LENGTH_SHORT)
                    .show()
            } else if (person == "") {
                Toast.makeText(
                    context,
                    "please write assigned person password.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (dueDate == "") {
                Toast.makeText(context, "please enter due date.", Toast.LENGTH_SHORT)
            } else {
                //send task to the firebase
                val id = SharedPrefsUtil.getInstance(context).get("accountId", "")
                refUsers = FirebaseDatabase.getInstance().reference.child("account").child(id)
                    .child("task")

                val userHashMap = HashMap<String, Any>()  // holds user data
                userHashMap["accountId"] = id
                userHashMap["taskName"] = taskName
                userHashMap["assignUser"] = person
                userHashMap["type"] = "chore"
                userHashMap["dueDate"] = dueDate
                userHashMap["photoRequired"] = ""
                userHashMap["verificationRequire"] = ""
                refUsers.push().setValue(userHashMap)
                    .addOnSuccessListener {
                        alertDialog.dismiss()

                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            activity,
                            "Error Message: Something is wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()

        //alertDialog.window!!.getAttributes().windowAnimations = R.style.PauseDialogAnimation
        alertDialog.show()

    }


    var postListener: ValueEventListener? = null

    private fun setUpTaskList() {
        if (postListener != null)
            return

        //val listView:ListView? = view?.findViewById<ListView>(R.id.listview_chore)
        val listItems = arrayListOf<String>()
        refUsers =FirebaseDatabase.getInstance().reference.child("account").child(SharedPrefsUtil.getInstance(context).get("accountId", "")).child("task")
        val taskRef = refUsers

        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot!!.exists()){
                    listItems.clear()
                    for (e in dataSnapshot.children){
                        val chore = e.getValue(Chore::class.java)
                        if(chore!!.type=="chore") {
                            listItems.add(chore!!.taskName)
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
}