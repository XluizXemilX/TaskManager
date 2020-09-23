package com.example.taskmanager.parentUI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_reports.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportsFragment : Fragment(), GenericRecyclerAdapter.GenericRecyclerListener<TaskReportCard> {
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
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    private fun getTaskReportCard(){


        val taskRef = FirebaseDatabase.getInstance().reference.child("account").child(SharedPrefsUtil.getInstance(context).get(Constants.CURRENT_ACCOUNT, "")).child("task")

        val thisContext = this
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val taskReportCardMap = HashMap<String, TaskReportCard?>()

                    for (e in dataSnapshot.children) {
                        val chore = e.getValue(Chore::class.java)

                        var taskReportCard: TaskReportCard? = taskReportCardMap.get(chore!!.assignUser)

                        if (taskReportCard == null) {
                            taskReportCard = TaskReportCard()
                            taskReportCard.profile = chore.assignUser
                            taskReportCard.picture = chore.userPhoto
                            taskReportCardMap.put(chore.assignUser, taskReportCard)
                        }

                        if (chore.status.equals(Constants.STATUS_COMPLETE)) {
                            taskReportCard.completed++
                        } else if (chore.status.equals(Constants.STATUS_INCOMPLETE)) {
                            taskReportCard.upcoming++
                        } else {
                            taskReportCard.failed++
                        }
                    }
                    reports_recyclerview.layoutManager = GridLayoutManager(context, 1);
                    var adapter = GenericRecyclerAdapter(taskReportCardMap.values.toList(), R.layout.row_reports_layout)
                    adapter.listener = thisContext as GenericRecyclerAdapter.GenericRecyclerListener<TaskReportCard?>
                    reports_recyclerview.adapter = adapter

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
            }

        }
        taskRef.addListenerForSingleValueEvent(listener as ValueEventListener)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getTaskReportCard()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(d: TaskReportCard?) {
        SharedPrefsUtil.getInstance(context).put(Constants.CURRENT_TASK_PROFILE, d!!.profile)
        val intent= Intent(context, TaskListReport::class.java)
        startActivity(intent)

    }
}