package com.example.taskmanager.parentMenuDrawer.settings

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskmanager.R
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialFragment : Fragment() {

    var pageText : String = ""
    var resource : Int = R.drawable.chores_icon
    var infoText : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tutorial_tv.text = pageText
        tutorial_image.setImageResource(resource)
        info_tutorial_tv.text = infoText
    }

    fun setTextTitle(text: String){
        pageText = text
    }

    fun setTextInfo(text: String){
        infoText = text
    }

    fun setImage(int : Int){
        resource = int
    }
}