package com.example.taskmanager.parentMenuDrawer.settings

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.taskmanager.R
import com.example.taskmanager.parentUI.HomeActivity
import kotlinx.android.synthetic.main.activity_tutorial_container.*

class TutorialContainerActivity : AppCompatActivity() {

    private val fragment1 = TutorialFragment()
    private val fragment2 = TutorialFragment()
    private val fragment3 = TutorialFragment()
    private val fragment4 = TutorialFragment()
    lateinit var adapter: myPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_container)

        fragment1.setTextTitle("Chores")
        fragment1.setTextInfo("Chores are task that need to be completed by a specific family member on a due date. They can be rewarded but it is optional.")
        fragment1.setImage(R.drawable.chores_icon)
        fragment2.setTextTitle("Jobs")
        fragment2.setTextInfo("Jobs are challenging tasks that normally are not as common to do as chores. Jobs are always rewarded and do not have to be completed by a specific family member.")
        fragment2.setImage(R.drawable.money_icon)
        fragment3.setTextTitle("List of Tasks")
        fragment3.setTextInfo("You can manage your task by clicking on them and marking them as complete or by clicking in the three dots and deleting them. Tasks are group by whoever they are assign to.")
        fragment3.setImage(R.drawable.checklist_icon)
        fragment4.setTextTitle("Add Tasks")
        fragment4.setTextInfo("Click on the plus button(+) to create a task and get started.")
        fragment4.setImage(R.drawable.add_icon)

        adapter = myPagerAdapter(supportFragmentManager)
        adapter.list.add(fragment1)
        adapter.list.add(fragment2)
        adapter.list.add(fragment3)
        adapter.list.add(fragment4)

        view_pager_tutorial.adapter = adapter
        next_tutorial.setOnClickListener {
            view_pager_tutorial.currentItem++
        }

        skip_tutorial.setOnClickListener {
            val intent = Intent(this@TutorialContainerActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        view_pager_tutorial.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if(position == adapter.list.size-1){
                    next_tutorial.text = "DONE"
                    next_tutorial.setOnClickListener {
                        val intent = Intent(this@TutorialContainerActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                else{
                    next_tutorial.text = "NEXT"
                    next_tutorial.setOnClickListener {
                        view_pager_tutorial.currentItem++
                    }
                }

                when(view_pager_tutorial.currentItem){
                    0->{
                        indicator1.setTextColor(Color.BLACK)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.GRAY)
                        indicator4.setTextColor(Color.GRAY)
                    }
                    1->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.BLACK)
                        indicator3.setTextColor(Color.GRAY)
                        indicator4.setTextColor(Color.GRAY)
                    }
                    2->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.BLACK)
                        indicator4.setTextColor(Color.GRAY)
                    }
                    3->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.GRAY)
                        indicator4.setTextColor(Color.BLACK)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}

class myPagerAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager){
    val list : MutableList<Fragment> = ArrayList()
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

}