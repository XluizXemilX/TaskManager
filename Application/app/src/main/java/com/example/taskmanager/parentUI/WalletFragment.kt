package com.example.taskmanager.parentUI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.GenericRecyclerAdapter
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bank_account.*
import kotlinx.android.synthetic.main.fragment_child_wallet.*
import kotlinx.android.synthetic.main.fragment_wallet.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var profile: Profile
/**
 * A simple [Fragment] subclass.
 * Use the [WalletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletFragment : Fragment(), GenericRecyclerAdapter.GenericRecyclerListener<Profile> {
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
        profile = SharedPrefsUtil.getInstance(context).get(Constants.CURRENT_PROFILE, Profile::class.java, null)// current profile
        return if(profile.type == Constants.PARENT) {
            inflater.inflate(R.layout.fragment_wallet, container, false)
        } else{
            inflater.inflate(R.layout.fragment_child_wallet, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(profile.type == Constants.PARENT){
            getWallets()
        }
        else{
            balance_tv.text =  "$" + profile.balance
        }

    }

    private fun getWallets() {

        val listProfiles = ArrayList<Profile>()
        val refUsers = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(context).get(
                Constants.CURRENT_ACCOUNT, ""
            )
        ).child("users")
        val thisContext = this
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    listProfiles.clear()
                    for (e in dataSnapshot.children) {
                        val profile = e.getValue(Profile::class.java)
                        profile!!.id = e.key
                        listProfiles.add(profile)
                    }
                    wallet_recyclerview.layoutManager = GridLayoutManager(context, 2);
                    var adapter = GenericRecyclerAdapter(listProfiles, R.layout.row_wallet_layout)
                    adapter.listener = thisContext
                    wallet_recyclerview.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "updateTaskList:onCancelled", databaseError.toException())
            }

        }
        refUsers.addListenerForSingleValueEvent(listener as ValueEventListener)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(d: Profile?) {

    }
}