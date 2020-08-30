package com.example.taskmanager.ParentUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.taskmanager.ParentMenuDrawer.BankAccountActivity
import com.example.taskmanager.ParentMenuDrawer.ProfileActivity
import com.example.taskmanager.SwitchAccountsActivity
import com.example.taskmanager.R
import com.example.taskmanager.SplashScreen
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

        //check if the user is login or not
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, SplashScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        //toolbar and nav drawer
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)        //sets the toolbar

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.inflateMenu(R.menu.drawer_view)
        navView.setNavigationItemSelectedListener(this)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorAppBlue, theme);


        //bottom nav bar
        val jobsFragment = JobsFragment()
        val choreFragment = ChoreFragment()
        val chatFragment = ChatFragment()
        val reportsFragment = ReportsFragment()
        val walletFragment = WalletFragment()

        makeCurrFragment(jobsFragment)

        bottom_nav_bar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.parent_jobs -> makeCurrFragment(jobsFragment)
                R.id.parent_chores -> makeCurrFragment(choreFragment)
                R.id.parent_chat -> makeCurrFragment(chatFragment)
                R.id.parent_report -> makeCurrFragment(reportsFragment)
                R.id.parent_wallet -> makeCurrFragment(walletFragment)
            }
            true
        }
    }

    private fun makeCurrFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment_container_layout, fragment)
            commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when ( item.itemId){
            R.id.parent_nav_profile->{
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this@HomeActivity, ProfileActivity::class.java) // send user to profile
                startActivity(intent)

            }
            R.id.parent_nav_bank_account->{
                Toast.makeText(this, "Bank", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this@HomeActivity, BankAccountActivity::class.java) // send user to Bank Accounts
                startActivity(intent)

            }
            R.id.parent_nav_settings->{
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this@HomeActivity, ProfileActivity::class.java) // send user to settings
                startActivity(intent)

            }
            R.id.parent_nav_switch_account->{
                Toast.makeText(this, "switch account", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this@HomeActivity, SwitchAccountsActivity::class.java) // send user to switch account
                startActivity(intent)

            }
            R.id.parent_nav_logout->{
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