package com.example.appcontrole

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcontrole.R.id.nav_host_fragment_activity_main
import com.example.appcontrole.databinding.ActivityLoggedBinding
import com.example.appcontrole.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoggedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoggedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = intent.getStringExtra("user").toString()

        // Missing pass the user information to the HomeFragment
        Log.d("user", user)

        binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_consumption, R.id.navigation_records,
                R.id.navigation_consultations, R.id.navigation_technicalsuppport
            )
        )
        //hide supportActionBar
        supportActionBar?.hide()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}