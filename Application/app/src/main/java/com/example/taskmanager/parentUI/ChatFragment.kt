package com.example.taskmanager.parentUI

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.classes.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var refMessages: DatabaseReference

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
        loadMessages()
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profile = SharedPrefsUtil.getInstance(context).get(
            Constants.CURRENT_PROFILE,
            Profile::class.java,
            null
        )
        refMessages = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(
                context
            ).get(Constants.CURRENT_ACCOUNT, "")
        )
        send_chat_btn.setOnClickListener(View.OnClickListener {
            val message = Message(
                profile.nickname,
                message_chat_et.text.toString(),
                profile.picture
            )
            refMessages.child("messages")
                .push().setValue(message).addOnSuccessListener {

                    message.isMine = true
                    messageList.add(message)
                    val messageRecyclerAdapter = GenericRecyclerAdapter<Message>(
                        messageList,
                        R.layout.row_chat_layout
                    )
                    chat_recycler_view.adapter = messageRecyclerAdapter
                    message_chat_et.setText("")
                    hideKeyboard()
                }


        })
    }

    val messageList = ArrayList<Message>()
    private fun loadMessages(){
        val currentProfile = SharedPrefsUtil.getInstance(context).get(Constants.CURRENT_PROFILE, Profile::class.java, null)
        val refMessage = FirebaseDatabase.getInstance().reference.child("account").child(
            SharedPrefsUtil.getInstance(
                context
            ).get(Constants.CURRENT_ACCOUNT, "")
        )
            .child("messages")
        val expandableListTitle = HashSet<Profile>()

       val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    messageList.clear()
                    for (e in dataSnapshot.children) {
                        val message = e.getValue(Message::class.java)

                        message!!.isMine = message!!.name == currentProfile.nickname
                        messageList.add(message!!)
                    }
                    val messageRecyclerAdapter = GenericRecyclerAdapter<Message>(
                        messageList,
                        R.layout.row_chat_layout
                    )
                    chat_recycler_view.layoutManager = LinearLayoutManager(context)
                    chat_recycler_view.adapter = messageRecyclerAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadMessages:onCancelled", databaseError.toException())

            }
        }
        refMessage.addListenerForSingleValueEvent(postListener)
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive()) imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}