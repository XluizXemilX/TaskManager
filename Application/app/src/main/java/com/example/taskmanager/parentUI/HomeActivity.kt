package com.example.taskmanager.parentUI

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.taskmanager.R
import com.example.taskmanager.SplashScreen
import com.example.taskmanager.SwitchProfileActivity
import com.example.taskmanager.classes.Constants
import com.example.taskmanager.classes.DataBindingAdapters
import com.example.taskmanager.classes.Profile
import com.example.taskmanager.classes.SharedPrefsUtil
import com.example.taskmanager.parentMenuDrawer.BankAccountActivity
import com.example.taskmanager.parentMenuDrawer.ProfileActivity
import com.example.taskmanager.parentMenuDrawer.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_main.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var  navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val profile = SharedPrefsUtil.getInstance(this).get(
            Constants.CURRENT_PROFILE,
            Profile::class.java,
            null
        )// current profile

        //toolbar and nav drawer
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)        //sets the toolbar

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        bar_title.text = profile.nickname
        DataBindingAdapters.setImageResourceByName(profile_icon_toolbar, profile.picture)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.inflateMenu(R.menu.drawer_view)
        if(profile.type == Constants.CHILD){
            navView.menu.removeItem(R.id.parent_nav_bank_account)
            navView.menu.removeItem(R.id.parent_nav_settings)
        }
        navView.setNavigationItemSelectedListener(this)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorAppBlue, theme);



        makeCurrFragment(JobsFragment())

        if(profile.type == Constants.CHILD){
            bottom_nav_bar.menu.removeItem(R.id.parent_report)
        }
        bottom_nav_bar.setOnNavigationItemSelectedListener {

            when(it.itemId){
                R.id.parent_jobs -> makeCurrFragment(JobsFragment())
                R.id.parent_chores -> makeCurrFragment(ChoreFragment())
                R.id.parent_chat -> makeCurrFragment(ChatFragment())
                R.id.parent_report -> makeCurrFragment(ReportsFragment())
                R.id.parent_wallet -> makeCurrFragment(WalletFragment())
            }
            true
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun makeCurrFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment_container_layout, fragment)
            commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when ( item.itemId){
            R.id.parent_nav_profile -> {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this@HomeActivity, ProfileActivity::class.java) // send user to profile
                startActivity(intent)

            }
            R.id.parent_nav_bank_account -> {
                Toast.makeText(this, "Bank", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(
                        this@HomeActivity,
                        BankAccountActivity::class.java
                    ) // send user to Bank Accounts
                startActivity(intent)
            }
            R.id.parent_nav_settings -> {
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this@HomeActivity, SettingsActivity::class.java) // send user to settings
                startActivity(intent)

            }
            R.id.parent_nav_switch_account -> {
                Toast.makeText(this, "switch account", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(
                        this@HomeActivity,
                        SwitchProfileActivity::class.java
                    ) // send user to switch account
                startActivity(intent)

            }
            R.id.parent_nav_logout -> {
                Toast.makeText(this, "bye", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                val intent =
                    Intent(this@HomeActivity, SplashScreen::class.java) // sign the user out
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}