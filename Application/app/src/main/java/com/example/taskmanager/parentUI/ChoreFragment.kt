package com.example.taskmanager.parentUI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.PopupMenu
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.example.taskmanager.parentUI.taskCreation.AddTaskActivity
import com.example.taskmanager.parentUI.taskCreation.TaskCompleted
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chore.*
import kotlinx.android.synthetic.main.row_tasks_layout.*
import kotlinx.android.synthetic.main.row_tasks_layout.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var refTasks: DatabaseReference


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
        val profile = SharedPrefsUtil.getInstance(context).get(
            Constants.CURRENT_PROFILE,
            Profile::class.java,
            null
        )
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

        expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

            val profile  =expandableListDetail.keys.toList().get(groupPosition)
            val chores =expandableListDetail.get(profile)
            val selectedChore = chores!!.get(childPosition)
            SharedPrefsUtil.getInstance(context).put(Constants.CURRENT_TASK, Chore::class.java, selectedChore)
            val intent = Intent(context,TaskCompleted::class.java)
            startActivity(intent)
            true
        }
        //expandableListView.task_menu_dots.setOnClickListener {
        //    val popupMenu = PopupMenu(context, expandableListView.task_menu_dots)
        //    popupMenu.inflate(R.menu.dots_menu)
        //    popupMenu.setOnMenuItemClickListener {
        //        when(it.itemId){
        //           // R.id.delete_dot_menu->
        //        }
        //    }
        //}

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

//

    val expandableListDetail = HashMap<Profile, ArrayList<Chore>>()
    private fun setUpTaskList(){
        if (postListener != null)
            return
        refTasks = FirebaseDatabase.getInstance().reference.child("account").child(SharedPrefsUtil.getInstance(context).get(Constants.CURRENT_ACCOUNT, "")).child("task")
        val expandableListTitle = HashSet<Profile>()

        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    expandableListDetail.clear()
                    for (e in dataSnapshot.children){

                        val chore = e.getValue(Chore::class.java)
                        chore!!.id = e.key

                        if(chore!!.type == Constants.TYPE_CHORE && chore.status == Constants.STATUS_INCOMPLETE) {
                            val profile = Profile(chore.assignUser, chore.userPhoto)
                            expandableListTitle.add(profile)

                            var taskList = expandableListDetail.get(profile)
                            if (taskList == null) {
                                taskList =  ArrayList(0)
                                expandableListDetail.put(profile, taskList)
                            }

                            taskList.add(chore)
                        }
                    }

                    val expandableListAdapter =CustomExpandableListAdapter(context!!, expandableListTitle.toList(), expandableListDetail as  HashMap<Profile, List<Chore>> )
                    expandableListView.setAdapter(expandableListAdapter)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())

            }
        }
        refTasks.addListenerForSingleValueEvent(postListener as ValueEventListener)
    }

    //SharePrefUtil.getInstance(context).put(Constants.CURRENT_TASK, Chore::class.java, task)

}