package com.example.bottomnavigation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amplifyframework.core.Amplify
import com.example.bottomnavigation.fragments.HomeFragment
import com.example.bottomnavigation.fragments.MyReviewsFragment
import com.example.bottomnavigation.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val myReviewsFragment = MyReviewsFragment()
    private val settingsFragment = SettingsFragment()
    lateinit var botNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "HandiApp"
        Amplify.DataStore.start(
            { Log.i("MyAmplifyApp", "start sync") },
            { Log.e("MyAmplifyApp", "sync failed", it) }
        )

        replaceFragment(homeFragment)

        botNav = findViewById(R.id.bottom_navigation)

        botNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.my_reviews -> replaceFragment(myReviewsFragment)
                R.id.settings_menu -> replaceFragment(settingsFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    fun submitData(view: View) {}


}







